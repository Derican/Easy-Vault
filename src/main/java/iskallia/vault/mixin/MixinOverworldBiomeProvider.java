package iskallia.vault.mixin;

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

    public void setSeed(long seed) {
        this.seed = seed;
        this.noiseBiomeLayer = LayerUtil.getDefaultLayer(this.seed, this.legacyBiomeInitLayer, this.largeBiomes ? 6 : 4, 4);
    }

    @Shadow
    @Final
    @Mutable
    private boolean largeBiomes;
    @Shadow
    @Final
    @Mutable
    private Layer noiseBiomeLayer;

    public void setLegacyBiomes(boolean legacyBiomes) {
        this.legacyBiomeInitLayer = legacyBiomes;
        this.noiseBiomeLayer = LayerUtil.getDefaultLayer(this.seed, this.legacyBiomeInitLayer, this.largeBiomes ? 6 : 4, 4);
    }


    public void setLargeBiomes(boolean largeBiomes) {
        this.largeBiomes = largeBiomes;
        this.noiseBiomeLayer = LayerUtil.getDefaultLayer(this.seed, this.legacyBiomeInitLayer, this.largeBiomes ? 6 : 4, 4);
    }


    public long getSeed() {
        return this.seed;
    }


    public boolean getLegacyBiomes() {
        return this.legacyBiomeInitLayer;
    }


    public boolean getLargeBiomes() {
        return this.largeBiomes;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinOverworldBiomeProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */