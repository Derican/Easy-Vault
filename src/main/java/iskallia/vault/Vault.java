package iskallia.vault;

import iskallia.vault.dump.VaultDataDump;
import iskallia.vault.init.*;
import iskallia.vault.integration.IntegrationDankStorage;
import iskallia.vault.util.ServerScheduler;
import iskallia.vault.util.scheduler.DailyScheduler;
import iskallia.vault.world.data.*;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import iskallia.vault.world.vault.event.VaultListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("the_vault")
public class Vault {
    public static final String MOD_ID = "the_vault";
    public static final Logger LOGGER = LogManager.getLogger();

    public static RegistryKey<World> VAULT_KEY = RegistryKey.create(Registry.DIMENSION_REGISTRY, id("vault"));
    public static RegistryKey<World> OTHER_SIDE_KEY = RegistryKey.create(Registry.DIMENSION_REGISTRY, id("the_other_side"));

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
        registerDeferredRegistries();
        ModCommands.registerArgumentTypes();

        if (ModList.get().isLoaded("dankstorage")) {
            MinecraftForge.EVENT_BUS.register(IntegrationDankStorage.class);
        }
    }

    public void registerDeferredRegistries() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModParticles.REGISTRY.register(modEventBus);
        ModFluids.REGISTRY.register(modEventBus);
        ModPotions.REGISTRY.register(modEventBus);
    }

    public void onCommandRegister(RegisterCommandsEvent event) {
        ModCommands.registerCommands(event.getDispatcher(), event.getEnvironment());
    }

    public void onBiomeLoad(BiomeLoadingEvent event) {
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModFeatures.VAULT_ROCK_ORE);
    }

    public void onBiomeLoadPost(BiomeLoadingEvent event) {
        if (event.getName().equals(id("spoopy"))) {
            for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
                event.getGeneration().getFeatures(stage).clear();
            }
            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, ModFeatures.BREADCRUMB_CHEST);
        }
    }

    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ServerWorld serverWorld = player.getLevel();
        MinecraftServer server = player.getServer();

        PlayerVaultStatsData.get(serverWorld).getVaultStats((PlayerEntity) player).sync(server);
        PlayerResearchesData.get(serverWorld).getResearches((PlayerEntity) player).sync(server);
        PlayerAbilitiesData.get(serverWorld).getAbilities((PlayerEntity) player).sync(server);
        PlayerTalentsData.get(serverWorld).getTalents((PlayerEntity) player).sync(server);
        EternalsData.get(serverWorld).syncTo(player);
        SoulShardTraderData.get(serverWorld).syncTo(player);
        ModConfigs.SOUL_SHARD.syncTo(player);

        GlobalDifficultyData.get(serverWorld).openDifficultySelection(player);
    }

    public static String sId(String name) {
        return "the_vault:" + name;
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation("the_vault", name);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\Vault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */