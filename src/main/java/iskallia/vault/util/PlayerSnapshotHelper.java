package iskallia.vault.util;

import iskallia.vault.Vault;
import iskallia.vault.dump.PlayerSnapshotDump;
import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@EventBusSubscriber
public class PlayerSnapshotHelper {
    @SubscribeEvent
    public static void onSave(PlayerEvent.SaveToFile event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }
        if (!ModConfigs.VAULT_GENERAL.SAVE_PLAYER_SNAPSHOTS) {
            return;
        }
        savePlayerSnapshot((ServerPlayerEntity) event.getPlayer());
    }

    private static void savePlayerSnapshot(ServerPlayerEntity sPlayer) {
        savePlayerSnapshot(sPlayer, getSnapshotDirectory());
    }

    private static void savePlayerSnapshot(ServerPlayerEntity sPlayer, File directory) {
        String uuidStr = sPlayer.getUUID().toString();
        File playerFile = new File(directory, uuidStr + ".json");
        if (!playerFile.exists()) {
            playerFile.delete();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile))) {
            writer.write(PlayerSnapshotDump.createAndSerializeSnapshot(sPlayer));
        } catch (IOException exc) {
            Vault.LOGGER.error("Error writing player snapshot file: " + sPlayer.getName().getString());
            exc.printStackTrace();
        }
    }

    private static File getSnapshotDirectory() {
        File snapshotDirectory = FMLPaths.GAMEDIR.get().resolve("playerSnapshots").toFile();
        if (!snapshotDirectory.exists()) {
            snapshotDirectory.mkdirs();
        }
        return snapshotDirectory;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\PlayerSnapshotHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */