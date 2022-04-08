package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class FrenzyModifier
        extends TexturedVaultModifier {
    public static final AttributeModifier.Operation FRENZY_HEALTH_OPERATION = AttributeModifier.Operation.MULTIPLY_TOTAL;


    @Expose
    private final float damageMultiplier;

    private UUID healthModifierID = null;
    @Expose
    private final float additionalMovementSpeed;
    @Expose
    private final boolean doHealthReduction;
    private UUID damageModifierID = null;
    private UUID movementSpeedModifierID = null;

    public FrenzyModifier(String name, ResourceLocation icon, float damageMultiplier, float additionalMovementSpeed, boolean doHealthReduction) {
        super(name, icon);

        this.damageMultiplier = damageMultiplier;
        this.additionalMovementSpeed = additionalMovementSpeed;
        this.doHealthReduction = doHealthReduction;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    public UUID getDamageModifierID() {
        if (this.damageModifierID == null) {
            Random r = new Random(getName().hashCode());
            this.damageModifierID = new UUID(r.nextLong(), r.nextLong());
        }
        return this.damageModifierID;
    }

    public float getAdditionalMovementSpeed() {
        return this.additionalMovementSpeed;
    }

    public UUID getMovementSpeedModifierID() {
        if (this.movementSpeedModifierID == null) {
            Random r = new Random(getName().hashCode());
            for (int i = 0; i < 5; i++) {
                r.nextLong();
            }
            this.movementSpeedModifierID = new UUID(r.nextLong(), r.nextLong());
        }
        return this.movementSpeedModifierID;
    }

    public static boolean isFrenzyHealthModifier(UUID uuid) {
        for (FrenzyModifier modifier : ModConfigs.VAULT_MODIFIERS.FRENZY_MODIFIERS) {
            if (modifier.doHealthReduction && uuid.equals(modifier.getHealthModifierID())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public UUID getHealthModifierID() {
        if (this.doHealthReduction) {
            if (this.healthModifierID == null) {
                Random r = new Random(getName().hashCode());
                for (int i = 0; i < 10; i++) {
                    r.nextLong();
                }
                this.healthModifierID = new UUID(r.nextLong(), r.nextLong());
            }
            return this.healthModifierID;
        }
        return null;
    }

    public void applyToEntity(LivingEntity entity) {
        applyModifier(entity, Attributes.ATTACK_DAMAGE, new AttributeModifier(getDamageModifierID(), "Frenzy Damage Multiplier",
                getDamageMultiplier(), AttributeModifier.Operation.MULTIPLY_BASE));
        applyModifier(entity, Attributes.MOVEMENT_SPEED, new AttributeModifier(getMovementSpeedModifierID(), "Frenzy MovementSpeed Addition",
                getAdditionalMovementSpeed(), AttributeModifier.Operation.ADDITION));
        if (this.doHealthReduction) {
            applyModifier(entity, Attributes.MAX_HEALTH, new AttributeModifier(getHealthModifierID(), "Frenzy MaxHealth 1", 1.0D, FRENZY_HEALTH_OPERATION));

            entity.setHealth(1.0F);
        }
    }

    private void applyModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
        ModifiableAttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance != null)
            attributeInstance.addPermanentModifier(modifier);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\FrenzyModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */