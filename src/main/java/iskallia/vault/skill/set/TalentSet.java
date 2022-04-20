package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.talent.type.PlayerTalent;
import net.minecraft.entity.player.PlayerEntity;

public abstract class TalentSet<T extends PlayerTalent> extends PlayerSet {
    @Expose
    private final T child;

    public TalentSet(final VaultGear.Set set, final T child) {
        super(set);
        this.child = child;
    }

    public T getChild() {
        return this.child;
    }

    @Override
    public void onAdded(final PlayerEntity player) {
        this.child.onAdded(player);
    }

    @Override
    public void onRemoved(final PlayerEntity player) {
        this.child.onRemoved(player);
    }
}
