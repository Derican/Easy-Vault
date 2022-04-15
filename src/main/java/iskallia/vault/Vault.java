// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault;

import com.mojang.brigadier.CommandDispatcher;
import iskallia.vault.dump.VaultDataDump;
import iskallia.vault.init.*;
import iskallia.vault.util.ServerScheduler;
import iskallia.vault.util.scheduler.DailyScheduler;
import iskallia.vault.world.data.*;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import iskallia.vault.world.vault.event.VaultListener;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("the_vault")
public class Vault {
    public static final String MOD_ID = "the_vault";
    public static final Logger LOGGER;
    public static RegistryKey<World> VAULT_KEY;
    public static RegistryKey<World> OTHER_SIDE_KEY;

    public Vault() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onCommandRegister);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::onBiomeLoad);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onBiomeLoadPost);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onPlayerLoggedIn);
        MinecraftForge.EVENT_BUS.addListener(VaultListener::onEvent);
        MinecraftForge.EVENT_BUS.addListener(ServerScheduler.INSTANCE::onServerTick);
        MinecraftForge.EVENT_BUS.addListener(VaultJigsawHelper::preloadVaultRooms);
        MinecraftForge.EVENT_BUS.addListener(PlayerVaultStatsData::onStartup);
        MinecraftForge.EVENT_BUS.addListener(DailyScheduler::start);
        MinecraftForge.EVENT_BUS.addListener(DailyScheduler::stop);
        MinecraftForge.EVENT_BUS.addListener(VaultDataDump::onStart);
        this.registerDeferredRegistries();
        ModCommands.registerArgumentTypes();
    }

    public void registerDeferredRegistries() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModParticles.REGISTRY.register(modEventBus);
        ModFluids.REGISTRY.register(modEventBus);
        ModPotions.REGISTRY.register(modEventBus);
    }

    public void onCommandRegister(final RegisterCommandsEvent event) {
        ModCommands.registerCommands((CommandDispatcher<CommandSource>) event.getDispatcher(), event.getEnvironment());
    }

    public void onBiomeLoad(final BiomeLoadingEvent event) {
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, (ConfiguredFeature) ModFeatures.VAULT_ROCK_ORE);
    }

    public void onBiomeLoadPost(final BiomeLoadingEvent event) {
        if (event.getName().equals(id("spoopy"))) {
            for (final GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
                event.getGeneration().getFeatures(stage).clear();
            }
            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, (ConfiguredFeature) ModFeatures.BREADCRUMB_CHEST);
        }
    }

    public void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        final ServerWorld serverWorld = player.getLevel();
        final MinecraftServer server = player.getServer();
        PlayerVaultStatsData.get(serverWorld).getVaultStats((PlayerEntity) player).sync(server);
        PlayerAbilitiesData.get(serverWorld).getAbilities((PlayerEntity) player).sync(server);
        PlayerTalentsData.get(serverWorld).getTalents((PlayerEntity) player).sync(server);
        EternalsData.get(serverWorld).syncTo(player);
        SoulShardTraderData.get(serverWorld).syncTo(player);
        ModConfigs.SOUL_SHARD.syncTo(player);
        GlobalDifficultyData.get(serverWorld).openDifficultySelection(player);
    }

    public static String sId(final String name) {
        return "the_vault:" + name;
    }

    public static ResourceLocation id(final String name) {
        return new ResourceLocation("the_vault", name);
    }

    static {
        LOGGER = LogManager.getLogger();
        Vault.VAULT_KEY = (RegistryKey<World>) RegistryKey.create(Registry.DIMENSION_REGISTRY, id("vault"));
        Vault.OTHER_SIDE_KEY = (RegistryKey<World>) RegistryKey.create(Registry.DIMENSION_REGISTRY, id("the_other_side"));
    }
}
