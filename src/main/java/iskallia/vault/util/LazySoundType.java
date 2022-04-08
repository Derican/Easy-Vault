package iskallia.vault.util;

import net.minecraft.block.SoundType;
import net.minecraft.util.SoundEvent;

public class LazySoundType
        extends SoundType {
    private boolean initialized;
    protected float lazyVolume;
    protected float lazyPitch;
    protected SoundEvent lazyBreakSound;
    protected SoundEvent lazyStepSound;
    protected SoundEvent lazyPlaceSound;
    protected SoundEvent lazyHitSound;
    protected SoundEvent lazyFallSound;

    public LazySoundType(SoundType vanillaType) {
        super(1.0F, 1.0F, vanillaType.getBreakSound(), vanillaType
                .getStepSound(), vanillaType
                .getPlaceSound(), vanillaType
                .getHitSound(), vanillaType
                .getFallSound());
    }

    public LazySoundType() {
        this(SoundType.STONE);
    }

    public void initialize(float volumeIn, float pitchIn, SoundEvent breakSoundIn, SoundEvent stepSoundIn, SoundEvent placeSoundIn, SoundEvent hitSoundIn, SoundEvent fallSoundIn) {
        if (this.initialized) throw new InternalError("LazySoundTypes should be initialized only once!");
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\LazySoundType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */