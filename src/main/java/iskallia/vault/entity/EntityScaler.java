package iskallia.vault.entity;

import iskallia.vault.config.VaultMobsConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.GlobalDifficultyData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.VaultUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;


public class EntityScaler {
    private static final String SCALED_TAG = "vault_scaled";

    public static void scaleVaultEntity(VaultRaid vault, EntityEvent event) {
        if (!VaultUtils.inVault(vault, event.getEntity())) {
            return;
        }


        scaleVaultEntity(vault, event.getEntity());
    }

    public static void scaleVaultEntity(VaultRaid vault, Entity entity) {
        if (!(entity instanceof MonsterEntity) || entity instanceof EternalEntity ||

                isScaled(entity))
            return;
        World world = entity.getCommandSenderWorld();
        if (!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld sWorld = (ServerWorld) world;

        MonsterEntity livingEntity = (MonsterEntity) entity;

        GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(sWorld).getVaultDifficulty();
        vault.getProperties().getBase(VaultRaid.LEVEL).ifPresent(level -> setScaledEquipment((LivingEntity) livingEntity, vault, difficulty, level.intValue(), new Random(), Type.MOB));


        setScaled((Entity) livingEntity);
        livingEntity.setPersistenceRequired();
    }

    public static boolean isScaled(Entity entity) {
        return entity.getTags().contains("vault_scaled");
    }

    public static void setScaled(Entity entity) {
        entity.addTag("vault_scaled");
    }

    public static void setScaledEquipment(LivingEntity entity, VaultRaid vault, GlobalDifficultyData.Difficulty vaultDifficulty, int level, Random random, Type type) {
        VaultMobsConfig.Level overrides = ModConfigs.VAULT_MOBS.getForLevel(level);
        if (!isScaled((Entity) entity)) {
            VaultMobsConfig.Mob.scale(entity, vault, vaultDifficulty);
        }

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getType() != EquipmentSlotType.Group.HAND || entity.getItemBySlot(slot).isEmpty()) {


                ItemStack loot = type.loot.apply(overrides, slot);

                for (int i = 0; i < ((Integer) type.trials.apply(overrides)).intValue(); i++) {
                    EnchantmentHelper.enchantItem(random, loot,
                            EnchantmentHelper.getEnchantmentCost(random, ((Integer) type.level.apply(overrides)).intValue(), 15, loot), true);
                }

                entity.setItemSlot(slot, loot);
                if (entity instanceof MobEntity)
                    ((MobEntity) entity).setDropChance(slot, 0.0F);
            }
        }
    }

    public enum Type {
        MOB(VaultMobsConfig.Level::getForMob, level -> Integer.valueOf(level.MOB_MISC.ENCH_TRIALS), level -> Integer.valueOf(level.MOB_MISC.ENCH_LEVEL)),
        BOSS(VaultMobsConfig.Level::getForBoss, level -> Integer.valueOf(level.BOSS_MISC.ENCH_TRIALS), level -> Integer.valueOf(level.BOSS_MISC.ENCH_LEVEL)),
        RAFFLE_BOSS(VaultMobsConfig.Level::getForRaffle, level -> Integer.valueOf(level.RAFFLE_BOSS_MISC.ENCH_TRIALS), level -> Integer.valueOf(level.RAFFLE_BOSS_MISC.ENCH_LEVEL));


        private final BiFunction<VaultMobsConfig.Level, EquipmentSlotType, ItemStack> loot;
        private final Function<VaultMobsConfig.Level, Integer> trials;
        private final Function<VaultMobsConfig.Level, Integer> level;

        Type(BiFunction<VaultMobsConfig.Level, EquipmentSlotType, ItemStack> loot, Function<VaultMobsConfig.Level, Integer> trials, Function<VaultMobsConfig.Level, Integer> level) {
            this.loot = loot;
            this.trials = trials;
            this.level = level;
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\EntityScaler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */