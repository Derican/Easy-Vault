package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;


public class OmegaStatueContainer
        extends Container {
    private CompoundNBT data;

    public OmegaStatueContainer(int windowId, CompoundNBT nbt) {
        super(ModContainers.OMEGA_STATUE_CONTAINER, windowId);
        this.data = nbt;
    }


    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

    public ListNBT getItemsCompound() {
        return this.data.getList("Items", 10);
    }

    public CompoundNBT getBlockPos() {
        return this.data.getCompound("Position");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\OmegaStatueContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */