// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import net.minecraft.entity.player.PlayerEntity;

public class AngelTalent extends PlayerTalent
{
    public AngelTalent(final int cost) {
        super(cost);
    }
    
    @Override
    public void tick(final PlayerEntity player) {
        if (!player.abilities.mayfly) {
            player.abilities.mayfly = true;
        }
        player.onUpdateAbilities();
    }
    
    @Override
    public void onRemoved(final PlayerEntity player) {
        player.abilities.mayfly = false;
        player.onUpdateAbilities();
    }
}
