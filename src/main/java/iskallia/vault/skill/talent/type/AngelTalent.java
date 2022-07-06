package iskallia.vault.skill.talent.type;

import iskallia.vault.Vault;
import net.minecraft.entity.player.PlayerEntity;

public class AngelTalent extends PlayerTalent {
    public AngelTalent(final int cost) {
        super(cost);
    }

    @Override
    public void tick(final PlayerEntity player) {
        if (player.level.dimension() == Vault.VAULT_KEY && !player.isSpectator() && !player.isCreative()) {
            player.abilities.mayfly = false;
            player.abilities.flying = false;
        } else if (!player.abilities.mayfly) {
            player.abilities.mayfly = true;
        }
        player.onUpdateAbilities();
    }

    @Override
    public void onRemoved(final PlayerEntity player) {
        if (!player.isSpectator()) {
            player.abilities.mayfly = false;
            player.abilities.flying = false;
            player.onUpdateAbilities();
        }
    }
}
