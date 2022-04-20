package iskallia.vault.skill.ability.effect;

import iskallia.vault.Vault;
import iskallia.vault.init.ModParticles;
import iskallia.vault.skill.ability.config.HunterConfig;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.ServerScheduler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HunterAbility<C extends HunterConfig> extends AbilityEffect<C> {
    private static final AxisAlignedBB SEARCH_BOX;

    @Override
    public String getAbilityGroupName() {
        return "Hunter";
    }

    @Override
    public boolean onAction(final C config, final ServerPlayerEntity player, final boolean active) {
        final World world = player.getCommandSenderWorld();
        if (!(player instanceof ServerPlayerEntity) || !(world instanceof ServerWorld) || world.dimension() != Vault.VAULT_KEY) {
            return false;
        }
        final ServerWorld sWorld = (ServerWorld) world;
        final ServerPlayerEntity sPlayer = player;
        for (int delay = 0; delay < config.getTickDuration() / 5; ++delay) {
            ServerScheduler.INSTANCE.schedule(delay * 5, () -> this.selectPositions(config, world, (PlayerEntity) player).forEach(tpl -> {
                final Color c = (Color) tpl.getB();
                for (int i = 0; i < 8; ++i) {
                    final Vector3d v = MiscUtils.getRandomOffset((BlockPos) tpl.getA(), HunterAbility.rand);
                    sWorld.sendParticles(sPlayer, (IParticleData) ModParticles.DEPTH_FIREWORK.get(), true, v.x, v.y, v.z, 0, (double) (c.getRed() / 255.0f), (double) (c.getGreen() / 255.0f), (double) (c.getBlue() / 255.0f), 1.0);
                }
            }));
        }
        return true;
    }

    protected Predicate<LivingEntity> getEntityFilter() {
        return e -> e.isAlive() && !e.isSpectator() && e.getClassification(false) == EntityClassification.MONSTER;
    }

    protected List<Tuple<BlockPos, Color>> selectPositions(final C config, final World world, final PlayerEntity player) {
        final Color c = new Color(config.getColor(), false);
        return (List) world.getLoadedEntitiesOfClass(LivingEntity.class, HunterAbility.SEARCH_BOX.move(player.blockPosition()).inflate(config.getSearchRadius()), this.getEntityFilter()).stream().map(Entity::blockPosition).map(pos -> new Tuple(pos, c)).collect(Collectors.toList());
    }

    protected void forEachTileEntity(final C config, final World world, final PlayerEntity player, final BiConsumer<BlockPos, TileEntity> tileFn) {
        final BlockPos playerOffset = player.blockPosition();
        final double radius = config.getSearchRadius();
        final double radiusSq = radius * radius;
        final int iRadius = MathHelper.ceil(radius);
        final Vector3i radVec = new Vector3i(iRadius, iRadius, iRadius);
        final ChunkPos posMin = new ChunkPos(player.blockPosition().subtract(radVec));
        final ChunkPos posMax = new ChunkPos(player.blockPosition().offset(radVec));
        for (int xx = posMin.x; xx <= posMax.x; ++xx) {
            for (int zz = posMin.z; zz <= posMax.z; ++zz) {
                final Chunk ch = world.getChunkSource().getChunkNow(xx, zz);
                if (ch != null) {
                    ch.getBlockEntities().forEach((pos, tile) -> {
                        if (tile != null && pos.distSqr((Vector3i) playerOffset) <= radiusSq) {
                            tileFn.accept(pos, tile);
                        }
                        return;
                    });
                }
            }
        }
    }

    static {
        SEARCH_BOX = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
}
