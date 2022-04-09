// 
// Decompiled by Procyon v0.6.0
// 

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

public class EffectAuraConfig extends EternalAuraConfig.AuraConfig
{
    @Expose
    private final EffectTalent effect;
    
    public EffectAuraConfig(final Effect effect, final String name, final String icon) {
        this(new EffectTalent(0, effect, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), name, icon);
    }
    
    public EffectAuraConfig(final EffectTalent effect, final String name, final String icon) {
        super(name, name, "Grants an aura of " + name, icon, 5.0f);
        this.effect = effect;
    }
    
    public EffectTalent getEffect() {
        return this.effect;
    }
    
    @Override
    public void onTick(final World world, final ActiveAura aura) {
        super.onTick(world, aura);
        if (!(aura.getAuraProvider() instanceof EntityAuraProvider)) {
            return;
        }
        final EffectInstance effect = this.getEffect().makeEffect(259);
        final LivingEntity auraTarget = ((EntityAuraProvider)aura.getAuraProvider()).getSource();
        if (!auraTarget.hasEffect(effect.getEffect()) || auraTarget.getEffect(effect.getEffect()).getDuration() < 40) {
            auraTarget.addEffect(effect);
        }
    }
}
