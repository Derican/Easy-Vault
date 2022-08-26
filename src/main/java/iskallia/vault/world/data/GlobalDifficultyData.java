package iskallia.vault.world.data;

import iskallia.vault.container.GlobalDifficultyContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GlobalDifficultyData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_GlobalDifficulty";
    private Difficulty crystalCost;
    private Difficulty vaultDifficulty;

    public GlobalDifficultyData() {
        this("the_vault_GlobalDifficulty");
    }

    public GlobalDifficultyData(final String name) {
        super(name);
        this.crystalCost = null;
        this.vaultDifficulty = null;
    }

    public Difficulty getCrystalCost() {
        return this.crystalCost;
    }

    public void setCrystalCost(final Difficulty crystalCost) {
        this.crystalCost = crystalCost;
        this.setDirty();
    }

    public Difficulty getVaultDifficulty() {
        return this.vaultDifficulty;
    }

    public void setVaultDifficulty(final Difficulty vaultDifficulty) {
        this.vaultDifficulty = vaultDifficulty;
        this.setDirty();
    }

    public void openDifficultySelection(final ServerPlayerEntity sPlayer) {
        if (ServerLifecycleHooks.getCurrentServer() != null && (!ServerLifecycleHooks.getCurrentServer().isDedicatedServer() || sPlayer.hasPermissions(sPlayer.getServer().getOperatorUserPermissionLevel())) && (this.getVaultDifficulty() == null || this.getCrystalCost() == null)) {
            final CompoundNBT data = new CompoundNBT();
            data.putInt("VaultDifficulty", Difficulty.STANDARD.ordinal());
            data.putInt("CrystalCost", Difficulty.STANDARD.ordinal());
            NetworkHooks.openGui(sPlayer, new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return new StringTextComponent("Welcome Vault Hunter!");
                }

                @Nullable
                public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                    return new GlobalDifficultyContainer(windowId, data);
                }
            }, buffer -> buffer.writeNbt(data));
        }
    }

    public void load(final CompoundNBT nbt) {
        if (nbt.contains("CrystalCost")) {
            this.crystalCost = Difficulty.values()[nbt.getInt("CrystalCost")];
        }
        if (nbt.contains("VaultDifficulty")) {
            this.vaultDifficulty = Difficulty.values()[nbt.getInt("VaultDifficulty")];
        }
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        if (this.crystalCost != null) {
            nbt.putInt("CrystalCost", this.crystalCost.ordinal());
        }
        if (this.vaultDifficulty != null) {
            nbt.putInt("VaultDifficulty", this.vaultDifficulty.ordinal());
        }
        return nbt;
    }

    public static GlobalDifficultyData get(final ServerWorld world) {
        return (GlobalDifficultyData) world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier) GlobalDifficultyData::new, "the_vault_GlobalDifficulty");
    }

    public enum Difficulty {
        TRIVIAL(0.5),
        CASUAL(0.75),
        STANDARD(1.0),
        HARD(1.25),
        EXTREME(1.5);

        double multiplier;

        Difficulty(final double multiplier) {
            this.multiplier = multiplier;
        }

        public double getMultiplier() {
            return this.multiplier;
        }

        @Override
        public String toString() {
            return StringUtils.capitalize(this.name().toLowerCase());
        }

        public Difficulty getNext() {
            int index = this.ordinal() + 1;
            if (index >= values().length) {
                index = 0;
            }
            return values()[index];
        }
    }
}
