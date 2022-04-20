package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class OmegaStatueContainer extends Container {
    private CompoundNBT data;

    public OmegaStatueContainer(final int windowId, final CompoundNBT nbt) {
        super((ContainerType) ModContainers.OMEGA_STATUE_CONTAINER, windowId);
        this.data = nbt;
    }

    public boolean stillValid(final PlayerEntity playerIn) {
        return true;
    }

    public ListNBT getItemsCompound() {
        return this.data.getList("Items", 10);
    }

    public CompoundNBT getBlockPos() {
        return this.data.getCompound("Position");
    }
}
