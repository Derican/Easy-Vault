// 
// Decompiled by Procyon v0.6.0
// 

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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class VaultBossSpawner
{
    public static LivingEntity spawnBoss(final VaultRaid vault, final ServerWorld world, final BlockPos pos) {
        final int level = vault.getProperties().getValue(VaultRaid.LEVEL);
        final String playerBossName = vault.getProperties().getBase(VaultRaid.PLAYER_BOSS_NAME).orElse(null);
        EntityScaler.Type bossScalingType = EntityScaler.Type.BOSS;
        VaultMobsConfig.Mob bossConfig = ModConfigs.VAULT_MOBS.getForLevel(level).BOSS_POOL.getRandom(world.getRandom());
        LivingEntity boss;
        ITextComponent bossName;
        if (vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, false)) {
            boss = (LivingEntity)ModEntities.AGGRESSIVE_COW_BOSS.create((World)world);
            boss.addTag("replaced_entity");
            bossName = (ITextComponent)new StringTextComponent("an ordinary Vault Boss");
        }
        else {
            if (playerBossName == null) {
                bossName = (ITextComponent)new StringTextComponent("Boss");
            }
            else {
                bossConfig = ModConfigs.VAULT_MOBS.getForLevel(level).RAFFLE_BOSS_POOL.getRandom(world.getRandom());
                bossName = (ITextComponent)new StringTextComponent(playerBossName);
                bossScalingType = EntityScaler.Type.RAFFLE_BOSS;
            }
            boss = bossConfig.create((World)world);
        }
        final GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(world).getVaultDifficulty();
        VaultMobsConfig.Mob.scale(boss, vault, difficulty);
        EntityScaler.setScaled((Entity)boss);
        if (boss instanceof FighterEntity) {
            ((FighterEntity)boss).changeSize(2.0f);
        }
        boss.moveTo(pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 0.0f, 0.0f);
        boss.getTags().add("vault_boss");
        world.addWithUUID((Entity)boss);
        if (boss instanceof FighterEntity) {
            ((FighterEntity)boss).bossInfo.setVisible(true);
        }
        if (boss instanceof VaultBoss) {
            ((VaultBoss)boss).getServerBossInfo().setVisible(true);
        }
        EntityScaler.setScaledEquipment(boss, vault, difficulty, level, new Random(), bossScalingType);
        boss.setCustomName(bossName);
        if (boss instanceof MobEntity) {
            ((MobEntity)boss).setPersistenceRequired();
        }
        for (int i = 0; i < 5; ++i) {
            BlockPos pos2 = pos.offset(world.random.nextInt(100) - 50, 0, world.random.nextInt(100) - 50);
            pos2 = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, pos2);
            final LightningBoltEntity bolt = (LightningBoltEntity)EntityType.LIGHTNING_BOLT.create((World)world);
            bolt.moveTo(Vector3d.atBottomCenterOf((Vector3i)pos2));
            bolt.setVisualOnly(true);
            world.addFreshEntity((Entity)bolt);
        }
        return boss;
    }
}
