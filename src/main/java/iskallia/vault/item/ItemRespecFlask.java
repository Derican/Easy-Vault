package iskallia.vault.item;

import iskallia.vault.client.ClientAbilityData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.PlayerAbilitiesData;
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
import java.util.List;

public class ItemRespecFlask extends Item {
    public ItemRespecFlask(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).stacksTo(2));
        this.setRegistryName(id);
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (ModConfigs.ABILITIES == null) {
            return;
        }
        if (this.allowdedIn(group)) {
            for (final AbilityGroup<?, ?> abilityGroup : ModConfigs.ABILITIES.getAll()) {
                final ItemStack stack = new ItemStack(this);
                setAbility(stack, abilityGroup.getParentName());
                items.add(stack);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        final String abilityStr = getAbility(stack);
        if (abilityStr != null) {
            final AbilityGroup<?, ?> grp = ModConfigs.ABILITIES.getAbilityGroupByName(abilityStr);
            final ITextComponent ability = new StringTextComponent(grp.getParentName()).withStyle(TextFormatting.GOLD);
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add(new StringTextComponent("Use to remove selected specialization"));
            tooltip.add(new StringTextComponent("of ability ").append(ability));
        }
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (getAbility(stack) != null) {
            return;
        }
        if (world instanceof ServerWorld && entity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);
                    final ItemStack flask = new ItemStack(this);
                    MiscUtils.giveItem(player, flask);
                }
            }
            final List<AbilityGroup<?, ?>> abilities = ModConfigs.ABILITIES.getAll();
            final AbilityGroup<?, ?> group = abilities.get(ItemRespecFlask.random.nextInt(abilities.size()));
            setAbility(stack, group.getParentName());
        }
    }

    public Rarity getRarity(final ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    public static void setAbility(final ItemStack stack, @Nullable final String ability) {
        if (!(stack.getItem() instanceof ItemRespecFlask)) {
            return;
        }
        stack.getOrCreateTag().putString("Ability", ability);
    }

    @Nullable
    public static String getAbility(final ItemStack stack) {
        if (!(stack.getItem() instanceof ItemRespecFlask)) {
            return null;
        }
        final CompoundNBT tag = stack.getOrCreateTag();
        return tag.contains("Ability", 8) ? tag.getString("Ability") : null;
    }

    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        final ItemStack held = player.getItemInHand(hand);
        final String abilityStr = getAbility(held);
        if (abilityStr == null) {
            return ActionResult.pass(held);
        }
        if (world.isClientSide()) {
            if (!this.hasAbilityClient(abilityStr)) {
                return ActionResult.pass(held);
            }
        } else {
            if (!(player instanceof ServerPlayerEntity)) {
                return ActionResult.pass(held);
            }
            final AbilityTree tree = PlayerAbilitiesData.get(((ServerPlayerEntity) player).getLevel()).getAbilities(player);
            final AbilityNode<?, ?> node = tree.getNodeByName(abilityStr);
            if (!node.isLearned() || node.getSpecialization() == null) {
                return ActionResult.pass(held);
            }
        }
        player.startUsingItem(hand);
        return ActionResult.consume(held);
    }

    @OnlyIn(Dist.CLIENT)
    private boolean hasAbilityClient(final String abilityStr) {
        final AbilityNode<?, ?> node = ClientAbilityData.getLearnedAbilityNode(abilityStr);
        return node != null && node.isLearned() && node.getSpecialization() != null;
    }

    public UseAction getUseAnimation(final ItemStack stack) {
        return UseAction.DRINK;
    }

    public int getUseDuration(final ItemStack stack) {
        return 24;
    }

    public ItemStack finishUsingItem(final ItemStack stack, final World world, final LivingEntity entityLiving) {
        if (world instanceof ServerWorld && entityLiving instanceof ServerPlayerEntity) {
            final String abilityStr = getAbility(stack);
            if (abilityStr == null) {
                return stack;
            }
            final ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            final ServerWorld sWorld = (ServerWorld) world;
            final PlayerAbilitiesData data = PlayerAbilitiesData.get(sWorld);
            final AbilityNode<?, ?> node = data.getAbilities(player).getNodeByName(abilityStr);
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
