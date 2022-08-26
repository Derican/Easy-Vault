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
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ClientEvents {
    private static final ResourceLocation OVERLAY_ICONS;

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void setupHealthTexture(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH) {
            return;
        }
        final PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final TalentNode<?> talentNode = ClientTalentData.getLearnedTalentNode((TalentGroup<?>) ModConfigs.TALENTS.FRENZY);
        if (talentNode == null || !talentNode.isLearned()) {
            return;
        }
        final PlayerTalent talent = talentNode.getTalent();
        if (!(talent instanceof FrenzyTalent)) {
            return;
        }
        if (player.getHealth() / player.getMaxHealth() <= ((FrenzyTalent) talent).getThreshold()) {
            Minecraft.getInstance().getTextureManager().bind(ClientEvents.OVERLAY_ICONS);
        }
    }

    @SubscribeEvent
    public static void cleanupHealthTexture(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH) {
            return;
        }
        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    @SubscribeEvent
    public static void onDisconnect(final ClientPlayerNetworkEvent.LoggedOutEvent event) {
        PlayerRageHelper.clearClientCache();
        ClientActiveEternalData.clearClientCache();
        ClientDamageData.clearClientCache();
    }

    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        ModConfigs.TOOLTIP.getTooltipString(event.getItemStack().getItem()).ifPresent(str -> {
            final List tooltip = event.getToolTip();
            final List<String> added = Lists.reverse(Lists.newArrayList(str.split("\n")));
            if (!added.isEmpty()) {
                tooltip.add(1, StringTextComponent.EMPTY);

                for (String newStr : added) {
                    tooltip.add(1, new StringTextComponent(newStr).withStyle(TextFormatting.GRAY));
                }
            }
        });
    }

    static {
        OVERLAY_ICONS = Vault.id("textures/gui/overlay_icons.png");
    }
}
