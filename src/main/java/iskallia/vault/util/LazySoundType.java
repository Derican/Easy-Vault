// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.block.SoundType;
import net.minecraft.util.SoundEvent;

public class LazySoundType extends SoundType
{
    private boolean initialized;
    protected float lazyVolume;
    protected float lazyPitch;
    protected SoundEvent lazyBreakSound;
    protected SoundEvent lazyStepSound;
    protected SoundEvent lazyPlaceSound;
    protected SoundEvent lazyHitSound;
    protected SoundEvent lazyFallSound;
    
    public LazySoundType(final SoundType vanillaType) {
        super(1.0f, 1.0f, vanillaType.getBreakSound(), vanillaType.getStepSound(), vanillaType.getPlaceSound(), vanillaType.getHitSound(), vanillaType.getFallSound());
    }
    
    public LazySoundType() {
        this(SoundType.STONE);
    }
    
    public void initialize(final float volumeIn, final float pitchIn, final SoundEvent breakSoundIn, final SoundEvent stepSoundIn, final SoundEvent placeSoundIn, final SoundEvent hitSoundIn, final SoundEvent fallSoundIn) {
        if (this.initialized) {
            throw new InternalError("LazySoundTypes should be initialized only once!");
        }
        this.lazyVolume = volumeIn;
        this.lazyPitch = pitchIn;
        this.lazyBreakSound = breakSoundIn;
        this.lazyStepSound = stepSoundIn;
        this.lazyPlaceSound = placeSoundIn;
        this.lazyHitSound = hitSoundIn;
        this.lazyFallSound = fallSoundIn;
        this.initialized = true;
    }
    
    public float getVolume() {
        return this.lazyVolume;
    }
    
    public float getPitch() {
        return this.lazyPitch;
    }
    
    public SoundEvent getBreakSound() {
        return (this.lazyBreakSound == null) ? super.getBreakSound() : this.lazyBreakSound;
    }
    
    public SoundEvent getStepSound() {
        return (this.lazyStepSound == null) ? super.getStepSound() : this.lazyStepSound;
    }
    
    public SoundEvent getPlaceSound() {
        return (this.lazyPlaceSound == null) ? super.getPlaceSound() : this.lazyPlaceSound;
    }
    
    public SoundEvent getHitSound() {
        return (this.lazyHitSound == null) ? super.getHitSound() : this.lazyHitSound;
    }
    
    public SoundEvent getFallSound() {
        return (this.lazyFallSound == null) ? super.getFallSound() : this.lazyFallSound;
    }
}
