// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ FoodStats.class })
public class MixinFoodStats
{
    @Redirect(method = { "tick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isHurt()Z"))
    public boolean shouldHeal(final PlayerEntity player) {
        if (!player.level.isClientSide) {
            final VaultRaid vault = VaultRaidData.get((ServerWorld)player.level).getActiveFor(player.getUUID());
            if (vault != null) {
                return false;
            }
        }
        return player.isHurt();
    }
}
