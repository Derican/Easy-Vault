package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import iskallia.vault.util.RenameType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;

public class RenamingContainer extends Container {
    private RenameType type;
    private CompoundNBT nbt;

    public RenamingContainer(final int windowId, final CompoundNBT nbt) {
        super((ContainerType) ModContainers.RENAMING_CONTAINER, windowId);
        this.type = RenameType.values()[nbt.getInt("RenameType")];
        this.nbt = nbt.getCompound("Data");
    }

    public boolean stillValid(final PlayerEntity playerIn) {
        return true;
    }

    public CompoundNBT getNbt() {
        return this.nbt;
    }

    public RenameType getRenameType() {
        return this.type;
    }
}
