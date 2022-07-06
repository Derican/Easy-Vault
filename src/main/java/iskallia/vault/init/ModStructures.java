package iskallia.vault.init;

import com.mojang.serialization.Codec;
import iskallia.vault.Vault;
import iskallia.vault.world.gen.structure.*;
import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModStructures {
    public static Structure<VaultStructure.Config> VAULT_STAR;
    public static Structure<ArchitectEventStructure.Config> ARCHITECT_EVENT;
    public static Structure<RaidChallengeStructure.Config> RAID_CHALLENGE;
    public static Structure<VaultTroveStructure.Config> VAULT_TROVE;
    public static Structure<FinalVaultLobbyStructure.Config> FINAL_VAULT_LOBBY;
    public static Structure<FinalVaultBossStructure.Config> FINAL_VAULT_BOSS;

    public static void register(final RegistryEvent.Register<Structure<?>> event) {
        ModStructures.VAULT_STAR = register(event.getRegistry(), "vault_star", new VaultStructure(VaultStructure.Config.CODEC));
        ModStructures.ARCHITECT_EVENT = register(event.getRegistry(), "architect_event", new ArchitectEventStructure(ArchitectEventStructure.Config.CODEC));
        ModStructures.RAID_CHALLENGE = register(event.getRegistry(), "raid_challenge", new RaidChallengeStructure(RaidChallengeStructure.Config.CODEC));
        ModStructures.VAULT_TROVE = register(event.getRegistry(), "trove", new VaultTroveStructure(VaultTroveStructure.Config.CODEC));
        ModStructures.FINAL_VAULT_LOBBY = register(event.getRegistry(), "final_vault_lobby", new FinalVaultLobbyStructure(FinalVaultLobbyStructure.Config.CODEC));
        ModStructures.FINAL_VAULT_BOSS = register(event.getRegistry(), "final_vault_boss", new FinalVaultBossStructure(FinalVaultBossStructure.Config.CODEC));
        PoolElements.register(event);
    }

    private static <T extends Structure<?>> T register(final IForgeRegistry<Structure<?>> registry, final String name, final T structure) {
        Structure.STRUCTURES_REGISTRY.put(name, structure);
        structure.setRegistryName(Vault.id(name));
        registry.register(structure);
        return structure;
    }

    public static class PoolElements {
        public static IJigsawDeserializer<PalettedSinglePoolElement> PALETTED_SINGLE_POOL_ELEMENT;
        public static IJigsawDeserializer<PalettedListPoolElement> PALETTED_LIST_POOL_ELEMENT;

        public static void register(final RegistryEvent.Register<Structure<?>> event) {
            PoolElements.PALETTED_SINGLE_POOL_ELEMENT = register("paletted_single_pool_element", PalettedSinglePoolElement.CODEC);
            PoolElements.PALETTED_LIST_POOL_ELEMENT = register("paletted_list_pool_element", PalettedListPoolElement.CODEC);
        }

        static <P extends JigsawPiece> IJigsawDeserializer<P> register(final String name, final Codec<P> codec) {
            return Registry.register(Registry.STRUCTURE_POOL_ELEMENT, Vault.id(name), (() -> codec));
        }
    }
}
