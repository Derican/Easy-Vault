// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.world.GameRules;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.player.PlayerEntity;

@Mixin({ ServerPlayerEntity.class })
public abstract class MixinServerPlayerEntity extends PlayerEntity
{
    @Shadow
    public abstract ServerWorld getLevel();
    
    public MixinServerPlayerEntity(final World p_i241920_1_, final BlockPos p_i241920_2_, final float p_i241920_3_, final GameProfile p_i241920_4_) {
        super(p_i241920_1_, p_i241920_2_, p_i241920_3_, p_i241920_4_);
    }
    
    @Redirect(method = { "restoreFrom" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"))
    public boolean yes(final GameRules instance, final GameRules.RuleKey<GameRules.BooleanValue> key) {
        final VaultRaid vault = VaultRaidData.get(this.getLevel()).getActiveFor(this.getUUID());
        return vault != null || instance.getBoolean((GameRules.RuleKey)key);
    }
}
