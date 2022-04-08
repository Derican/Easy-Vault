package iskallia.vault.mixin;

import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({FoodStats.class})
public class MixinFoodStats {
    @Redirect(method = {"tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;shouldHeal()Z"))
    public boolean shouldHeal(PlayerEntity player) {
        if (!player.level.isClientSide) {
            VaultRaid vault = VaultRaidData.get((ServerWorld) player.level).getActiveFor(player.getUUID());
            if (vault != null) return false;

        }
        return player.isHurt();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinFoodStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */