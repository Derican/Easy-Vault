package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.util.PlayerRageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

@OnlyIn(Dist.CLIENT)
public class PlayerRageOverlay {
    private static final ResourceLocation OVERLAY_ICONS = Vault.id("textures/gui/overlay_icons.png");

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void setupHealthTexture(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        ClientPlayerEntity clientPlayerEntity = (Minecraft.getInstance()).player;
        if (clientPlayerEntity == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (!mc.gameMode.hasExperience()) {
            return;
        }

        int rage = PlayerRageHelper.getCurrentRage((PlayerEntity) clientPlayerEntity, LogicalSide.CLIENT);
        if (rage <= 0) {
            return;
        }

        int scaledWidth = event.getWindow().getGuiScaledWidth();
        int scaledHeight = event.getWindow().getGuiScaledHeight();
        MatrixStack matrixStack = event.getMatrixStack();

        int offsetX = scaledWidth / 2 - 91;
        int offsetY = scaledHeight - 32 + 3;
        int width = Math.round(182.0F * rage / 100.0F);
        int height = 5;

        int uOffset = 0;
        int vOffset = 64;
        mc.getTextureManager().bind(OVERLAY_ICONS);

        AbstractGui.blit(matrixStack, offsetX, offsetY, 0, uOffset, vOffset, width, height, 256, 256);

        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\PlayerRageOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */