package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import iskallia.vault.util.RenameType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;

public class RenamingContainer
        extends Container {
    private RenameType type;
    private CompoundNBT nbt;

    public RenamingContainer(int windowId, CompoundNBT nbt) {
        super(ModContainers.RENAMING_CONTAINER, windowId);
        this.type = RenameType.values()[nbt.getInt("RenameType")];
        this.nbt = nbt.getCompound("Data");
    }


    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

    public CompoundNBT getNbt() {
        return this.nbt;
    }

    public RenameType getRenameType() {
        return this.type;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\RenamingContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */