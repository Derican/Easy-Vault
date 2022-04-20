package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.init.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AbilityVignetteOverlay {
    @SubscribeEvent
    public static void onPreRender(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        final MatrixStack matrixStack = event.getMatrixStack();
        final Minecraft minecraft = Minecraft.getInstance();
        final int width = minecraft.getWindow().getGuiScaledWidth();
        final int height = minecraft.getWindow().getGuiScaledHeight();
        if (minecraft.player == null) {
            return;
        }
        if (minecraft.player.getEffect(ModEffects.GHOST_WALK) != null) {
            AbstractGui.fill(matrixStack, 0, 0, width, height, 548137662);
        } else if (minecraft.player.getEffect(ModEffects.TANK) != null) {
            AbstractGui.fill(matrixStack, 0, 0, width, height, 545887369);
        }
    }
}
