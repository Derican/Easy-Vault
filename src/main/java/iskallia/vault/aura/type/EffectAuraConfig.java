package iskallia.vault.aura.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.aura.ActiveAura;
import iskallia.vault.aura.EntityAuraProvider;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class EffectAuraConfig extends EternalAuraConfig.AuraConfig {
    @Expose
    private final EffectTalent effect;

    public EffectAuraConfig(Effect effect, String name, String icon) {
        this(new EffectTalent(0, effect, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), name, icon);
    }

    public EffectAuraConfig(EffectTalent effect, String name, String icon) {
        super(name, name, "Grants an aura of " + name, icon, 5.0F);
        this.effect = effect;
    }

    public EffectTalent getEffect() {
        return this.effect;
    }


    public void onTick(World world, ActiveAura aura) {
        super.onTick(world, aura);

        if (!(aura.getAuraProvider() instanceof EntityAuraProvider)) {
            return;
        }
        EffectInstance effect = getEffect().makeEffect(259);

        LivingEntity auraTarget = ((EntityAuraProvider) aura.getAuraProvider()).getSource();
        if (!auraTarget.hasEffect(effect.getEffect()) || auraTarget.getEffect(effect.getEffect()).getDuration() < 40)
            auraTarget.addEffect(effect);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\type\EffectAuraConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */