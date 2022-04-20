package iskallia.vault.item.paxel.enhancement;

import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class EffectEnhancement extends PaxelEnhancement {
    public Map<UUID, EffectInstance> EFFECT_INSTANCE_CAPTURES;
    protected String effectName;
    protected int extraAmplifier;
    protected Effect effect;

    public EffectEnhancement(final Effect effect, final int extraAmplifier) {
        this.EFFECT_INSTANCE_CAPTURES = new HashMap<UUID, EffectInstance>();
        this.effectName = Objects.requireNonNull(Registry.MOB_EFFECT.getKey(effect)).toString();
        this.extraAmplifier = extraAmplifier;
    }

    @Override
    public Color getColor() {
        return Color.fromRgb(-10047745);
    }

    @Override
    public IFormattableTextComponent getDescription() {
        return (IFormattableTextComponent) new TranslationTextComponent("paxel_enhancement.the_vault.effects.desc", new Object[]{this.extraAmplifier, this.getEffect().getDisplayName().getString()});
    }

    public Effect getEffect() {
        if (this.effect == null) {
            this.effect = (Effect) Registry.MOB_EFFECT.get(new ResourceLocation(this.effectName));
        }
        return this.effect;
    }

    public int getExtraAmplifier() {
        return this.extraAmplifier;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putString("EffectName", this.effectName);
        nbt.putInt("ExtraAmplifier", this.extraAmplifier);
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.effectName = nbt.getString("EffectName");
        this.extraAmplifier = nbt.getInt("ExtraAmplifier");
    }

    public void captureEffect(final ServerPlayerEntity player, final EffectInstance instance) {
        final UUID playerUUID = player.getUUID();
        if (instance == null) {
            this.EFFECT_INSTANCE_CAPTURES.put(playerUUID, null);
        } else {
            final EffectInstance copiedInstance = new EffectInstance(instance);
            this.EFFECT_INSTANCE_CAPTURES.put(playerUUID, copiedInstance);
        }
    }

    public void revertCapturedEffect(final ServerPlayerEntity player) {
        final EffectInstance capturedInstance = this.EFFECT_INSTANCE_CAPTURES.remove(player.getUUID());
        if (capturedInstance != null) {
            player.addEffect(capturedInstance);
        }
    }

    @Override
    public void onEnhancementActivated(final ServerPlayerEntity player, final ItemStack paxelStack) {
    }

    @Override
    public void onEnhancementDeactivated(final ServerPlayerEntity player, final ItemStack paxelStack) {
    }

    @Override
    public void heldTick(final ServerPlayerEntity player, final ItemStack paxelStack, final int slotIndex) {
    }

    public EffectInstance createEnhancedEffect(final EffectInstance instance) {
        return this.createEnhancedEffect(instance.getAmplifier(), instance.isVisible(), instance.showIcon());
    }

    public EffectInstance createEnhancedEffect(final int baseAmplifier, final boolean doesShowParticles, final boolean doesShowIcon) {
        return new EffectInstance(this.getEffect(), 310, baseAmplifier + this.extraAmplifier, false, doesShowParticles, doesShowIcon);
    }

    public EffectTalent makeTalent() {
        return new EffectTalent(0, this.getEffect(), this.getExtraAmplifier(), EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD);
    }
}
