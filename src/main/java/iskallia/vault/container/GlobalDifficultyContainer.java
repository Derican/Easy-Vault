package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;

public class GlobalDifficultyContainer extends Container {
    private CompoundNBT data;

    public GlobalDifficultyContainer(final int windowId, final CompoundNBT data) {
        super((ContainerType) ModContainers.GLOBAL_DIFFICULTY_CONTAINER, windowId);
        this.data = data;
    }

    public boolean stillValid(final PlayerEntity player) {
        return true;
    }

    public CompoundNBT getData() {
        return this.data;
    }
}
