// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import iskallia.vault.client.ClientStatisticsData;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.tab.PlayerStatisticsTab;
import iskallia.vault.client.gui.tab.SkillTab;
import iskallia.vault.container.slot.ReadOnlySlot;
import iskallia.vault.container.slot.player.ArmorViewSlot;
import iskallia.vault.container.slot.player.OffHandSlot;
import iskallia.vault.util.calc.PlayerStatisticsCollector;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayerStatisticsDialog extends ComponentDialog
{
    private final List<Slot> slots;
    
    public PlayerStatisticsDialog(final SkillTreeScreen skillTreeScreen) {
        super(skillTreeScreen);
        this.slots = new ArrayList<Slot>();
        this.descriptionComponent = new ScrollableContainer(this::renderPlayerAttributes);
    }
    
    private void createGearSlots() {
        final PlayerEntity player = (PlayerEntity)Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final int startX = this.bounds.width - 24;
        final int startY = 6;
        this.slots.add(new ReadOnlySlot((IInventory)player.inventory, player.inventory.selected, startX, startY));
        this.slots.add(new OffHandSlot(player, startX, startY + 18));
        for (final EquipmentSlotType slotType : EquipmentSlotType.values()) {
            if (slotType.getType() == EquipmentSlotType.Group.ARMOR) {
                this.slots.add(new ArmorViewSlot(player, slotType, startX, startY + 36 + slotType.getIndex() * 18));
            }
        }
    }
    
    @Override
    public void refreshWidgets() {
        this.slots.clear();
    }
    
    @Override
    public int getHeaderHeight() {
        return 0;
    }
    
    @Override
    public void setBounds(final Rectangle bounds) {
        super.setBounds(bounds);
        this.slots.clear();
        this.createGearSlots();
    }
    
    @Override
    public SkillTab createTab() {
        return new PlayerStatisticsTab(this.getSkillTreeScreen());
    }
    
    @Override
    public Point getIconUV() {
        return new Point(48, 60);
    }
    
    public Rectangle getFavourBoxBounds() {
        final int playerBoxWidth = 80;
        return new Rectangle(5, 5, this.bounds.width - playerBoxWidth - 30, 108);
    }
    
    public Rectangle getPlayerBoxBounds() {
        final int playerBoxWidth = 80;
        final Rectangle ctBounds = this.getFavourBoxBounds();
        return new Rectangle(ctBounds.x + ctBounds.width, ctBounds.y, playerBoxWidth, 108);
    }
    
    public Rectangle getStatBoxBounds() {
        final Rectangle ctBounds = this.getFavourBoxBounds();
        return new Rectangle(5, ctBounds.y + ctBounds.height + 5, this.bounds.width - 12, this.bounds.height - ctBounds.height - 16);
    }
    
    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.pushPose();
        matrixStack.translate((double)this.bounds.x, (double)this.bounds.y, 0.0);
        this.renderContainers(matrixStack);
        this.renderPlayer(matrixStack, mouseX, mouseY, partialTicks);
        this.descriptionComponent.setBounds(this.getStatBoxBounds());
        this.descriptionComponent.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderPlayerFavour(matrixStack);
        this.renderPlayerItems(matrixStack, mouseX, mouseY, partialTicks);
        matrixStack.popPose();
    }
    
    private void renderContainers(final MatrixStack matrixStack) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        UIHelper.renderContainerBorder(this, matrixStack, this.getFavourBoxBounds(), 14, 44, 2, 2, 2, 2, -7631989);
        UIHelper.renderContainerBorder(this, matrixStack, this.getPlayerBoxBounds(), 14, 44, 2, 2, 2, 2, -16777216);
        UIHelper.renderContainerBorder(this, matrixStack, this.getStatBoxBounds(), 14, 44, 2, 2, 2, 2, -7631989);
    }
    
    private void renderPlayerFavour(final MatrixStack matrixStack) {
        final Rectangle favBounds = this.getFavourBoxBounds();
        final FontRenderer fr = Minecraft.getInstance().font;
        int titleLengthRequired = 0;
        for (final PlayerFavourData.VaultGodType vgType : PlayerFavourData.VaultGodType.values()) {
            final int titleLength = fr.width((ITextProperties)new StringTextComponent(vgType.getTitle()));
            if (titleLength > titleLengthRequired) {
                titleLengthRequired = titleLength;
            }
        }
        final boolean drawTitles = titleLengthRequired + 10 + 10 < favBounds.width;
        matrixStack.pushPose();
        matrixStack.translate((double)favBounds.x, (double)favBounds.y, 0.0);
        fr.draw(matrixStack, (ITextComponent)new StringTextComponent("Favour:"), 5.0f, 5.0f, -15130590);
        matrixStack.pushPose();
        matrixStack.translate(5.0, 20.0, 0.0);
        int maxLength = 0;
        for (final PlayerFavourData.VaultGodType vgType2 : PlayerFavourData.VaultGodType.values()) {
            final IFormattableTextComponent name = new StringTextComponent(vgType2.getName()).withStyle(vgType2.getChatColor());
            fr.drawShadow(matrixStack, (ITextComponent)name, 0.0f, 0.0f, -1);
            final int length = fr.width((ITextProperties)name);
            if (length > maxLength) {
                maxLength = length;
            }
            matrixStack.translate(0.0, 10.0, 0.0);
            if (drawTitles) {
                final IFormattableTextComponent title = new StringTextComponent(vgType2.getTitle()).withStyle(vgType2.getChatColor());
                fr.drawShadow(matrixStack, (ITextComponent)title, 5.0f, 0.0f, -1);
                matrixStack.translate(0.0, 10.0, 0.0);
            }
            matrixStack.translate(0.0, 2.0, 0.0);
        }
        matrixStack.popPose();
        maxLength += 5;
        matrixStack.pushPose();
        matrixStack.translate(5.0, 20.0, 0.0);
        matrixStack.translate((double)maxLength, 0.0, 0.0);
        for (final PlayerFavourData.VaultGodType vgType2 : PlayerFavourData.VaultGodType.values()) {
            final int favour = ClientStatisticsData.getFavour(vgType2);
            fr.drawShadow(matrixStack, (ITextComponent)new StringTextComponent(String.valueOf(favour)), 0.0f, 0.0f, -1052689);
            matrixStack.translate(0.0, drawTitles ? 22.0 : 12.0, 0.0);
        }
        matrixStack.popPose();
        matrixStack.popPose();
        RenderSystem.enableDepthTest();
    }
    
    private void renderPlayerAttributes(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Rectangle plBounds = this.getStatBoxBounds();
        final FontRenderer fr = Minecraft.getInstance().font;
        int maxLength = 0;
        final List<PlayerStatisticsCollector.AttributeSnapshot> snapshots = ClientStatisticsData.getPlayerAttributeSnapshots();
        final Point offset = plBounds.getLocation();
        for (int i = 0; i < snapshots.size(); ++i) {
            final PlayerStatisticsCollector.AttributeSnapshot snapshot = snapshots.get(i);
            final ITextComponent cmp = (ITextComponent)new TranslationTextComponent(snapshot.getAttributeName());
            fr.draw(matrixStack, cmp.getVisualOrderText(), 10.0f, (float)(10 * i + 10), -15130590);
            final int length = fr.width((ITextProperties)cmp);
            if (length > maxLength) {
                maxLength = length;
            }
        }
        this.descriptionComponent.setInnerHeight(snapshots.size() * 10 + 20);
        maxLength += 5;
        int intLength = 0;
        for (final PlayerStatisticsCollector.AttributeSnapshot snapshot2 : snapshots) {
            final int intStrLength = fr.width(String.valueOf((int)snapshot2.getValue()));
            if (intStrLength > intLength) {
                intLength = intStrLength;
            }
        }
        final DecimalFormat format = new DecimalFormat("0.##");
        for (int j = 0; j < snapshots.size(); ++j) {
            matrixStack.pushPose();
            matrixStack.translate((double)(maxLength + intLength + 4), (double)(j * 10 + 10), 0.0);
            final PlayerStatisticsCollector.AttributeSnapshot snapshot3 = snapshots.get(j);
            final int intStrLength2 = fr.width(String.valueOf((int)snapshot3.getValue()));
            String numberStr = format.format(snapshot3.getValue());
            if (snapshot3.isPercentage()) {
                numberStr += "%";
            }
            IFormattableTextComponent txt;
            if (snapshot3.hasHitLimit()) {
                String limitStr = format.format(snapshot3.getLimit());
                if (snapshot3.isPercentage()) {
                    limitStr += "%";
                }
                txt = new StringTextComponent(limitStr).withStyle(style -> style.withColor(Color.fromRgb(-8519680)));
            }
            else {
                txt = new StringTextComponent(numberStr).withStyle(style -> style.withColor(Color.fromRgb(-15130590)));
            }
            final int displayLength = fr.width(txt.getVisualOrderText());
            matrixStack.pushPose();
            matrixStack.translate((double)(-intStrLength2), 0.0, 0.0);
            fr.draw(matrixStack, txt.getVisualOrderText(), 0.0f, 0.0f, -1);
            matrixStack.popPose();
            matrixStack.popPose();
            final Rectangle bounds = new Rectangle(this.bounds.x + offset.x + 10, this.bounds.y + offset.y + 10 * j + 10 - this.descriptionComponent.getyOffset(), maxLength + displayLength, 8);
            if (bounds.contains(mouseX, mouseY)) {
                if (snapshot3.hasHitLimit()) {
                    final List<ITextComponent> list = new ArrayList<ITextComponent>();
                    list.add((ITextComponent)new StringTextComponent("Uncapped: ").append(numberStr));
                    final int offsetX = mouseX - (this.bounds.x + offset.x);
                    final int offsetY = mouseY - (this.bounds.y + offset.y) + this.descriptionComponent.getyOffset();
                    GuiUtils.drawHoveringText(matrixStack, (List)list, offsetX, offsetY, offset.x + plBounds.width - 14, offset.y + plBounds.height, -1, fr);
                }
                else if (snapshot3.hasLimit()) {
                    String limitStr2 = format.format(snapshot3.getLimit());
                    if (snapshot3.isPercentage()) {
                        limitStr2 += "%";
                    }
                    final List<ITextComponent> list2 = new ArrayList<ITextComponent>();
                    list2.add((ITextComponent)new StringTextComponent("Limit: ").append(limitStr2));
                    final int offsetX2 = mouseX - (this.bounds.x + offset.x);
                    final int offsetY2 = mouseY - (this.bounds.y + offset.y) + this.descriptionComponent.getyOffset();
                    GuiUtils.drawHoveringText(matrixStack, (List)list2, offsetX2, offsetY2, offset.x + plBounds.width - 14, offset.y + plBounds.height, -1, fr);
                }
            }
        }
        RenderSystem.enableDepthTest();
    }
    
    private void renderPlayer(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        final Rectangle plBounds = this.getPlayerBoxBounds();
        final int offsetX = plBounds.x + plBounds.width / 2;
        final int offsetY = plBounds.y + 108 - 10;
        matrixStack.pushPose();
        matrixStack.translate((double)offsetX, (double)offsetY, 0.0);
        matrixStack.scale(1.6f, 1.6f, 1.6f);
        UIHelper.drawFacingPlayer(matrixStack, mouseX - this.bounds.x - offsetX, mouseY - this.bounds.y - offsetY);
        matrixStack.popPose();
    }
    
    private void renderPlayerItems(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        Slot hoveredSlot = null;
        final int slotHover = -2130706433;
        for (final Slot slot : this.slots) {
            this.drawSlot(matrixStack, slot);
            final Rectangle box = this.getSlotBox(slot);
            if (box.contains(mouseX - this.bounds.x, mouseY - this.bounds.y)) {
                final int slotX = slot.x;
                final int slotY = slot.y;
                matrixStack.pushPose();
                matrixStack.translate((double)slotX, (double)slotY, 0.0);
                RenderSystem.disableDepthTest();
                RenderSystem.colorMask(true, true, true, false);
                this.fillGradient(matrixStack, 0, 0, 16, 16, slotHover, slotHover);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();
                matrixStack.popPose();
                if (!slot.hasItem()) {
                    continue;
                }
                hoveredSlot = slot;
            }
        }
        if (hoveredSlot != null) {
            final ItemStack toHover = hoveredSlot.getItem();
            final FontRenderer fr = toHover.getItem().getFontRenderer(toHover);
            final List<ITextComponent> tooltip = toHover.getTooltipLines((PlayerEntity)Minecraft.getInstance().player, (ITooltipFlag)(Minecraft.getInstance().options.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL));
            final Screen mainScreen = (Screen)this.getSkillTreeScreen();
            matrixStack.pushPose();
            matrixStack.translate(0.0, 0.0, 550.0);
            GuiUtils.preItemToolTip(toHover);
            GuiUtils.drawHoveringText(matrixStack, (List)tooltip, mouseX - this.bounds.x, mouseY - this.bounds.y, mainScreen.width - this.bounds.x, mainScreen.height - this.bounds.y, -1, (fr == null) ? Minecraft.getInstance().font : fr);
            GuiUtils.postItemToolTip();
            matrixStack.popPose();
            RenderSystem.enableDepthTest();
        }
    }
    
    private void drawSlot(final MatrixStack matrixStack, final Slot slot) {
        final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        final ItemStack slotStack = slot.getItem();
        final int slotX = slot.x;
        final int slotY = slot.y;
        matrixStack.pushPose();
        matrixStack.translate((double)slotX, (double)slotY, 0.0);
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        this.blit(matrixStack, -1, -1, 173, 0, 18, 18);
        this.setBlitOffset(100);
        itemRenderer.blitOffset = 100.0f;
        if (slotStack.isEmpty()) {
            final Pair<ResourceLocation, ResourceLocation> pair = (Pair<ResourceLocation, ResourceLocation>)slot.getNoItemIcon();
            if (pair != null) {
                final TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getTextureAtlas((ResourceLocation)pair.getFirst()).apply(pair.getSecond());
                Minecraft.getInstance().getTextureManager().bind(textureatlassprite.atlas().location());
                blit(matrixStack, 0, 0, this.getBlitOffset(), 16, 16, textureatlassprite);
            }
        }
        else {
            RenderSystem.pushMatrix();
            RenderSystem.multMatrix(matrixStack.last().pose());
            RenderSystem.enableDepthTest();
            itemRenderer.renderAndDecorateItem((LivingEntity)Minecraft.getInstance().player, slotStack, 0, 0);
            itemRenderer.renderGuiItemDecorations(Minecraft.getInstance().font, slotStack, 0, 0, (String)null);
            RenderSystem.popMatrix();
        }
        itemRenderer.blitOffset = 0.0f;
        this.setBlitOffset(0);
        matrixStack.popPose();
    }
    
    private Rectangle getSlotBox(final Slot slot) {
        return new Rectangle(slot.x - 1, slot.y - 1, 18, 18);
    }
}
