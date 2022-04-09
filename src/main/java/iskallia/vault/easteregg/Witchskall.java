// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.easteregg;

import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModModels;
import iskallia.vault.init.ModParticles;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.util.AdvancementHelper;
import iskallia.vault.util.GearItemStackBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class Witchskall
{
    public static final float WITCHSKALL_CHANCE = 0.001f;
    public static final int WITCHSKALLIFICATION_TICKS = 100;
    public static DataParameter<Integer> WITCHSKALL_TICKS;
    public static DataParameter<Boolean> IS_WITCHSKALL;
    
    public static int getWitchskallificationTicks(final WitchEntity witchEntity) {
        return (int)witchEntity.getEntityData().get((DataParameter)Witchskall.WITCHSKALL_TICKS);
    }
    
    public static boolean isWitchskall(final WitchEntity witchEntity) {
        return (boolean)witchEntity.getEntityData().get((DataParameter)Witchskall.IS_WITCHSKALL);
    }
    
    public static int setWitchskallificationTicks(final WitchEntity witchEntity, final int ticks) {
        witchEntity.getEntityData().set((DataParameter)Witchskall.WITCHSKALL_TICKS, ticks);
        return ticks;
    }
    
    public static void witchskallificate(final WitchEntity witchEntity) {
        setWitchskallificationTicks(witchEntity, 0);
        witchEntity.getEntityData().set((DataParameter)Witchskall.IS_WITCHSKALL, true);
        witchEntity.setCustomName((ITextComponent)new StringTextComponent("Witchskall"));
    }
    
    @SubscribeEvent
    public static void onWitchTick(final LivingEvent.LivingUpdateEvent event) {
        final LivingEntity entity = event.getEntityLiving();
        final World world = entity.level;
        if (world.isClientSide) {
            return;
        }
        if (!(entity instanceof WitchEntity)) {
            return;
        }
        final WitchEntity witchEntity = (WitchEntity)entity;
        if (isWitchskall(witchEntity)) {
            return;
        }
        final int witchskallTicks = getWitchskallificationTicks(witchEntity);
        if (witchskallTicks == 0) {
            return;
        }
        if (witchskallTicks <= -1) {
            if (new Random().nextFloat() <= 0.001f) {
                setWitchskallificationTicks(witchEntity, 100);
            }
            else {
                setWitchskallificationTicks(witchEntity, 0);
            }
            return;
        }
        final int setWitchskallTicks = setWitchskallificationTicks(witchEntity, witchskallTicks - 1);
        if (setWitchskallTicks == 0) {
            final ServerWorld serverWorld = (ServerWorld)world;
            serverWorld.sendParticles((IParticleData)ModParticles.GREEN_FLAME.get(), entity.getX(), entity.getY(), entity.getZ(), 100, 0.5, 1.0, 0.5, 0.1);
            serverWorld.playSound((PlayerEntity)null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.WITCHSKALL_IDLE, SoundCategory.MASTER, 1.1f, 1.0f);
            witchskallificate(witchEntity);
        }
    }
    
    @SubscribeEvent
    public static void onWitchskallDeath(final LivingDeathEvent event) {
        final Entity entity = event.getEntity();
        if (entity.level.isClientSide()) {
            return;
        }
        if (!(entity instanceof WitchEntity) || !isWitchskall((WitchEntity)entity)) {
            return;
        }
        final Entity trueSource = event.getSource().getEntity();
        if (!(trueSource instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity)trueSource;
        AdvancementHelper.grantCriterion(player, Vault.id("main/witchskall"), "witchskall_killed");
    }
    
    @SubscribeEvent
    public static void onWitchskallDrops(final LivingDropsEvent event) {
        final Entity entity = event.getEntity();
        if (entity.level.isClientSide()) {
            return;
        }
        if (!(entity instanceof WitchEntity)) {
            return;
        }
        if (!isWitchskall((WitchEntity)entity)) {
            return;
        }
        final ServerWorld world = (ServerWorld)entity.level;
        final ItemStack itemStack = new GearItemStackBuilder((Item)ModItems.HELMET).setGearRarity(VaultGear.Rarity.UNIQUE).setColor(-5384139).setSpecialModelId(ModModels.SpecialGearModel.ISKALL_HOLOLENS.getId()).build();
        final ItemEntity itemEntity = new ItemEntity((World)world, entity.getX(), entity.getY(), entity.getZ(), itemStack);
        itemEntity.setDefaultPickUpDelay();
        event.getDrops().add(itemEntity);
    }
}
