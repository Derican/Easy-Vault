package iskallia.vault.mixin;

import net.minecraft.inventory.container.RepairContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({RepairContainer.class})
public class MixinRepairContainer {
    @ModifyConstant(method = {"updateRepairOutput"}, constant = {@Constant(intValue = 40, ordinal = 2)})
    private int overrideMaxRepairLevel(int oldValue) {
        return Integer.MAX_VALUE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinRepairContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */