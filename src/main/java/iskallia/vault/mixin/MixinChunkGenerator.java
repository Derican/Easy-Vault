package iskallia.vault.mixin;

import iskallia.vault.util.IBiomeGen;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ChunkGenerator.class})
public class MixinChunkGenerator
        implements IBiomeGen {
    @Shadow
    @Final
    protected BiomeProvider biomeSource;

    public BiomeProvider getProvider1() {
        return this.biomeSource;
    }

    @Shadow
    @Final
    protected BiomeProvider runtimeBiomeSource;

    public BiomeProvider getProvider2() {
        return this.runtimeBiomeSource;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinChunkGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */