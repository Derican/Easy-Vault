package iskallia.vault.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@EventBusSubscriber(modid = "the_vault", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "the_vault");

    public static final RegistryObject<BasicParticleType> GREEN_FLAME = REGISTRY.register("green_flame", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> BLUE_FLAME = REGISTRY.register("blue_flame", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> RED_FLAME = REGISTRY.register("red_flame", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> YELLOW_FLAME = REGISTRY.register("yellow_flame", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> DEPTH_FIREWORK = REGISTRY.register("depth_ignoring_firework", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> STABILIZER_CUBE = REGISTRY.register("stabilizer_cube", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> RAID_EFFECT_CUBE = REGISTRY.register("raid_cube", () -> new BasicParticleType(true));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        ParticleManager particleManager = (Minecraft.getInstance()).particleEngine;
        particleManager.register((ParticleType) GREEN_FLAME.get(), iskallia.vault.client.particles.AltarFlameParticle.Factory::new);
        particleManager.register((ParticleType) BLUE_FLAME.get(), iskallia.vault.client.particles.AltarFlameParticle.Factory::new);
        particleManager.register((ParticleType) RED_FLAME.get(), iskallia.vault.client.particles.AltarFlameParticle.Factory::new);
        particleManager.register((ParticleType) YELLOW_FLAME.get(), iskallia.vault.client.particles.AltarFlameParticle.Factory::new);
        particleManager.register((ParticleType) DEPTH_FIREWORK.get(), iskallia.vault.client.particles.DepthFireworkParticle.Factory::new);
        particleManager.register((ParticleType) STABILIZER_CUBE.get(), iskallia.vault.client.particles.StabilizerCubeParticle.Factory::new);
        particleManager.register((ParticleType) RAID_EFFECT_CUBE.get(), iskallia.vault.client.particles.RaidCubeParticle.Factory::new);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModParticles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */