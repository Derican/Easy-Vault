// 
// Decompiled by Procyon v0.6.0
// 

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
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BackupManager
{
    private static final DateTimeFormatter DATE_FORMAT;
    private static final Pattern DATE_FORMAT_EXTRACTOR;
    
    private BackupManager() {
    }
    
    public static boolean createPlayerInventorySnapshot(final ServerPlayerEntity playerEntity) {
        final MinecraftServer srv = playerEntity.getServer();
        if (srv == null) {
            return false;
        }
        final ListNBT list = new ListNBT();
        for (int index = 0; index < playerEntity.inventory.getContainerSize(); ++index) {
            final ItemStack stack = playerEntity.inventory.getItem(index);
            if (!stack.isEmpty()) {
                list.add(stack.serializeNBT());
            }
        }
        if (ModList.get().isLoaded("curios")) {
            list.addAll((Collection)IntegrationCurios.getSerializedCuriosItemStacks((PlayerEntity)playerEntity));
        }
        final CompoundNBT tag = new CompoundNBT();
        tag.put("data", (INBT)list);
        final File datFile = getStoredFile(srv, playerEntity.getUUID(), BackupManager.DATE_FORMAT.format(LocalDateTime.now()));
        try {
            CompressedStreamTools.write(tag, datFile);
        }
        catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static Optional<List<ItemStack>> getStoredItemStacks(final MinecraftServer server, final UUID playerUUID, final String timestampRef) {
        final File storedFile = getStoredFile(server, playerUUID, timestampRef);
        if (!storedFile.exists()) {
            return Optional.empty();
        }
        CompoundNBT tag;
        try {
            tag = CompressedStreamTools.read(storedFile);
        }
        catch (final IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        final ListNBT data = tag.getList("data", 10);
        final List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (int i = 0; i < data.size(); ++i) {
            final ItemStack stack = ItemStack.of(data.getCompound(i));
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return Optional.of(stacks);
    }
    
    public static List<String> getMostRecentBackupFileTimestamps(final MinecraftServer server, final UUID playerUUID) {
        return getBackupFileTimestamps(server, playerUUID, 5);
    }
    
    private static List<String> getBackupFileTimestamps(final MinecraftServer server, final UUID playerUUID, final int count) {
        final File dir = getStorageDir(server, playerUUID);
        final File[] files = dir.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        Comparator<? super Tuple<File, LocalDateTime>> tplTimeComparator = Comparator.comparing((Function<? super Tuple<File, LocalDateTime>, ? extends Comparable>)Tuple::getB);
        tplTimeComparator = tplTimeComparator.reversed();
        final long limit = (count < 0) ? Long.MAX_VALUE : count;
        return Arrays.asList(files).stream().map(file -> {
            final Matcher match = BackupManager.DATE_FORMAT_EXTRACTOR.matcher(file.getName());
            if (!match.find()) {
                return null;
            }
            else {
                final String dateGroup = match.group(1);
                LocalDateTime dateTime;
                try {
                    dateTime = LocalDateTime.parse(dateGroup, BackupManager.DATE_FORMAT);
                }
                catch (final DateTimeParseException exc) {
                    return null;
                }
                return new Tuple(file, dateTime);
            }
        }).filter(Objects::nonNull).sorted((Comparator<? super Object>)tplTimeComparator).limit(limit).map(tpl -> BackupManager.DATE_FORMAT.format((TemporalAccessor)tpl.getB())).collect(Collectors.toList());
    }
    
    private static File getStoredFile(final MinecraftServer srv, final UUID playerUUID, final String timestamp) {
        return new File(getStorageDir(srv, playerUUID), timestamp + ".dat");
    }
    
    private static File getStorageDir(final MinecraftServer server, final UUID playerUUID) {
        final File dir = server.getWorldPath(FolderName.ROOT).resolve("vault_inventory_backup").resolve(playerUUID.toString()).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
    
    static {
        DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
        DATE_FORMAT_EXTRACTOR = Pattern.compile("^(.*)\\.dat$");
    }
}
