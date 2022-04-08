package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.DashConfig;
import iskallia.vault.skill.ability.config.sub.DashBuffConfig;
import iskallia.vault.skill.ability.effect.DashAbility;
import iskallia.vault.util.PlayerDamageHelper;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class DashBuffAbility
        extends DashAbility<DashBuffConfig> {
    private static final Set<UUID> dashingPlayers = new HashSet<>();


    public boolean onAction(DashBuffConfig config, ServerPlayerEntity player, boolean active) {
        World world = player.getCommandSenderWorld();
        if (world instanceof net.minecraft.world.server.ServerWorld && dashingPlayers.add(player.getUUID()) &&
                super.onAction((DashConfig) config, player, active)) {
            float dmgIncrease = config.getDamageIncreasePerDash();
            int tickTime = config.getDamageIncreaseTickTime();
            PlayerDamageHelper.applyMultiplier(player, dmgIncrease, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY, true, tickTime, sPlayer -> {
                dashingPlayers.remove(player.getUUID());

                PlayerAbilitiesData.setAbilityOnCooldown(player, "Dash");
            });
        }

        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\DashBuffAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */