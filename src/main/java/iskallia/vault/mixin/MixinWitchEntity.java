package iskallia.vault.mixin;

import iskallia.vault.easteregg.Witchskall;
import iskallia.vault.init.ModSounds;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin({WitchEntity.class})
public abstract class MixinWitchEntity {
    @Inject(method = {"registerData"}, at = {@At("TAIL")})
    protected void registerData(CallbackInfo ci) {
        WitchEntity thiz = (WitchEntity) this;

        if (Witchskall.WITCHSKALL_TICKS == null)
            Witchskall.WITCHSKALL_TICKS = EntityDataManager.defineId(WitchEntity.class, DataSerializers.INT);
        thiz.getEntityData().define(Witchskall.WITCHSKALL_TICKS, Integer.valueOf(-1));

        if (Witchskall.IS_WITCHSKALL == null)
            Witchskall.IS_WITCHSKALL = EntityDataManager.defineId(WitchEntity.class, DataSerializers.BOOLEAN);
        thiz.getEntityData().define(Witchskall.IS_WITCHSKALL, Boolean.valueOf(false));
    }


    @Inject(method = {"getAmbientSound"}, at = {@At("HEAD")}, cancellable = true)
    protected void getAmbientSound(CallbackInfoReturnable<SoundEvent> ci) {
        WitchEntity thiz = (WitchEntity) this;
        if (Witchskall.isWitchskall(thiz))
            ci.setReturnValue(ModSounds.WITCHSKALL_IDLE);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinWitchEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */