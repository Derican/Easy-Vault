package iskallia.vault.skill.set;

import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.registry.Registry;

public class EffectSet
        extends TalentSet<EffectTalent> {
    public EffectSet(VaultGear.Set set, Effect effect, int amplifier, EffectTalent.Type type, EffectTalent.Operator operator) {
        this(set, Registry.MOB_EFFECT.getKey(effect).toString(), amplifier, type.toString(), operator.toString());
    }

    public EffectSet(VaultGear.Set set, String effect, int amplifier, String type, String operator) {
        super(set, new EffectTalent(-1, effect, amplifier, type, operator));
    }

    public EffectSet(VaultGear.Set set, EffectTalent child) {
        super(set, child);
    }


    public void onAdded(PlayerEntity player) {
        getChild().onAdded(player);
    }


    public void onTick(PlayerEntity player) {
        getChild().tick(player);
    }


    public void onRemoved(PlayerEntity player) {
        getChild().onRemoved(player);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\EffectSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */