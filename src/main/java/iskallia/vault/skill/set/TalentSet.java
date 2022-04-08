package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.talent.type.PlayerTalent;
import net.minecraft.entity.player.PlayerEntity;

public abstract class TalentSet<T extends PlayerTalent> extends PlayerSet {
    @Expose
    private final T child;

    public TalentSet(VaultGear.Set set, T child) {
        super(set);
        this.child = child;
    }

    public T getChild() {
        return this.child;
    }


    public void onAdded(PlayerEntity player) {
        this.child.onAdded(player);
    }


    public void onRemoved(PlayerEntity player) {
        this.child.onRemoved(player);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\TalentSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */