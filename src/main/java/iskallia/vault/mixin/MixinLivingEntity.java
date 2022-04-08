package iskallia.vault.mixin;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.util.calc.ResistanceHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin({LivingEntity.class})
public abstract class MixinLivingEntity extends Entity {
    private float prevSize = -1.0F;
    @Shadow
    @Final
    protected static EntitySize SLEEPING_DIMENSIONS;

    public MixinLivingEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }


    @Shadow
    public abstract EffectInstance getEffect(Effect paramEffect);


    @Shadow
    @Nullable
    public abstract ModifiableAttributeInstance getAttribute(Attribute paramAttribute);


    @Shadow
    public abstract boolean hasEffect(Effect paramEffect);

    @Redirect(method = {"registerAttributes"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/AttributeModifierMap;createMutableAttribute()Lnet/minecraft/entity/ai/attributes/AttributeModifierMap$MutableAttribute;"))
    private static AttributeModifierMap.MutableAttribute registerAttributes() {
        return AttributeModifierMap.builder()
                .add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED)
                .add(Attributes.ARMOR)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add((Attribute) ForgeMod.SWIM_SPEED.get())
                .add((Attribute) ForgeMod.NAMETAG_DISTANCE.get())
                .add((Attribute) ForgeMod.ENTITY_GRAVITY.get())
                .add(ModAttributes.CRIT_CHANCE)
                .add(ModAttributes.CRIT_MULTIPLIER)
                .add(ModAttributes.TP_CHANCE)
                .add(ModAttributes.TP_INDIRECT_CHANCE)
                .add(ModAttributes.TP_RANGE)
                .add(ModAttributes.POTION_RESISTANCE)
                .add(ModAttributes.SIZE_SCALE)
                .add(ModAttributes.BREAK_ARMOR_CHANCE);
    }

    @Shadow
    public abstract float getScale();

    @Shadow
    public abstract boolean causeFallDamage(float paramFloat1, float paramFloat2);

    @Shadow
    public abstract ItemStack getItemInHand(Hand paramHand);

    @Shadow
    public abstract EntitySize getDimensions(Pose paramPose);

    @Redirect(method = {"applyPotionDamageCalculations"}, at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"))
    protected float applyPotionDamageCalculations(float a, float b) {
        if (!this.level.isClientSide) {
            int resistance = hasEffect(Effects.DAMAGE_RESISTANCE) ? 0 : (getEffect(Effects.DAMAGE_RESISTANCE).getAmplifier() + 1);
            float damageCancel = (resistance * 5) / 25.0F;
            float damage = a * 25.0F / (25 - resistance * 5);

            if (this instanceof ServerPlayerEntity) {
                damageCancel += ResistanceHelper.getPlayerResistancePercent((ServerPlayerEntity) this);
            } else {
                damageCancel += ResistanceHelper.getResistancePercent((LivingEntity) this);
            }

            return Math.max(damage - damage * damageCancel, 0.0F);
        }

        return Math.max(a, b);
    }


    @Inject(method = {"tick"}, at = {@At("RETURN")})
    public void tick(CallbackInfo ci) {
        ModifiableAttributeInstance scale = getAttribute(ModAttributes.SIZE_SCALE);
        if (scale == null)
            return;
        if (this.prevSize != scale.getValue()) {
            this.prevSize = (float) scale.getValue();
            this.dimensions = getDimensions(Pose.STANDING).scale(this.prevSize);

            refreshDimensions();
        }
    }

    @Inject(method = {"addPotionEffect"}, at = {@At("HEAD")}, cancellable = true)
    private void addPotionEffect(EffectInstance effect, CallbackInfoReturnable<Boolean> ci) {
        ModifiableAttributeInstance attribute = getAttribute(ModAttributes.POTION_RESISTANCE);
        if (attribute == null)
            return;
        if (this.random.nextDouble() >= attribute.getValue())
            return;
        ci.setReturnValue(Boolean.valueOf(false));
    }

    @Inject(method = {"checkTotemDeathProtection"}, at = {@At(value = "RETURN", ordinal = 1)}, cancellable = true)
    private void checkTotemDeathProtection(DamageSource damageSourceIn, CallbackInfoReturnable<Boolean> cir) {
        if (((Boolean) cir.getReturnValue()).booleanValue() || damageSourceIn.isBypassInvul()) {
            return;
        }

        ItemStack idol = ItemStack.EMPTY;
        for (Hand hand : Hand.values()) {
            ItemStack it = getItemInHand(hand);
            if (it.getItem() instanceof iskallia.vault.item.gear.IdolItem) {
                idol = it.copy();
                it.shrink(1);

                break;
            }
        }
        if (!idol.isEmpty()) {
            if (this instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) this;
                serverplayerentity.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, idol);
            }

            ((LivingEntity) this).setHealth(1.0F);
            ((LivingEntity) this).removeAllEffects();
            ((LivingEntity) this).addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
            ((LivingEntity) this).addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
            ((LivingEntity) this).addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
            this.level.broadcastEntityEvent(this, (byte) 35);

            cir.setReturnValue(Boolean.valueOf(true));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinLivingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */