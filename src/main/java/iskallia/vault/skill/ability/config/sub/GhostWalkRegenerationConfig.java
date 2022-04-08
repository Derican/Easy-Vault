package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.GhostWalkConfig;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.potion.Effects;

public class GhostWalkRegenerationConfig
        extends GhostWalkConfig {
    @Expose
    private final int regenerationAmplifier;

    public GhostWalkRegenerationConfig(int cost, int level, int durationTicks, int regenAmplifier) {
        super(cost, level, durationTicks);
        this.regenerationAmplifier = regenAmplifier;
        this.regenerationType = EffectTalent.Type.ALL.toString();
    }

    @Expose
    private final String regenerationType;

    public int getRegenAmplifier() {
        return this.regenerationAmplifier;
    }

    public EffectTalent.Type getRegenerationType() {
        return EffectTalent.Type.fromString(this.regenerationType);
    }

    public EffectTalent makeRegenerationTalent() {
        return new EffectTalent(0, Effects.REGENERATION, getRegenAmplifier(), getRegenerationType(), EffectTalent.Operator.ADD);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\GhostWalkRegenerationConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */