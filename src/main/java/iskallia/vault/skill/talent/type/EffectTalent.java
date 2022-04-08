package iskallia.vault.skill.talent.type;

import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;
import iskallia.vault.attribute.EffectAttribute;
import iskallia.vault.attribute.EffectTalentAttribute;
import iskallia.vault.aura.AuraManager;
import iskallia.vault.aura.type.EffectAuraConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.paxel.enhancement.EffectEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.sub.GhostWalkRegenerationConfig;
import iskallia.vault.skill.set.EffectSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.archetype.WardTalent;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.EffectInfluence;
import iskallia.vault.world.vault.modifier.EffectModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@EventBusSubscriber
public class EffectTalent extends PlayerTalent {
    @Expose
    private final String effect;

    public String toString() {
        return "EffectTalent{effect='" + this.effect + '\'' + ", amplifier=" + this.amplifier + ", type='" + this.type + '\'' + ", operator='" + this.operator + '\'' + '}';
    }

    @Expose
    private final int amplifier;
    @Expose
    private final String type;
    @Expose
    private final String operator;

    public EffectTalent(int cost, Effect effect, int amplifier, Type type, Operator operator) {
        this(cost, Registry.MOB_EFFECT.getKey(effect).toString(), amplifier, type.toString(), operator.toString());
    }

    public EffectTalent(int cost, String effect, int amplifier, String type, String operator) {
        super(cost);
        this.effect = effect;
        this.amplifier = amplifier;
        this.type = type;
        this.operator = operator;
    }

    public Effect getEffect() {
        return (Effect) Registry.MOB_EFFECT.get(new ResourceLocation(this.effect));
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public Type getType() {
        return Type.fromString(this.type);
    }

    public Operator getOperator() {
        return Operator.fromString(this.operator);
    }


    public EffectInstance makeEffect(int duration) {
        return new EffectInstance(getEffect(), duration, getAmplifier(), true,
                (getType()).showParticles, (getType()).showIcon);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }

        Collection<Effect> immunities = getImmunities((LivingEntity) player);
        Map<Effect, CombinedEffects> effectMap = getEffectData(player, (ServerWorld) player.getCommandSenderWorld(), effect -> !immunities.contains(effect));
        applyEffects((LivingEntity) player, effectMap);
    }

    public static void applyEffects(LivingEntity entity, Map<Effect, CombinedEffects> effects) {
        effects.forEach((effect, combinedEffects) -> {
            int amplifier = combinedEffects.getAmplifier();
            if (amplifier >= 0) {
                EffectTalent displayTalent = combinedEffects.getDisplayEffect();
                EffectInstance activeEffect = entity.getEffect(effect);
                EffectInstance newEffect = new EffectInstance(effect, 339, amplifier, false, (displayTalent.getType()).showParticles, (displayTalent.getType()).showIcon);
                if (activeEffect == null || activeEffect.getAmplifier() < newEffect.getAmplifier()) {
                    entity.addEffect(newEffect);
                } else if (activeEffect.getDuration() <= 259) {
                    entity.addEffect(newEffect);
                }
            }
        });
    }


    public static Map<Effect, CombinedEffects> getEffectData(PlayerEntity player, ServerWorld world) {
        return getEffectData(player, world, effect -> true);
    }

    public static CombinedEffects getEffectData(PlayerEntity player, ServerWorld world, Effect effect) {
        Map<Effect, CombinedEffects> effectData = getEffectData(player, world, otherEffect -> (otherEffect == effect));
        return effectData.getOrDefault(effect, new CombinedEffects());
    }

    public static Map<Effect, CombinedEffects> getEffectData(PlayerEntity player, ServerWorld world, Effect... effects) {
        Set<Effect> filter = Sets.newHashSet((Object[]) effects);
        return getEffectData(player, world, filter::contains);
    }

    public static Map<Effect, CombinedEffects> getEffectData(PlayerEntity player, ServerWorld world, Predicate<Effect> effectFilter) {
        Map<Effect, CombinedEffects> effectMap = new HashMap<>();
        TalentTree talents = PlayerTalentsData.get(world).getTalents(player);
        SetTree sets = PlayerSetsData.get(world).getSets(player);

        talents.getLearnedNodes(EffectTalent.class).stream().map(TalentNode::getTalent).forEach(effectTalent -> {
            if (effectFilter.test(effectTalent.getEffect())) {
                ((CombinedEffects) effectMap.computeIfAbsent(effectTalent.getEffect(), ())).addTalent(effectTalent);
            }
        });

        for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
            if (!(node.getSet() instanceof EffectSet))
                continue;
            EffectSet set = (EffectSet) node.getSet();
            if (effectFilter.test(((EffectTalent) set.getChild()).getEffect())) {
                CombinedEffects combinedEffects = effectMap.computeIfAbsent(((EffectTalent) set.getChild()).getEffect(), effect -> new CombinedEffects());
                combinedEffects.addTalent((EffectTalent) set.getChild());
            }
        }

        if (effectFilter.test(Effects.REGENERATION) && player.hasEffect(ModEffects.GHOST_WALK)) {
            AbilityTree abilities = PlayerAbilitiesData.get(world).getAbilities(player);

            for (AbilityNode<?, ?> node : (Iterable<AbilityNode<?, ?>>) abilities.getNodes()) {
                if (!node.isLearned() || !(node.getAbility() instanceof iskallia.vault.skill.ability.effect.GhostWalkAbility))
                    continue;
                AbilityConfig cfg = node.getAbilityConfig();
                if (cfg instanceof GhostWalkRegenerationConfig) {
                    GhostWalkRegenerationConfig config = (GhostWalkRegenerationConfig) cfg;

                    ((CombinedEffects) effectMap.computeIfAbsent(Effects.REGENERATION, effect -> new CombinedEffects()))
                            .addTalent(config.makeRegenerationTalent());
                }
            }
        }

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {


                List<EffectTalent> effects = (List<EffectTalent>) ((EffectTalentAttribute) ModAttributes.EXTRA_EFFECTS.getOrDefault(stack, new ArrayList())).getValue(stack);

                for (EffectTalent gearEffect : effects) {
                    if (effectFilter.test(gearEffect.getEffect())) {
                        ((CombinedEffects) effectMap.computeIfAbsent(gearEffect.getEffect(), effect -> new CombinedEffects())).addTalent(gearEffect);
                    }
                }
            }
        }
        ItemStack heldItem = player.getItemInHand(Hand.MAIN_HAND);
        if (heldItem.getItem() == ModItems.VAULT_PAXEL) {
            PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(heldItem);
            if (enhancement instanceof EffectEnhancement) {
                EffectEnhancement effectEnhancement = (EffectEnhancement) enhancement;
                if (effectFilter.test(effectEnhancement.getEffect())) {
                    ((CombinedEffects) effectMap.computeIfAbsent(effectEnhancement.getEffect(), ct -> new CombinedEffects()))
                            .addTalent(effectEnhancement.makeTalent());
                }
            }
        }

        if (WardTalent.isGrantedFullAbsorptionEffect(world, player)) {
            talents.getLearnedNodes(WardTalent.class).forEach(talent -> {
                EffectTalent effect = ((WardTalent) talent.getTalent()).getFullAbsorptionEffect();

                if (effect != null && effectFilter.test(effect.getEffect())) {
                    ((CombinedEffects) effectMap.computeIfAbsent(effect.getEffect(), ())).addTalent(effect);
                }
            });
        }

        VaultRaid vault = VaultRaidData.get(world).getActiveFor(player.getUUID());
        if (vault != null) {
            vault.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{player}, ), EffectModifier.class).forEach(modifier -> {
                if (effectFilter.test(modifier.getEffect())) {
                    ((CombinedEffects) effectMap.computeIfAbsent(modifier.getEffect(), ())).addTalent(modifier.makeTalent());
                }
            });

            vault.getInfluences().getInfluences(EffectInfluence.class).forEach(influence -> {
                if (effectFilter.test(influence.getEffect())) {
                    ((CombinedEffects) effectMap.computeIfAbsent(influence.getEffect(), ())).addTalent(influence.makeTalent());
                }
            });
        }


        AuraManager.getInstance().getAurasAffecting((Entity) player).stream()
                .filter(aura -> aura.getAura() instanceof EffectAuraConfig)
                .map(aura -> (EffectAuraConfig) aura.getAura())
                .forEach(effectAura -> {
                    EffectTalent auraTalent = effectAura.getEffect();


                    if (effectFilter.test(auraTalent.getEffect())) {
                        ((CombinedEffects) effectMap.computeIfAbsent(auraTalent.getEffect(), ())).addTalent(auraTalent);
                    }
                });

        effectMap.values().forEach(rec$ -> ((CombinedEffects) rec$).finish());
        return effectMap;
    }

    public static Map<Effect, CombinedEffects> getGearEffectData(LivingEntity entity) {
        return getGearEffectData(entity, effect -> true);
    }

    public static Map<Effect, CombinedEffects> getGearEffectData(LivingEntity entity, Predicate<Effect> effectFilter) {
        Map<Effect, CombinedEffects> effectMap = new HashMap<>();

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {


                List<EffectTalent> effects = (List<EffectTalent>) ((EffectTalentAttribute) ModAttributes.EXTRA_EFFECTS.getOrDefault(stack, new ArrayList())).getValue(stack);

                for (EffectTalent gearEffect : effects) {
                    if (effectFilter.test(gearEffect.getEffect())) {
                        ((CombinedEffects) effectMap.computeIfAbsent(gearEffect.getEffect(), effect -> new CombinedEffects())).addTalent(gearEffect);
                    }
                }
            }
        }
        effectMap.values().forEach(rec$ -> ((CombinedEffects) rec$).finish());
        return effectMap;
    }

    public static Collection<Effect> getImmunities(LivingEntity entity) {
        Set<Effect> immunities = new HashSet<>();

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = entity.getItemBySlot(slot);

            ModAttributes.EFFECT_IMMUNITY.get(stack)
                    .map(attribute -> (List) attribute.getValue(stack))
                    .ifPresent(effectList -> effectList.stream().map(EffectAttribute.Instance::toEffect).forEach(immunities::add));
        }


        if (PlayerSet.isActive(VaultGear.Set.DIVINITY, entity)) {
            ForgeRegistries.POTIONS.getValues().stream().filter(e -> !e.isBeneficial()).forEach(immunities::add);
        }

        return immunities;
    }


    public void onRemoved(PlayerEntity player) {
        player.removeEffect(getEffect());
    }

    public enum Type {
        HIDDEN("hidden", false, false),
        PARTICLES_ONLY("particles_only", true, false),
        ICON_ONLY("icon_only", false, true),
        ALL("all", true, true);
        private static final Map<String, Type> STRING_TO_TYPE;
        public final String name;
        public final boolean showParticles;
        public final boolean showIcon;

        static {
            STRING_TO_TYPE = (Map<String, Type>) Arrays.<Type>stream(values()).collect(Collectors.toMap(Type::toString, o -> o));
        }


        Type(String name, boolean showParticles, boolean showIcon) {
            this.name = name;
            this.showParticles = showParticles;
            this.showIcon = showIcon;
        }

        public static Type fromString(String type) {
            return STRING_TO_TYPE.get(type);
        }


        public String toString() {
            return this.name;
        }
    }

    public enum Operator {
        SET("set"), ADD("add");
        public final String name;
        private static final Map<String, Operator> STRING_TO_TYPE;

        static {
            STRING_TO_TYPE = (Map<String, Operator>) Arrays.<Operator>stream(values()).collect(Collectors.toMap(Operator::toString, o -> o));
        }


        Operator(String name) {
            this.name = name;
        }

        public static Operator fromString(String type) {
            return STRING_TO_TYPE.get(type);
        }


        public String toString() {
            return this.name;
        }
    }

    public static class CombinedEffects {
        private EffectTalent maxOverride = null;
        private final List<EffectTalent> addends = new ArrayList<>();

        private int amplifier = -1;
        private EffectTalent displayEffect = null;

        private void addTalent(EffectTalent talent) {
            if (talent.getOperator() == EffectTalent.Operator.SET) {
                setOverride(talent);
            } else if (talent.getOperator() == EffectTalent.Operator.ADD) {
                addAddend(talent);
            }
        }

        private void setOverride(EffectTalent talent) {
            if (this.maxOverride == null) {
                this.maxOverride = talent;
            } else if (talent.amplifier > this.maxOverride.amplifier) {
                this.maxOverride = talent;
            }
        }

        private void addAddend(EffectTalent talent) {
            this.addends.add(talent);
        }

        private void finish() {
            this.amplifier = (this.maxOverride == null) ? -1 : this.maxOverride.getAmplifier();
            this.amplifier += this.addends.stream().mapToInt(EffectTalent::getAmplifier).sum();

            if (this.maxOverride == null && !this.addends.isEmpty()) {
                this.displayEffect = this.addends.stream().max(Comparator.comparingInt(EffectTalent::getAmplifier)).get();
            } else {
                this.displayEffect = this.maxOverride;
            }
        }

        public int getAmplifier() {
            return this.amplifier;
        }

        @Nullable
        public EffectTalent getDisplayEffect() {
            return this.displayEffect;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\EffectTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */