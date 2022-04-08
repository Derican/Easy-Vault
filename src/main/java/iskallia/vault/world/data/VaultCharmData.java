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


public class VaultCharmData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_VaultCharm";
    private final HashMap<UUID, VaultCharmInventory> whitelistedItems = new HashMap<>();

    public VaultCharmData() {
        super("the_vault_VaultCharm");
    }

    public void updateWhitelist(ServerPlayerEntity player, List<ResourceLocation> ids) {
        VaultCharmInventory inventory = getInventory(player);
        inventory.updateWhitelist(ids);
        setDirty();
    }

    public void upgradeInventorySize(ServerPlayerEntity player, int newSize) {
        getInventory(player).setSize(newSize);
        setDirty();
    }

    public List<ResourceLocation> getWhitelistedItems(ServerPlayerEntity player) {
        VaultCharmInventory inventory = getInventory(player);
        return inventory.getWhitelist();
    }

    public VaultCharmInventory getInventory(ServerPlayerEntity player) {
        if (this.whitelistedItems.containsKey(player.getUUID())) {
            return this.whitelistedItems.get(player.getUUID());
        }

        VaultCharmInventory inventory = new VaultCharmInventory();
        this.whitelistedItems.put(player.getUUID(), inventory);
        setDirty();
        return inventory;
    }


    public void load(CompoundNBT nbt) {
        for (String key : nbt.getAllKeys()) {
            UUID playerId;
            try {
                playerId = UUID.fromString(key);
            } catch (IllegalArgumentException exception) {
                continue;
            }

            CompoundNBT inventoryNbt = nbt.getCompound(key);
            VaultCharmInventory inventory = new VaultCharmInventory();
            inventory.deserializeNBT(inventoryNbt);
            this.whitelistedItems.put(playerId, inventory);
        }
    }


    public CompoundNBT save(CompoundNBT compound) {
        this.whitelistedItems.forEach((uuid, inventory) -> compound.put(uuid.toString(), (INBT) inventory.serializeNBT()));


        return compound;
    }

    public static VaultCharmData get(ServerWorld world) {
        return (VaultCharmData) world.getServer().overworld().getDataStorage().computeIfAbsent(VaultCharmData::new, "the_vault_VaultCharm");
    }

    public static class VaultCharmInventory implements INBTSerializable<CompoundNBT> {
        private int size;
        private List<ResourceLocation> whitelist = new ArrayList<>();

        public VaultCharmInventory() {
            this(9);
        }

        public VaultCharmInventory(int size) {
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<ResourceLocation> getWhitelist() {
            return this.whitelist;
        }


        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT whitelistNbt = new ListNBT();
            this.whitelist.forEach(id -> whitelistNbt.add(StringNBT.valueOf(id.toString())));


            nbt.putInt("InventorySize", this.size);
            nbt.put("Whitelist", (INBT) whitelistNbt);
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.size = nbt.getInt("InventorySize");
            ListNBT itemList = nbt.getList("Whitelist", 8);
            for (int i = 0; i < itemList.size(); i++) {
                this.whitelist.add(new ResourceLocation(itemList.getString(i)));
            }
        }

        private void updateWhitelist(List<ResourceLocation> whitelist) {
            this.whitelist = new ArrayList<>(whitelist);
        }

        public static VaultCharmInventory fromNbt(CompoundNBT nbt) {
            VaultCharmInventory inventory = new VaultCharmInventory();
            inventory.deserializeNBT(nbt);
            return inventory;
        }

        public boolean canUpgrade(int newSize) {
            VaultCharmUpgrade.Tier current = VaultCharmUpgrade.Tier.getTierBySize(this.size);
            VaultCharmUpgrade.Tier potential = VaultCharmUpgrade.Tier.getTierBySize(newSize);
            if (potential == null) return false;

            if (current == null) {
                return (potential == VaultCharmUpgrade.Tier.ONE);
            }
            VaultCharmUpgrade.Tier next = current.getNext();
            if (next == null) return false;

            return (next == potential);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\VaultCharmData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */