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

public class OtherSideData implements INBTSerializable<CompoundNBT> {
    private CompoundNBT delegate;
    private BlockPos linkedPos;
    private RegistryKey<World> linkedDim;

    public OtherSideData() {
    }

    public OtherSideData(final ItemStack delegate) {
        if (delegate != null) {
            this.delegate = delegate.getOrCreateTag();
            this.deserializeNBT(this.delegate.getCompound("OtherSideData"));
        }
    }

    public CompoundNBT getDelegate() {
        return this.delegate;
    }

    public void updateDelegate() {
        if (this.delegate != null) {
            this.delegate.put("OtherSideData", this.serializeNBT());
        }
    }

    public BlockPos getLinkedPos() {
        return this.linkedPos;
    }

    public RegistryKey<World> getLinkedDim() {
        return this.linkedDim;
    }

    public OtherSideData setLinkedPos(final BlockPos linkedPos) {
        this.linkedPos = linkedPos;
        this.updateDelegate();
        return this;
    }

    public OtherSideData setLinkedDim(final RegistryKey<World> linkedDim) {
        final RegistryKey<World> linkedDim2 = this.linkedDim;
        this.linkedDim = linkedDim;
        if (linkedDim2 != linkedDim) {
            this.updateDelegate();
        }
        return this;
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putIntArray("LinkedPos", new int[]{this.linkedPos.getX(), this.linkedPos.getY(), this.linkedPos.getZ()});
        nbt.putString("LinkedDim", this.linkedDim.location().toString());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        final int[] arr = nbt.getIntArray("LinkedPos");
        this.linkedPos = new BlockPos(arr[0], arr[1], arr[2]);
        this.linkedDim = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("LinkedDim")));
    }
}
