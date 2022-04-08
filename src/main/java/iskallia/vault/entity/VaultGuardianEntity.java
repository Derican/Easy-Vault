package iskallia.vault.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;


public class VaultGuardianEntity
        extends PiglinBruteEntity {
    public VaultGuardianEntity(EntityType<? extends PiglinBruteEntity> type, World world) {
        super(type, world);
        setCanPickUpLoot(false);
        ModifiableAttributeInstance attribute = getAttribute(Attributes.ATTACK_KNOCKBACK);
        if (attribute != null) attribute.setBaseValue(6.0D);

    }


    protected void dropFromLootTable(DamageSource source, boolean attackedRecently) {
    }

    public boolean hurt(DamageSource source, float amount) {
        if (!(source instanceof iskallia.vault.skill.ability.effect.sub.RampageDotAbility.PlayerDamageOverTimeSource) &&
                !(source.getEntity() instanceof net.minecraft.entity.player.PlayerEntity) &&
                !(source.getEntity() instanceof EternalEntity) && source != DamageSource.OUT_OF_WORLD) {
            return false;
        }

        if (isInvulnerableTo(source) || source == DamageSource.FALL) {
            return false;
        }

        playHurtSound(source);

        return super.hurt(source, amount);
    }


    public boolean isInvulnerableTo(DamageSource source) {
        return (super.isInvulnerableTo(source) || source.isProjectile());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        setImmuneToZombification(true);
        this.timeInOverworld = compound.getInt("TimeInOverworld");
    }


    public void knockback(float strength, double ratioX, double ratioZ) {
    }


    protected float getBlockSpeedFactor() {
        return 0.75F;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\VaultGuardianEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */