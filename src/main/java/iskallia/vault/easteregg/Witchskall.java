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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Random;

@EventBusSubscriber
public class Witchskall {
    public static final float WITCHSKALL_CHANCE = 0.001F;
    public static final int WITCHSKALLIFICATION_TICKS = 100;
    public static DataParameter<Integer> WITCHSKALL_TICKS;
    public static DataParameter<Boolean> IS_WITCHSKALL;

    public static int getWitchskallificationTicks(WitchEntity witchEntity) {
        return ((Integer) witchEntity.getEntityData().get(WITCHSKALL_TICKS)).intValue();
    }

    public static boolean isWitchskall(WitchEntity witchEntity) {
        return ((Boolean) witchEntity.getEntityData().get(IS_WITCHSKALL)).booleanValue();
    }

    public static int setWitchskallificationTicks(WitchEntity witchEntity, int ticks) {
        witchEntity.getEntityData().set(WITCHSKALL_TICKS, Integer.valueOf(ticks));
        return ticks;
    }

    public static void witchskallificate(WitchEntity witchEntity) {
        setWitchskallificationTicks(witchEntity, 0);
        witchEntity.getEntityData().set(IS_WITCHSKALL, Boolean.valueOf(true));
        witchEntity.setCustomName((ITextComponent) new StringTextComponent("Witchskall"));
    }

    @SubscribeEvent
    public static void onWitchTick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;

        if (world.isClientSide) {
            return;
        }
        if (!(entity instanceof WitchEntity)) {
            return;
        }
        WitchEntity witchEntity = (WitchEntity) entity;

        if (isWitchskall(witchEntity)) {
            return;
        }
        int witchskallTicks = getWitchskallificationTicks(witchEntity);

        if (witchskallTicks == 0) {
            return;
        }
        if (witchskallTicks <= -1) {
            if ((new Random()).nextFloat() <= 0.001F) {
                setWitchskallificationTicks(witchEntity, 100);
            } else {
                setWitchskallificationTicks(witchEntity, 0);
            }

            return;
        }
        int setWitchskallTicks = setWitchskallificationTicks(witchEntity, witchskallTicks - 1);
        if (setWitchskallTicks == 0) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.sendParticles((IParticleData) ModParticles.GREEN_FLAME.get(), entity
                    .getX(), entity.getY(), entity.getZ(), 100, 0.5D, 1.0D, 0.5D, 0.1D);

            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.WITCHSKALL_IDLE, SoundCategory.MASTER, 1.1F, 1.0F);

            witchskallificate(witchEntity);
        }
    }

    @SubscribeEvent
    public static void onWitchskallDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity.level.isClientSide()) {
            return;
        }
        if (!(entity instanceof WitchEntity) || !isWitchskall((WitchEntity) entity)) {
            return;
        }
        Entity trueSource = event.getSource().getEntity();

        if (!(trueSource instanceof ServerPlayerEntity)) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) trueSource;
        AdvancementHelper.grantCriterion(player, Vault.id("main/witchskall"), "witchskall_killed");
    }

    @SubscribeEvent
    public static void onWitchskallDrops(LivingDropsEvent event) {
        Entity entity = event.getEntity();

        if (entity.level.isClientSide()) {
            return;
        }
        if (!(entity instanceof WitchEntity)) {
            return;
        }
        if (!isWitchskall((WitchEntity) entity)) {
            return;
        }
        ServerWorld world = (ServerWorld) entity.level;


        ItemStack itemStack = (new GearItemStackBuilder((Item) ModItems.HELMET)).setGearRarity(VaultGear.Rarity.UNIQUE).setColor(-5384139).setSpecialModelId(ModModels.SpecialGearModel.ISKALL_HOLOLENS.getId()).build();

        ItemEntity itemEntity = new ItemEntity((World) world, entity.getX(), entity.getY(), entity.getZ(), itemStack);
        itemEntity.setDefaultPickUpDelay();
        event.getDrops().add(itemEntity);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\easteregg\Witchskall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */