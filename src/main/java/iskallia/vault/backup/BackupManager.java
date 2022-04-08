package iskallia.vault.backup;

import iskallia.vault.integration.IntegrationCurios;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fml.ModList;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BackupManager {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
    private static final Pattern DATE_FORMAT_EXTRACTOR = Pattern.compile("^(.*)\\.dat$");


    public static boolean createPlayerInventorySnapshot(ServerPlayerEntity playerEntity) {
        MinecraftServer srv = playerEntity.getServer();
        if (srv == null) {
            return false;
        }
        ListNBT list = new ListNBT();
        for (int index = 0; index < playerEntity.inventory.getContainerSize(); index++) {
            ItemStack stack = playerEntity.inventory.getItem(index);
            if (!stack.isEmpty()) {
                list.add(stack.serializeNBT());
            }
        }
        if (ModList.get().isLoaded("curios")) {
            list.addAll(IntegrationCurios.getSerializedCuriosItemStacks((PlayerEntity) playerEntity));
        }
        CompoundNBT tag = new CompoundNBT();
        tag.put("data", (INBT) list);

        File datFile = getStoredFile(srv, playerEntity.getUUID(), DATE_FORMAT.format(LocalDateTime.now()));
        try {
            CompressedStreamTools.write(tag, datFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Optional<List<ItemStack>> getStoredItemStacks(MinecraftServer server, UUID playerUUID, String timestampRef) {
        CompoundNBT tag;
        File storedFile = getStoredFile(server, playerUUID, timestampRef);
        if (!storedFile.exists()) {
            return Optional.empty();
        }


        try {
            tag = CompressedStreamTools.read(storedFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        ListNBT data = tag.getList("data", 10);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ItemStack stack = ItemStack.of(data.getCompound(i));
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return Optional.of(stacks);
    }

    public static List<String> getMostRecentBackupFileTimestamps(MinecraftServer server, UUID playerUUID) {
        return getBackupFileTimestamps(server, playerUUID, 5);
    }

    private static List<String> getBackupFileTimestamps(MinecraftServer server, UUID playerUUID, int count) {
        File dir = getStorageDir(server, playerUUID);
        File[] files = dir.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        Comparator<? super Tuple<File, LocalDateTime>> tplTimeComparator = Comparator.comparing(Tuple::getB);
        tplTimeComparator = tplTimeComparator.reversed();

        long limit = (count < 0) ? Long.MAX_VALUE : count;
        return (List<String>) Arrays.<File>asList(files).stream()
                .map(file -> {
                    LocalDateTime dateTime;
                    Matcher match = DATE_FORMAT_EXTRACTOR.matcher(file.getName());
                    if (!match.find()) {
                        return null;
                    }
                    String dateGroup = match.group(1);
                    try {
                        dateTime = LocalDateTime.parse(dateGroup, DATE_FORMAT);
                    } catch (DateTimeParseException exc) {
                        return null;
                    }

                    return new Tuple<>(file, dateTime);
                }).filter(Objects::nonNull)
                .sorted(tplTimeComparator)
                .limit(limit)
                .map(tpl -> DATE_FORMAT.format((TemporalAccessor) tpl.getB()))
                .collect(Collectors.toList());
    }

    private static File getStoredFile(MinecraftServer srv, UUID playerUUID, String timestamp) {
        return new File(getStorageDir(srv, playerUUID), timestamp + ".dat");
    }

    private static File getStorageDir(MinecraftServer server, UUID playerUUID) {
        File dir = server.getWorldPath(FolderName.ROOT).resolve("vault_inventory_backup").resolve(playerUUID.toString()).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\backup\BackupManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */