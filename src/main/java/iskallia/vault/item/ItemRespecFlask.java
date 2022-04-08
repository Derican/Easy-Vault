package iskallia.vault.item;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRespecFlask extends Item {
    public ItemRespecFlask(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(2));

        setRegistryName(id);
    }


    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (ModConfigs.ABILITIES == null) {
            return;
        }
        if (allowdedIn(group)) {
            for (AbilityGroup<?, ?> abilityGroup : (Iterable<AbilityGroup<?, ?>>) ModConfigs.ABILITIES.getAll()) {
                ItemStack stack = new ItemStack((IItemProvider) this);
                setAbility(stack, abilityGroup.getParentName());
                items.add(stack);
            }
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        String abilityStr = getAbility(stack);
        if (abilityStr != null) {
            AbilityGroup<?, ?> grp = ModConfigs.ABILITIES.getAbilityGroupByName(abilityStr);
            IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(grp.getParentName())).withStyle(TextFormatting.GOLD);

            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add(new StringTextComponent("Use to remove selected specialization"));
            tooltip.add((new StringTextComponent("of ability ")).append((ITextComponent) iFormattableTextComponent));
        }
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (getAbility(stack) != null) {
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

            List<AbilityGroup<?, ?>> abilities = ModConfigs.ABILITIES.getAll();
            AbilityGroup<?, ?> group = abilities.get(random.nextInt(abilities.size()));
            setAbility(stack, group.getParentName());
        }
    }


    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    public static void setAbility(ItemStack stack, @Nullable String ability) {
        if (!(stack.getItem() instanceof ItemRespecFlask)) {
            return;
        }
        stack.getOrCreateTag().putString("Ability", ability);
    }

    @Nullable
    public static String getAbility(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemRespecFlask)) {
            return null;
        }
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.contains("Ability", 8) ? tag.getString("Ability") : null;
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack held = player.getItemInHand(hand);
        String abilityStr = getAbility(held);
        if (abilityStr == null) {
            return ActionResult.pass(held);
        }
        if (world.isClientSide()) {
            if (!hasAbilityClient(abilityStr)) {
                return ActionResult.pass(held);
            }
        } else if (player instanceof ServerPlayerEntity) {
            AbilityTree tree = PlayerAbilitiesData.get(((ServerPlayerEntity) player).getLevel()).getAbilities(player);
            AbilityNode<?, ?> node = tree.getNodeByName(abilityStr);
            if (!node.isLearned() || node.getSpecialization() == null) {
                return ActionResult.pass(held);
            }
        } else {
            return ActionResult.pass(held);
        }
        player.startUsingItem(hand);
        return ActionResult.consume(held);
    }

    @OnlyIn(Dist.CLIENT)
    private boolean hasAbilityClient(String abilityStr) {
        AbilityNode<?, ?> node = ClientAbilityData.getLearnedAbilityNode(abilityStr);
        if (node == null) {
            return false;
        }
        return (node.isLearned() && node.getSpecialization() != null);
    }


    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }


    public int getUseDuration(ItemStack stack) {
        return 24;
    }


    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entityLiving) {
        if (world instanceof ServerWorld && entityLiving instanceof ServerPlayerEntity) {
            String abilityStr = getAbility(stack);
            if (abilityStr == null) {
                return stack;
            }

            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            ServerWorld sWorld = (ServerWorld) world;

            PlayerAbilitiesData data = PlayerAbilitiesData.get(sWorld);
            AbilityNode<?, ?> node = data.getAbilities((PlayerEntity) player).getNodeByName(abilityStr);
            if (node.isLearned() && node.getSpecialization() != null) {
                data.selectSpecialization(player, abilityStr, null);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                return stack;
            }
        }
        return stack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemRespecFlask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */