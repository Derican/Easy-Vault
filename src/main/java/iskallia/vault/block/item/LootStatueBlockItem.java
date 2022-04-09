// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.item;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.NameProviderPublic;
import iskallia.vault.util.StatueType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class LootStatueBlockItem extends BlockItem
{
    private final StatueType type;
    
    public LootStatueBlockItem(final Block block, final StatueType type) {
        super(block, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
        this.type = type;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> toolTip, final ITooltipFlag flagIn) {
        final CompoundNBT nbt = stack.getTag();
        if (nbt != null && nbt.contains("BlockEntityTag", 10)) {
            this.addStatueInformation(nbt.getCompound("BlockEntityTag"), toolTip);
        }
        super.appendHoverText(stack, worldIn, (List)toolTip, flagIn);
    }
    
    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (world.isClientSide()) {
            return;
        }
        final CompoundNBT tag = stack.getOrCreateTagElement("BlockEntityTag");
        if (!tag.contains("PlayerNickname", 8)) {
            final String name = NameProviderPublic.getRandomName();
            initRandomStatue(tag, this.type, name);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void addStatueInformation(final CompoundNBT dataTag, final List<ITextComponent> toolTip) {
        final String nickname = dataTag.getString("PlayerNickname");
        toolTip.add((ITextComponent)new StringTextComponent("Player: "));
        toolTip.add((ITextComponent)new StringTextComponent("- ").append((ITextComponent)new StringTextComponent(nickname).withStyle(TextFormatting.GOLD)));
        if (this.type.dropsItems()) {
            ITextComponent itemDescriptor = (ITextComponent)new StringTextComponent("NOT SELECTED").withStyle(TextFormatting.RED);
            if (dataTag.contains("LootItem")) {
                final ItemStack lootItem = ItemStack.of(dataTag.getCompound("LootItem"));
                itemDescriptor = (ITextComponent)new StringTextComponent(lootItem.getHoverName().getString()).withStyle(TextFormatting.GREEN);
            }
            toolTip.add(StringTextComponent.EMPTY);
            toolTip.add((ITextComponent)new StringTextComponent("Item: ").withStyle(TextFormatting.WHITE));
            toolTip.add((ITextComponent)new StringTextComponent("- ").append(itemDescriptor));
        }
    }
    
    private static StatueType getStatueType(final ItemStack stack) {
        if (stack.getItem() instanceof LootStatueBlockItem) {
            return ((LootStatueBlockItem)stack.getItem()).type;
        }
        return StatueType.GIFT_NORMAL;
    }
    
    public static ItemStack getStatueBlockItem(final String nickname, final StatueType type) {
        ItemStack itemStack = ItemStack.EMPTY;
        switch (type) {
            case GIFT_NORMAL: {
                itemStack = new ItemStack((IItemProvider)ModBlocks.GIFT_NORMAL_STATUE);
                break;
            }
            case GIFT_MEGA: {
                itemStack = new ItemStack((IItemProvider)ModBlocks.GIFT_MEGA_STATUE);
                break;
            }
            case VAULT_BOSS: {
                itemStack = new ItemStack((IItemProvider)ModBlocks.VAULT_PLAYER_LOOT_STATUE);
                break;
            }
            case OMEGA: {
                itemStack = new ItemStack((IItemProvider)ModBlocks.OMEGA_STATUE);
                break;
            }
            case OMEGA_VARIANT: {
                itemStack = new ItemStack((IItemProvider)ModBlocks.OMEGA_STATUE_VARIANT);
                break;
            }
        }
        final CompoundNBT nbt = new CompoundNBT();
        initRandomStatue(nbt, type, nickname);
        final CompoundNBT stackNBT = new CompoundNBT();
        stackNBT.put("BlockEntityTag", (INBT)nbt);
        itemStack.setTag(stackNBT);
        return itemStack;
    }
    
    private static void initRandomStatue(final CompoundNBT out, final StatueType type, final String name) {
        out.putString("PlayerNickname", name);
        out.putInt("StatueType", type.ordinal());
        if (type.dropsItems()) {
            out.putInt("Interval", ModConfigs.STATUE_LOOT.getInterval(type));
            if (!type.isOmega()) {
                final ItemStack loot = ModConfigs.STATUE_LOOT.randomLoot(type);
                out.put("LootItem", (INBT)loot.serializeNBT());
            }
            final int decay = ModConfigs.STATUE_LOOT.getDecay(type);
            out.putInt("ItemsRemaining", decay);
            out.putInt("TotalItems", decay);
        }
    }
    
    protected boolean canPlace(final BlockItemUseContext ctx, final BlockState state) {
        if (!ctx.getItemInHand().hasTag()) {
            return false;
        }
        final CompoundNBT tag = ctx.getItemInHand().getOrCreateTag();
        final CompoundNBT blockTag = tag.getCompound("BlockEntityTag");
        return blockTag.contains("PlayerNickname", 8) && blockTag.contains("StatueType", 3) && super.canPlace(ctx, state);
    }
}
