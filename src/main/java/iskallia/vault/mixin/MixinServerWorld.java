package iskallia.vault.mixin;

import iskallia.vault.Vault;
import iskallia.vault.util.IBiomeUpdate;
import net.minecraft.profiler.IProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.ISpawnWorldInfo;
import net.minecraft.world.storage.SaveFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;


@Mixin({ServerWorld.class})
public abstract class MixinServerWorld
        extends World {
    protected MixinServerWorld(ISpawnWorldInfo worldInfo, RegistryKey<World> dimension, DimensionType dimensionType, Supplier<IProfiler> profiler, boolean isRemote, boolean isDebug, long seed) {
        super(worldInfo, dimension, dimensionType, profiler, isRemote, isDebug, seed);
    }


    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    public void ctor(MinecraftServer server, Executor executor, SaveFormat.LevelSave save, IServerWorldInfo info, RegistryKey<World> key, DimensionType type, IChunkStatusListener listener, ChunkGenerator gen, boolean p_i241885_9_, long p_i241885_10_, List<ISpecialSpawner> spawners, boolean p_i241885_13_, CallbackInfo ci) {
        if (key == Vault.OTHER_SIDE_KEY) {
            ((IBiomeUpdate) getChunkSource().getGenerator())
                    .update(getServer().getLevel(World.OVERWORLD).getChunkSource().getGenerator().getBiomeSource());
        }
    }

    @Inject(method = {"tickEnvironment"}, at = {@At("HEAD")}, cancellable = true)
    public void tickEnvironment(Chunk chunk, int randomTickSpeed, CallbackInfo ci) {
        if (dimension() == Vault.VAULT_KEY)
            ci.cancel();
    }

    @Shadow
    public abstract ServerChunkProvider getChunkSource();

    @Shadow
    @Nonnull
    public abstract MinecraftServer getServer();
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinServerWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */