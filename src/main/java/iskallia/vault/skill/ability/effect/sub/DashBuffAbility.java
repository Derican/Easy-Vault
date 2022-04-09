// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.DashBuffConfig;
import iskallia.vault.skill.ability.effect.DashAbility;
import iskallia.vault.util.PlayerDamageHelper;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DashBuffAbility extends DashAbility<DashBuffConfig>
{
    private static final Set<UUID> dashingPlayers;
    
    @Override
    public boolean onAction(final DashBuffConfig config, final ServerPlayerEntity player, final boolean active) {
        final World world = player.getCommandSenderWorld();
        if (world instanceof ServerWorld && DashBuffAbility.dashingPlayers.add(player.getUUID()) && super.onAction(config, player, active)) {
            final float dmgIncrease = config.getDamageIncreasePerDash();
            final int tickTime = config.getDamageIncreaseTickTime();
            PlayerDamageHelper.applyMultiplier(player, dmgIncrease, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY, true, tickTime, sPlayer -> {
                DashBuffAbility.dashingPlayers.remove(player.getUUID());
                PlayerAbilitiesData.setAbilityOnCooldown(player, "Dash");
                return;
            });
        }
        return false;
    }
    
    static {
        dashingPlayers = new HashSet<UUID>();
    }
}
