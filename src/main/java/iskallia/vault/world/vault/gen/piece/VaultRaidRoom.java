package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;

public class VaultRaidRoom extends VaultRoom {
    public static final ResourceLocation ID = Vault.id("raid_room");
    private boolean raidFinished = false;

    public VaultRaidRoom() {
        super(ID);
    }

    public VaultRaidRoom(ResourceLocation template, MutableBoundingBox boundingBox, Rotation rotation) {
        super(ID, template, boundingBox, rotation);
    }

    public boolean isRaidFinished() {
        return this.raidFinished;
    }

    public void setRaidFinished() {
        this.raidFinished = true;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putBoolean("raidFinished", this.raidFinished);
        return tag;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.raidFinished = nbt.getBoolean("raidFinished");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\piece\VaultRaidRoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */