package iskallia.vault.skill.talent.type;

import net.minecraft.entity.player.PlayerEntity;

public class AngelTalent
        extends PlayerTalent {
    public AngelTalent(int cost) {
        super(cost);
    }


    public void tick(PlayerEntity player) {
        if (!player.abilities.mayfly) {
            player.abilities.mayfly = true;
        }
        player.onUpdateAbilities();
    }


    public void onRemoved(PlayerEntity player) {
        player.abilities.mayfly = false;
        player.onUpdateAbilities();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\AngelTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */