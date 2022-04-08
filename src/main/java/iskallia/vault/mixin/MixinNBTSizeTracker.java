package iskallia.vault.mixin;

import net.minecraft.nbt.NBTSizeTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({NBTSizeTracker.class})
public class MixinNBTSizeTracker {
    @Overwrite
    public void accountBits(long bits) {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinNBTSizeTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */