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

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class BreadcrumbFeature
        extends Feature<NoFeatureConfig> {
    public static Feature<NoFeatureConfig> INSTANCE;

    public BreadcrumbFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }


    public boolean place(ISeedReader world, ChunkGenerator gen, Random rand, BlockPos pos, NoFeatureConfig config) {
        VaultRaid vault = VaultRaidData.get(world.getLevel()).getAt(world.getLevel(), pos);
        if (vault == null) {
            return false;
        }

        placeBreadcrumbFeatures(vault, world, (at, state) -> world.setBlock(at, state, 2), rand, pos);
        return false;
    }

    public static void generateVaultBreadcrumb(VaultRaid vault, ServerWorld sWorld, List<VaultPiece> pieces) {
        runGeneration(() -> {
            Predicate<BlockPos> filter = ();
            Set<ChunkPos> chunks = new HashSet<>();
            for (VaultPiece piece : pieces) {
                MutableBoundingBox box = piece.getBoundingBox();
                filter = filter.or(box::isInside);
                ChunkPos chMin = new ChunkPos(box.x0 >> 4, box.z0 >> 4);
                ChunkPos chMax = new ChunkPos(box.x1 >> 4, box.z1 >> 4);
                for (int x = chMin.x; x <= chMax.x; x++) {
                    for (int z = chMin.z; z <= chMax.z; z++) {
                        chunks.add(new ChunkPos(x, z));
                    }
                }
            }
            Predicate<BlockPos> featurePlacementFilter = filter;
            for (ChunkPos pos : chunks) {
                BlockPos featurePos = pos.getWorldPosition();
                placeBreadcrumbFeatures(vault, (ISeedReader) sWorld, (), sWorld.getRandom(), featurePos);
            }
        });
    }


    private static void placeBreadcrumbFeatures(VaultRaid vault, ISeedReader world, BiPredicate<BlockPos, BlockState> blockPlacer, Random rand, BlockPos featurePos) {
        vault.getActiveObjective(ScavengerHuntObjective.class).ifPresent(objective -> doTreasureSpawnPass(rand, (IWorld) world, blockPlacer, featurePos));


        doChestSpawnPass(rand, (IWorld) world, blockPlacer, featurePos, ModBlocks.VAULT_CHEST.defaultBlockState());


        List<VaultPlayer> runners = (List<VaultPlayer>) vault.getPlayers().stream().filter(vaultPlayer -> vaultPlayer instanceof iskallia.vault.world.vault.player.VaultRunner).collect(Collectors.toList());
        for (int i = 0; i < runners.size() - 1; i++) {
            doChestSpawnPass(rand, (IWorld) world, blockPlacer, featurePos, ModBlocks.VAULT_COOP_CHEST.defaultBlockState());
        }

        placeChestModifierFeatures(vault, world, blockPlacer, rand, featurePos);
    }

    private static void placeChestModifierFeatures(VaultRaid vault, ISeedReader world, BiPredicate<BlockPos, BlockState> blockPlacer, Random rand, BlockPos featurePos) {
        vault.getActiveModifiersFor(PlayerFilter.any(), ChestModifier.class).forEach(modifier -> {
            int attempts = modifier.getChestGenerationAttempts();
            for (int i = 0; i < modifier.getAdditionalBonusChestPasses(); i++) {
                doChestSpawnPass(rand, (IWorld) world, blockPlacer, featurePos, ModBlocks.VAULT_BONUS_CHEST.defaultBlockState(), attempts);
            }
        });
    }

    private static void doTreasureSpawnPass(Random rand, IWorld world, BiPredicate<BlockPos, BlockState> blockPlacer, BlockPos pos) {
        doPlacementPass(rand, world, blockPlacer, pos, ModBlocks.SCAVENGER_TREASURE.defaultBlockState(), 45, offset -> {

        });
    }

    private static void doChestSpawnPass(Random rand, IWorld world, BiPredicate<BlockPos, BlockState> blockPlacer, BlockPos pos, BlockState toPlace) {
        doChestSpawnPass(rand, world, blockPlacer, pos, toPlace, 12);
    }

    private static void doChestSpawnPass(Random rand, IWorld world, BiPredicate<BlockPos, BlockState> blockPlacer, BlockPos pos, BlockState toPlace, int attempts) {
        doPlacementPass(rand, world, blockPlacer, pos, toPlace, attempts, offset -> {

        });
    }

    private static void doPlacementPass(Random rand, IWorld world, BiPredicate<BlockPos, BlockState> blockPlacer, BlockPos pos, BlockState toPlace, int attempts, Consumer<BlockPos> pass) {
        for (int i = 0; i < attempts; i++) {
            int x = rand.nextInt(16);
            int z = rand.nextInt(16);
            int y = rand.nextInt(64);
            BlockPos offset = pos.offset(x, y, z);
            BlockState state = world.getBlockState(offset);

            if (state.getBlock() == Blocks.AIR && world.getBlockState(offset.below()).isFaceSturdy((IBlockReader) world, offset, Direction.UP) &&
                    blockPlacer.test(offset, toPlace)) {
                pass.accept(offset);
            }
        }
    }


    private static void runGeneration(Runnable run) {
        JigsawPiecePlacer.generationPlacementCount++;
        try {
            run.run();
        } finally {
            JigsawPiecePlacer.generationPlacementCount--;
        }
    }

    public static void register(RegistryEvent.Register<Feature<?>> event) {
        INSTANCE = new BreadcrumbFeature(NoFeatureConfig.CODEC);
        INSTANCE.setRegistryName(Vault.id("breadcrumb_chest"));
        event.getRegistry().register((IForgeRegistryEntry) INSTANCE);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\decorator\BreadcrumbFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */