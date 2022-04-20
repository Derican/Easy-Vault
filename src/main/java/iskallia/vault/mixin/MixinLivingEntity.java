package iskallia.vault.mixin;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.IdolItem;
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
    private float prevSize;
    @Shadow
    @Final
    protected static EntitySize SLEEPING_DIMENSIONS;

    public MixinLivingEntity(final EntityType<?> entityType, final World world) {
        super((EntityType) entityType, world);
        this.prevSize = -1.0f;
    }

    @Shadow
    public abstract EffectInstance getEffect(final Effect p0);

    @Shadow
    @Nullable
    public abstract ModifiableAttributeInstance getAttribute(final Attribute p0);

    @Shadow
    public abstract boolean hasEffect(final Effect p0);

    @Shadow
    public abstract float getScale();

    @Shadow
    public abstract boolean causeFallDamage(final float p0, final float p1);

    @Shadow
    public abstract ItemStack getItemInHand(final Hand p0);

    @Shadow
    public abstract EntitySize getDimensions(final Pose p0);

    @Redirect(method = {"createLivingAttributes"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/AttributeModifierMap;builder()Lnet/minecraft/entity/ai/attributes/AttributeModifierMap$MutableAttribute;"))
    private static AttributeModifierMap.MutableAttribute registerAttributes() {
        return AttributeModifierMap.builder().add(Attributes.MAX_HEALTH).add(Attributes.KNOCKBACK_RESISTANCE).add(Attributes.MOVEMENT_SPEED).add(Attributes.ARMOR).add(Attributes.ARMOR_TOUGHNESS).add((Attribute) ForgeMod.SWIM_SPEED.get()).add((Attribute) ForgeMod.NAMETAG_DISTANCE.get()).add((Attribute) ForgeMod.ENTITY_GRAVITY.get()).add(ModAttributes.CRIT_CHANCE).add(ModAttributes.CRIT_MULTIPLIER).add(ModAttributes.TP_CHANCE).add(ModAttributes.TP_INDIRECT_CHANCE).add(ModAttributes.TP_RANGE).add(ModAttributes.POTION_RESISTANCE).add(ModAttributes.SIZE_SCALE).add(ModAttributes.BREAK_ARMOR_CHANCE);
    }

    @Redirect(method = {"getDamageAfterMagicAbsorb"}, at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"))
    protected float applyPotionDamageCalculations(final float a, final float b) {
        if (!this.level.isClientSide) {
            final int resistance = this.hasEffect(Effects.DAMAGE_RESISTANCE) ? 0 : (this.getEffect(Effects.DAMAGE_RESISTANCE).getAmplifier() + 1);
            float damageCancel = resistance * 5 / 25.0f;
            final float damage = a * 25.0f / (25 - resistance * 5);
            if ((LivingEntity) (Object) this instanceof ServerPlayerEntity) {
                damageCancel += ResistanceHelper.getPlayerResistancePercent((ServerPlayerEntity) (Object) this);
            } else {
                damageCancel += ResistanceHelper.getResistancePercent((LivingEntity) (Object) this);
            }
            return Math.max(damage - damage * damageCancel, 0.0f);
        }
        return Math.max(a, b);
    }

    @Inject(method = {"tick"}, at = {@At("RETURN")})
    public void tick(final CallbackInfo ci) {
        final ModifiableAttributeInstance scale = this.getAttribute(ModAttributes.SIZE_SCALE);
        if (scale == null) {
            return;
        }
        if (this.prevSize != scale.getValue()) {
            this.prevSize = (float) scale.getValue();
//            this.dimensions = this.getDimensions(Pose.STANDING).scale(this.prevSize);
            this.refreshDimensions();
        }
    }

    @Inject(method = {"addEffect"}, at = {@At("HEAD")}, cancellable = true)
    private void addPotionEffect(final EffectInstance effect, final CallbackInfoReturnable<Boolean> ci) {
        final ModifiableAttributeInstance attribute = this.getAttribute(ModAttributes.POTION_RESISTANCE);
        if (attribute == null) {
            return;
        }
        if (this.random.nextDouble() >= attribute.getValue()) {
            return;
        }
        ci.setReturnValue(false);
    }

    @Inject(method = {"checkTotemDeathProtection"}, at = {@At(value = "RETURN", ordinal = 1)}, cancellable = true)
    private void checkTotemDeathProtection(final DamageSource damageSourceIn, final CallbackInfoReturnable<Boolean> cir) {
        if ((boolean) cir.getReturnValue() || damageSourceIn.isBypassInvul()) {
            return;
        }
        ItemStack idol = ItemStack.EMPTY;
        for (final Hand hand : Hand.values()) {
            final ItemStack it = this.getItemInHand(hand);
            if (it.getItem() instanceof IdolItem) {
                idol = it.copy();
                it.shrink(1);
                break;
            }
        }
        if (!idol.isEmpty()) {
            if ((LivingEntity) (Object) this instanceof ServerPlayerEntity) {
                final ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) (Object) this;
                serverplayerentity.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, idol);
            }
            ((LivingEntity) (Object) this).setHealth(1.0f);
            ((LivingEntity) (Object) this).removeAllEffects();
            ((LivingEntity) (Object) this).addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
            ((LivingEntity) (Object) this).addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
            ((LivingEntity) (Object) this).addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
            this.level.broadcastEntityEvent((Entity) this, (byte) 35);
            cir.setReturnValue(true);
        }
    }
}
