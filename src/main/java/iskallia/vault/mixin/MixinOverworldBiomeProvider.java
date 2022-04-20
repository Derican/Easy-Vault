package iskallia.vault.mixin;

import iskallia.vault.util.IBiomeAccessor;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({OverworldBiomeProvider.class})
public class MixinOverworldBiomeProvider implements IBiomeAccessor {
    @Shadow
    @Final
    @Mutable
    private long seed;
    @Shadow
    @Final
    @Mutable
    private boolean legacyBiomeInitLayer;
    @Shadow
    @Final
    @Mutable
    private boolean largeBiomes;
    @Shadow
    @Final
    @Mutable
    private Layer noiseBiomeLayer;

    @Override
    public void setSeed(final long seed) {
        this.seed = seed;
        this.noiseBiomeLayer = LayerUtil.getDefaultLayer(this.seed, this.legacyBiomeInitLayer, this.largeBiomes ? 6 : 4, 4);
    }

    @Override
    public void setLegacyBiomes(final boolean legacyBiomes) {
        this.legacyBiomeInitLayer = legacyBiomes;
        this.noiseBiomeLayer = LayerUtil.getDefaultLayer(this.seed, this.legacyBiomeInitLayer, this.largeBiomes ? 6 : 4, 4);
    }

    @Override
    public void setLargeBiomes(final boolean largeBiomes) {
        this.largeBiomes = largeBiomes;
        this.noiseBiomeLayer = LayerUtil.getDefaultLayer(this.seed, this.legacyBiomeInitLayer, this.largeBiomes ? 6 : 4, 4);
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    @Override
    public boolean getLegacyBiomes() {
        return this.legacyBiomeInitLayer;
    }

    @Override
    public boolean getLargeBiomes() {
        return this.largeBiomes;
    }
}
