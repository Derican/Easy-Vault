package iskallia.vault.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Supplier;

public class ArenaStructure extends Structure<ArenaStructure.Config> {
    public ArenaStructure(Codec<Config> config) {
        super(config);
    }

    public static final int START_Y = 32;

    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    public Structure.IStartFactory<Config> getStartFactory() {
        return (p_242778_1_, p_242778_2_, p_242778_3_, p_242778_4_, p_242778_5_, p_242778_6_) -> new Start(this, p_242778_2_, p_242778_3_, p_242778_4_, p_242778_5_, p_242778_6_);
    }

    public static class Start extends MarginedStructureStart<Config> {
        private final ArenaStructure structure;

        public Start(ArenaStructure structure, int chunkX, int chunkZ, MutableBoundingBox box, int references, long worldSeed) {
            super(structure, chunkX, chunkZ, box, references, worldSeed);
            this.structure = structure;
        }

        public void generatePieces(DynamicRegistries registry, ChunkGenerator gen, TemplateManager manager, int chunkX, int chunkZ, Biome biome, ArenaStructure.Config config) {
            BlockPos blockpos = new BlockPos(chunkX * 16, 32, chunkZ * 16);
            ArenaStructure.Pools.init();
            JigsawGeneratorLegacy.addPieces(registry, config.toVillageConfig(), net.minecraft.world.gen.feature.structure.AbstractVillagePiece::new, gen, manager, blockpos, this.pieces, (Random) this.random, false, false);
            calculateBoundingBox();
        }
    }

    public static class Config implements IFeatureConfig {
        public static final Codec<Config> CODEC;

        static {
            CODEC = RecordCodecBuilder.create(builder -> builder.group((App) JigsawPattern.CODEC.fieldOf("start_pool").forGetter(Config::getStartPool), (App) Codec.intRange(0, 2147483647).fieldOf("size").forGetter(Config::getSize)).apply((Applicative) builder, Config::new));
        }


        private final Supplier<JigsawPattern> startPool;

        private final int size;


        public Config(Supplier<JigsawPattern> startPool, int size) {
            this.startPool = startPool;
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }

        public Supplier<JigsawPattern> getStartPool() {
            return this.startPool;
        }

        public VillageConfig toVillageConfig() {
            return new VillageConfig(getStartPool(), getSize());
        }
    }


    public static class Pools {
        public static final JigsawPattern START = JigsawPatternRegistry.register(new JigsawPattern(
                Vault.id("arena/starts"), new ResourceLocation("empty"), (List) ImmutableList.of(
                Pair.of(JigsawPiece.single(Vault.sId("arena/arena1/p_p"), ProcessorLists.EMPTY), Integer.valueOf(1))), JigsawPattern.PlacementBehaviour.RIGID));

        public static void init() {
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\structure\ArenaStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */