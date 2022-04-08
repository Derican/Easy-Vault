package iskallia.vault.init;

import com.mojang.serialization.Codec;
import iskallia.vault.Vault;
import iskallia.vault.world.gen.structure.ArchitectEventStructure;
import iskallia.vault.world.gen.structure.RaidChallengeStructure;
import iskallia.vault.world.gen.structure.VaultStructure;
import iskallia.vault.world.gen.structure.VaultTroveStructure;
import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModStructures {
    public static Structure<VaultStructure.Config> VAULT_STAR;
    public static Structure<ArchitectEventStructure.Config> ARCHITECT_EVENT;

    public static void register(RegistryEvent.Register<Structure<?>> event) {
        VAULT_STAR = register(event.getRegistry(), "vault_star", new VaultStructure(VaultStructure.Config.CODEC));
        ARCHITECT_EVENT = register(event.getRegistry(), "architect_event", new ArchitectEventStructure(ArchitectEventStructure.Config.CODEC));
        RAID_CHALLENGE = register(event.getRegistry(), "raid_challenge", new RaidChallengeStructure(RaidChallengeStructure.Config.CODEC));
        VAULT_TROVE = register(event.getRegistry(), "trove", new VaultTroveStructure(VaultTroveStructure.Config.CODEC));
        PoolElements.register(event);
    }

    public static Structure<RaidChallengeStructure.Config> RAID_CHALLENGE;
    public static Structure<VaultTroveStructure.Config> VAULT_TROVE;

    private static <T extends Structure<?>> T register(IForgeRegistry<Structure<?>> registry, String name, T structure) {
        Structure.STRUCTURES_REGISTRY.put(name, structure);
        structure.setRegistryName(Vault.id(name));
        registry.register( structure);
        return structure;
    }

    public static class PoolElements {
        public static IJigsawDeserializer<PalettedSinglePoolElement> PALETTED_SINGLE_POOL_ELEMENT;
        public static IJigsawDeserializer<PalettedListPoolElement> PALETTED_LIST_POOL_ELEMENT;

        public static void register(RegistryEvent.Register<Structure<?>> event) {
            PALETTED_SINGLE_POOL_ELEMENT = register("paletted_single_pool_element", PalettedSinglePoolElement.CODEC);
            PALETTED_LIST_POOL_ELEMENT = register("paletted_list_pool_element", PalettedListPoolElement.CODEC);
        }

        static <P extends net.minecraft.world.gen.feature.jigsaw.JigsawPiece> IJigsawDeserializer<P> register(String name, Codec<P> codec) {
            return (IJigsawDeserializer<P>) Registry.register(Registry.STRUCTURE_POOL_ELEMENT, Vault.id(name), () -> codec);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModStructures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */