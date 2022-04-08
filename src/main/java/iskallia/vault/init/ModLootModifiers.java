package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.loot.LootModifierAutoSmelt;
import iskallia.vault.loot.LootModifierDestructive;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModLootModifiers {
    public static void registerGlobalModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        IForgeRegistry<GlobalLootModifierSerializer<?>> registry = event.getRegistry();

        registry.register( (new LootModifierAutoSmelt.Serializer()).setRegistryName(Vault.id("paxel_auto_smelt")));
        registry.register( (new LootModifierDestructive.Serializer()).setRegistryName(Vault.id("paxel_destructive")));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModLootModifiers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */