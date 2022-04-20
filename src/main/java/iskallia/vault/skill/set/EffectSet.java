package iskallia.vault.skill.set;

import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.registry.Registry;

public class EffectSet extends TalentSet<EffectTalent> {
    public EffectSet(final VaultGear.Set set, final Effect effect, final int amplifier, final EffectTalent.Type type, final EffectTalent.Operator operator) {
        this(set, Registry.MOB_EFFECT.getKey(effect).toString(), amplifier, type.toString(), operator.toString());
    }

    public EffectSet(final VaultGear.Set set, final String effect, final int amplifier, final String type, final String operator) {
        super(set, new EffectTalent(-1, effect, amplifier, type, operator));
    }

    public EffectSet(final VaultGear.Set set, final EffectTalent child) {
        super(set, child);
    }

    @Override
    public void onAdded(final PlayerEntity player) {
        this.getChild().onAdded(player);
    }

    @Override
    public void onTick(final PlayerEntity player) {
        this.getChild().tick(player);
    }

    @Override
    public void onRemoved(final PlayerEntity player) {
        this.getChild().onRemoved(player);
    }
}
