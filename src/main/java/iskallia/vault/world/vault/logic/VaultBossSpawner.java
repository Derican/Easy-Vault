package iskallia.vault.world.vault.logic;

import iskallia.vault.config.VaultMobsConfig;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.entity.VaultBoss;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEntities;
import iskallia.vault.world.data.GlobalDifficultyData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class VaultBossSpawner {
    public static LivingEntity spawnBoss(VaultRaid vault, ServerWorld world, BlockPos pos) {
        LivingEntity boss;
        StringTextComponent stringTextComponent;
        int level = ((Integer) vault.getProperties().getValue(VaultRaid.LEVEL)).intValue();
        String playerBossName = vault.getProperties().getBase(VaultRaid.PLAYER_BOSS_NAME).orElse(null);


        EntityScaler.Type bossScalingType = EntityScaler.Type.BOSS;
        VaultMobsConfig.Mob bossConfig = (VaultMobsConfig.Mob) (ModConfigs.VAULT_MOBS.getForLevel(level)).BOSS_POOL.getRandom(world.getRandom());

        if (((Boolean) vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, Boolean.valueOf(false))).booleanValue()) {
            boss = (LivingEntity) ModEntities.AGGRESSIVE_COW_BOSS.create((World) world);
            boss.addTag("replaced_entity");
            stringTextComponent = new StringTextComponent("an ordinary Vault Boss");
        } else {
            if (playerBossName == null) {
                stringTextComponent = new StringTextComponent("Boss");
            } else {
                bossConfig = (VaultMobsConfig.Mob) (ModConfigs.VAULT_MOBS.getForLevel(level)).RAFFLE_BOSS_POOL.getRandom(world.getRandom());
                stringTextComponent = new StringTextComponent(playerBossName);
                bossScalingType = EntityScaler.Type.RAFFLE_BOSS;
            }
            boss = bossConfig.create((World) world);
        }

        GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(world).getVaultDifficulty();
        VaultMobsConfig.Mob.scale(boss, vault, difficulty);
        EntityScaler.setScaled((Entity) boss);

        if (boss instanceof FighterEntity) {
            ((FighterEntity) boss).changeSize(2.0F);
        }
        boss.moveTo(pos.getX() + 0.5D, pos.getY() + 0.2D, pos.getZ() + 0.5D, 0.0F, 0.0F);
        boss.getTags().add("vault_boss");
        world.addWithUUID((Entity) boss);

        if (boss instanceof FighterEntity) {
            ((FighterEntity) boss).bossInfo.setVisible(true);
        }
        if (boss instanceof VaultBoss) {
            ((VaultBoss) boss).getServerBossInfo().setVisible(true);
        }

        EntityScaler.setScaledEquipment(boss, vault, difficulty, level, new Random(), bossScalingType);
        boss.setCustomName((ITextComponent) stringTextComponent);

        if (boss instanceof MobEntity) {
            ((MobEntity) boss).setPersistenceRequired();
        }

        for (int i = 0; i < 5; i++) {
            BlockPos pos2 = pos.offset(world.random.nextInt(100) - 50, 0, world.random.nextInt(100) - 50);
            pos2 = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, pos2);

            LightningBoltEntity bolt = (LightningBoltEntity) EntityType.LIGHTNING_BOLT.create((World) world);
            bolt.moveTo(Vector3d.atBottomCenterOf((Vector3i) pos2));
            bolt.setVisualOnly(true);
            world.addFreshEntity((Entity) bolt);
        }

        return boss;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\VaultBossSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */