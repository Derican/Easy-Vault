package iskallia.vault.item.paxel;

import com.google.common.collect.Multimap;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.paxel.enhancement.DurabilityEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.OverlevelEnchantHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VaultPaxelItem extends ToolItem {
    public static final ToolType PAXEL_TOOL_TYPE;

    public VaultPaxelItem(final ResourceLocation id) {
        super(3.0f, -3.0f, PaxelItemTier.INSTANCE, Collections.emptySet(), new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).addToolType(VaultPaxelItem.PAXEL_TOOL_TYPE, PaxelItemTier.INSTANCE.getLevel()).stacksTo(1));
        this.setRegistryName(id);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlotType slot, final ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
    }

    public int getMaxDamage(final ItemStack itemStack) {
        final PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(itemStack);
        if (enhancement instanceof DurabilityEnhancement) {
            return super.getMaxDamage(itemStack) + ((DurabilityEnhancement) enhancement).getExtraDurability();
        }
        return super.getMaxDamage(itemStack);
    }

    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        return enchantment != Enchantments.MENDING && (enchantment.canEnchant(new ItemStack(Items.DIAMOND_SWORD)) || enchantment.canEnchant(new ItemStack(Items.DIAMOND_AXE)) || enchantment.canEnchant(new ItemStack(Items.DIAMOND_SHOVEL)) || enchantment.canEnchant(new ItemStack(Items.DIAMOND_HOE)));
    }

    public boolean isBookEnchantable(final ItemStack stack, final ItemStack book) {
        if (EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.MENDING)) {
            return false;
        }
        for (final Enchantment e : EnchantmentHelper.getEnchantments(book).keySet()) {
            if (e.canEnchant(new ItemStack(Items.DIAMOND_SWORD)) || e.canEnchant(new ItemStack(Items.DIAMOND_AXE)) || e.canEnchant(new ItemStack(Items.DIAMOND_SHOVEL)) || e.canEnchant(new ItemStack(Items.DIAMOND_HOE))) {
                return true;
            }
        }
        return false;
    }

    public int getHarvestLevel(final ItemStack stack, final ToolType tool, final PlayerEntity player, final BlockState blockState) {
        return this.getTier().getLevel();
    }

    public boolean isCorrectToolForDrops(final BlockState state) {
        final ToolType harvestTool = state.getHarvestTool();
        if ((harvestTool == ToolType.AXE || harvestTool == ToolType.PICKAXE || harvestTool == ToolType.SHOVEL) && this.getTier().getLevel() >= state.getHarvestLevel()) {
            return true;
        }
        if (state.is(Blocks.SNOW) || state.is(Blocks.SNOW_BLOCK)) {
            return true;
        }
        final Material material = state.getMaterial();
        return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL;
    }

    public float getDestroySpeed(@Nonnull final ItemStack stack, final BlockState state) {
        return this.getTier().getSpeed();
    }

    public boolean isValidRepairItem(final ItemStack toRepair, final ItemStack repair) {
        return false;
    }

    public boolean isRepairable(final ItemStack stack) {
        return false;
    }

    @Nonnull
    public ActionResultType useOn(final ItemUseContext context) {
        final World world = context.getLevel();
        final BlockPos blockpos = context.getClickedPos();
        final PlayerEntity player = context.getPlayer();
        final ItemStack stack = context.getItemInHand();
        final BlockState blockstate = world.getBlockState(blockpos);
        BlockState resultToSet = blockstate.getToolModifiedState(world, blockpos, player, stack, ToolType.AXE);
        if (resultToSet != null) {
            world.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f);
        } else {
            if (context.getClickedFace() == Direction.DOWN) {
                return ActionResultType.PASS;
            }
            final BlockState foundResult = blockstate.getToolModifiedState(world, blockpos, player, stack, ToolType.SHOVEL);
            if (foundResult != null && world.isEmptyBlock(blockpos.above())) {
                world.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0f, 1.0f);
                resultToSet = foundResult;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!world.isClientSide) {
                    world.levelEvent(null, 1009, blockpos, 0);
                }
                CampfireBlock.dowse(world, blockpos, blockstate);
                resultToSet = blockstate.setValue(CampfireBlock.LIT, false);
            }
        }
        if (resultToSet == null) {
            return ActionResultType.PASS;
        }
        if (!world.isClientSide) {
            world.setBlock(blockpos, resultToSet, 11);
            if (player != null) {
                stack.hurtAndBreak(1, (LivingEntity) player, onBroken -> onBroken.broadcastBreakEvent(context.getHand()));
            }
        }
        return ActionResultType.sidedSuccess(world.isClientSide);
    }

    public void inventoryTick(@Nonnull final ItemStack itemStack, @Nonnull final World world, @Nonnull final Entity entity, final int itemSlot, final boolean isSelected) {
        super.inventoryTick(itemStack, world, entity, itemSlot, isSelected);
        if (!world.isClientSide && PaxelEnhancements.shouldEnhance(itemStack)) {
            final PaxelEnhancement randomEnhancement = ModConfigs.PAXEL_ENHANCEMENTS.getRandomEnhancement(world.getRandom());
            if (randomEnhancement != null) {
                PaxelEnhancements.enhance(itemStack, randomEnhancement);
            }
        }
        final PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(itemStack);
        if (enhancement != null) {
            enhancement.inventoryTick(itemStack, world, entity, itemSlot, isSelected);
        }
    }

    public void appendHoverText(@Nonnull final ItemStack itemStack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);
        final PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(itemStack);
        if (enhancement != null) {
            final IFormattableTextComponent label = new TranslationTextComponent("paxel_enhancement.name").append(": ");
            tooltip.add(label.append(enhancement.getName().setStyle(Style.EMPTY.withColor(enhancement.getColor()).withBold(Boolean.valueOf(true)))));
            tooltip.add(enhancement.getDescription().setStyle(Style.EMPTY.withColor(enhancement.getColor())));
        }
        if (PaxelEnhancements.shouldEnhance(itemStack)) {
            final IFormattableTextComponent label = new TranslationTextComponent("paxel_enhancement.name").append(": ");
            tooltip.add(label.append(new StringTextComponent("???").withStyle(TextFormatting.GRAY)));
        }
        final Map<Enchantment, Integer> enchantments = OverlevelEnchantHelper.getEnchantments(itemStack);
        if (enchantments.size() > 0) {
            tooltip.add(new StringTextComponent(""));
        }
    }

    static {
        PAXEL_TOOL_TYPE = ToolType.get("paxel");
    }
}
