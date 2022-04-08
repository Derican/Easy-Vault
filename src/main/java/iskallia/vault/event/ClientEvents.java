package iskallia.vault.event;

import com.google.common.collect.Lists;
import iskallia.vault.Vault;
import iskallia.vault.client.ClientActiveEternalData;
import iskallia.vault.client.ClientDamageData;
import iskallia.vault.client.ClientTalentData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.skill.talent.type.archetype.FrenzyTalent;
import iskallia.vault.util.PlayerRageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;


@EventBusSubscriber({Dist.CLIENT})
public class ClientEvents {
    private static final ResourceLocation OVERLAY_ICONS = Vault.id("textures/gui/overlay_icons.png");

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void setupHealthTexture(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH) {
            return;
        }
        ClientPlayerEntity clientPlayerEntity = (Minecraft.getInstance()).player;
        if (clientPlayerEntity == null) {
            return;
        }
        TalentNode<?> talentNode = ClientTalentData.getLearnedTalentNode((TalentGroup) ModConfigs.TALENTS.FRENZY);
        if (talentNode == null || !talentNode.isLearned()) {
            return;
        }
        PlayerTalent talent = talentNode.getTalent();
        if (!(talent instanceof FrenzyTalent)) {
            return;
        }

        if (clientPlayerEntity.getHealth() / clientPlayerEntity.getMaxHealth() <= ((FrenzyTalent) talent).getThreshold()) {
            Minecraft.getInstance().getTextureManager().bind(OVERLAY_ICONS);
        }
    }

    @SubscribeEvent
    public static void cleanupHealthTexture(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH) {
            return;
        }

        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    @SubscribeEvent
    public static void onDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        PlayerRageHelper.clearClientCache();
        ClientActiveEternalData.clearClientCache();
        ClientDamageData.clearClientCache();
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ModConfigs.TOOLTIP.getTooltipString(event.getItemStack().getItem()).ifPresent(str -> {
            List<ITextComponent> tooltip = event.getToolTip();
            List<String> added = Lists.reverse(Lists.newArrayList( str.split("\n")));
            if (added.isEmpty())
                return;
            tooltip.add(1, StringTextComponent.EMPTY);
            for (String newStr : added)
                tooltip.add(1, (new StringTextComponent(newStr)).withStyle(TextFormatting.GRAY));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\ClientEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */