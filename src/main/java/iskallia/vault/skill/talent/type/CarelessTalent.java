package iskallia.vault.skill.talent.type;

import net.minecraft.util.DamageSource;

public class CarelessTalent
        extends DamageCancellingTalent {
    public CarelessTalent(int cost) {
        super(cost);
    }


    protected boolean shouldCancel(DamageSource src) {
        return (src == DamageSource.FLY_INTO_WALL);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\CarelessTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */