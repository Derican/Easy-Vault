package iskallia.vault.world.vault.logic.objective.architect.modifier;

import iskallia.vault.init.ModConfigs;

public class RandomVoteModifier
        extends VoteModifier {
    public RandomVoteModifier(String name, String description, int voteLockDurationChangeSeconds) {
        super(name, description, voteLockDurationChangeSeconds);
    }

    public VoteModifier rollModifier() {
        VoteModifier random = null;
        while (random == null || random instanceof RandomVoteModifier) {
            random = ModConfigs.ARCHITECT_EVENT.generateRandomModifier();
        }
        return random;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\modifier\RandomVoteModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */