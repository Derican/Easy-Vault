// 
// Decompiled by Procyon v0.6.0
// 

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

@Mixin({ ServerWorld.class })
public abstract class MixinServerWorld extends World
{
    @Shadow
    public abstract ServerChunkProvider getChunkSource();
    
    @Shadow
    @Nonnull
    public abstract MinecraftServer getServer();
    
    protected MixinServerWorld(final ISpawnWorldInfo worldInfo, final RegistryKey<World> dimension, final DimensionType dimensionType, final Supplier<IProfiler> profiler, final boolean isRemote, final boolean isDebug, final long seed) {
        super(worldInfo, (RegistryKey)dimension, dimensionType, (Supplier)profiler, isRemote, isDebug, seed);
    }
    
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    public void ctor(final MinecraftServer server, final Executor executor, final SaveFormat.LevelSave save, final IServerWorldInfo info, final RegistryKey<World> key, final DimensionType type, final IChunkStatusListener listener, final ChunkGenerator gen, final boolean p_i241885_9_, final long p_i241885_10_, final List<ISpecialSpawner> spawners, final boolean p_i241885_13_, final CallbackInfo ci) {
        if (key == Vault.OTHER_SIDE_KEY) {
            ((IBiomeUpdate)this.getChunkSource().getGenerator()).update(this.getServer().getLevel(World.OVERWORLD).getChunkSource().getGenerator().getBiomeSource());
        }
    }
    
    @Inject(method = { "tickChunk" }, at = { @At("HEAD") }, cancellable = true)
    public void tickEnvironment(final Chunk chunk, final int randomTickSpeed, final CallbackInfo ci) {
        if (this.dimension() == Vault.VAULT_KEY) {
            ci.cancel();
        }
    }
}
