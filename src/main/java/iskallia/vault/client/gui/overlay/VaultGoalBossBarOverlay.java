package iskallia.vault.client.gui.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.client.ClientVaultRaidData;
import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.network.message.VaultOverlayMessage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


@OnlyIn(Dist.CLIENT)
public class VaultGoalBossBarOverlay {
    @SubscribeEvent
    public static void onBossBarRender(RenderGameOverlayEvent.Pre event) {
        VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (event.getType() != RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
            return;
        }
        if (type != VaultOverlayMessage.OverlayType.VAULT) {
            return;
        }

        VaultGoalData data = VaultGoalData.CURRENT_DATA;
        if (data == null) {
            return;
        }
        BossBarOverlay overlay = data.getBossBarOverlay();
        if (overlay == null || !overlay.shouldDisplay()) {
            return;
        }

        MatrixStack renderStack = event.getMatrixStack();
        overlay.drawOverlay(renderStack, event.getPartialTicks());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\overlay\VaultGoalBossBarOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */