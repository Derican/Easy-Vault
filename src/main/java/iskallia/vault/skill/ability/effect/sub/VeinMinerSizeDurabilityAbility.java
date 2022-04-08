package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.VeinMinerConfig;
import iskallia.vault.skill.ability.config.sub.VeinMinerSizeDurabilityConfig;
import iskallia.vault.skill.ability.effect.VeinMinerAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class VeinMinerSizeDurabilityAbility
        extends VeinMinerAbility<VeinMinerSizeDurabilityConfig> {
    public void damageMiningItem(ItemStack heldItem, PlayerEntity player, VeinMinerSizeDurabilityConfig config) {
        super.damageMiningItem(heldItem, player, (VeinMinerConfig) config);
        if (rand.nextFloat() < config.getDoubleDurabilityCostChance())
            super.damageMiningItem(heldItem, player, (VeinMinerConfig) config);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\VeinMinerSizeDurabilityAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */