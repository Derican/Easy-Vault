// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import iskallia.vault.easteregg.Witchskall;
import iskallia.vault.init.ModSounds;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ WitchEntity.class })
public abstract class MixinWitchEntity
{
    @Inject(method = { "defineSynchedData" }, at = { @At("TAIL") })
    protected void registerData(final CallbackInfo ci) {
        final WitchEntity thiz = (WitchEntity)this;
        if (Witchskall.WITCHSKALL_TICKS == null) {
            Witchskall.WITCHSKALL_TICKS = (DataParameter<Integer>)EntityDataManager.defineId((Class)WitchEntity.class, DataSerializers.INT);
        }
        thiz.getEntityData().define((DataParameter)Witchskall.WITCHSKALL_TICKS, (-1));
        if (Witchskall.IS_WITCHSKALL == null) {
            Witchskall.IS_WITCHSKALL = (DataParameter<Boolean>)EntityDataManager.defineId((Class)WitchEntity.class, DataSerializers.BOOLEAN);
        }
        thiz.getEntityData().define((DataParameter)Witchskall.IS_WITCHSKALL, false);
    }
    
    @Inject(method = { "getAmbientSound" }, at = { @At("HEAD") }, cancellable = true)
    protected void getAmbientSound(final CallbackInfoReturnable<SoundEvent> ci) {
        final WitchEntity thiz = (WitchEntity)this;
        if (Witchskall.isWitchskall(thiz)) {
            ci.setReturnValue(ModSounds.WITCHSKALL_IDLE);
        }
    }
}
