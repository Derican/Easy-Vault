// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import net.minecraft.util.DamageSource;

public class ElvishTalent extends DamageCancellingTalent
{
    public ElvishTalent(final int cost) {
        super(cost);
    }
    
    @Override
    protected boolean shouldCancel(final DamageSource src) {
        return src == DamageSource.FALL;
    }
}
