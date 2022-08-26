package iskallia.vault.entity;

import iskallia.vault.skill.ability.effect.sub.RampageDotAbility;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class VaultGuardianEntity extends PiglinBruteEntity {
    public VaultGuardianEntity(final EntityType<? extends PiglinBruteEntity> type, final World world) {
        super(type, world);
        this.setCanPickUpLoot(false);
        final ModifiableAttributeInstance attribute = this.getAttribute(Attributes.ATTACK_KNOCKBACK);
        if (attribute != null) {
            attribute.setBaseValue(6.0);
        }
    }

    protected void dropFromLootTable(final DamageSource source, final boolean attackedRecently) {
    }

    public boolean hurt(final DamageSource source, final float amount) {
        if (!(source instanceof RampageDotAbility.PlayerDamageOverTimeSource) && !(source.getEntity() instanceof PlayerEntity) && !(source.getEntity() instanceof EternalEntity) && source != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        if (this.isInvulnerableTo(source) || source == DamageSource.FALL) {
            return false;
        }
        this.playHurtSound(source);
        return super.hurt(source, amount);
    }

    public boolean isInvulnerableTo(final DamageSource source) {
        return super.isInvulnerableTo(source) || source.isProjectile();
    }

    public void readAdditionalSaveData(final CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setImmuneToZombification(true);
        this.timeInOverworld = compound.getInt("TimeInOverworld");
    }

    public void knockback(final float strength, final double ratioX, final double ratioZ) {
    }

    protected float getBlockSpeedFactor() {
        return 0.75f;
    }
}
