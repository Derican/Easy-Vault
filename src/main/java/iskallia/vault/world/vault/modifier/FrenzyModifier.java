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

public class FrenzyModifier extends TexturedVaultModifier {
    public static final AttributeModifier.Operation FRENZY_HEALTH_OPERATION;
    @Expose
    private final float damageMultiplier;
    @Expose
    private final float additionalMovementSpeed;
    @Expose
    private final boolean doHealthReduction;
    private UUID healthModifierID;
    private UUID damageModifierID;
    private UUID movementSpeedModifierID;

    public FrenzyModifier(final String name, final ResourceLocation icon, final float damageMultiplier, final float additionalMovementSpeed, final boolean doHealthReduction) {
        super(name, icon);
        this.healthModifierID = null;
        this.damageModifierID = null;
        this.movementSpeedModifierID = null;
        this.damageMultiplier = damageMultiplier;
        this.additionalMovementSpeed = additionalMovementSpeed;
        this.doHealthReduction = doHealthReduction;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    public UUID getDamageModifierID() {
        if (this.damageModifierID == null) {
            final Random r = new Random(this.getName().hashCode());
            this.damageModifierID = new UUID(r.nextLong(), r.nextLong());
        }
        return this.damageModifierID;
    }

    public float getAdditionalMovementSpeed() {
        return this.additionalMovementSpeed;
    }

    public UUID getMovementSpeedModifierID() {
        if (this.movementSpeedModifierID == null) {
            final Random r = new Random(this.getName().hashCode());
            for (int i = 0; i < 5; ++i) {
                r.nextLong();
            }
            this.movementSpeedModifierID = new UUID(r.nextLong(), r.nextLong());
        }
        return this.movementSpeedModifierID;
    }

    public static boolean isFrenzyHealthModifier(final UUID uuid) {
        for (final FrenzyModifier modifier : ModConfigs.VAULT_MODIFIERS.FRENZY_MODIFIERS) {
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
                final Random r = new Random(this.getName().hashCode());
                for (int i = 0; i < 10; ++i) {
                    r.nextLong();
                }
                this.healthModifierID = new UUID(r.nextLong(), r.nextLong());
            }
            return this.healthModifierID;
        }
        return null;
    }

    public void applyToEntity(final LivingEntity entity) {
        this.applyModifier(entity, Attributes.ATTACK_DAMAGE, new AttributeModifier(this.getDamageModifierID(), "Frenzy Damage Multiplier", this.getDamageMultiplier(), AttributeModifier.Operation.MULTIPLY_BASE));
        this.applyModifier(entity, Attributes.MOVEMENT_SPEED, new AttributeModifier(this.getMovementSpeedModifierID(), "Frenzy MovementSpeed Addition", this.getAdditionalMovementSpeed(), AttributeModifier.Operation.ADDITION));
        if (this.doHealthReduction) {
            this.applyModifier(entity, Attributes.MAX_HEALTH, new AttributeModifier(this.getHealthModifierID(), "Frenzy MaxHealth 1", 1.0, FrenzyModifier.FRENZY_HEALTH_OPERATION));
            entity.setHealth(1.0f);
        }
    }

    private void applyModifier(final LivingEntity entity, final Attribute attribute, final AttributeModifier modifier) {
        final ModifiableAttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.addPermanentModifier(modifier);
        }
    }

    static {
        FRENZY_HEALTH_OPERATION = AttributeModifier.Operation.MULTIPLY_TOTAL;
    }
}
