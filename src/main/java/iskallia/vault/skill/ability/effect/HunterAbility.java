package iskallia.vault.skill.ability.effect;

import iskallia.vault.Vault;
import iskallia.vault.skill.ability.config.HunterConfig;
import iskallia.vault.util.ServerScheduler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HunterAbility<C extends HunterConfig>
        extends AbilityEffect<C> {
    private static final AxisAlignedBB SEARCH_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);


    public String getAbilityGroupName() {
        return "Hunter";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        World world = player.getCommandSenderWorld();
        if (!(player instanceof ServerPlayerEntity) || !(world instanceof ServerWorld) || world

                .dimension() != Vault.VAULT_KEY) {
            return false;
        }
        ServerWorld sWorld = (ServerWorld) world;
        ServerPlayerEntity sPlayer = player;

        for (int delay = 0; delay < config.getTickDuration() / 5; delay++) {
            ServerScheduler.INSTANCE.schedule(delay * 5, () -> selectPositions((C) config, world, (PlayerEntity) player).forEach(()));
        }


        return true;
    }

    protected Predicate<LivingEntity> getEntityFilter() {
        return e -> (e.isAlive() && !e.isSpectator() && e.getClassification(false) == EntityClassification.MONSTER);
    }

    protected List<Tuple<BlockPos, Color>> selectPositions(C config, World world, PlayerEntity player) {
        Color c = new Color(config.getColor(), false);
        return (List<Tuple<BlockPos, Color>>) world.getLoadedEntitiesOfClass(LivingEntity.class, SEARCH_BOX
                        .move(player.blockPosition()).inflate(config.getSearchRadius()), getEntityFilter()).stream()
                .map(Entity::blockPosition)
                .map(pos -> new Tuple(pos, c))
                .collect(Collectors.toList());
    }

    protected void forEachTileEntity(C config, World world, PlayerEntity player, BiConsumer<BlockPos, TileEntity> tileFn) {
        BlockPos playerOffset = player.blockPosition();
        double radius = config.getSearchRadius();
        double radiusSq = radius * radius;

        int iRadius = MathHelper.ceil(radius);
        Vector3i radVec = new Vector3i(iRadius, iRadius, iRadius);
        ChunkPos posMin = new ChunkPos(player.blockPosition().subtract(radVec));
        ChunkPos posMax = new ChunkPos(player.blockPosition().offset(radVec));

        for (int xx = posMin.x; xx <= posMax.x; xx++) {
            for (int zz = posMin.z; zz <= posMax.z; zz++) {
                Chunk ch = world.getChunkSource().getChunkNow(xx, zz);
                if (ch != null)
                    ch.getBlockEntities().forEach((pos, tile) -> {
                        if (tile != null && pos.distSqr((Vector3i) playerOffset) <= radiusSq)
                            tileFn.accept(pos, tile);
                    });
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\HunterAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */