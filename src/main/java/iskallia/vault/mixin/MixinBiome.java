package iskallia.vault.mixin;

import iskallia.vault.Vault;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin({Biome.class})
public abstract class MixinBiome {
    @Inject(method = {"generate"}, at = {@At("HEAD")})
    public void generate(final StructureManager structureManager, final ChunkGenerator chunkGenerator, final WorldGenRegion worldGenRegion, final long seed, final SharedSeedRandom rand, final BlockPos pos, final CallbackInfo ci) {
        this.generateVault(structureManager, chunkGenerator, worldGenRegion, seed, rand, pos);
    }

    private void generateVault(final StructureManager structureManager, final ChunkGenerator chunkGenerator, final WorldGenRegion worldGenRegion, final long seed, final SharedSeedRandom rand, final BlockPos pos) {
        final ServerWorld world = worldGenRegion.getLevel();
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
        if (vault == null) {
            return;
        }
        final ChunkPos startChunk = vault.getGenerator().getStartChunk();
        if ((pos.getX() >> 4 != startChunk.x || pos.getZ() >> 4 != startChunk.z) && worldGenRegion.getLevel().getChunkSource().hasChunk(startChunk.x, startChunk.z)) {
            worldGenRegion.getLevel().getChunk(startChunk.x, startChunk.z).getAllStarts().values().forEach(start -> start.placeInChunk((ISeedReader) worldGenRegion, structureManager, chunkGenerator, (Random) rand, new MutableBoundingBox(pos.getX(), pos.getZ(), pos.getX() + 15, pos.getZ() + 15), new ChunkPos(pos)));
        } else {
            Vault.LOGGER.error("Start chunk at [" + startChunk.x + ", " + startChunk.z + "] has no ticket. Failed to generate chunk [" + (pos.getX() >> 4) + ", " + (pos.getZ() >> 4) + "].");
        }
    }
}
