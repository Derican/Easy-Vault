// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import iskallia.vault.Vault;
import iskallia.vault.config.Config;
import iskallia.vault.world.gen.VaultJigsawGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class VaultStructure extends Structure<VaultStructure.Config>
{
    public static final int START_Y = 19;
    
    public VaultStructure(final Codec<Config> config) {
        super((Codec)config);
    }
    
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }
    
    public IStartFactory<Config> getStartFactory() {
        return ((p_242778_1_, p_242778_2_, p_242778_3_, p_242778_4_, p_242778_5_, p_242778_6_) -> new Start(this, p_242778_2_, p_242778_3_, p_242778_4_, p_242778_5_, p_242778_6_));
    }
    
    public static class Start extends MarginedStructureStart<Config>
    {
        private final VaultStructure structure;
        
        public Start(final VaultStructure structure, final int chunkX, final int chunkZ, final MutableBoundingBox box, final int references, final long worldSeed) {
            super((Structure)structure, chunkX, chunkZ, box, references, worldSeed);
            this.structure = structure;
        }
        
        public void generatePieces(final DynamicRegistries registry, final ChunkGenerator gen, final TemplateManager manager, final int chunkX, final int chunkZ, final Biome biome, final Config config) {
            final BlockPos blockpos = new BlockPos(chunkX * 16, 19, chunkZ * 16);
            Pools.init();
            JigsawGeneratorLegacy.addPieces(registry, config.toVillageConfig(), AbstractVillagePiece::new, gen, manager, blockpos, this.pieces, (Random)this.random, false, false);
            this.calculateBoundingBox();
        }
        
        public void generate(final VaultJigsawGenerator jigsaw, final DynamicRegistries registry, final ChunkGenerator gen, final TemplateManager manager) {
            Pools.init();
            jigsaw.generate(registry, new Config(() -> registry.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(Vault.id("vault/starts")), 11).toVillageConfig(), AbstractVillagePiece::new, gen, manager, this.pieces, (Random)this.random, false, false);
            this.calculateBoundingBox();
        }
    }
    
    public static class Config implements IFeatureConfig
    {
        public static final Codec<Config> CODEC;
        private final Supplier<JigsawPattern> startPool;
        private final int size;
        
        public Config(final Supplier<JigsawPattern> startPool, final int size) {
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
            return new VillageConfig((Supplier)this.getStartPool(), this.getSize());
        }
        
        static {
            CODEC = RecordCodecBuilder.create(builder -> builder.group(JigsawPattern.CODEC.fieldOf("start_pool").forGetter(Config::getStartPool), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("size").forGetter(Config::getSize)).apply(builder, Config::new));
        }
    }
    
    public static class Pools
    {
        public static final JigsawPattern FINAL_START;
        
        public static void init() {
        }
        
        static {
            FINAL_START = JigsawPatternRegistry.register(new JigsawPattern(Vault.id("final_vault/start"), new ResourceLocation("empty"), (List)ImmutableList.of(Pair.of(JigsawPiece.single(Vault.sId("final_vault/start"), ProcessorLists.EMPTY), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        }
    }
}
