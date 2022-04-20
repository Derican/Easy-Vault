package iskallia.vault.item;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.client.ClientTalentData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ItemResetFlask extends Item {
    public ItemResetFlask(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).stacksTo(3));
        this.setRegistryName(id);
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (ModConfigs.ABILITIES == null || ModConfigs.TALENTS == null) {
            return;
        }
        if (this.allowdedIn(group)) {
            for (final AbilityGroup<?, ?> abilityGroup : ModConfigs.ABILITIES.getAll()) {
                final ItemStack stack = new ItemStack((IItemProvider) this);
                setSkillable(stack, abilityGroup.getParentName());
                items.add(stack);
            }
            for (final TalentGroup<?> talentGroup : ModConfigs.TALENTS.getAll()) {
                final String talentName = talentGroup.getParentName();
                if (!talentName.equals("Trader") && !talentName.equals("Looter") && !talentName.equals("Artisan") && !talentName.equals("Treasure Hunter")) {
                    if (talentName.equals("Lucky Altar")) {
                        continue;
                    }
                    final ItemStack stack2 = new ItemStack((IItemProvider) this);
                    setSkillable(stack2, talentGroup.getParentName());
                    items.add(stack2);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        final String skillableStr = getSkillable(stack);
        if (skillableStr != null) {
            ModConfigs.ABILITIES.getAbility(skillableStr).ifPresent(abilityGrp -> {
                final ITextComponent ability = (ITextComponent) new StringTextComponent(abilityGrp.getParentName()).withStyle(TextFormatting.GOLD);
                tooltip.add(StringTextComponent.EMPTY);
                tooltip.add(new StringTextComponent("Remove one level of Ability ").append(ability));
                tooltip.add(new StringTextComponent("and regain the Skillpoints spent."));
                return;
            });
            ModConfigs.TALENTS.getTalent(skillableStr).ifPresent(talentGrp -> {
                final ITextComponent talent = (ITextComponent) new StringTextComponent(talentGrp.getParentName()).withStyle(TextFormatting.AQUA);
                tooltip.add(StringTextComponent.EMPTY);
                tooltip.add(new StringTextComponent("Remove one level of Talent ").append(talent));
                tooltip.add(new StringTextComponent("and regain the Skillpoints spent."));
            });
        }
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (getSkillable(stack) != null) {
            return;
        }
        if (world instanceof ServerWorld && entity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);
                    final ItemStack flask = new ItemStack((IItemProvider) this);
                    MiscUtils.giveItem(player, flask);
                }
            }
            final List<String> skillables = new ArrayList<String>();
            ModConfigs.ABILITIES.getAll().forEach(ability -> skillables.add(ability.getParentName()));
            ModConfigs.TALENTS.getAll().forEach(talent -> skillables.add(talent.getParentName()));
            skillables.remove("Trader");
            skillables.remove("Looter");
            skillables.remove("Artisan");
            skillables.remove("Treasure Hunter");
            skillables.remove("Lucky Altar");
            setSkillable(stack, MiscUtils.getRandomEntry(skillables, ItemResetFlask.random));
        }
    }

    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        final ItemStack held = player.getItemInHand(hand);
        final String skillableStr = getSkillable(held);
        if (skillableStr == null) {
            return (ActionResult<ItemStack>) ActionResult.pass(held);
        }
        if (world.isClientSide()) {
            if (!this.canRevertSkillableClient(skillableStr)) {
                return (ActionResult<ItemStack>) ActionResult.pass(held);
            }
        } else {
            if (!(player instanceof ServerPlayerEntity)) {
                return (ActionResult<ItemStack>) ActionResult.pass(held);
            }
            final Optional<AbilityGroup<?, ?>> abilityOpt = ModConfigs.ABILITIES.getAbility(skillableStr);
            if (abilityOpt.isPresent()) {
                final AbilityTree abilityTree = PlayerAbilitiesData.get(((ServerPlayerEntity) player).getLevel()).getAbilities(player);
                final AbilityNode<?, ?> node = abilityTree.getNodeOf(abilityOpt.get());
                if (!node.isLearned()) {
                    return (ActionResult<ItemStack>) ActionResult.pass(held);
                }
                if (node.getLevel() == 1) {
                    final List<AbilityGroup<?, ?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getAbilitiesDependingOn(node.getGroup().getParentName());
                    for (final AbilityGroup<?, ?> dependent : dependentNodes) {
                        if (abilityTree.getNodeOf(dependent).isLearned()) {
                            return (ActionResult<ItemStack>) ActionResult.pass(held);
                        }
                    }
                }
            }
            final Optional<TalentGroup<?>> talentOpt = ModConfigs.TALENTS.getTalent(skillableStr);
            if (talentOpt.isPresent()) {
                final TalentTree talentTree = PlayerTalentsData.get(((ServerPlayerEntity) player).getLevel()).getTalents(player);
                final TalentNode<?> node2 = talentTree.getNodeOf(talentOpt.get());
                if (!node2.isLearned()) {
                    return (ActionResult<ItemStack>) ActionResult.pass(held);
                }
                if (node2.getLevel() == 1) {
                    final List<TalentGroup<?>> dependentNodes2 = ModConfigs.SKILL_GATES.getGates().getTalentsDependingOn(node2.getGroup().getParentName());
                    for (final TalentGroup<?> dependent2 : dependentNodes2) {
                        if (talentTree.getNodeOf(dependent2).isLearned()) {
                            return (ActionResult<ItemStack>) ActionResult.pass(held);
                        }
                    }
                }
            }
        }
        player.startUsingItem(hand);
        return (ActionResult<ItemStack>) ActionResult.consume(held);
    }

    @OnlyIn(Dist.CLIENT)
    private boolean canRevertSkillableClient(final String skillableStr) {
        final Optional<AbilityGroup<?, ?>> abilityOpt = ModConfigs.ABILITIES.getAbility(skillableStr);
        if (abilityOpt.isPresent()) {
            final AbilityNode<?, ?> node = ClientAbilityData.getLearnedAbilityNode(abilityOpt.get());
            if (node != null && node.isLearned()) {
                if (node.getLevel() == 1) {
                    final List<AbilityGroup<?, ?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getAbilitiesDependingOn(node.getGroup().getParentName());
                    for (final AbilityGroup<?, ?> dependent : dependentNodes) {
                        if (ClientAbilityData.getLearnedAbilityNode(dependent) != null) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        final Optional<TalentGroup<?>> talentOpt = ModConfigs.TALENTS.getTalent(skillableStr);
        if (talentOpt.isPresent()) {
            final TalentNode<?> node2 = ClientTalentData.getLearnedTalentNode(talentOpt.get());
            if (node2 != null && node2.isLearned()) {
                if (node2.getLevel() == 1) {
                    final List<TalentGroup<?>> dependentNodes2 = ModConfigs.SKILL_GATES.getGates().getTalentsDependingOn(node2.getGroup().getParentName());
                    for (final TalentGroup<?> dependent2 : dependentNodes2) {
                        if (ClientTalentData.getLearnedTalentNode(dependent2) != null) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public UseAction getUseAnimation(final ItemStack stack) {
        return UseAction.DRINK;
    }

    public int getUseDuration(final ItemStack stack) {
        return 24;
    }

    public Rarity getRarity(final ItemStack stack) {
        return Rarity.RARE;
    }

    public static void setSkillable(final ItemStack stack, @Nullable final String ability) {
        if (!(stack.getItem() instanceof ItemResetFlask)) {
            return;
        }
        stack.getOrCreateTag().putString("Skillable", ability);
    }

    @Nullable
    public static String getSkillable(final ItemStack stack) {
        if (!(stack.getItem() instanceof ItemResetFlask)) {
            return null;
        }
        final CompoundNBT tag = stack.getOrCreateTag();
        return tag.contains("Skillable", 8) ? tag.getString("Skillable") : null;
    }

    public ItemStack finishUsingItem(final ItemStack stack, final World world, final LivingEntity entityLiving) {
        if (world instanceof ServerWorld && entityLiving instanceof ServerPlayerEntity) {
            final String skillableStr = getSkillable(stack);
            if (skillableStr == null) {
                return stack;
            }
            final ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            final ServerWorld sWorld = (ServerWorld) world;
            ModConfigs.ABILITIES.getAbility(skillableStr).ifPresent(ability -> {
                final PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get(sWorld);
                final AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity) player);
                final AbilityNode<?, ?> node = abilityTree.getNodeOf(ability);
                if (node.isLearned()) {
                    if (node.getLevel() == 1) {
                        final List<AbilityGroup<?, ?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getAbilitiesDependingOn(node.getGroup().getParentName());

                        final Iterator<AbilityGroup<?, ?>> iterator = dependentNodes.iterator();
                        while (iterator.hasNext()) {
                            final AbilityGroup<?, ?> dependent = iterator.next();
                            if (abilityTree.getNodeOf(dependent).isLearned()) {
                                return;
                            }
                        }
                    }
                    PlayerVaultStatsData.get(sWorld).spendSkillPts(player, -((AbilityConfig) node.getAbilityConfig()).getLearningCost());
                    abilitiesData.downgradeAbility(player, node);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
                return;
            });
            ModConfigs.TALENTS.getTalent(skillableStr).ifPresent(talent -> {
                final PlayerTalentsData talentsData = PlayerTalentsData.get(sWorld);
                final TalentTree talentTree = talentsData.getTalents((PlayerEntity) player);
                final TalentNode<?> node2 = talentTree.getNodeOf((TalentGroup<?>) talent);
                if (node2.isLearned()) {
                    if (node2.getLevel() == 1) {
                        final List<TalentGroup<?>> dependentNodes2 = ModConfigs.SKILL_GATES.getGates().getTalentsDependingOn(node2.getGroup().getParentName());

                        final Iterator<TalentGroup<?>> iterator2 = dependentNodes2.iterator();
                        while (iterator2.hasNext()) {
                            final TalentGroup<?> dependent2 = iterator2.next();
                            if (talentTree.getNodeOf(dependent2).isLearned()) {
                                return;
                            }
                        }
                    }
                    PlayerVaultStatsData.get(sWorld).spendSkillPts(player, -((PlayerTalent) node2.getTalent()).getCost());
                    talentsData.downgradeTalent(player, node2);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
                return;
            });
        }
        return stack;
    }
}
