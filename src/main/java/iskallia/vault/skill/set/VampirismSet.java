package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VampirismSet extends PlayerSet {
    @Expose
    private final float leechRatio;

    public VampirismSet(float leechRatio) {
        super(VaultGear.Set.VAMPIRE);
        this.leechRatio = leechRatio;
    }

    public float getLeechRatio() {
        return this.leechRatio;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\VampirismSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */