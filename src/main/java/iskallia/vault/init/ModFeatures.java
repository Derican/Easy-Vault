package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.decorator.*;
import iskallia.vault.world.gen.structure.ArchitectEventStructure;
import iskallia.vault.world.gen.structure.RaidChallengeStructure;
import iskallia.vault.world.gen.structure.VaultStructure;
import iskallia.vault.world.gen.structure.VaultTroveStructure;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.RegistryEvent;

public class ModFeatures {
    public static VaultFeature VAULT_FEATURE;
    public static ArchitectEventFeature ARCHITECT_EVENT_FEATURE;
    public static RaidChallengeFeature RAID_CHALLENGE_FEATURE;
    public static VaultTroveFeature VAULT_TROVE_FEATURE;
    public static ConfiguredFeature<?, ?> BREADCRUMB_CHEST;
    public static ConfiguredFeature<?, ?> VAULT_ROCK_ORE;

    public static void registerStructureFeatures() {
        ModFeatures.VAULT_FEATURE = register("vault", new VaultFeature(ModStructures.VAULT_STAR, new VaultStructure.Config(() -> VaultStructure.Pools.FINAL_START, 11)));
        ModFeatures.ARCHITECT_EVENT_FEATURE = register("architect_event", new ArchitectEventFeature(ModStructures.ARCHITECT_EVENT, new ArchitectEventStructure.Config(() -> ArchitectEventStructure.Pools.START, 1)));
        ModFeatures.RAID_CHALLENGE_FEATURE = register("raid_challenge", new RaidChallengeFeature(ModStructures.RAID_CHALLENGE, new RaidChallengeStructure.Config(() -> RaidChallengeStructure.Pools.START, 1)));
        ModFeatures.VAULT_TROVE_FEATURE = register("trove", new VaultTroveFeature(ModStructures.VAULT_TROVE, new VaultTroveStructure.Config(() -> VaultTroveStructure.Pools.START, 1)));
    }

    public static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
        BreadcrumbFeature.register(event);
        OverworldOreFeature.register(event);
        ModFeatures.BREADCRUMB_CHEST = register("breadcrumb_chest", (net.minecraft.world.gen.feature.ConfiguredFeature<?, ?>) BreadcrumbFeature.INSTANCE.configured(NoFeatureConfig.INSTANCE));
        ModFeatures.VAULT_ROCK_ORE = register("vault_rock_ore", (net.minecraft.world.gen.feature.ConfiguredFeature<?, ?>) OverworldOreFeature.INSTANCE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.VAULT_ROCK_ORE.defaultBlockState(), 1)).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(5, 0, 6))).squared());
    }

    private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(final String name, final ConfiguredFeature<FC, F> feature) {
        return (ConfiguredFeature<FC, F>) WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, Vault.id(name), feature);
    }

    private static <SF extends StructureFeature<FC, F>, FC extends IFeatureConfig, F extends Structure<FC>> SF register(final String name, final SF feature) {
        return (SF) WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, Vault.id(name), feature);
    }
}
