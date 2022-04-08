package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.time.extension.ModifierExtension;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TimeInfluence extends VaultInfluence {
    public static final ResourceLocation ID = Vault.id("time");

    private int timeChange;

    TimeInfluence() {
        super(ID);
    }

    public TimeInfluence(int timeChange) {
        this();
        this.timeChange = timeChange;
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.getTimer().addTime((TimeExtension) new ModifierExtension(this.timeChange), 0);
    }


    public void remove(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        player.getTimer().addTime((TimeExtension) new ModifierExtension(-this.timeChange), 0);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putInt("timeChange", this.timeChange);
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.timeChange = tag.getInt("timeChange");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\influence\TimeInfluence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */