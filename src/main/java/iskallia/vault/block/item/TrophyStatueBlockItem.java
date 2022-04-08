package iskallia.vault.block.item;

import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.StatueType;
import iskallia.vault.util.WeekKey;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class TrophyStatueBlockItem extends LootStatueBlockItem {
    public TrophyStatueBlockItem(Block block) {
        super(block, StatueType.TROPHY);
    }


    @OnlyIn(Dist.CLIENT)
    protected void addStatueInformation(CompoundNBT dataTag, List<ITextComponent> toolTip) {
        super.addStatueInformation(dataTag, toolTip);

        if (!dataTag.contains("recordEntry", 10) || !dataTag.contains("trophyWeek", 10)) {
            return;
        }
        WeekKey week = WeekKey.deserialize(dataTag.getCompound("trophyWeek"));
        PlayerVaultStatsData.PlayerRecordEntry recordEntry = PlayerVaultStatsData.PlayerRecordEntry.deserialize(dataTag.getCompound("recordEntry"));

        StringTextComponent stringTextComponent = new StringTextComponent(week.getWeek() + " / " + week.getYear());
        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(UIHelper.formatTimeString(recordEntry.getTickCount()))).withStyle(TextFormatting.GOLD);

        toolTip.add(StringTextComponent.EMPTY);
        toolTip.add((new StringTextComponent("Week: ")).append((ITextComponent) stringTextComponent));
        toolTip.add((new StringTextComponent("Record: ")).append((ITextComponent) iFormattableTextComponent));
    }

    public static ItemStack getTrophy(ServerWorld serverWorld, WeekKey week) {
        PlayerVaultStatsData statsData = PlayerVaultStatsData.get(serverWorld);
        PlayerVaultStatsData.PlayerRecordEntry record = statsData.getFastestVaultTime(week);
        if (StringUtils.isNullOrEmpty(record.getPlayerName())) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack((IItemProvider) ModBlocks.TROPHY_STATUE);

        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("PlayerNickname", record.getPlayerName());
        nbt.putInt("StatueType", StatueType.TROPHY.ordinal());

        nbt.putInt("Interval", -1);
        nbt.put("LootItem", (INBT) ItemStack.EMPTY.serializeNBT());
        nbt.putInt("ItemsRemaining", -1);
        nbt.putInt("TotalItems", -1);

        nbt.put("trophyWeek", (INBT) week.serialize());
        nbt.put("recordEntry", (INBT) record.serialize());

        CompoundNBT stackNBT = new CompoundNBT();
        stackNBT.put("BlockEntityTag", (INBT) nbt);
        stack.setTag(stackNBT);

        return stack;
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\item\TrophyStatueBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */