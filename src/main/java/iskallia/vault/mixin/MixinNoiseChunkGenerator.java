package iskallia.vault.mixin;

import iskallia.vault.util.IBiomeAccessor;
import iskallia.vault.util.IBiomeGen;
import iskallia.vault.util.IBiomeUpdate;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.settings.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Mixin({NoiseChunkGenerator.class})
public class MixinNoiseChunkGenerator implements IBiomeUpdate {
    @Mutable
    @Shadow
    @Final
    protected SharedSeedRandom random;
    @Mutable
    @Shadow
    @Final
    private OctavesNoiseGenerator minLimitPerlinNoise;
    @Mutable
    @Shadow
    @Final
    private OctavesNoiseGenerator maxLimitPerlinNoise;
    @Mutable
    @Shadow
    @Final
    private OctavesNoiseGenerator mainPerlinNoise;
    @Mutable
    @Shadow
    @Final
    private INoiseGenerator surfaceNoise;
    @Mutable
    @Shadow
    @Final
    private OctavesNoiseGenerator depthNoise;
    @Mutable
    @Shadow
    @Final
    private SimplexNoiseGenerator islandNoise;
    @Shadow
    @Final
    protected Supplier<DimensionSettings> settings;

    @Override
    public void update(final BiomeProvider source) {
        if (!(source instanceof OverworldBiomeProvider)) {
            return;
        }
        final IBiomeAccessor s = (IBiomeAccessor) source;
        if (((IBiomeGen) this).getProvider1() instanceof OverworldBiomeProvider) {
            final OverworldBiomeProvider owSource = (OverworldBiomeProvider) ((IBiomeGen) this).getProvider1();
            ((IBiomeAccessor) owSource).setSeed(s.getSeed());
            ((IBiomeAccessor) owSource).setLegacyBiomes(s.getLegacyBiomes());
            ((IBiomeAccessor) owSource).setLargeBiomes(s.getLargeBiomes());
        }
        if (((IBiomeGen) this).getProvider2() instanceof OverworldBiomeProvider) {
            final OverworldBiomeProvider owSource = (OverworldBiomeProvider) ((IBiomeGen) this).getProvider2();
            ((IBiomeAccessor) owSource).setSeed(s.getSeed());
            ((IBiomeAccessor) owSource).setLegacyBiomes(s.getLegacyBiomes());
            ((IBiomeAccessor) owSource).setLargeBiomes(s.getLargeBiomes());
        }
        final NoiseSettings noiseSettings = this.settings.get().noiseSettings();
        this.random = new SharedSeedRandom(s.getSeed());
        this.minLimitPerlinNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
        this.maxLimitPerlinNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
        this.mainPerlinNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-7, 0));
        this.surfaceNoise = (INoiseGenerator) (noiseSettings.useSimplexSurfaceNoise() ? new PerlinNoiseGenerator(this.random, IntStream.rangeClosed(-3, 0)) : new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-3, 0)));
        this.random.consumeCount(2620);
        this.depthNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
        if (noiseSettings.islandNoiseOverride()) {
            final SharedSeedRandom sharedseedrandom = new SharedSeedRandom(s.getSeed());
            sharedseedrandom.consumeCount(17292);
            this.islandNoise = new SimplexNoiseGenerator((Random) sharedseedrandom);
        } else {
            this.islandNoise = null;
        }
    }
}
