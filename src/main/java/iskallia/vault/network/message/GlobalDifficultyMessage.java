// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.world.data.GlobalDifficultyData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GlobalDifficultyMessage
{
    public CompoundNBT data;
    
    public static void encode(final GlobalDifficultyMessage message, final PacketBuffer buffer) {
        buffer.writeNbt(message.data);
    }
    
    public static GlobalDifficultyMessage decode(final PacketBuffer buffer) {
        final GlobalDifficultyMessage message = new GlobalDifficultyMessage();
        message.data = buffer.readNbt();
        return message;
    }
    
    public static void handle(final GlobalDifficultyMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity player = context.getSender();
            if (player == null) {
                return;
            }
            else {
                final CompoundNBT data = message.data;
                final GlobalDifficultyData.Difficulty vaultDifficulty = GlobalDifficultyData.Difficulty.values()[data.getInt("VaultDifficulty")];
                final GlobalDifficultyData.Difficulty crystalCost = GlobalDifficultyData.Difficulty.values()[data.getInt("CrystalCost")];
                final ServerWorld world = player.getLevel();
                final GlobalDifficultyData difficultyData = GlobalDifficultyData.get(world);
                if (difficultyData.getVaultDifficulty() == null) {
                    GlobalDifficultyData.get(world).setVaultDifficulty(vaultDifficulty);
                    GlobalDifficultyData.get(world).setCrystalCost(crystalCost);
                }
                return;
            }
        });
        context.setPacketHandled(true);
    }
    
    public static GlobalDifficultyMessage setGlobalDifficultyOptions(final CompoundNBT data) {
        final GlobalDifficultyMessage message = new GlobalDifficultyMessage();
        message.data = data;
        return message;
    }
}
