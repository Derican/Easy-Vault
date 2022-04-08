package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.init.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class AbilityVignetteOverlay {
    @SubscribeEvent
    public static void onPreRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        MatrixStack matrixStack = event.getMatrixStack();

        Minecraft minecraft = Minecraft.getInstance();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();

        if (minecraft.player == null)
            return;
        if (minecraft.player.getEffect(ModEffects.GHOST_WALK) != null) {
            AbstractGui.fill(matrixStack, 0, 0, width, height, 548137662);
        } else if (minecraft.player.getEffect(ModEffects.TANK) != null) {
            AbstractGui.fill(matrixStack, 0, 0, width, height, 545887369);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\AbilityVignetteOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */