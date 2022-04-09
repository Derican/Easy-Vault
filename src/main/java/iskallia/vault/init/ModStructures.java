// 
// Decompiled by Procyon v0.6.0
// 

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
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModStructures
{
    public static Structure<VaultStructure.Config> VAULT_STAR;
    public static Structure<ArchitectEventStructure.Config> ARCHITECT_EVENT;
    public static Structure<RaidChallengeStructure.Config> RAID_CHALLENGE;
    public static Structure<VaultTroveStructure.Config> VAULT_TROVE;
    
    public static void register(final RegistryEvent.Register<Structure<?>> event) {
        ModStructures.VAULT_STAR = register((IForgeRegistry<Structure<?>>)event.getRegistry(), "vault_star", new VaultStructure(VaultStructure.Config.CODEC));
        ModStructures.ARCHITECT_EVENT = register((IForgeRegistry<Structure<?>>)event.getRegistry(), "architect_event", new ArchitectEventStructure(ArchitectEventStructure.Config.CODEC));
        ModStructures.RAID_CHALLENGE = register((IForgeRegistry<Structure<?>>)event.getRegistry(), "raid_challenge", new RaidChallengeStructure(RaidChallengeStructure.Config.CODEC));
        ModStructures.VAULT_TROVE = register((IForgeRegistry<Structure<?>>)event.getRegistry(), "trove", new VaultTroveStructure(VaultTroveStructure.Config.CODEC));
        PoolElements.register(event);
    }
    
    private static <T extends Structure<?>> T register(final IForgeRegistry<Structure<?>> registry, final String name, final T structure) {
        Structure.STRUCTURES_REGISTRY.put(name, structure);
        structure.setRegistryName(Vault.id(name));
        registry.register(structure);
        return structure;
    }
    
    public static class PoolElements
    {
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
