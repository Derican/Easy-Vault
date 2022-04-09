// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.architect.modifier;

import iskallia.vault.init.ModConfigs;

public class RandomVoteModifier extends VoteModifier
{
    public RandomVoteModifier(final String name, final String description, final int voteLockDurationChangeSeconds) {
        super(name, description, voteLockDurationChangeSeconds);
    }
    
    public VoteModifier rollModifier() {
        VoteModifier random;
        for (random = null; random == null || random instanceof RandomVoteModifier; random = ModConfigs.ARCHITECT_EVENT.generateRandomModifier()) {}
        return random;
    }
}
