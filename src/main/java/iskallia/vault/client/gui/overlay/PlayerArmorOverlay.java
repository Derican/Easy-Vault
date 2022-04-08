package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.client.gui.helper.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;


@OnlyIn(Dist.CLIENT)
public class PlayerArmorOverlay {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void setupHealthTexture(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ARMOR) {
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

        int armor = clientPlayerEntity.getArmorValue();
        if (armor <= 20) {
            return;
        }
        event.setCanceled(true);

        MatrixStack matrixStack = event.getMatrixStack();

        RenderSystem.enableBlend();
        int left = mc.getWindow().getGuiScaledWidth() / 2 - 91;
        int top = mc.getWindow().getGuiScaledHeight() - ForgeIngameGui.left_height;

        for (int i = 0; i < 8; i++) {
            AbstractGui.blit(matrixStack, left, top, 0, 34.0F, 9.0F, 9, 9, 256, 256);
            left += 8;
        }

        FontHelper.drawStringWithBorder(matrixStack, String.valueOf(armor), (left + 2), (top + 1), -4671036, -16777216);

        ForgeIngameGui.left_height += 10;
        RenderSystem.disableBlend();
        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\PlayerArmorOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */