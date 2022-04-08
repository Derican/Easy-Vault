package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.potion.Effects;

public class DreamSet
        extends EffectSet {
    public static int MULTIPLIER_ID = -3;

    @Expose
    private float increasedDamage;
    @Expose
    private float increasedResistance;

    public DreamSet(float increasedDamage, int hasteAddition, float increasedResistance, float increasedParry, float increasedChestRarity) {
        super(VaultGear.Set.DREAM, Effects.DIG_SPEED, hasteAddition, EffectTalent.Type.HIDDEN, EffectTalent.Operator.ADD);
        this.increasedDamage = increasedDamage;
        this.increasedResistance = increasedResistance;
        this.increasedParry = increasedParry;
        this.increasedChestRarity = increasedChestRarity;
    }

    @Expose
    private float increasedParry;
    @Expose
    private float increasedChestRarity;

    public float getIncreasedDamage() {
        return this.increasedDamage;
    }

    public float getIncreasedResistance() {
        return this.increasedResistance;
    }

    public float getIncreasedParry() {
        return this.increasedParry;
    }

    public float getIncreasedChestRarity() {
        return this.increasedChestRarity;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\DreamSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */