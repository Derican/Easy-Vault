// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VampirismSet extends PlayerSet
{
    @Expose
    private final float leechRatio;
    
    public VampirismSet(final float leechRatio) {
        super(VaultGear.Set.VAMPIRE);
        this.leechRatio = leechRatio;
    }
    
    public float getLeechRatio() {
        return this.leechRatio;
    }
}
