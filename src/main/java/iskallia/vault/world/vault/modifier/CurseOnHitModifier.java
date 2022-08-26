package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.registries.ForgeRegistries;

public class CurseOnHitModifier extends TexturedVaultModifier {
    @Expose
    protected String effectName;
    @Expose
    protected int effectAmplifier;
    @Expose
    protected int effectDuration;
    @Expose
    protected double onHitApplyChance;

    public CurseOnHitModifier(final String name, final ResourceLocation icon, final Effect effect) {
        this(name, icon, effect.getRegistryName().toString(), 0, 100, 1.0);
    }

    public CurseOnHitModifier(final String name, final ResourceLocation icon, final String effectName, final int effectAmplifier, final int effectDuration, final double onHitApplyChance) {
        super(name, icon);
        this.effectName = effectName;
        this.effectAmplifier = effectAmplifier;
        this.effectDuration = effectDuration;
        this.onHitApplyChance = onHitApplyChance;
    }

    public void applyCurse(final ServerPlayerEntity player) {
        if (CurseOnHitModifier.rand.nextFloat() > this.onHitApplyChance) {
            return;
        }
        Effect effect;
        try {
            effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(this.effectName));
        } catch (final ResourceLocationException exc) {
            Vault.LOGGER.error("Invalid resource location: " + this.effectName, exc);
            return;
        }
        if (effect == null || EffectTalent.getImmunities(player).contains(effect)) {
            return;
        }
        final EffectTalent.CombinedEffects effects = EffectTalent.getEffectData(player, player.getLevel(), effect);
        final int amplifier = effects.getAmplifier() + this.effectAmplifier + 1;
        player.addEffect(new EffectInstance(effect, this.effectDuration, amplifier, true, false));
    }
}
