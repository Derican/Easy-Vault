package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;

public class GlobalDifficultyContainer
        extends Container {
    private CompoundNBT data;

    public GlobalDifficultyContainer(int windowId, CompoundNBT data) {
        super(ModContainers.GLOBAL_DIFFICULTY_CONTAINER, windowId);

        this.data = data;
    }


    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public CompoundNBT getData() {
        return this.data;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\GlobalDifficultyContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */