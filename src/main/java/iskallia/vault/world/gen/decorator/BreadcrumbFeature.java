// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.gen.decorator;

import com.mojang.serialization.Codec;
import iskallia.vault.Vault;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.gen.structure.JigsawPiecePlacer;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import iskallia.vault.world.vault.modifier.ChestModifier;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.player.VaultRunner;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BreadcrumbFeature extends Feature<NoFeatureConfig>
{
    public static Feature<NoFeatureConfig> INSTANCE;
    
    public BreadcrumbFeature(final Codec<NoFeatureConfig> codec) {
        super((Codec)codec);
    }
    
    public boolean place(final ISeedReader world, final ChunkGenerator gen, final Random rand, final BlockPos pos, final NoFeatureConfig config) {
        final VaultRaid vault = VaultRaidData.get(world.getLevel()).getAt(world.getLevel(), pos);
        if (vault == null) {
            return false;
        }
        placeBreadcrumbFeatures(vault, world, (at, state) -> world.setBlock(at, state, 2), rand, pos);
        return false;
    }
    
    public static void generateVaultBreadcrumb(final VaultRaid vault, final ServerWorld sWorld, final List<VaultPiece> pieces) {
        runGeneration(() -> {
            Predicate<BlockPos> filter = pos -> false;
            final Set<ChunkPos> chunks = new HashSet<ChunkPos>();

            final Iterator<VaultPiece> iterator=pieces.iterator();
            while (iterator.hasNext()) {
                final VaultPiece piece = iterator.next();
                final MutableBoundingBox box = piece.getBoundingBox();
                filter = filter.or((Predicate<? super BlockPos>)box::isInside);
                final ChunkPos chMin = new ChunkPos(box.x0 >> 4, box.z0 >> 4);
                final ChunkPos chMax = new ChunkPos(box.x1 >> 4, box.z1 >> 4);
                for (int x = chMin.x; x <= chMax.x; ++x) {
                    for (int z = chMin.z; z <= chMax.z; ++z) {
                        chunks.add(new ChunkPos(x, z));
                    }
                }
            }
            final Predicate<BlockPos> featurePlacementFilter = filter;

            final Iterator<ChunkPos> iterator2=chunks.iterator();
            while (iterator2.hasNext()) {
                final ChunkPos pos = iterator2.next();
                final BlockPos featurePos = pos.getWorldPosition();
                placeBreadcrumbFeatures(vault, (ISeedReader)sWorld, (at, state) -> featurePlacementFilter.test(at) && sWorld.setBlock(at, state, 2), sWorld.getRandom(), featurePos);
            }
        });
    }
    
    private static void placeBreadcrumbFeatures(final VaultRaid vault, final ISeedReader world, final BiPredicate<BlockPos, BlockState> blockPlacer, final Random rand, final BlockPos featurePos) {
        vault.getActiveObjective(ScavengerHuntObjective.class).ifPresent(objective -> doTreasureSpawnPass(rand, (IWorld)world, blockPlacer, featurePos));
        doChestSpawnPass(rand, (IWorld)world, blockPlacer, featurePos, ModBlocks.VAULT_CHEST.defaultBlockState());
        final List<VaultPlayer> runners = vault.getPlayers().stream().filter(vaultPlayer -> vaultPlayer instanceof VaultRunner).collect(Collectors.toList());
        for (int i = 0; i < runners.size() - 1; ++i) {
            doChestSpawnPass(rand, (IWorld)world, blockPlacer, featurePos, ModBlocks.VAULT_COOP_CHEST.defaultBlockState());
        }
        placeChestModifierFeatures(vault, world, blockPlacer, rand, featurePos);
    }
    
    private static void placeChestModifierFeatures(final VaultRaid vault, final ISeedReader world, final BiPredicate<BlockPos, BlockState> blockPlacer, final Random rand, final BlockPos featurePos) {
        vault.getActiveModifiersFor(PlayerFilter.any(), ChestModifier.class).forEach(modifier -> {
            final int attempts = modifier.getChestGenerationAttempts();
            for (int i = 0; i < modifier.getAdditionalBonusChestPasses(); ++i) {
                doChestSpawnPass(rand, (IWorld)world, blockPlacer, featurePos, ModBlocks.VAULT_BONUS_CHEST.defaultBlockState(), attempts);
            }
        });
    }
    
    private static void doTreasureSpawnPass(final Random rand, final IWorld world, final BiPredicate<BlockPos, BlockState> blockPlacer, final BlockPos pos) {
        doPlacementPass(rand, world, blockPlacer, pos, ModBlocks.SCAVENGER_TREASURE.defaultBlockState(), 45, offset -> {});
    }
    
    private static void doChestSpawnPass(final Random rand, final IWorld world, final BiPredicate<BlockPos, BlockState> blockPlacer, final BlockPos pos, final BlockState toPlace) {
        doChestSpawnPass(rand, world, blockPlacer, pos, toPlace, 12);
    }
    
    private static void doChestSpawnPass(final Random rand, final IWorld world, final BiPredicate<BlockPos, BlockState> blockPlacer, final BlockPos pos, final BlockState toPlace, final int attempts) {
        doPlacementPass(rand, world, blockPlacer, pos, toPlace, attempts, offset -> {});
    }
    
    private static void doPlacementPass(final Random rand, final IWorld world, final BiPredicate<BlockPos, BlockState> blockPlacer, final BlockPos pos, final BlockState toPlace, final int attempts, final Consumer<BlockPos> pass) {
        for (int i = 0; i < attempts; ++i) {
            final int x = rand.nextInt(16);
            final int z = rand.nextInt(16);
            final int y = rand.nextInt(64);
            final BlockPos offset = pos.offset(x, y, z);
            final BlockState state = world.getBlockState(offset);
            if (state.getBlock() == Blocks.AIR && world.getBlockState(offset.below()).isFaceSturdy((IBlockReader)world, offset, Direction.UP) && blockPlacer.test(offset, toPlace)) {
                pass.accept(offset);
            }
        }
    }
    
    private static void runGeneration(final Runnable run) {
        ++JigsawPiecePlacer.generationPlacementCount;
        try {
            run.run();
        }
        finally {
            --JigsawPiecePlacer.generationPlacementCount;
        }
    }
    
    public static void register(final RegistryEvent.Register<Feature<?>> event) {
        (BreadcrumbFeature.INSTANCE = new BreadcrumbFeature((Codec<NoFeatureConfig>)NoFeatureConfig.CODEC)).setRegistryName(Vault.id("breadcrumb_chest"));
        event.getRegistry().register(BreadcrumbFeature.INSTANCE);
    }
}
