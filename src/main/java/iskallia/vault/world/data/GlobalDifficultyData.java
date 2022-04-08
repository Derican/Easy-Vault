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

public class GlobalDifficultyData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_GlobalDifficulty";
    private Difficulty crystalCost = null;
    private Difficulty vaultDifficulty = null;

    public GlobalDifficultyData() {
        this("the_vault_GlobalDifficulty");
    }

    public GlobalDifficultyData(String name) {
        super(name);
    }

    public Difficulty getCrystalCost() {
        return this.crystalCost;
    }

    public void setCrystalCost(Difficulty crystalCost) {
        this.crystalCost = crystalCost;
        setDirty();
    }

    public Difficulty getVaultDifficulty() {
        return this.vaultDifficulty;
    }

    public void setVaultDifficulty(Difficulty vaultDifficulty) {
        this.vaultDifficulty = vaultDifficulty;
        setDirty();
    }

    public void openDifficultySelection(ServerPlayerEntity sPlayer) {
        if (ServerLifecycleHooks.getCurrentServer() != null && (!ServerLifecycleHooks.getCurrentServer().isDedicatedServer() || sPlayer.hasPermissions(sPlayer.getServer().getOperatorUserPermissionLevel())) && (
                getVaultDifficulty() == null || getCrystalCost() == null)) {
            final CompoundNBT data = new CompoundNBT();
            data.putInt("VaultDifficulty", Difficulty.STANDARD.ordinal());
            data.putInt("CrystalCost", Difficulty.STANDARD.ordinal());
            NetworkHooks.openGui(sPlayer, new INamedContainerProvider() {

                public ITextComponent getDisplayName() {
                    return (ITextComponent) new StringTextComponent("Welcome Vault Hunter!");
                }


                @Nullable
                public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return (Container) new GlobalDifficultyContainer(windowId, data);
                }
            }buffer -> buffer.writeNbt(data));
        }
    }


    public void load(CompoundNBT nbt) {
        if (nbt.contains("CrystalCost")) {
            this.crystalCost = Difficulty.values()[nbt.getInt("CrystalCost")];
        }
        if (nbt.contains("VaultDifficulty")) {
            this.vaultDifficulty = Difficulty.values()[nbt.getInt("VaultDifficulty")];
        }
    }


    public CompoundNBT save(CompoundNBT nbt) {
        if (this.crystalCost != null) {
            nbt.putInt("CrystalCost", this.crystalCost.ordinal());
        }
        if (this.vaultDifficulty != null) {
            nbt.putInt("VaultDifficulty", this.vaultDifficulty.ordinal());
        }
        return nbt;
    }

    public static GlobalDifficultyData get(ServerWorld world) {
        return (GlobalDifficultyData) world.getServer().overworld().getDataStorage().computeIfAbsent(GlobalDifficultyData::new, "the_vault_GlobalDifficulty");
    }

    public enum Difficulty {
        TRIVIAL(0.5D),
        CASUAL(0.75D),
        STANDARD(1.0D),
        HARD(1.25D),
        EXTREME(1.5D);

        double multiplier;

        Difficulty(double multiplier) {
            this.multiplier = multiplier;
        }

        public double getMultiplier() {
            return this.multiplier;
        }


        public String toString() {
            return StringUtils.capitalize(name().toLowerCase());
        }

        public Difficulty getNext() {
            int index = ordinal() + 1;
            if (index >= (values()).length) {
                index = 0;
            }
            return values()[index];
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\GlobalDifficultyData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */