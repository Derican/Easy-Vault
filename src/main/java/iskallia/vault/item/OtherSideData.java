package iskallia.vault.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class OtherSideData
        implements INBTSerializable<CompoundNBT> {
    private CompoundNBT delegate;
    private BlockPos linkedPos;
    private RegistryKey<World> linkedDim;

    public OtherSideData() {
    }

    public OtherSideData(ItemStack delegate) {
        if (delegate != null) {
            this.delegate = delegate.getOrCreateTag();
            deserializeNBT(this.delegate.getCompound("OtherSideData"));
        }
    }

    public CompoundNBT getDelegate() {
        return this.delegate;
    }

    public void updateDelegate() {
        if (this.delegate != null) {
            this.delegate.put("OtherSideData", (INBT) serializeNBT());
        }
    }

    public BlockPos getLinkedPos() {
        return this.linkedPos;
    }

    public RegistryKey<World> getLinkedDim() {
        return this.linkedDim;
    }

    public OtherSideData setLinkedPos(BlockPos linkedPos) {
        this.linkedPos = linkedPos;
        updateDelegate();
        return this;
    }

    public OtherSideData setLinkedDim(RegistryKey<World> linkedDim) {
        if (this.linkedDim != (this.linkedDim = linkedDim)) {
            updateDelegate();
        }

        return this;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putIntArray("LinkedPos", new int[]{this.linkedPos.getX(), this.linkedPos.getY(), this.linkedPos.getZ()});
        nbt.putString("LinkedDim", this.linkedDim.location().toString());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        int[] arr = nbt.getIntArray("LinkedPos");
        this.linkedPos = new BlockPos(arr[0], arr[1], arr[2]);
        this.linkedDim = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("LinkedDim")));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\OtherSideData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */