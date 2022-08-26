package iskallia.vault.skill.set;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.potion.Effects;

public class DreamSet extends EffectSet {
    public static int MULTIPLIER_ID;
    @Expose
    private final float increasedDamage;
    @Expose
    private final float increasedResistance;
    @Expose
    private final float increasedParry;
    @Expose
    private final float increasedChestRarity;

    public DreamSet(final float increasedDamage, final int hasteAddition, final float increasedResistance, final float increasedParry, final float increasedChestRarity) {
        super(VaultGear.Set.DREAM, Effects.DIG_SPEED, hasteAddition, EffectTalent.Type.HIDDEN, EffectTalent.Operator.ADD);
        this.increasedDamage = increasedDamage;
        this.increasedResistance = increasedResistance;
        this.increasedParry = increasedParry;
        this.increasedChestRarity = increasedChestRarity;
    }

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

    static {
        DreamSet.MULTIPLIER_ID = -3;
    }
}
