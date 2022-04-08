package iskallia.vault.item.paxel.enhancement;

import iskallia.vault.event.ActiveFlags;
import iskallia.vault.util.BlockDropCaptureHelper;
import iskallia.vault.util.BlockHelper;
import iskallia.vault.util.OverlevelEnchantHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FortuneEnhancement
        extends PaxelEnhancement {
    protected int extraFortune;

    public FortuneEnhancement(int extraFortune) {
        this.extraFortune = extraFortune;
    }

    public int getExtraFortune() {
        return this.extraFortune;
    }


    public Color getColor() {
        return Color.fromRgb(-22784);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("ExtraFortune", this.extraFortune);
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.extraFortune = nbt.getInt("ExtraFortune");
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBlockMined(BlockEvent.BreakEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack heldStack = player.getMainHandItem();

        PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(heldStack);
        if (!(enhancement instanceof FortuneEnhancement)) {
            return;
        }

        FortuneEnhancement fortuneEnhancement = (FortuneEnhancement) enhancement;

        ActiveFlags.IS_FORTUNE_MINING.runIfNotSet(() -> {
            ServerWorld world = (ServerWorld) event.getWorld();
            ItemStack miningStack = OverlevelEnchantHelper.increaseFortuneBy(heldStack.copy(), fortuneEnhancement.getExtraFortune());
            BlockPos pos = event.getPos();
            BlockDropCaptureHelper.startCapturing();
            try {
                BlockHelper.breakBlock(world, player, pos, world.getBlockState(pos), miningStack, true, true);
                BlockHelper.damageMiningItem(heldStack, player, 1);
            } finally {
                BlockDropCaptureHelper.getCapturedStacksAndStop().forEach(());
            }
            event.setCanceled(true);
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\paxel\enhancement\FortuneEnhancement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */