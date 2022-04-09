// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CPlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ServerPlayNetHandler.class })
public class MixinServerPlayNetHandler
{
    private boolean doesOwnerCheck;
    
    public MixinServerPlayNetHandler() {
        this.doesOwnerCheck = false;
    }
    
    @Inject(method = { "handleMovePlayer" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/network/play/ServerPlayNetHandler;isSingleplayerOwner()Z", shift = At.Shift.BEFORE) })
    public void onSpeedCheck(final CPlayerPacket packetIn, final CallbackInfo ci) {
        this.doesOwnerCheck = true;
    }
    
    @Inject(method = { "isSingleplayerOwner" }, at = { @At("HEAD") }, cancellable = true)
    public void isOwnerCheck(final CallbackInfoReturnable<Boolean> cir) {
        if (this.doesOwnerCheck) {
            cir.setReturnValue(true);
        }
        this.doesOwnerCheck = false;
    }
}
