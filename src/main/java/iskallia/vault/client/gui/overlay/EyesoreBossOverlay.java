// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.overlay;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.datasync.DataParameter;
import java.util.Optional;
import net.minecraft.entity.player.PlayerEntity;
import iskallia.vault.entity.EyesoreEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.util.ResourceLocation;

public class EyesoreBossOverlay
{
    public static final ResourceLocation LASER_VIGNETTE;
    
    @SubscribeEvent
    public static void onPreRender(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        final Minecraft minecraft = Minecraft.getInstance();
        final MatrixStack matrixStack = event.getMatrixStack();
        if (minecraft.player == null) {
            return;
        }
        if (minecraft.level == null) {
            return;
        }
        minecraft.level.entitiesForRendering().iterator().forEachRemaining(entity -> {
            if (entity instanceof EyesoreEntity) {
                final EyesoreEntity eyesore = (EyesoreEntity)entity;
                final PlayerEntity target = (eyesore.getEntityData().get(EyesoreEntity.LASER_TARGET)).map(id -> entity.getCommandSenderWorld().getPlayerByUUID(id)).orElse(null);
                if (target != null) {
                    if (target == minecraft.player) {
                        minecraft.textureManager.bind(EyesoreBossOverlay.LASER_VIGNETTE);
                        RenderSystem.enableBlend();
                        AbstractGui.blit(matrixStack, 0, 0, 0.0f, 0.0f, minecraft.getWindow().getScreenWidth(), minecraft.getWindow().getScreenHeight(), minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
                    }
                }
            }
        });
    }
    
    static {
        LASER_VIGNETTE = new ResourceLocation("the_vault", "textures/gui/overlay/vignette_red.png");
    }
}
