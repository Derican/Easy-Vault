package iskallia.vault.item.paxel;

import com.google.common.collect.Multimap;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.state.Property;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class VaultPaxelItem extends ToolItem {
    public static final ToolType PAXEL_TOOL_TYPE = ToolType.get("paxel");

    public VaultPaxelItem(ResourceLocation id) {
        super(3.0F, -3.0F, PaxelItemTier.INSTANCE, Collections.emptySet(), (new Item.Properties())

                .tab(ModItems.VAULT_MOD_GROUP)
                .addToolType(PAXEL_TOOL_TYPE, PaxelItemTier.INSTANCE.getLevel())
                .stacksTo(1));


        setRegistryName(id);
    }


    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
    }


    public int getMaxDamage(ItemStack itemStack) {
        PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(itemStack);

        if (enhancement instanceof DurabilityEnhancement) {
            return super.getMaxDamage(itemStack) + ((DurabilityEnhancement) enhancement)
                    .getExtraDurability();
        }

        return super.getMaxDamage(itemStack);
    }


    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.MENDING) return false;

        return (enchantment.canEnchant(new ItemStack((IItemProvider) Items.DIAMOND_SWORD)) || enchantment
                .canEnchant(new ItemStack((IItemProvider) Items.DIAMOND_AXE)) || enchantment
                .canEnchant(new ItemStack((IItemProvider) Items.DIAMOND_SHOVEL)) || enchantment
                .canEnchant(new ItemStack((IItemProvider) Items.DIAMOND_HOE)));
    }


    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if (EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.MENDING)) return false;

        for (Enchantment e : EnchantmentHelper.getEnchantments(book).keySet()) {
            if (e.canEnchant(new ItemStack((IItemProvider) Items.DIAMOND_SWORD)) || e
                    .canEnchant(new ItemStack((IItemProvider) Items.DIAMOND_AXE)) || e
                    .canEnchant(new ItemStack((IItemProvider) Items.DIAMOND_SHOVEL)) || e
                    .canEnchant(new ItemStack((IItemProvider) Items.DIAMOND_HOE))) return true;

        }
        return false;
    }


    public int getHarvestLevel(ItemStack stack, ToolType tool, PlayerEntity player, BlockState blockState) {
        return getTier().getLevel();
    }


    public boolean isCorrectToolForDrops(BlockState state) {
        ToolType harvestTool = state.getHarvestTool();
        if ((harvestTool == ToolType.AXE || harvestTool == ToolType.PICKAXE || harvestTool == ToolType.SHOVEL) &&
                getTier().getLevel() >= state.getHarvestLevel()) {
            return true;
        }

        if (state.is(Blocks.SNOW) || state.is(Blocks.SNOW_BLOCK)) {
            return true;
        }
        Material material = state.getMaterial();
        return (material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL);
    }


    public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
        return getTier().getSpeed();
    }


    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }


    public boolean isRepairable(ItemStack stack) {
        return false;
    }


    @Nonnull
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState blockstate = world.getBlockState(blockpos);
        BlockState resultToSet = blockstate.getToolModifiedState(world, blockpos, player, stack, ToolType.AXE);
        if (resultToSet != null) {
            world.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        } else {
            if (context.getClickedFace() == Direction.DOWN) {
                return ActionResultType.PASS;
            }
            BlockState foundResult = blockstate.getToolModifiedState(world, blockpos, player, stack, ToolType.SHOVEL);
            if (foundResult != null && world.isEmptyBlock(blockpos.above())) {
                world.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                resultToSet = foundResult;
            } else if (blockstate.getBlock() instanceof CampfireBlock && ((Boolean) blockstate.getValue((Property) CampfireBlock.LIT)).booleanValue()) {
                if (!world.isClientSide) {
                    world.levelEvent(null, 1009, blockpos, 0);
                }
                CampfireBlock.dowse((IWorld) world, blockpos, blockstate);
                resultToSet = (BlockState) blockstate.setValue((Property) CampfireBlock.LIT, Boolean.valueOf(false));
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


    public void inventoryTick(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(itemStack, world, entity, itemSlot, isSelected);

        if (!world.isClientSide &&
                PaxelEnhancements.shouldEnhance(itemStack)) {
            PaxelEnhancement randomEnhancement = ModConfigs.PAXEL_ENHANCEMENTS.getRandomEnhancement(world.getRandom());
            if (randomEnhancement != null) PaxelEnhancements.enhance(itemStack, randomEnhancement);

        }

        PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(itemStack);
        if (enhancement != null) enhancement.inventoryTick(itemStack, world, entity, itemSlot, isSelected);

    }

    public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(itemStack);

        if (enhancement != null) {

            IFormattableTextComponent label = (new TranslationTextComponent("paxel_enhancement.name")).append(": ");
            tooltip.add(label.append((ITextComponent) enhancement.getName()
                    .setStyle(Style.EMPTY.withColor(enhancement.getColor()).withBold(Boolean.valueOf(true)))));
            tooltip.add(enhancement.getDescription()
                    .setStyle(Style.EMPTY.withColor(enhancement.getColor())));
        }

        if (PaxelEnhancements.shouldEnhance(itemStack)) {

            IFormattableTextComponent label = (new TranslationTextComponent("paxel_enhancement.name")).append(": ");
            tooltip.add(label.append((ITextComponent) (new StringTextComponent("???"))
                    .withStyle(TextFormatting.GRAY)));
        }

        Map<Enchantment, Integer> enchantments = OverlevelEnchantHelper.getEnchantments(itemStack);
        if (enchantments.size() > 0)
            tooltip.add(new StringTextComponent(""));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\paxel\VaultPaxelItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */