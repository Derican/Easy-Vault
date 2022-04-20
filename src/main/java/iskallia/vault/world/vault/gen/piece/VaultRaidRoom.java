package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;

public class VaultRaidRoom extends VaultRoom {
    public static final ResourceLocation ID;
    private boolean raidFinished;

    public VaultRaidRoom() {
        super(VaultRaidRoom.ID);
        this.raidFinished = false;
    }

    public VaultRaidRoom(final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(VaultRaidRoom.ID, template, boundingBox, rotation);
        this.raidFinished = false;
    }

    public boolean isRaidFinished() {
        return this.raidFinished;
    }

    public void setRaidFinished() {
        this.raidFinished = true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        tag.putBoolean("raidFinished", this.raidFinished);
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.raidFinished = nbt.getBoolean("raidFinished");
    }

    static {
        ID = Vault.id("raid_room");
    }
}
