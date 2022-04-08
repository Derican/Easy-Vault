package iskallia.vault.skill.talent.type.archetype;

import iskallia.vault.Vault;
import iskallia.vault.skill.talent.type.PlayerTalent;
import net.minecraft.world.World;

public abstract class ArchetypeTalent
        extends PlayerTalent {
    public ArchetypeTalent(int cost) {
        super(cost);
    }

    public ArchetypeTalent(int cost, int levelRequirement) {
        super(cost, levelRequirement);
    }

    public static boolean isEnabled(World world) {
        return (world.dimension() == Vault.VAULT_KEY);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\archetype\ArchetypeTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */