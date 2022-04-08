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

public class EffectEnhancement
        extends PaxelEnhancement {
    public Map<UUID, EffectInstance> EFFECT_INSTANCE_CAPTURES = new HashMap<>();

    protected String effectName;
    protected int extraAmplifier;
    protected Effect effect;

    public EffectEnhancement(Effect effect, int extraAmplifier) {
        this.effectName = ((ResourceLocation) Objects.<ResourceLocation>requireNonNull(Registry.MOB_EFFECT.getKey(effect))).toString();
        this.extraAmplifier = extraAmplifier;
    }


    public Color getColor() {
        return Color.fromRgb(-10047745);
    }


    public IFormattableTextComponent getDescription() {
        return (IFormattableTextComponent) new TranslationTextComponent("paxel_enhancement.the_vault.effects.desc", new Object[]{
                Integer.valueOf(this.extraAmplifier),
                getEffect().getDisplayName().getString()});
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


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString("EffectName", this.effectName);
        nbt.putInt("ExtraAmplifier", this.extraAmplifier);
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.effectName = nbt.getString("EffectName");
        this.extraAmplifier = nbt.getInt("ExtraAmplifier");
    }

    public void captureEffect(ServerPlayerEntity player, EffectInstance instance) {
        UUID playerUUID = player.getUUID();

        if (instance == null) {
            this.EFFECT_INSTANCE_CAPTURES.put(playerUUID, null);
        } else {
            EffectInstance copiedInstance = new EffectInstance(instance);
            this.EFFECT_INSTANCE_CAPTURES.put(playerUUID, copiedInstance);
        }
    }

    public void revertCapturedEffect(ServerPlayerEntity player) {
        EffectInstance capturedInstance = this.EFFECT_INSTANCE_CAPTURES.remove(player.getUUID());
        if (capturedInstance != null) {
            player.addEffect(capturedInstance);
        }
    }


    public void onEnhancementActivated(ServerPlayerEntity player, ItemStack paxelStack) {
    }


    public void onEnhancementDeactivated(ServerPlayerEntity player, ItemStack paxelStack) {
    }


    public void heldTick(ServerPlayerEntity player, ItemStack paxelStack, int slotIndex) {
    }


    public EffectInstance createEnhancedEffect(EffectInstance instance) {
        return createEnhancedEffect(instance.getAmplifier(), instance.isVisible(), instance.showIcon());
    }

    public EffectInstance createEnhancedEffect(int baseAmplifier, boolean doesShowParticles, boolean doesShowIcon) {
        return new EffectInstance(getEffect(), 310, baseAmplifier + this.extraAmplifier, false, doesShowParticles, doesShowIcon);
    }


    public EffectTalent makeTalent() {
        return new EffectTalent(0, getEffect(), getExtraAmplifier(), EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\paxel\enhancement\EffectEnhancement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */