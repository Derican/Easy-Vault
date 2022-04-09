// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type.archetype;

import iskallia.vault.Vault;
import iskallia.vault.skill.talent.type.PlayerTalent;
import net.minecraft.world.World;

public abstract class ArchetypeTalent extends PlayerTalent
{
    public ArchetypeTalent(final int cost) {
        super(cost);
    }
    
    public ArchetypeTalent(final int cost, final int levelRequirement) {
        super(cost, levelRequirement);
    }
    
    public static boolean isEnabled(final World world) {
        return world.dimension() == Vault.VAULT_KEY;
    }
}
