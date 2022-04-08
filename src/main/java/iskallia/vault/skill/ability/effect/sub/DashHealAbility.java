package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.DashConfig;
import iskallia.vault.skill.ability.config.sub.DashHealConfig;
import iskallia.vault.skill.ability.effect.DashAbility;
import net.minecraft.entity.player.ServerPlayerEntity;

public class DashHealAbility
        extends DashAbility<DashHealConfig> {
    public boolean onAction(DashHealConfig config, ServerPlayerEntity player, boolean active) {
        if (super.onAction((DashConfig) config, player, active)) {

            player.heal(config.getHealPerDash());
            return true;
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\DashHealAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */