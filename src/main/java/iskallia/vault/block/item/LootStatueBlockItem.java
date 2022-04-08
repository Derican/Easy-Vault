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
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class LootStatueBlockItem extends BlockItem {
    private final StatueType type;

    public LootStatueBlockItem(Block block, StatueType type) {
        super(block, (new Item.Properties())
                .tab(ModItems.VAULT_MOD_GROUP)
                .stacksTo(1));
        this.type = type;
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> toolTip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();
        if (nbt != null && nbt.contains("BlockEntityTag", 10)) {
            addStatueInformation(nbt.getCompound("BlockEntityTag"), toolTip);
        }

        super.appendHoverText(stack, worldIn, toolTip, flagIn);
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (world.isClientSide()) {
            return;
        }

        CompoundNBT tag = stack.getOrCreateTagElement("BlockEntityTag");
        if (!tag.contains("PlayerNickname", 8)) {
            String name = NameProviderPublic.getRandomName();

            initRandomStatue(tag, this.type, name);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void addStatueInformation(CompoundNBT dataTag, List<ITextComponent> toolTip) {
        String nickname = dataTag.getString("PlayerNickname");
        toolTip.add(new StringTextComponent("Player: "));
        toolTip.add((new StringTextComponent("- ")).append((ITextComponent) (new StringTextComponent(nickname)).withStyle(TextFormatting.GOLD)));

        if (this.type.dropsItems()) {
            IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("NOT SELECTED")).withStyle(TextFormatting.RED);
            if (dataTag.contains("LootItem")) {
                ItemStack lootItem = ItemStack.of(dataTag.getCompound("LootItem"));
                iFormattableTextComponent = (new StringTextComponent(lootItem.getHoverName().getString())).withStyle(TextFormatting.GREEN);
            }
            toolTip.add(StringTextComponent.EMPTY);
            toolTip.add((new StringTextComponent("Item: ")).withStyle(TextFormatting.WHITE));
            toolTip.add((new StringTextComponent("- ")).append((ITextComponent) iFormattableTextComponent));
        }
    }

    private static StatueType getStatueType(ItemStack stack) {
        if (stack.getItem() instanceof LootStatueBlockItem) {
            return ((LootStatueBlockItem) stack.getItem()).type;
        }
        return StatueType.GIFT_NORMAL;
    }

    public static ItemStack getStatueBlockItem(String nickname, StatueType type) {
        ItemStack itemStack = ItemStack.EMPTY;
        switch (type) {
            case GIFT_NORMAL:
                itemStack = new ItemStack((IItemProvider) ModBlocks.GIFT_NORMAL_STATUE);
                break;
            case GIFT_MEGA:
                itemStack = new ItemStack((IItemProvider) ModBlocks.GIFT_MEGA_STATUE);
                break;
            case VAULT_BOSS:
                itemStack = new ItemStack((IItemProvider) ModBlocks.VAULT_PLAYER_LOOT_STATUE);
                break;
            case OMEGA:
                itemStack = new ItemStack((IItemProvider) ModBlocks.OMEGA_STATUE);
                break;
            case OMEGA_VARIANT:
                itemStack = new ItemStack((IItemProvider) ModBlocks.OMEGA_STATUE_VARIANT);
                break;
        }

        CompoundNBT nbt = new CompoundNBT();
        initRandomStatue(nbt, type, nickname);

        CompoundNBT stackNBT = new CompoundNBT();
        stackNBT.put("BlockEntityTag", (INBT) nbt);
        itemStack.setTag(stackNBT);

        return itemStack;
    }

    private static void initRandomStatue(CompoundNBT out, StatueType type, String name) {
        out.putString("PlayerNickname", name);
        out.putInt("StatueType", type.ordinal());
        if (type.dropsItems()) {
            out.putInt("Interval", ModConfigs.STATUE_LOOT.getInterval(type));
            if (!type.isOmega()) {
                ItemStack loot = ModConfigs.STATUE_LOOT.randomLoot(type);
                out.put("LootItem", (INBT) loot.serializeNBT());
            }
            int decay = ModConfigs.STATUE_LOOT.getDecay(type);
            out.putInt("ItemsRemaining", decay);
            out.putInt("TotalItems", decay);
        }
    }


    protected boolean canPlace(BlockItemUseContext ctx, BlockState state) {
        if (!ctx.getItemInHand().hasTag()) {
            return false;
        }
        CompoundNBT tag = ctx.getItemInHand().getOrCreateTag();
        CompoundNBT blockTag = tag.getCompound("BlockEntityTag");
        if (!blockTag.contains("PlayerNickname", 8) ||
                !blockTag.contains("StatueType", 3)) {
            return false;
        }
        return super.canPlace(ctx, state);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\item\LootStatueBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */