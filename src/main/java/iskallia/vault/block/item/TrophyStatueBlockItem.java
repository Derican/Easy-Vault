// 
// Decompiled by Procyon v0.6.0
// 

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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class TrophyStatueBlockItem extends LootStatueBlockItem
{
    public TrophyStatueBlockItem(final Block block) {
        super(block, StatueType.TROPHY);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void addStatueInformation(final CompoundNBT dataTag, final List<ITextComponent> toolTip) {
        super.addStatueInformation(dataTag, toolTip);
        if (!dataTag.contains("recordEntry", 10) || !dataTag.contains("trophyWeek", 10)) {
            return;
        }
        final WeekKey week = WeekKey.deserialize(dataTag.getCompound("trophyWeek"));
        final PlayerVaultStatsData.PlayerRecordEntry recordEntry = PlayerVaultStatsData.PlayerRecordEntry.deserialize(dataTag.getCompound("recordEntry"));
        final ITextComponent weekCmp = (ITextComponent)new StringTextComponent(week.getWeek() + " / " + week.getYear());
        final ITextComponent recordCmp = (ITextComponent)new StringTextComponent(UIHelper.formatTimeString(recordEntry.getTickCount())).withStyle(TextFormatting.GOLD);
        toolTip.add(StringTextComponent.EMPTY);
        toolTip.add((ITextComponent)new StringTextComponent("Week: ").append(weekCmp));
        toolTip.add((ITextComponent)new StringTextComponent("Record: ").append(recordCmp));
    }
    
    public static ItemStack getTrophy(final ServerWorld serverWorld, final WeekKey week) {
        final PlayerVaultStatsData statsData = PlayerVaultStatsData.get(serverWorld);
        final PlayerVaultStatsData.PlayerRecordEntry record = statsData.getFastestVaultTime(week);
        if (StringUtils.isNullOrEmpty(record.getPlayerName())) {
            return ItemStack.EMPTY;
        }
        final ItemStack stack = new ItemStack((IItemProvider)ModBlocks.TROPHY_STATUE);
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("PlayerNickname", record.getPlayerName());
        nbt.putInt("StatueType", StatueType.TROPHY.ordinal());
        nbt.putInt("Interval", -1);
        nbt.put("LootItem", (INBT)ItemStack.EMPTY.serializeNBT());
        nbt.putInt("ItemsRemaining", -1);
        nbt.putInt("TotalItems", -1);
        nbt.put("trophyWeek", (INBT)week.serialize());
        nbt.put("recordEntry", (INBT)record.serialize());
        final CompoundNBT stackNBT = new CompoundNBT();
        stackNBT.put("BlockEntityTag", (INBT)nbt);
        stack.setTag(stackNBT);
        return stack;
    }
    
    @Override
    protected boolean canPlace(final BlockItemUseContext ctx, final BlockState state) {
        if (!ctx.getItemInHand().hasTag()) {
            return false;
        }
        final CompoundNBT tag = ctx.getItemInHand().getOrCreateTag();
        final CompoundNBT blockTag = tag.getCompound("BlockEntityTag");
        return blockTag.contains("PlayerNickname", 8) && blockTag.contains("StatueType", 3) && super.canPlace(ctx, state);
    }
}
