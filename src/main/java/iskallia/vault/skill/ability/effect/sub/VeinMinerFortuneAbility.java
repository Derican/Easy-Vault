package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.VeinMinerConfig;
import iskallia.vault.skill.ability.config.sub.VeinMinerFortuneConfig;
import iskallia.vault.skill.ability.effect.VeinMinerAbility;
import iskallia.vault.util.OverlevelEnchantHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;


public class VeinMinerFortuneAbility
        extends VeinMinerAbility<VeinMinerFortuneConfig> {
    protected ItemStack getVeinMiningItem(PlayerEntity player, VeinMinerFortuneConfig config) {
        ItemStack stack = super.getVeinMiningItem(player, (VeinMinerConfig) config).copy();
        return OverlevelEnchantHelper.increaseFortuneBy(stack, config.getAdditionalFortuneLevel());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\VeinMinerFortuneAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */