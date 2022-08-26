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
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import iskallia.vault.entity.eternal.EternalHelper;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.EternalInteractionMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
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
    private static final DecimalFormat ATTRIBUTE_FORMAT;
    private static final DecimalFormat ATTRIBUTE_MS_FORMAT;
    private static final DecimalFormat PERCENT_FORMAT;
    private static final ResourceLocation TEXTURE;
    private EternalDataSnapshot prevSnapshot;
    private EternalEntity eternalSkinCache;

    public CryochamberScreen(final CryochamberContainer screenContainer, final PlayerInventory inv, final ITextComponent title) {
        super(screenContainer, inv, title);
        this.prevSnapshot = null;
        this.eternalSkinCache = null;
        this.imageWidth = 176;
        this.imageHeight = 211;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    protected void init() {
        super.init();
        this.refreshButtons();
    }

    private void refreshButtons() {
        this.buttons.clear();
        this.children.clear();
        final EternalDataSnapshot snapshot = this.getEternal();
        if (snapshot == null) {
            return;
        }
        if (snapshot.getUsedLevels() < snapshot.getLevel()) {
            final int offsetX = this.leftPos + 78;
            final int yOffset = 0;
            final int yShift = 16;
            this.addButton((Widget) new ImageButton(offsetX, this.topPos + 18, 16, 16, 176, yOffset, yShift, CryochamberScreen.TEXTURE, 256, 256, btn -> {
                if (snapshot.getUsedLevels() >= snapshot.getLevel()) {
                    return;
                }
                ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.levelUp("health"));
            }, (btn, matrixStack, mouseX, mouseY) -> this.renderAttributeHoverTooltip(ModConfigs.ETERNAL_ATTRIBUTES.getHealthRollRange(), 1.0f, CryochamberScreen.ATTRIBUTE_FORMAT, matrixStack, mouseX, mouseY), StringTextComponent.EMPTY));
            this.addButton((Widget) new ImageButton(offsetX, this.topPos + 36, 16, 16, 176, yOffset, yShift, CryochamberScreen.TEXTURE, 256, 256, btn -> {
                if (snapshot.getUsedLevels() >= snapshot.getLevel()) {
                    return;
                }
                ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.levelUp("damage"));
            }, (btn, matrixStack, mouseX, mouseY) -> this.renderAttributeHoverTooltip(ModConfigs.ETERNAL_ATTRIBUTES.getDamageRollRange(), 1.0f, CryochamberScreen.ATTRIBUTE_FORMAT, matrixStack, mouseX, mouseY), StringTextComponent.EMPTY));
            this.addButton((Widget) new ImageButton(offsetX, this.topPos + 54, 16, 16, 176, yOffset, yShift, CryochamberScreen.TEXTURE, 256, 256, btn -> {
                if (snapshot.getUsedLevels() >= snapshot.getLevel()) {
                    return;
                }
                ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.levelUp("movespeed"));
            }, (btn, matrixStack, mouseX, mouseY) -> this.renderAttributeHoverTooltip(ModConfigs.ETERNAL_ATTRIBUTES.getMoveSpeedRollRange(), 10.0f, CryochamberScreen.ATTRIBUTE_MS_FORMAT, matrixStack, mouseX, mouseY), StringTextComponent.EMPTY));
        }
        if (snapshot.getAbilityName() == null) {
            final List<EternalAuraConfig.AuraConfig> options = ModConfigs.ETERNAL_AURAS.getRandom(snapshot.getSeededRand(), 3);
            int abilityX = this.leftPos + 8;
            final int abilityY = this.topPos + 90;
            for (final EternalAuraConfig.AuraConfig abilityOption : options) {
                this.addButton((Widget) new TooltipImageButton(abilityX, abilityY, 24, 24, 192, 0, 24, CryochamberScreen.TEXTURE, 256, 256, btn -> ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.selectEffect(abilityOption.getName()))));
                abilityX += 30;
            }
        }
    }

    private void renderAttributeHoverTooltip(final FloatRangeEntry range, final float multiplier, final DecimalFormat format, final MatrixStack matrixStack, final int mouseX, final int mouseY) {
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.0, 300.0);
        final String min = format.format(range.getMin() * multiplier);
        final String max = format.format(range.getMax() * multiplier);
        final StringTextComponent txt = new StringTextComponent("Adds +" + min + " to +" + max);
        this.renderToolTip(matrixStack, (List) Lists.newArrayList((Object[]) new IReorderingProcessor[]{txt.getVisualOrderText()}), mouseX, mouseY, this.font);
        matrixStack.popPose();
    }

    public void tick() {
        super.tick();
        final EternalDataSnapshot snapshot = this.getEternal();
        if (snapshot == null) {
            return;
        }
        if (this.prevSnapshot == null || !this.prevSnapshot.areStatisticsEqual(snapshot)) {
            this.prevSnapshot = snapshot;
            this.refreshButtons();
        }
    }

    protected void renderBg(final MatrixStack matrixStack, final float partialTicks, final int x, final int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(CryochamberScreen.TEXTURE);
        final int offsetX = (this.width - this.imageWidth) / 2;
        final int offsetY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, offsetX, offsetY, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderLabels(final MatrixStack matrixStack, final int mouseX, final int mouseY) {
        final EternalDataSnapshot snapshot = this.getEternal();
        if (snapshot == null) {
            return;
        }
        if (this.eternalSkinCache == null) {
            this.eternalSkinCache = EternalHelper.spawnEternal(Minecraft.getInstance().level, snapshot);
            this.eternalSkinCache.skin.updateSkin(snapshot.getName());
            Arrays.stream(EquipmentSlotType.values()).forEach(slot -> this.eternalSkinCache.setItemSlot(slot, ItemStack.EMPTY));
        }
        if (snapshot.isAncient()) {
            FontHelper.drawStringWithBorder(matrixStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 15910161, 4210752);
        } else {
            this.font.draw(matrixStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
        }
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
        this.renderEternal(snapshot, matrixStack, mouseX, mouseY);
        RenderSystem.enableDepthTest();
        this.renderLevel(snapshot, matrixStack);
        this.renderAttributeDisplay(snapshot, matrixStack);
        this.renderAbility(snapshot, matrixStack, mouseX, mouseY);
    }

    private void renderAbility(final EternalDataSnapshot snapshot, final MatrixStack matrixStack, final int mouseX, final int mouseY) {
        if (snapshot.getAbilityName() == null) {
            final List<EternalAuraConfig.AuraConfig> options = ModConfigs.ETERNAL_AURAS.getRandom(snapshot.getSeededRand(), 3);
            int abilityX = 12;
            int abilityY = 94;
            for (final EternalAuraConfig.AuraConfig abilityOption : options) {
                this.minecraft.getTextureManager().bind(new ResourceLocation(abilityOption.getIconPath()));
                blit(matrixStack, abilityX, abilityY, 16.0f, 16.0f, 16, 16, 16, 16);
                abilityX += 30;
            }
            abilityX = 8;
            abilityY = 90;
            for (final EternalAuraConfig.AuraConfig abilityOption : options) {
                final Rectangle box = new Rectangle(abilityX, abilityY, 24, 24);
                if (box.contains(mouseX - this.leftPos, mouseY - this.topPos)) {
                    this.renderComponentTooltip(matrixStack, abilityOption.getTooltip(), mouseX - this.leftPos, mouseY - this.topPos);
                }
                abilityX += 30;
            }
        } else {
            final EternalAuraConfig.AuraConfig cfg = ModConfigs.ETERNAL_AURAS.getByName(snapshot.getAbilityName());
            if (cfg == null) {
                return;
            }
            this.minecraft.getTextureManager().bind(new ResourceLocation(cfg.getIconPath()));
            blit(matrixStack, 8, 92, 0.0f, 0.0f, 16, 16, 16, 16);
            matrixStack.pushPose();
            matrixStack.translate(26.0, 92.0, 0.0);
            matrixStack.scale(0.8f, 0.8f, 0.8f);
            UIHelper.renderWrappedText(matrixStack, new StringTextComponent(cfg.getDescription()), 82, 0, 4210752);
            matrixStack.popPose();
        }
    }

    private void renderAttributeDisplay(final EternalDataSnapshot snapshot, final MatrixStack matrixStack) {
        final String healthStr = CryochamberScreen.ATTRIBUTE_FORMAT.format(snapshot.getEntityAttributes().get(Attributes.MAX_HEALTH));
        this.renderAttributeStats(matrixStack, "Health:", healthStr, 18, 32);
        final String damageStr = CryochamberScreen.ATTRIBUTE_FORMAT.format(snapshot.getEntityAttributes().get(Attributes.ATTACK_DAMAGE));
        this.renderAttributeStats(matrixStack, "Damage:", damageStr, 36, 48);
        final String speedStr = CryochamberScreen.ATTRIBUTE_MS_FORMAT.format(snapshot.getEntityAttributes().get(Attributes.MOVEMENT_SPEED) * 10.0f);
        this.renderAttributeStats(matrixStack, "Speed:", speedStr, 54, 64);
        final int availableLevels = Math.max(snapshot.getLevel() - snapshot.getUsedLevels(), 0);
        if (availableLevels > 0) {
            final String display = String.valueOf(availableLevels);
            final int offsetX = this.font.width(display) / 2;
            matrixStack.pushPose();
            matrixStack.translate(86.0, 13.0, 0.0);
            matrixStack.scale(0.8f, 0.8f, 0.8f);
            matrixStack.translate(-offsetX, 0.0, 0.0);
            this.font.draw(matrixStack, display, 0.0f, 0.0f, 4210752);
            matrixStack.popPose();
        }
        final String parryPercent = CryochamberScreen.PERCENT_FORMAT.format(snapshot.getParry() * 100.0f) + "%";
        final String resistPercent = CryochamberScreen.PERCENT_FORMAT.format(snapshot.getResistance() * 100.0f) + "%";
        final String armorAmount = CryochamberScreen.PERCENT_FORMAT.format(snapshot.getArmor());
        this.minecraft.getTextureManager().bind(CryochamberScreen.TEXTURE);
        this.blit(matrixStack, 8, 72, 216, 0, 16, 16);
        matrixStack.pushPose();
        matrixStack.translate(24.0, 72.0, 0.0);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        this.font.draw(matrixStack, parryPercent, 0.0f, 5.0f, 4210752);
        matrixStack.popPose();
        this.minecraft.getTextureManager().bind(CryochamberScreen.TEXTURE);
        this.blit(matrixStack, 39, 71, 216, 16, 16, 16);
        matrixStack.pushPose();
        matrixStack.translate(55.0, 72.0, 0.0);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        this.font.draw(matrixStack, resistPercent, 0.0f, 5.0f, 4210752);
        matrixStack.popPose();
        this.minecraft.getTextureManager().bind(CryochamberScreen.TEXTURE);
        this.blit(matrixStack, 70, 72, 216, 80, 16, 16);
        matrixStack.pushPose();
        matrixStack.translate(86.0, 72.0, 0.0);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        this.font.draw(matrixStack, armorAmount, 0.0f, 5.0f, 4210752);
        matrixStack.popPose();
    }

    private void renderAttributeStats(final MatrixStack matrixStack, final String description, final String valueStr, final int offsetY, final int vOffset) {
        this.minecraft.getTextureManager().bind(CryochamberScreen.TEXTURE);
        this.blit(matrixStack, 8, offsetY, 216, vOffset, 16, 16);
        matrixStack.pushPose();
        matrixStack.translate(26.0, offsetY + 6, 0.0);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        this.font.draw(matrixStack, description, 0.0f, 0.0f, 4210752);
        matrixStack.popPose();
        final float xShift = this.font.width(valueStr) * 0.8f;
        matrixStack.pushPose();
        matrixStack.translate(73.0, offsetY + 6, 0.0);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        matrixStack.translate(-xShift, 0.0, 0.0);
        this.font.draw(matrixStack, valueStr, 0.0f, 0.0f, 4210752);
        matrixStack.popPose();
    }

    private void renderLevel(final EternalDataSnapshot snapshot, final MatrixStack matrixStack) {
        this.minecraft.getTextureManager().bind(CryochamberScreen.TEXTURE);
        final int levelPart = MathHelper.floor(snapshot.getLevelPercent() * 62.0f);
        this.blit(matrixStack, 103, 17, 0, 212, 62, 5);
        this.blit(matrixStack, 103, 17, 0, 218, levelPart, 5);
        final String lvlStr = snapshot.getLevel() + " / " + snapshot.getMaxLevel();
        final float x = 136.0f - this.font.width(lvlStr) / 2.0f;
        final int y = 12;
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0.0);
        matrixStack.scale(0.8f, 0.8f, 1.0f);
        FontHelper.drawStringWithBorder(matrixStack, lvlStr, 0.0f, 0.0f, -6601, -12698050);
        matrixStack.popPose();
    }

    private void renderEternal(final EternalDataSnapshot snapshot, final MatrixStack matrixStack, final int mouseX, final int mouseY) {
        final int offsetX = 125;
        final int offsetY = 105;
        if (!snapshot.isAlive()) {
            ShaderUtil.useShader(ShaderUtil.GRAYSCALE_SHADER, () -> {
                final int grayScaleFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "grayFactor");
                ARBShaderObjects.glUniform1fARB(grayScaleFactor, 0.0f);
                final int brightnessFactor = ShaderUtil.getUniformLocation(ShaderUtil.GRAYSCALE_SHADER, "brightness");
                ARBShaderObjects.glUniform1fARB(brightnessFactor, 1.0f);
                return;
            });
        }
        int lookX = mouseX - this.leftPos - offsetX;
        int lookY = mouseY - this.topPos - offsetY;
        if (!snapshot.isAlive()) {
            lookX = 0;
            lookY = -30;
        }
        matrixStack.pushPose();
        matrixStack.translate(offsetX, offsetY, -400.0);
        if (!snapshot.isAncient()) {
            matrixStack.scale(1.2f, 1.2f, 1.2f);
        }
        UIHelper.drawFacingEntity(this.eternalSkinCache, matrixStack, lookX, lookY);
        matrixStack.popPose();
        if (!snapshot.isAlive()) {
            ShaderUtil.releaseShader();
        }
        final ItemStack heldStack = this.inventory.getCarried();
        if (!heldStack.isEmpty() && EternalInteractionMessage.canBeFed(snapshot, heldStack)) {
            final Rectangle feedRct = new Rectangle(99, 25, 51, 90);
            if (feedRct.contains(mouseX - this.leftPos, mouseY - this.topPos)) {
                this.renderTooltip(matrixStack, new StringTextComponent("Give to " + this.title.getString()), mouseX - this.leftPos, mouseY - this.topPos);
            }
        }
        if (!snapshot.isAlive()) {
            final String deadTxt = "Unalived";
            matrixStack.pushPose();
            matrixStack.translate(0.0, 0.0, 600.0);
            final int width = this.font.width(deadTxt);
            FontHelper.drawStringWithBorder(matrixStack, deadTxt, 125.0f - width / 2.0f, 100.0f, 16724016, 0);
            matrixStack.popPose();
        }
        if (snapshot.isAncient()) {
            final String ancientTxt = "Ancient";
            matrixStack.pushPose();
            matrixStack.translate(0.0, 0.0, 600.0);
            final int width = this.font.width(ancientTxt);
            FontHelper.drawStringWithBorder(matrixStack, ancientTxt, 125.0f - width / 2.0f, 28.0f, 15910161, 0);
            matrixStack.popPose();
        }
    }

    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (button != 0) {
            return false;
        }
        final EternalDataSnapshot snapshot = this.getEternal();
        if (snapshot == null) {
            return false;
        }
        final ItemStack heldStack = this.inventory.getCarried();
        if (heldStack.isEmpty() || !EternalInteractionMessage.canBeFed(snapshot, heldStack)) {
            return false;
        }
        final Rectangle feedRct = new Rectangle(99, 25, 51, 90);
        if (!feedRct.contains(mouseX - this.leftPos, mouseY - this.topPos)) {
            return false;
        }
        ModNetwork.CHANNEL.sendToServer(EternalInteractionMessage.feedItem(heldStack));
        if (!Minecraft.getInstance().player.isCreative()) {
            heldStack.shrink(1);
        }
        return true;
    }

    public boolean isPauseScreen() {
        return false;
    }

    @Nullable
    private EternalDataSnapshot getEternal() {
        final World world = Minecraft.getInstance().level;
        if (world == null) {
            return null;
        }
        final CryoChamberTileEntity tile = this.menu.getCryoChamber(world);
        if (tile == null) {
            return null;
        }
        return ClientEternalData.getSnapshot(tile.getEternalId());
    }

    static {
        ATTRIBUTE_FORMAT = new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        ATTRIBUTE_MS_FORMAT = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
        PERCENT_FORMAT = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        TEXTURE = Vault.id("textures/gui/cryochamber_inventory.png");
    }
}
