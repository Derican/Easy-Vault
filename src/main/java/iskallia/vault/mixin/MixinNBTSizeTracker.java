// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import net.minecraft.nbt.NBTSizeTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ NBTSizeTracker.class })
public class MixinNBTSizeTracker
{
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void accountBits(final long bits) {
    }
}
