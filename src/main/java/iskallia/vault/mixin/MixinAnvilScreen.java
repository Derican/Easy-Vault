package iskallia.vault.mixin;

import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({AnvilScreen.class})
public class MixinAnvilScreen {
    @ModifyConstant(method = {"drawGuiContainerForegroundLayer"}, constant = {@Constant(intValue = 40)})
    private int overrideMaxRepairLevel(int oldValue) {
        return Integer.MAX_VALUE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinAnvilScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */