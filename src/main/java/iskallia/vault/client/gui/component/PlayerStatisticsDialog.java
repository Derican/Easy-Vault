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
import net.minecraft.client.entity.player.ClientPlayerEntity;
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

public class PlayerStatisticsDialog extends ComponentDialog {
    private final List<Slot> slots = new ArrayList<>();

    public PlayerStatisticsDialog(SkillTreeScreen skillTreeScreen) {
        super(skillTreeScreen);
        this.descriptionComponent = new ScrollableContainer(this::renderPlayerAttributes);
    }

    private void createGearSlots() {
        ClientPlayerEntity clientPlayerEntity = (Minecraft.getInstance()).player;
        if (clientPlayerEntity == null) {
            return;
        }
        int startX = this.bounds.width - 24;
        int startY = 6;

        this.slots.add(new ReadOnlySlot((IInventory) ((PlayerEntity) clientPlayerEntity).inventory, ((PlayerEntity) clientPlayerEntity).inventory.selected, startX, startY));
        this.slots.add(new OffHandSlot((PlayerEntity) clientPlayerEntity, startX, startY + 18));
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            if (slotType.getType() == EquipmentSlotType.Group.ARMOR) {

                this.slots.add(new ArmorViewSlot((PlayerEntity) clientPlayerEntity, slotType, startX, startY + 36 + slotType.getIndex() * 18));
            }
        }
    }

    public void refreshWidgets() {
        this.slots.clear();
    }


    public int getHeaderHeight() {
        return 0;
    }


    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        this.slots.clear();
        createGearSlots();
    }


    public SkillTab createTab() {
        return (SkillTab) new PlayerStatisticsTab(getSkillTreeScreen());
    }


    public Point getIconUV() {
        return new Point(48, 60);
    }

    public Rectangle getFavourBoxBounds() {
        int playerBoxWidth = 80;
        return new Rectangle(5, 5, this.bounds.width - playerBoxWidth - 30, 108);
    }

    public Rectangle getPlayerBoxBounds() {
        int playerBoxWidth = 80;
        Rectangle ctBounds = getFavourBoxBounds();
        return new Rectangle(ctBounds.x + ctBounds.width, ctBounds.y, playerBoxWidth, 108);
    }

    public Rectangle getStatBoxBounds() {
        Rectangle ctBounds = getFavourBoxBounds();
        return new Rectangle(5, ctBounds.y + ctBounds.height + 5, this.bounds.width - 12, this.bounds.height - ctBounds.height - 16);
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack, mouseX, mouseY, partialTicks);

        matrixStack.pushPose();
        matrixStack.translate(this.bounds.x, this.bounds.y, 0.0D);
        renderContainers(matrixStack);
        renderPlayer(matrixStack, mouseX, mouseY, partialTicks);

        this.descriptionComponent.setBounds(getStatBoxBounds());
        this.descriptionComponent.render(matrixStack, mouseX, mouseY, partialTicks);

        renderPlayerFavour(matrixStack);
        renderPlayerItems(matrixStack, mouseX, mouseY, partialTicks);

        matrixStack.popPose();
    }

    private void renderContainers(MatrixStack matrixStack) {
        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);

        UIHelper.renderContainerBorder(this, matrixStack,
                getFavourBoxBounds(), 14, 44, 2, 2, 2, 2, -7631989);


        UIHelper.renderContainerBorder(this, matrixStack,
                getPlayerBoxBounds(), 14, 44, 2, 2, 2, 2, -16777216);


        UIHelper.renderContainerBorder(this, matrixStack,
                getStatBoxBounds(), 14, 44, 2, 2, 2, 2, -7631989);
    }


    private void renderPlayerFavour(MatrixStack matrixStack) {
        Rectangle favBounds = getFavourBoxBounds();
        FontRenderer fr = (Minecraft.getInstance()).font;

        int titleLengthRequired = 0;
        for (PlayerFavourData.VaultGodType vgType : PlayerFavourData.VaultGodType.values()) {
            int titleLength = fr.width((ITextProperties) new StringTextComponent(vgType.getTitle()));
            if (titleLength > titleLengthRequired) {
                titleLengthRequired = titleLength;
            }
        }


        boolean drawTitles = (titleLengthRequired + 10 + 10 < favBounds.width);

        matrixStack.pushPose();
        matrixStack.translate(favBounds.x, favBounds.y, 0.0D);

        fr.draw(matrixStack, (ITextComponent) new StringTextComponent("Favour:"), 5.0F, 5.0F, -15130590);

        matrixStack.pushPose();
        matrixStack.translate(5.0D, 20.0D, 0.0D);

        int maxLength = 0;
        for (PlayerFavourData.VaultGodType vgType : PlayerFavourData.VaultGodType.values()) {
            IFormattableTextComponent name = (new StringTextComponent(vgType.getName())).withStyle(vgType.getChatColor());
            fr.drawShadow(matrixStack, (ITextComponent) name, 0.0F, 0.0F, -1);

            int length = fr.width((ITextProperties) name);
            if (length > maxLength) {
                maxLength = length;
            }

            matrixStack.translate(0.0D, 10.0D, 0.0D);

            if (drawTitles) {
                IFormattableTextComponent title = (new StringTextComponent(vgType.getTitle())).withStyle(vgType.getChatColor());
                fr.drawShadow(matrixStack, (ITextComponent) title, 5.0F, 0.0F, -1);
                matrixStack.translate(0.0D, 10.0D, 0.0D);
            }


            matrixStack.translate(0.0D, 2.0D, 0.0D);
        }
        matrixStack.popPose();

        maxLength += 5;

        matrixStack.pushPose();
        matrixStack.translate(5.0D, 20.0D, 0.0D);


        matrixStack.translate(maxLength, 0.0D, 0.0D);
        for (PlayerFavourData.VaultGodType vgType : PlayerFavourData.VaultGodType.values()) {
            int favour = ClientStatisticsData.getFavour(vgType);
            fr.drawShadow(matrixStack, (ITextComponent) new StringTextComponent(String.valueOf(favour)), 0.0F, 0.0F, -1052689);

            matrixStack.translate(0.0D, drawTitles ? 22.0D : 12.0D, 0.0D);
        }
        matrixStack.popPose();
        matrixStack.popPose();

        RenderSystem.enableDepthTest();
    }

    private void renderPlayerAttributes(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Rectangle plBounds = getStatBoxBounds();
        FontRenderer fr = (Minecraft.getInstance()).font;

        int maxLength = 0;
        List<PlayerStatisticsCollector.AttributeSnapshot> snapshots = ClientStatisticsData.getPlayerAttributeSnapshots();
        Point offset = plBounds.getLocation();

        for (int i = 0; i < snapshots.size(); i++) {
            PlayerStatisticsCollector.AttributeSnapshot snapshot = snapshots.get(i);
            TranslationTextComponent translationTextComponent = new TranslationTextComponent(snapshot.getAttributeName());
            fr.draw(matrixStack, translationTextComponent.getVisualOrderText(), 10.0F, (10 * i + 10), -15130590);

            int length = fr.width((ITextProperties) translationTextComponent);
            if (length > maxLength) {
                maxLength = length;
            }
        }
        this.descriptionComponent.setInnerHeight(snapshots.size() * 10 + 20);

        maxLength += 5;

        int intLength = 0;
        for (PlayerStatisticsCollector.AttributeSnapshot snapshot : snapshots) {
            int intStrLength = fr.width(String.valueOf((int) snapshot.getValue()));
            if (intStrLength > intLength) {
                intLength = intStrLength;
            }
        }

        DecimalFormat format = new DecimalFormat("0.##");
        for (int j = 0; j < snapshots.size(); j++) {
            IFormattableTextComponent txt;
            matrixStack.pushPose();
            matrixStack.translate((maxLength + intLength + 4), (j * 10 + 10), 0.0D);

            PlayerStatisticsCollector.AttributeSnapshot snapshot = snapshots.get(j);
            int intStrLength = fr.width(String.valueOf((int) snapshot.getValue()));

            String numberStr = format.format(snapshot.getValue());
            if (snapshot.isPercentage()) {
                numberStr = numberStr + "%";
            }

            if (snapshot.hasHitLimit()) {
                String limitStr = format.format(snapshot.getLimit());
                if (snapshot.isPercentage()) {
                    limitStr = limitStr + "%";
                }
                txt = (new StringTextComponent(limitStr)).withStyle(style -> style.withColor(Color.fromRgb(-8519680)));
            } else {
                txt = (new StringTextComponent(numberStr)).withStyle(style -> style.withColor(Color.fromRgb(-15130590)));
            }
            int displayLength = fr.width(txt.getVisualOrderText());

            matrixStack.pushPose();
            matrixStack.translate(-intStrLength, 0.0D, 0.0D);

            fr.draw(matrixStack, txt.getVisualOrderText(), 0.0F, 0.0F, -1);

            matrixStack.popPose();
            matrixStack.popPose();


            Rectangle bounds = new Rectangle(this.bounds.x + offset.x + 10, this.bounds.y + offset.y + 10 * j + 10 - this.descriptionComponent.getyOffset(), maxLength + displayLength, 8);

            if (bounds.contains(mouseX, mouseY)) {
                if (snapshot.hasHitLimit()) {
                    List<ITextComponent> list = new ArrayList<>();
                    list.add((new StringTextComponent("Uncapped: ")).append(numberStr));

                    int offsetX = mouseX - this.bounds.x + offset.x;
                    int offsetY = mouseY - this.bounds.y + offset.y + this.descriptionComponent.getyOffset();
                    GuiUtils.drawHoveringText(matrixStack, list, offsetX, offsetY, offset.x + plBounds.width - 14, offset.y + plBounds.height, -1, fr);
                } else if (snapshot.hasLimit()) {
                    String limitStr = format.format(snapshot.getLimit());
                    if (snapshot.isPercentage()) {
                        limitStr = limitStr + "%";
                    }

                    List<ITextComponent> list = new ArrayList<>();
                    list.add((new StringTextComponent("Limit: ")).append(limitStr));

                    int offsetX = mouseX - this.bounds.x + offset.x;
                    int offsetY = mouseY - this.bounds.y + offset.y + this.descriptionComponent.getyOffset();
                    GuiUtils.drawHoveringText(matrixStack, list, offsetX, offsetY, offset.x + plBounds.width - 14, offset.y + plBounds.height, -1, fr);
                }
            }
        }


        RenderSystem.enableDepthTest();
    }

    private void renderPlayer(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Rectangle plBounds = getPlayerBoxBounds();

        int offsetX = plBounds.x + plBounds.width / 2;
        int offsetY = plBounds.y + 108 - 10;

        matrixStack.pushPose();
        matrixStack.translate(offsetX, offsetY, 0.0D);
        matrixStack.scale(1.6F, 1.6F, 1.6F);
        UIHelper.drawFacingPlayer(matrixStack, mouseX - this.bounds.x - offsetX, mouseY - this.bounds.y - offsetY);
        matrixStack.popPose();
    }

    private void renderPlayerItems(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Slot hoveredSlot = null;
        int slotHover = -2130706433;

        for (Slot slot : this.slots) {
            drawSlot(matrixStack, slot);

            Rectangle box = getSlotBox(slot);
            if (box.contains(mouseX - this.bounds.x, mouseY - this.bounds.y)) {
                int slotX = slot.x;
                int slotY = slot.y;

                matrixStack.pushPose();
                matrixStack.translate(slotX, slotY, 0.0D);

                RenderSystem.disableDepthTest();
                RenderSystem.colorMask(true, true, true, false);
                fillGradient(matrixStack, 0, 0, 16, 16, slotHover, slotHover);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();

                matrixStack.popPose();

                if (slot.hasItem()) {
                    hoveredSlot = slot;
                }
            }
        }

        if (hoveredSlot != null) {
            ItemStack toHover = hoveredSlot.getItem();
            FontRenderer fr = toHover.getItem().getFontRenderer(toHover);
            List<ITextComponent> tooltip = toHover.getTooltipLines((PlayerEntity) (Minecraft.getInstance()).player, (Minecraft.getInstance()).options.advancedItemTooltips ? (ITooltipFlag) ITooltipFlag.TooltipFlags.ADVANCED : (ITooltipFlag) ITooltipFlag.TooltipFlags.NORMAL);
            SkillTreeScreen skillTreeScreen = getSkillTreeScreen();

            matrixStack.pushPose();
            matrixStack.translate(0.0D, 0.0D, 550.0D);
            GuiUtils.preItemToolTip(toHover);
            GuiUtils.drawHoveringText(matrixStack, tooltip, mouseX - this.bounds.x, mouseY - this.bounds.y, ((Screen) skillTreeScreen).width - this.bounds.x, ((Screen) skillTreeScreen).height - this.bounds.y, -1, (fr == null) ?


                    (Minecraft.getInstance()).font : fr);
            GuiUtils.postItemToolTip();
            matrixStack.popPose();
            RenderSystem.enableDepthTest();
        }
    }

    private void drawSlot(MatrixStack matrixStack, Slot slot) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack slotStack = slot.getItem();
        int slotX = slot.x;
        int slotY = slot.y;

        matrixStack.pushPose();
        matrixStack.translate(slotX, slotY, 0.0D);

        Minecraft.getInstance().getTextureManager().bind(SkillTreeScreen.UI_RESOURCE);
        blit(matrixStack, -1, -1, 173, 0, 18, 18);

        setBlitOffset(100);
        itemRenderer.blitOffset = 100.0F;

        if (slotStack.isEmpty()) {
            Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
            if (pair != null) {
                TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getTextureAtlas((ResourceLocation) pair.getFirst()).apply(pair.getSecond());
                Minecraft.getInstance().getTextureManager().bind(textureatlassprite.atlas().location());
                blit(matrixStack, 0, 0, getBlitOffset(), 16, 16, textureatlassprite);
            }
        } else {
            RenderSystem.pushMatrix();
            RenderSystem.multMatrix(matrixStack.last().pose());

            RenderSystem.enableDepthTest();
            itemRenderer.renderAndDecorateItem((LivingEntity) (Minecraft.getInstance()).player, slotStack, 0, 0);
            itemRenderer.renderGuiItemDecorations((Minecraft.getInstance()).font, slotStack, 0, 0, null);
            RenderSystem.popMatrix();
        }

        itemRenderer.blitOffset = 0.0F;
        setBlitOffset(0);

        matrixStack.popPose();
    }

    private Rectangle getSlotBox(Slot slot) {
        return new Rectangle(slot.x - 1, slot.y - 1, 18, 18);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\component\PlayerStatisticsDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */