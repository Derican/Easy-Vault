// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.VeinMinerFortuneConfig;
import iskallia.vault.skill.ability.effect.VeinMinerAbility;
import iskallia.vault.util.OverlevelEnchantHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class VeinMinerFortuneAbility extends VeinMinerAbility<VeinMinerFortuneConfig>
{
    @Override
    protected ItemStack getVeinMiningItem(final PlayerEntity player, final VeinMinerFortuneConfig config) {
        final ItemStack stack = super.getVeinMiningItem(player, config).copy();
        return OverlevelEnchantHelper.increaseFortuneBy(stack, config.getAdditionalFortuneLevel());
    }
}
