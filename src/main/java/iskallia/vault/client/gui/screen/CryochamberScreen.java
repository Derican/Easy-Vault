package iskallia.vault.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import iskallia.vault.Vault;
import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.client.ClientEternalData;
import iskallia.vault.client.gui.helper.FontHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.widget.TooltipImageButton;
import iskallia.vault.client.util.ShaderUtil;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.config.entry.FloatRangeEntry;
import iskallia.vault.container.inventory.CryochamberContainer;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.entity.eternal.EternalDataAccess;
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import iskallia.vault.entity.eternal.EternalHelper;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.EternalInteractionMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.lwjgl.opengl.ARBShaderObjects;

import javax.annotation.Nullable;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CryochamberScreen extends ContainerScreen<CryochamberContainer> {
    private static final DecimalFormat ATTRIBUTE_FORMAT = new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ROOT));
    private static final DecimalFormat ATTRIBUTE_MS_FORMAT = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ROOT));
    private static final ResourceLocation TEXTURE = Vault.id("textures/gui/cryochamber_inventory.png");

    private EternalDataSnapshot prevSnapshot = null;

    private EternalEntity eternalSkinCache = null;

    public CryochamberScreen(CryochamberContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title);

        this.imageWidth = 176;
        this.imageHeight = 211;
        this.inventoryLabelY = this.imageHeight - 94;
    }


    protected void init() {
        super.init();

        refreshButtons();
    }

    private void refreshButtons() {
        this.buttons.clear();
        this.children.clear();

        EternalDataSnapshot snapshot = getEternal();
        if (snapshot == null) {
            return;
        }

        if (snapshot.getUsedLevels() < snapshot.getLevel()) {
            int offsetX = this.leftPos + 78;
            int yOffset = 0;
            int yShift = 16;
            addButton((Widget) new ImageButton(offsetX, this.topPos + 18, 16, 16, 176, yOffset, yShift, TEXTURE, 256, 256, btn -> {
                if (snapshot.getUsedLevels() >= snapshot.getLevel()) return;
                ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.levelUp("health"));
            },
            (btn, matrixStack, mouseX, mouseY) -> renderAttributeHoverTooltip(ModConfigs.ETERNAL_ATTRIBUTES.getHealthRollRange(), 1.0F, ATTRIBUTE_FORMAT, matrixStack, mouseX, mouseY), StringTextComponent.EMPTY))
            ;


            addButton((Widget) new ImageButton(offsetX, this.topPos + 36, 16, 16, 176, yOffset, yShift, TEXTURE, 256, 256, btn -> {
                if (snapshot.getUsedLevels() >= snapshot.getLevel()) return;
                ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.levelUp("damage"));
            },
            (btn, matrixStack, mouseX, mouseY) -> renderAttributeHoverTooltip(ModConfigs.ETERNAL_ATTRIBUTES.getDamageRollRange(), 1.0F, ATTRIBUTE_FORMAT, matrixStack, mouseX, mouseY), StringTextComponent.EMPTY))
            ;


            addButton((Widget) new ImageButton(offsetX, this.topPos + 54, 16, 16, 176, yOffset, yShift, TEXTURE, 256, 256, btn -> {
                if (snapshot.getUsedLevels() >= snapshot.getLevel()) return;
                ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.levelUp("movespeed"));
            },
            (btn, matrixStack, mouseX, mouseY) -> renderAttributeHoverTooltip(ModConfigs.ETERNAL_ATTRIBUTES.getMoveSpeedRollRange(), 10.0F, ATTRIBUTE_MS_FORMAT, matrixStack, mouseX, mouseY), StringTextComponent.EMPTY))
            ;
        }


        if (snapshot.getAbilityName() == null) {
            List<EternalAuraConfig.AuraConfig> options = ModConfigs.ETERNAL_AURAS.getRandom(snapshot.getSeededRand(), 3);
            int abilityX = this.leftPos + 8;
            int abilityY = this.topPos + 90;

            for (EternalAuraConfig.AuraConfig abilityOption : options) {
                addButton((Widget) new TooltipImageButton(abilityX, abilityY, 24, 24, 192, 0, 24, TEXTURE, 256, 256, btn -> ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.selectEffect(abilityOption.getName()))));


                abilityX += 30;
            }
        }
    }

    private void renderAttributeHoverTooltip(FloatRangeEntry range, float multiplier, DecimalFormat format, MatrixStack matrixStack, int mouseX, int mouseY) {
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, 300.0D);
        String min = format.format((range.getMin() * multiplier));
        String max = format.format((range.getMax() * multiplier));
        StringTextComponent txt = new StringTextComponent("Adds +" + min + " to +" + max);
        renderToolTip(matrixStack, Lists.newArrayList(new IReorderingProcessor[]{txt.getVisualOrderText()} ), mouseX, mouseY, this.font);
        matrixStack.popPose();
    }


    public void tick() {
        super.tick();

        EternalDataSnapshot snapshot = getEternal();
        if (snapshot == null) {
            return;
        }

        if (this.prevSnapshot == null || !this.prevSnapshot.areStatisticsEqual(snapshot)) {
            this.prevSnapshot = snapshot;
            refreshButtons();
        }
    }


    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int offsetX = (this.width - this.imageWidth) / 2;
        int offsetY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, offsetX, offsetY, 0, 0, this.imageWidth, this.imageHeight);
    }


    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        EternalDataSnapshot snapshot = getEternal();
        if (snapshot == null) {
            return;
        }
        if (this.eternalSkinCache == null) {
            this.eternalSkinCache = EternalHelper.spawnEternal((World) (Minecraft.getInstance()).level, (EternalDataAccess) snapshot);
            this.eternalSkinCache.skin.updateSkin(snapshot.getName());
            Arrays.<EquipmentSlotType>stream(EquipmentSlotType.values()).forEach(slot -> this.eternalSkinCache.setItemSlot(slot, ItemStack.EMPTY));
        }

        if (snapshot.isAncient()) {
            FontHelper.drawStringWithBorder(matrixStack, this.title, this.titleLabelX, this.titleLabelY, 15910161, 4210752);
        } else {
            this.font.draw(matrixStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
        }
        this.font.draw(matrixStack, this.inventory.getDisplayName(), this.inventoryLabelX, this.inventoryLabelY, 4210752);

        renderEternal(snapshot, matrixStack, mouseX, mouseY);

        RenderSystem.enableDepthTest();
        renderLevel(snapshot, matrixStack);
        renderAttributeDisplay(snapshot, matrixStack);
        renderAbility(snapshot, matrixStack, mouseX, mouseY);
    }

    private void renderAbility(EternalDataSnapshot snapshot, MatrixStack matrixStack, int mouseX, int mouseY) {
        if (snapshot.getAbilityName() == null) {
            List<EternalAuraConfig.AuraConfig> options = ModConfigs.ETERNAL_AURAS.getRandom(snapshot.getSeededRand(), 3);
            int abilityX = 12;
            int abilityY = 94;

            for (EternalAuraConfig.AuraConfig abilityOption : options) {
                this.minecraft.getTextureManager().bind(new ResourceLocation(abilityOption.getIconPath()));
                blit(matrixStack, abilityX, abilityY, 16.0F, 16.0F, 16, 16, 16, 16);

                abilityX += 30;
            }

            abilityX = 8;
            abilityY = 90;
            for (EternalAuraConfig.AuraConfig abilityOption : options) {
                Rectangle box = new Rectangle(abilityX, abilityY, 24, 24);
                if (box.contains(mouseX - this.leftPos, mouseY - this.topPos)) {
                    renderComponentTooltip(matrixStack, abilityOption.getTooltip(), mouseX - this.leftPos, mouseY - this.topPos);
                }

                abilityX += 30;
            }
        } else {
            EternalAuraConfig.AuraConfig cfg = ModConfigs.ETERNAL_AURAS.getByName(snapshot.getAbilityName());
            if (cfg == null) {
                return;
            }
            this.minecraft.getTextureManager().bind(new ResourceLocation(cfg.getIconPath()));
            blit(matrixStack, 8, 92, 0.0F, 0.0F, 16, 16, 16, 16);

            matrixStack.pushPose();
            matrixStack.translate(26.0D, 92.0D, 0.0D);
            matrixStack.scale(0.8F, 0.8F, 0.8F);
            UIHelper.renderWrappedText(matrixStack, (ITextComponent) new StringTextComponent(cfg.getDescription()), 82, 0, 4210752);
            matrixStack.popPose();
        }
    }

    private void renderAttributeDisplay(EternalDataSnapshot snapshot, MatrixStack matrixStack) {
        String healthStr = ATTRIBUTE_FORMAT.format(snapshot.getEntityAttributes().get(Attributes.MAX_HEALTH));
        renderAttributeStats(matrixStack, "Health:", healthStr, 18, 32);
        String damageStr = ATTRIBUTE_FORMAT.format(snapshot.getEntityAttributes().get(Attributes.ATTACK_DAMAGE));
        renderAttributeStats(matrixStack, "Damage:", damageStr, 36, 48);
        String speedStr = ATTRIBUTE_MS_FORMAT.format((((Float) snapshot.getEntityAttributes().get(Attributes.MOVEMENT_SPEED)).floatValue() * 10.0F));
        renderAttributeStats(matrixStack, "Speed:", speedStr, 54, 64);

        int availableLevels = Math.max(snapshot.getLevel() - snapshot.getUsedLevels(), 0);
        if (availableLevels > 0) {
            String display = String.valueOf(availableLevels);
            int offsetX = this.font.width(display) / 2;

            matrixStack.pushPose();
            matrixStack.translate(86.0D, 13.0D, 0.0D);
            matrixStack.scale(0.8F, 0.8F, 0.8F);
            matrixStack.translate(-offsetX, 0.0D, 0.0D);
            this.font.draw(matrixStack, display, 0.0F, 0.0F, 4210752);
            matrixStack.popPose();
        }

        String parryPercent = PERCENT_FORMAT.format((snapshot.getParry() * 100.0F)) + "%";
        String resistPercent = PERCENT_FORMAT.format((snapshot.getResistance() * 100.0F)) + "%";
        String armorAmount = PERCENT_FORMAT.format(EternalHelper.getEternalGearModifierAdjustments((EternalDataAccess) snapshot, Attributes.ARMOR, 0.0F));

        this.minecraft.getTextureManager().bind(TEXTURE);
        blit(matrixStack, 8, 72, 216, 0, 16, 16);
        matrixStack.pushPose();
        matrixStack.translate(24.0D, 72.0D, 0.0D);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
        this.font.draw(matrixStack, parryPercent, 0.0F, 5.0F, 4210752);
        matrixStack.popPose();

        this.minecraft.getTextureManager().bind(TEXTURE);
        blit(matrixStack, 39, 71, 216, 16, 16, 16);
        matrixStack.pushPose();
        matrixStack.translate(55.0D, 72.0D, 0.0D);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
        this.font.draw(matrixStack, resistPercent, 0.0F, 5.0F, 4210752);
        matrixStack.popPose();

        this.minecraft.getTextureManager().bind(TEXTURE);
        blit(matrixStack, 70, 72, 216, 80, 16, 16);
        matrixStack.pushPose();
        matrixStack.translate(86.0D, 72.0D, 0.0D);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
        this.font.draw(matrixStack, armorAmount, 0.0F, 5.0F, 4210752);
        matrixStack.popPose();
    }

    private void renderAttributeStats(MatrixStack matrixStack, String description, String valueStr, int offsetY, int vOffset) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        blit(matrixStack, 8, offsetY, 216, vOffset, 16, 16);
        matrixStack.pushPose();
        matrixStack.translate(26.0D, (offsetY + 6), 0.0D);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
        this.font.draw(matrixStack, description, 0.0F, 0.0F, 4210752);
        matrixStack.popPose();

        float xShift = this.font.width(valueStr) * 0.8F;

        matrixStack.pushPose();
        matrixStack.translate(73.0D, (offsetY + 6), 0.0D);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
        matrixStack.translate(-xShift, 0.0D, 0.0D);
        this.font.draw(matrixStack, valueStr, 0.0F, 0.0F, 4210752);
        matrixStack.popPose();
    }

    private void renderLevel(EternalDataSnapshot snapshot, MatrixStack matrixStack) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        int levelPart = MathHelper.floor(snapshot.getLevelPercent() * 62.0F);

        blit(matrixStack, 103, 17, 0, 212, 62, 5);
        blit(matrixStack, 103, 17, 0, 218, levelPart, 5);

        String lvlStr = snapshot.getLevel() + " / " + snapshot.getMaxLevel();
        float x = 136.0F - this.font.width(lvlStr) / 2.0F;
        int y = 12;
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0.0D);
        matrixStack.scale(0.8F, 0.8F, 1.0F);
        FontHelper.drawStringWithBorder(matrixStack, lvlStr, 0.0F, 0.0F, -6601, -12698050);
        matrixStack.popPose();
    }

    private void renderEternal(EternalDataSnapshot snapshot, MatrixStack matrixStack, int mouseX, int mouseY) {
        int offsetX = 125;
        int offsetY = 105;

        if (!snapshot.isAlive()) {
            ShaderUtil.useShader(ShaderUtil.GRAYSCALE_SHADER, () -> {
                int grayScaleFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "grayFactor");

                ARBShaderObjects.glUniform1fARB(grayScaleFactor, 0.0F);
                int brightnessFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "brightness");
                ARBShaderObjects.glUniform1fARB(brightnessFactor, 1.0F);
            });
        }
        int lookX = mouseX - this.leftPos - offsetX;
        int lookY = mouseY - this.topPos - offsetY;
        if (!snapshot.isAlive()) {
            lookX = 0;
            lookY = -30;
        }

        matrixStack.pushPose();
        matrixStack.translate(offsetX, offsetY, -400.0D);
        if (!snapshot.isAncient()) {
            matrixStack.scale(1.2F, 1.2F, 1.2F);
        }
        UIHelper.drawFacingEntity((LivingEntity) this.eternalSkinCache, matrixStack, lookX, lookY);
        matrixStack.popPose();

        if (!snapshot.isAlive()) {
            ShaderUtil.releaseShader();
        }

        ItemStack heldStack = this.inventory.getCarried();
        if (!heldStack.isEmpty() && EternalInteractionMessage.canBeFed((EternalDataAccess) snapshot, heldStack)) {
            Rectangle feedRct = new Rectangle(99, 25, 51, 90);
            if (feedRct.contains(mouseX - this.leftPos, mouseY - this.topPos)) {
                renderTooltip(matrixStack, (ITextComponent) new StringTextComponent("Give to " + this.title.getString()), mouseX - this.leftPos, mouseY - this.topPos);
            }
        }

        if (!snapshot.isAlive()) {
            String deadTxt = "Unalived";

            matrixStack.pushPose();
            matrixStack.translate(0.0D, 0.0D, 600.0D);
            int width = this.font.width(deadTxt);
            FontHelper.drawStringWithBorder(matrixStack, deadTxt, 125.0F - width / 2.0F, 100.0F, 16724016, 0);
            matrixStack.popPose();
        }

        if (snapshot.isAncient()) {
            String ancientTxt = "Ancient";

            matrixStack.pushPose();
            matrixStack.translate(0.0D, 0.0D, 600.0D);
            int width = this.font.width(ancientTxt);
            FontHelper.drawStringWithBorder(matrixStack, ancientTxt, 125.0F - width / 2.0F, 28.0F, 15910161, 0);
            matrixStack.popPose();
        }
    }


    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (button != 0) {
            return false;
        }
        EternalDataSnapshot snapshot = getEternal();
        if (snapshot == null) {
            return false;
        }
        ItemStack heldStack = this.inventory.getCarried();
        if (heldStack.isEmpty() || !EternalInteractionMessage.canBeFed((EternalDataAccess) snapshot, heldStack)) {
            return false;
        }

        Rectangle feedRct = new Rectangle(99, 25, 51, 90);
        if (!feedRct.contains(mouseX - this.leftPos, mouseY - this.topPos)) {
            return false;
        }

        ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.feedItem(heldStack));
        if (!(Minecraft.getInstance()).player.isCreative()) {
            heldStack.shrink(1);
        }
        return true;
    }


    public boolean isPauseScreen() {
        return false;
    }

    @Nullable
    private EternalDataSnapshot getEternal() {
        ClientWorld clientWorld = (Minecraft.getInstance()).level;
        if (clientWorld == null) {
            return null;
        }
        CryoChamberTileEntity tile = ((CryochamberContainer) this.menu).getCryoChamber((World) clientWorld);
        if (tile == null) {
            return null;
        }
        return ClientEternalData.getSnapshot(tile.getEternalId());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\screen\CryochamberScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */