package iskallia.vault.item;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.client.ClientTalentData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
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
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemResetFlask extends Item {
    public ItemResetFlask(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(3));

        setRegistryName(id);
    }


    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (ModConfigs.ABILITIES == null || ModConfigs.TALENTS == null) {
            return;
        }
        if (allowdedIn(group)) {
            for (AbilityGroup<?, ?> abilityGroup : (Iterable<AbilityGroup<?, ?>>) ModConfigs.ABILITIES.getAll()) {
                ItemStack stack = new ItemStack((IItemProvider) this);
                setSkillable(stack, abilityGroup.getParentName());
                items.add(stack);
            }
            for (TalentGroup<?> talentGroup : (Iterable<TalentGroup<?>>) ModConfigs.TALENTS.getAll()) {
                String talentName = talentGroup.getParentName();
                if (talentName.equals("Trader") || talentName.equals("Looter") || talentName
                        .equals("Artisan") || talentName.equals("Treasure Hunter") || talentName
                        .equals("Lucky Altar")) {
                    continue;
                }
                ItemStack stack = new ItemStack((IItemProvider) this);
                setSkillable(stack, talentGroup.getParentName());
                items.add(stack);
            }
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        String skillableStr = getSkillable(stack);
        if (skillableStr != null) {

            ModConfigs.ABILITIES.getAbility(skillableStr).ifPresent(abilityGrp -> {
                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(abilityGrp.getParentName())).withStyle(TextFormatting.GOLD);
                tooltip.add(StringTextComponent.EMPTY);
                tooltip.add((new StringTextComponent("Remove one level of Ability ")).append((ITextComponent) iFormattableTextComponent));
                tooltip.add(new StringTextComponent("and regain the Skillpoints spent."));
            });
            ModConfigs.TALENTS.getTalent(skillableStr).ifPresent(talentGrp -> {
                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(talentGrp.getParentName())).withStyle(TextFormatting.AQUA);
                tooltip.add(StringTextComponent.EMPTY);
                tooltip.add((new StringTextComponent("Remove one level of Talent ")).append((ITextComponent) iFormattableTextComponent));
                tooltip.add(new StringTextComponent("and regain the Skillpoints spent."));
            });
        }
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (getSkillable(stack) != null) {
            return;
        }
        if (world instanceof ServerWorld && entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);

                    ItemStack flask = new ItemStack((IItemProvider) this);
                    MiscUtils.giveItem(player, flask);
                }
            }

            List<String> skillables = new ArrayList<>();
            ModConfigs.ABILITIES.getAll().forEach(ability -> skillables.add(ability.getParentName()));
            ModConfigs.TALENTS.getAll().forEach(talent -> skillables.add(talent.getParentName()));

            skillables.remove("Trader");
            skillables.remove("Looter");
            skillables.remove("Artisan");
            skillables.remove("Treasure Hunter");
            skillables.remove("Lucky Altar");

            setSkillable(stack, (String) MiscUtils.getRandomEntry(skillables, random));
        }
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack held = player.getItemInHand(hand);
        String skillableStr = getSkillable(held);
        if (skillableStr == null) {
            return ActionResult.pass(held);
        }
        if (world.isClientSide()) {
            if (!canRevertSkillableClient(skillableStr)) {
                return ActionResult.pass(held);
            }
        } else if (player instanceof ServerPlayerEntity) {
            Optional<AbilityGroup<?, ?>> abilityOpt = ModConfigs.ABILITIES.getAbility(skillableStr);
            if (abilityOpt.isPresent()) {
                AbilityTree abilityTree = PlayerAbilitiesData.get(((ServerPlayerEntity) player).getLevel()).getAbilities(player);
                AbilityNode<?, ?> node = abilityTree.getNodeOf(abilityOpt.get());
                if (!node.isLearned()) {
                    return ActionResult.pass(held);
                }
                if (node.getLevel() == 1) {
                    List<AbilityGroup<?, ?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getAbilitiesDependingOn(node.getGroup().getParentName());
                    for (AbilityGroup<?, ?> dependent : dependentNodes) {
                        if (abilityTree.getNodeOf(dependent).isLearned()) {
                            return ActionResult.pass(held);
                        }
                    }
                }
            }
            Optional<TalentGroup<?>> talentOpt = ModConfigs.TALENTS.getTalent(skillableStr);
            if (talentOpt.isPresent()) {
                TalentTree talentTree = PlayerTalentsData.get(((ServerPlayerEntity) player).getLevel()).getTalents(player);
                TalentNode<?> node = talentTree.getNodeOf(talentOpt.get());
                if (!node.isLearned()) {
                    return ActionResult.pass(held);
                }
                if (node.getLevel() == 1) {
                    List<TalentGroup<?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getTalentsDependingOn(node.getGroup().getParentName());
                    for (TalentGroup<?> dependent : dependentNodes) {
                        if (talentTree.getNodeOf(dependent).isLearned()) {
                            return ActionResult.pass(held);
                        }
                    }
                }
            }
        } else {
            return ActionResult.pass(held);
        }
        player.startUsingItem(hand);
        return ActionResult.consume(held);
    }

    @OnlyIn(Dist.CLIENT)
    private boolean canRevertSkillableClient(String skillableStr) {
        Optional<AbilityGroup<?, ?>> abilityOpt = ModConfigs.ABILITIES.getAbility(skillableStr);
        if (abilityOpt.isPresent()) {
            AbilityNode<?, ?> node = ClientAbilityData.getLearnedAbilityNode(abilityOpt.get());
            if (node != null && node.isLearned()) {
                if (node.getLevel() == 1) {
                    List<AbilityGroup<?, ?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getAbilitiesDependingOn(node.getGroup().getParentName());
                    for (AbilityGroup<?, ?> dependent : dependentNodes) {
                        if (ClientAbilityData.getLearnedAbilityNode(dependent) != null) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        Optional<TalentGroup<?>> talentOpt = ModConfigs.TALENTS.getTalent(skillableStr);
        if (talentOpt.isPresent()) {
            TalentNode<?> node = ClientTalentData.getLearnedTalentNode(talentOpt.get());
            if (node != null && node.isLearned()) {
                if (node.getLevel() == 1) {
                    List<TalentGroup<?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getTalentsDependingOn(node.getGroup().getParentName());
                    for (TalentGroup<?> dependent : dependentNodes) {
                        if (ClientTalentData.getLearnedTalentNode(dependent) != null) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }


    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }


    public int getUseDuration(ItemStack stack) {
        return 24;
    }


    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }

    public static void setSkillable(ItemStack stack, @Nullable String ability) {
        if (!(stack.getItem() instanceof ItemResetFlask)) {
            return;
        }
        stack.getOrCreateTag().putString("Skillable", ability);
    }

    @Nullable
    public static String getSkillable(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemResetFlask)) {
            return null;
        }
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.contains("Skillable", 8) ? tag.getString("Skillable") : null;
    }


    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entityLiving) {
        if (world instanceof ServerWorld && entityLiving instanceof ServerPlayerEntity) {
            String skillableStr = getSkillable(stack);
            if (skillableStr == null) {
                return stack;
            }

            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            ServerWorld sWorld = (ServerWorld) world;

            ModConfigs.ABILITIES.getAbility(skillableStr).ifPresent(ability -> {
                PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get(sWorld);
                AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity) player);
                AbilityNode<?, ?> node = abilityTree.getNodeOf(ability);
                if (node.isLearned()) {
                    if (node.getLevel() == 1) {
                        List<AbilityGroup<?, ?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getAbilitiesDependingOn(node.getGroup().getParentName());
                        for (AbilityGroup<?, ?> dependent : dependentNodes) {
                            if (abilityTree.getNodeOf(dependent).isLearned()) {
                                return;
                            }
                        }
                    }
                    PlayerVaultStatsData.get(sWorld).spendSkillPts(player, -node.getAbilityConfig().getLearningCost());
                    abilitiesData.downgradeAbility(player, node);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
            });
            ModConfigs.TALENTS.getTalent(skillableStr).ifPresent(talent -> {
                PlayerTalentsData talentsData = PlayerTalentsData.get(sWorld);
                TalentTree talentTree = talentsData.getTalents((PlayerEntity) player);
                TalentNode<?> node = talentTree.getNodeOf(talent);
                if (node.isLearned()) {
                    if (node.getLevel() == 1) {
                        List<TalentGroup<?>> dependentNodes = ModConfigs.SKILL_GATES.getGates().getTalentsDependingOn(node.getGroup().getParentName());
                        for (TalentGroup<?> dependent : dependentNodes) {
                            if (talentTree.getNodeOf(dependent).isLearned()) {
                                return;
                            }
                        }
                    }
                    PlayerVaultStatsData.get(sWorld).spendSkillPts(player, -node.getTalent().getCost());
                    talentsData.downgradeTalent(player, node);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
            });
        }
        return stack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemResetFlask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */