package iskallia.vault.skill.talent.type;

import net.minecraft.util.DamageSource;

public class ElvishTalent
        extends DamageCancellingTalent {
    public ElvishTalent(int cost) {
        super(cost);
    }


    protected boolean shouldCancel(DamageSource src) {
        return (src == DamageSource.FALL);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\ElvishTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */