// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.init;

import iskallia.vault.client.particles.AltarFlameParticle;
import iskallia.vault.client.particles.DepthFireworkParticle;
import iskallia.vault.client.particles.RaidCubeParticle;
import iskallia.vault.client.particles.StabilizerCubeParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "the_vault", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles
{
    public static final DeferredRegister<ParticleType<?>> REGISTRY;
    public static final RegistryObject<BasicParticleType> GREEN_FLAME;
    public static final RegistryObject<BasicParticleType> BLUE_FLAME;
    public static final RegistryObject<BasicParticleType> RED_FLAME;
    public static final RegistryObject<BasicParticleType> YELLOW_FLAME;
    public static final RegistryObject<BasicParticleType> DEPTH_FIREWORK;
    public static final RegistryObject<BasicParticleType> STABILIZER_CUBE;
    public static final RegistryObject<BasicParticleType> RAID_EFFECT_CUBE;
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(final ParticleFactoryRegisterEvent event) {
        final ParticleManager particleManager = Minecraft.getInstance().particleEngine;
        particleManager.register((ParticleType)ModParticles.GREEN_FLAME.get(), AltarFlameParticle.Factory::new);
        particleManager.register((ParticleType)ModParticles.BLUE_FLAME.get(), AltarFlameParticle.Factory::new);
        particleManager.register((ParticleType)ModParticles.RED_FLAME.get(), AltarFlameParticle.Factory::new);
        particleManager.register((ParticleType)ModParticles.YELLOW_FLAME.get(), AltarFlameParticle.Factory::new);
        particleManager.register((ParticleType)ModParticles.DEPTH_FIREWORK.get(), DepthFireworkParticle.Factory::new);
        particleManager.register((ParticleType)ModParticles.STABILIZER_CUBE.get(), StabilizerCubeParticle.Factory::new);
        particleManager.register((ParticleType)ModParticles.RAID_EFFECT_CUBE.get(), RaidCubeParticle.Factory::new);
    }
    
    static {
        REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "the_vault");
        GREEN_FLAME = ModParticles.REGISTRY.register("green_flame", () -> new BasicParticleType(true));
        BLUE_FLAME = ModParticles.REGISTRY.register("blue_flame", () -> new BasicParticleType(true));
        RED_FLAME = ModParticles.REGISTRY.register("red_flame", () -> new BasicParticleType(true));
        YELLOW_FLAME = ModParticles.REGISTRY.register("yellow_flame", () -> new BasicParticleType(true));
        DEPTH_FIREWORK = ModParticles.REGISTRY.register("depth_ignoring_firework", () -> new BasicParticleType(true));
        STABILIZER_CUBE = ModParticles.REGISTRY.register("stabilizer_cube", () -> new BasicParticleType(true));
        RAID_EFFECT_CUBE = ModParticles.REGISTRY.register("raid_cube", () -> new BasicParticleType(true));
    }
}
