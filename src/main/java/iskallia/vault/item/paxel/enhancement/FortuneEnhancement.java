package iskallia.vault.item.paxel.enhancement;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.util.BlockDropCaptureHelper;
import iskallia.vault.util.BlockHelper;
import iskallia.vault.util.OverlevelEnchantHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FortuneEnhancement extends PaxelEnhancement {
    protected int extraFortune;

    public FortuneEnhancement(final int extraFortune) {
        this.extraFortune = extraFortune;
    }

    public int getExtraFortune() {
        return this.extraFortune;
    }

    @Override
    public Color getColor() {
        return Color.fromRgb(-22784);
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("ExtraFortune", this.extraFortune);
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.extraFortune = nbt.getInt("ExtraFortune");
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBlockMined(final BlockEvent.BreakEvent event) {
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        final ItemStack heldStack = player.getMainHandItem();
        final PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(heldStack);
        if (!(enhancement instanceof FortuneEnhancement)) {
            return;
        }
        final FortuneEnhancement fortuneEnhancement = (FortuneEnhancement) enhancement;
        ActiveFlags.IS_FORTUNE_MINING.runIfNotSet(() -> {
            final ServerWorld world = (ServerWorld) event.getWorld();
            final ItemStack miningStack = OverlevelEnchantHelper.increaseFortuneBy(heldStack.copy(), fortuneEnhancement.getExtraFortune());
            final BlockPos pos = event.getPos();
            BlockDropCaptureHelper.startCapturing();
            try {
                BlockHelper.breakBlock(world, player, pos, world.getBlockState(pos), miningStack, true, true);
                BlockHelper.damageMiningItem(heldStack, player, 1);
            } finally {
                BlockDropCaptureHelper.getCapturedStacksAndStop().forEach(entity -> Block.popResource(world, entity.blockPosition(), entity.getItem()));
            }
            event.setCanceled(true);
        });
    }
}
