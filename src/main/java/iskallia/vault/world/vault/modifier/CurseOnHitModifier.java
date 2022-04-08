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

public class CurseOnHitModifier
        extends TexturedVaultModifier {
    @Expose
    protected String effectName;
    @Expose
    protected int effectAmplifier;

    public CurseOnHitModifier(String name, ResourceLocation icon, Effect effect) {
        this(name, icon, effect.getRegistryName().toString(), 0, 100, 1.0D);
    }

    @Expose
    protected int effectDuration;
    @Expose
    protected double onHitApplyChance;

    public CurseOnHitModifier(String name, ResourceLocation icon, String effectName, int effectAmplifier, int effectDuration, double onHitApplyChance) {
        super(name, icon);
        this.effectName = effectName;
        this.effectAmplifier = effectAmplifier;
        this.effectDuration = effectDuration;
        this.onHitApplyChance = onHitApplyChance;
    }

    public void applyCurse(ServerPlayerEntity player) {
        Effect effect;
        if (rand.nextFloat() > this.onHitApplyChance) {
            return;
        }

        try {
            effect = (Effect) ForgeRegistries.POTIONS.getValue(new ResourceLocation(this.effectName));
        } catch (ResourceLocationException exc) {
            Vault.LOGGER.error("Invalid resource location: " + this.effectName, (Throwable) exc);
            return;
        }
        if (effect == null || EffectTalent.getImmunities((LivingEntity) player).contains(effect)) {
            return;
        }
        EffectTalent.CombinedEffects effects = EffectTalent.getEffectData((PlayerEntity) player, player.getLevel(), effect);
        int amplifier = effects.getAmplifier() + this.effectAmplifier + 1;
        player.addEffect(new EffectInstance(effect, this.effectDuration, amplifier, true, false));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\CurseOnHitModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */