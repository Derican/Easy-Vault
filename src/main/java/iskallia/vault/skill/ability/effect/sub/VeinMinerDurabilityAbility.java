package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.VeinMinerConfig;
import iskallia.vault.skill.ability.config.sub.VeinMinerDurabilityConfig;
import iskallia.vault.skill.ability.effect.VeinMinerAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class VeinMinerDurabilityAbility
        extends VeinMinerAbility<VeinMinerDurabilityConfig> {
    public void damageMiningItem(ItemStack heldItem, PlayerEntity player, VeinMinerDurabilityConfig config) {
        if (rand.nextFloat() >= config.getNoDurabilityUsageChance()) {
            return;
        }
        super.damageMiningItem(heldItem, player, (VeinMinerConfig) config);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\VeinMinerDurabilityAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */