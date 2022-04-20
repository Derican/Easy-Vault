package iskallia.vault.world.data;

import iskallia.vault.item.VaultCharmUpgrade;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class VaultCharmData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_VaultCharm";
    private final HashMap<UUID, VaultCharmInventory> whitelistedItems;

    public VaultCharmData() {
        super("the_vault_VaultCharm");
        this.whitelistedItems = new HashMap<UUID, VaultCharmInventory>();
    }

    public void updateWhitelist(final ServerPlayerEntity player, final List<ResourceLocation> ids) {
        final VaultCharmInventory inventory = this.getInventory(player);
        inventory.updateWhitelist(ids);
        this.setDirty();
    }

    public void upgradeInventorySize(final ServerPlayerEntity player, final int newSize) {
        this.getInventory(player).setSize(newSize);
        this.setDirty();
    }

    public List<ResourceLocation> getWhitelistedItems(final ServerPlayerEntity player) {
        final VaultCharmInventory inventory = this.getInventory(player);
        return inventory.getWhitelist();
    }

    public VaultCharmInventory getInventory(final ServerPlayerEntity player) {
        if (this.whitelistedItems.containsKey(player.getUUID())) {
            return this.whitelistedItems.get(player.getUUID());
        }
        final VaultCharmInventory inventory = new VaultCharmInventory();
        this.whitelistedItems.put(player.getUUID(), inventory);
        this.setDirty();
        return inventory;
    }

    public void load(final CompoundNBT nbt) {
        for (final String key : nbt.getAllKeys()) {
            UUID playerId;
            try {
                playerId = UUID.fromString(key);
            } catch (final IllegalArgumentException exception) {
                continue;
            }
            final CompoundNBT inventoryNbt = nbt.getCompound(key);
            final VaultCharmInventory inventory = new VaultCharmInventory();
            inventory.deserializeNBT(inventoryNbt);
            this.whitelistedItems.put(playerId, inventory);
        }
    }

    public CompoundNBT save(final CompoundNBT compound) {
        this.whitelistedItems.forEach((uuid, inventory) -> compound.put(uuid.toString(), (INBT) inventory.serializeNBT()));
        return compound;
    }

    public static VaultCharmData get(final ServerWorld world) {
        return (VaultCharmData) world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier) VaultCharmData::new, "the_vault_VaultCharm");
    }

    public static class VaultCharmInventory implements INBTSerializable<CompoundNBT> {
        private int size;
        private List<ResourceLocation> whitelist;

        public VaultCharmInventory() {
            this(9);
        }

        public VaultCharmInventory(final int size) {
            this.whitelist = new ArrayList<ResourceLocation>();
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(final int size) {
            this.size = size;
        }

        public List<ResourceLocation> getWhitelist() {
            return this.whitelist;
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            final ListNBT whitelistNbt = new ListNBT();
            this.whitelist.forEach(id -> whitelistNbt.add(StringNBT.valueOf(id.toString())));
            nbt.putInt("InventorySize", this.size);
            nbt.put("Whitelist", (INBT) whitelistNbt);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.size = nbt.getInt("InventorySize");
            final ListNBT itemList = nbt.getList("Whitelist", 8);
            for (int i = 0; i < itemList.size(); ++i) {
                this.whitelist.add(new ResourceLocation(itemList.getString(i)));
            }
        }

        private void updateWhitelist(final List<ResourceLocation> whitelist) {
            this.whitelist = new ArrayList<ResourceLocation>(whitelist);
        }

        public static VaultCharmInventory fromNbt(final CompoundNBT nbt) {
            final VaultCharmInventory inventory = new VaultCharmInventory();
            inventory.deserializeNBT(nbt);
            return inventory;
        }

        public boolean canUpgrade(final int newSize) {
            final VaultCharmUpgrade.Tier current = VaultCharmUpgrade.Tier.getTierBySize(this.size);
            final VaultCharmUpgrade.Tier potential = VaultCharmUpgrade.Tier.getTierBySize(newSize);
            if (potential == null) {
                return false;
            }
            if (current == null) {
                return potential == VaultCharmUpgrade.Tier.ONE;
            }
            final VaultCharmUpgrade.Tier next = current.getNext();
            return next != null && next == potential;
        }
    }
}
