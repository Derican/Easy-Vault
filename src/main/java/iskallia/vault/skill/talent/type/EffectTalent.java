// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.common.collect.Sets;
import com.google.gson.annotations.Expose;
import iskallia.vault.attribute.EffectAttribute;
import iskallia.vault.attribute.VAttribute;
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
import iskallia.vault.skill.ability.effect.GhostWalkAbility;
import iskallia.vault.skill.set.EffectSet;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.archetype.WardTalent;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerSetsData;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class EffectTalent extends PlayerTalent
{
    @Expose
    private final String effect;
    @Expose
    private final int amplifier;
    @Expose
    private final String type;
    @Expose
    private final String operator;
    
    @Override
    public String toString() {
        return "EffectTalent{effect='" + this.effect + '\'' + ", amplifier=" + this.amplifier + ", type='" + this.type + '\'' + ", operator='" + this.operator + '\'' + '}';
    }
    
    public EffectTalent(final int cost, final Effect effect, final int amplifier, final Type type, final Operator operator) {
        this(cost, Registry.MOB_EFFECT.getKey(effect).toString(), amplifier, type.toString(), operator.toString());
    }
    
    public EffectTalent(final int cost, final String effect, final int amplifier, final String type, final String operator) {
        super(cost);
        this.effect = effect;
        this.amplifier = amplifier;
        this.type = type;
        this.operator = operator;
    }
    
    public Effect getEffect() {
        return (Effect)Registry.MOB_EFFECT.get(new ResourceLocation(this.effect));
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
    
    public EffectInstance makeEffect(final int duration) {
        return new EffectInstance(this.getEffect(), duration, this.getAmplifier(), true, this.getType().showParticles, this.getType().showIcon);
    }
    
    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        final PlayerEntity player = event.player;
        if (player.level.isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }
        final Collection<Effect> immunities = getImmunities((LivingEntity)player);
        final Map<Effect, CombinedEffects> effectMap = getEffectData(player, (ServerWorld)player.getCommandSenderWorld(), effect -> !immunities.contains(effect));
        applyEffects((LivingEntity)player, effectMap);
    }
    
    public static void applyEffects(final LivingEntity entity, final Map<Effect, CombinedEffects> effects) {
        effects.forEach((effect, combinedEffects) -> {
            final int amplifier = combinedEffects.getAmplifier();
            if (amplifier >= 0) {
                final EffectTalent displayTalent = combinedEffects.getDisplayEffect();
                final EffectInstance activeEffect = entity.getEffect(effect);
                final EffectInstance newEffect = new EffectInstance(effect, 339, amplifier, false, displayTalent.getType().showParticles, displayTalent.getType().showIcon);
                if (activeEffect == null || activeEffect.getAmplifier() < newEffect.getAmplifier()) {
                    entity.addEffect(newEffect);
                }
                else if (activeEffect.getDuration() <= 259) {
                    entity.addEffect(newEffect);
                }
            }
        });
    }
    
    public static Map<Effect, CombinedEffects> getEffectData(final PlayerEntity player, final ServerWorld world) {
        return getEffectData(player, world, effect -> true);
    }
    
    public static CombinedEffects getEffectData(final PlayerEntity player, final ServerWorld world, final Effect effect) {
        final Map<Effect, CombinedEffects> effectData = getEffectData(player, world, otherEffect -> otherEffect == effect);
        return effectData.getOrDefault(effect, new CombinedEffects());
    }
    
    public static Map<Effect, CombinedEffects> getEffectData(final PlayerEntity player, final ServerWorld world, final Effect... effects) {
        final Set<Effect> filter = Sets.newHashSet(effects);
        return getEffectData(player, world, filter::contains);
    }
    
    public static Map<Effect, CombinedEffects> getEffectData(final PlayerEntity player, final ServerWorld world, final Predicate<Effect> effectFilter) {
        final Map<Effect, CombinedEffects> effectMap = new HashMap<Effect, CombinedEffects>();
        final TalentTree talents = PlayerTalentsData.get(world).getTalents(player);
        final SetTree sets = PlayerSetsData.get(world).getSets(player);
        talents.getLearnedNodes(EffectTalent.class).stream().map(TalentNode::getTalent).forEach(effectTalent -> {
            if (effectFilter.test(((EffectTalent)effectTalent).getEffect())) {
                effectMap.computeIfAbsent(((EffectTalent)effectTalent).getEffect(), effect -> new CombinedEffects()).addTalent(((EffectTalent)effectTalent));
            }
            return;
        });
        for (final SetNode<?> node : sets.getNodes()) {
            if (!(node.getSet() instanceof EffectSet)) {
                continue;
            }
            final EffectSet set = (EffectSet)node.getSet();
            if (!effectFilter.test(set.getChild().getEffect())) {
                continue;
            }
            final CombinedEffects combinedEffects = effectMap.computeIfAbsent(set.getChild().getEffect(), effect -> new CombinedEffects());
            combinedEffects.addTalent(set.getChild());
        }
        if (effectFilter.test(Effects.REGENERATION) && player.hasEffect(ModEffects.GHOST_WALK)) {
            final AbilityTree abilities = PlayerAbilitiesData.get(world).getAbilities(player);
            for (final AbilityNode<?, ?> node2 : abilities.getNodes()) {
                if (node2.isLearned()) {
                    if (!(node2.getAbility() instanceof GhostWalkAbility)) {
                        continue;
                    }
                    final AbilityConfig cfg = (AbilityConfig)node2.getAbilityConfig();
                    if (!(cfg instanceof GhostWalkRegenerationConfig)) {
                        continue;
                    }
                    final GhostWalkRegenerationConfig config = (GhostWalkRegenerationConfig)cfg;
                    effectMap.computeIfAbsent(Effects.REGENERATION, effect -> new CombinedEffects()).addTalent(config.makeRegenerationTalent());
                }
            }
        }
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear)stack.getItem()).isIntendedForSlot(slot)) {
                final List<EffectTalent> effects = ModAttributes.EXTRA_EFFECTS.getOrDefault(stack, new ArrayList<EffectTalent>()).getValue(stack);
                for (final EffectTalent gearEffect : effects) {
                    if (effectFilter.test(gearEffect.getEffect())) {
                        effectMap.computeIfAbsent(gearEffect.getEffect(), effect -> new CombinedEffects()).addTalent(gearEffect);
                    }
                }
            }
        }
        final ItemStack heldItem = player.getItemInHand(Hand.MAIN_HAND);
        if (heldItem.getItem() == ModItems.VAULT_PAXEL) {
            final PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(heldItem);
            if (enhancement instanceof EffectEnhancement) {
                final EffectEnhancement effectEnhancement = (EffectEnhancement)enhancement;
                if (effectFilter.test(effectEnhancement.getEffect())) {
                    effectMap.computeIfAbsent(effectEnhancement.getEffect(), ct -> new CombinedEffects()).addTalent(effectEnhancement.makeTalent());
                }
            }
        }
        if (WardTalent.isGrantedFullAbsorptionEffect(world, player)) {
            talents.getLearnedNodes(WardTalent.class).forEach(talent -> {
                final EffectTalent effect = ((WardTalent)talent.getTalent()).getFullAbsorptionEffect();
                if (effect != null && effectFilter.test(effect.getEffect())) {
                    effectMap.computeIfAbsent(effect.getEffect(), ct -> new CombinedEffects()).addTalent(effect);
                }
                return;
            });
        }
        final VaultRaid vault = VaultRaidData.get(world).getActiveFor(player.getUUID());
        if (vault != null) {
            vault.getActiveModifiersFor(PlayerFilter.of(player), EffectModifier.class).forEach(modifier -> {
                if (effectFilter.test(modifier.getEffect())) {
                    effectMap.computeIfAbsent(modifier.getEffect(), effect -> new CombinedEffects()).addTalent(modifier.makeTalent());
                }
                return;
            });
            vault.getInfluences().getInfluences(EffectInfluence.class).forEach(influence -> {
                if (effectFilter.test(((EffectInfluence)influence).getEffect())) {
                    effectMap.computeIfAbsent(((EffectInfluence)influence).getEffect(), effect -> new CombinedEffects()).addTalent(((EffectInfluence)influence).makeTalent());
                }
                return;
            });
        }
        AuraManager.getInstance().getAurasAffecting(player).stream().filter(aura -> aura.getAura() instanceof EffectAuraConfig).map(aura -> aura.getAura()).forEach(effectAura -> {
            final EffectTalent auraTalent = ((EffectAuraConfig)effectAura).getEffect();
            if (effectFilter.test(auraTalent.getEffect())) {
                effectMap.computeIfAbsent(auraTalent.getEffect(), effect -> new CombinedEffects()).addTalent(auraTalent);
            }
            return;
        });
        effectMap.values().forEach(rec$ -> ((CombinedEffects)rec$).finish());
        return effectMap;
    }
    
    public static Map<Effect, CombinedEffects> getGearEffectData(final LivingEntity entity) {
        return getGearEffectData(entity, effect -> true);
    }
    
    public static Map<Effect, CombinedEffects> getGearEffectData(final LivingEntity entity, final Predicate<Effect> effectFilter) {
        final Map<Effect, CombinedEffects> effectMap = new HashMap<Effect, CombinedEffects>();
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear)stack.getItem()).isIntendedForSlot(slot)) {
                final List<EffectTalent> effects = ModAttributes.EXTRA_EFFECTS.getOrDefault(stack, new ArrayList<EffectTalent>()).getValue(stack);
                for (final EffectTalent gearEffect : effects) {
                    if (effectFilter.test(gearEffect.getEffect())) {
                        effectMap.computeIfAbsent(gearEffect.getEffect(), effect -> new CombinedEffects()).addTalent(gearEffect);
                    }
                }
            }
        }
        effectMap.values().forEach(rec$ -> ((CombinedEffects)rec$).finish());
        return effectMap;
    }
    
    public static Collection<Effect> getImmunities(final LivingEntity entity) {
        final Set<Effect> immunities = new HashSet<Effect>();
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = entity.getItemBySlot(slot);
            ModAttributes.EFFECT_IMMUNITY.get(stack).map(attribute -> (attribute).getValue(stack)).ifPresent(effectList -> effectList.stream().map(EffectAttribute.Instance::toEffect).forEach(immunities::add));
        }
        if (PlayerSet.isActive(VaultGear.Set.DIVINITY, entity)) {
            ForgeRegistries.POTIONS.getValues().stream().filter(e -> !e.isBeneficial()).forEach(immunities::add);
        }
        return immunities;
    }
    
    @Override
    public void onRemoved(final PlayerEntity player) {
        player.removeEffect(this.getEffect());
    }
    
    public enum Type
    {
        HIDDEN("hidden", false, false), 
        PARTICLES_ONLY("particles_only", true, false), 
        ICON_ONLY("icon_only", false, true), 
        ALL("all", true, true);
        
        private static final Map<String, Type> STRING_TO_TYPE;
        public final String name;
        public final boolean showParticles;
        public final boolean showIcon;
        
        private Type(final String name, final boolean showParticles, final boolean showIcon) {
            this.name = name;
            this.showParticles = showParticles;
            this.showIcon = showIcon;
        }
        
        public static Type fromString(final String type) {
            return Type.STRING_TO_TYPE.get(type);
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        static {
            STRING_TO_TYPE = Arrays.stream(values()).collect(Collectors.toMap((Function<? super Type, ? extends String>)Type::toString, o -> o));
        }
    }
    
    public enum Operator
    {
        SET("set"), 
        ADD("add");
        
        private static final Map<String, Operator> STRING_TO_TYPE;
        public final String name;
        
        private Operator(final String name) {
            this.name = name;
        }
        
        public static Operator fromString(final String type) {
            return Operator.STRING_TO_TYPE.get(type);
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        static {
            STRING_TO_TYPE = Arrays.stream(values()).collect(Collectors.toMap((Function<? super Operator, ? extends String>)Operator::toString, o -> o));
        }
    }
    
    public static class CombinedEffects
    {
        private EffectTalent maxOverride;
        private final List<EffectTalent> addends;
        private int amplifier;
        private EffectTalent displayEffect;
        
        public CombinedEffects() {
            this.maxOverride = null;
            this.addends = new ArrayList<EffectTalent>();
            this.amplifier = -1;
            this.displayEffect = null;
        }
        
        private void addTalent(final EffectTalent talent) {
            if (talent.getOperator() == Operator.SET) {
                this.setOverride(talent);
            }
            else if (talent.getOperator() == Operator.ADD) {
                this.addAddend(talent);
            }
        }
        
        private void setOverride(final EffectTalent talent) {
            if (this.maxOverride == null) {
                this.maxOverride = talent;
            }
            else if (talent.amplifier > this.maxOverride.amplifier) {
                this.maxOverride = talent;
            }
        }
        
        private void addAddend(final EffectTalent talent) {
            this.addends.add(talent);
        }
        
        private void finish() {
            this.amplifier = ((this.maxOverride == null) ? -1 : this.maxOverride.getAmplifier());
            this.amplifier += this.addends.stream().mapToInt(EffectTalent::getAmplifier).sum();
            if (this.maxOverride == null && !this.addends.isEmpty()) {
                this.displayEffect = this.addends.stream().max(Comparator.comparingInt(EffectTalent::getAmplifier)).get();
            }
            else {
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
