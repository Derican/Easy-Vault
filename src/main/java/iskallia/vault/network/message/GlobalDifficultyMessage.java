package iskallia.vault.network.message;

import iskallia.vault.world.data.GlobalDifficultyData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class GlobalDifficultyMessage {
    public CompoundNBT data;

    public static void encode(GlobalDifficultyMessage message, PacketBuffer buffer) {
        buffer.writeNbt(message.data);
    }

    public static GlobalDifficultyMessage decode(PacketBuffer buffer) {
        GlobalDifficultyMessage message = new GlobalDifficultyMessage();
        message.data = buffer.readNbt();
        return message;
    }

    public static void handle(GlobalDifficultyMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player == null)
                return;
            CompoundNBT data = message.data;
            GlobalDifficultyData.Difficulty vaultDifficulty = GlobalDifficultyData.Difficulty.values()[data.getInt("VaultDifficulty")];
            GlobalDifficultyData.Difficulty crystalCost = GlobalDifficultyData.Difficulty.values()[data.getInt("CrystalCost")];
            ServerWorld world = player.getLevel();
            GlobalDifficultyData difficultyData = GlobalDifficultyData.get(world);
            if (difficultyData.getVaultDifficulty() == null) {
                GlobalDifficultyData.get(world).setVaultDifficulty(vaultDifficulty);
                GlobalDifficultyData.get(world).setCrystalCost(crystalCost);
            }
        });
        context.setPacketHandled(true);
    }

    public static GlobalDifficultyMessage setGlobalDifficultyOptions(CompoundNBT data) {
        GlobalDifficultyMessage message = new GlobalDifficultyMessage();
        message.data = data;
        return message;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\GlobalDifficultyMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */