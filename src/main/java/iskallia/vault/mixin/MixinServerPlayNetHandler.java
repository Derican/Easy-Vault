package iskallia.vault.mixin;

import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CPlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ServerPlayNetHandler.class})
public class MixinServerPlayNetHandler {
    private boolean doesOwnerCheck = false;

    @Inject(method = {"processPlayer"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/network/play/ServerPlayNetHandler;isSingleplayerOwner()Z", shift = At.Shift.BEFORE)})
    public void onSpeedCheck(CPlayerPacket packetIn, CallbackInfo ci) {
        this.doesOwnerCheck = true;
    }

    @Inject(method = {"isSingleplayerOwner"}, at = {@At("HEAD")}, cancellable = true)
    public void isOwnerCheck(CallbackInfoReturnable<Boolean> cir) {
        if (this.doesOwnerCheck) {
            cir.setReturnValue(Boolean.valueOf(true));
        }
        this.doesOwnerCheck = false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinServerPlayNetHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */