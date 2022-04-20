package iskallia.vault.mixin;

import iskallia.vault.util.IBiomeGen;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ChunkGenerator.class})
public class MixinChunkGenerator implements IBiomeGen {
    @Shadow
    @Final
    protected BiomeProvider biomeSource;
    @Shadow
    @Final
    protected BiomeProvider runtimeBiomeSource;

    @Override
    public BiomeProvider getProvider1() {
        return this.biomeSource;
    }

    @Override
    public BiomeProvider getProvider2() {
        return this.runtimeBiomeSource;
    }
}
