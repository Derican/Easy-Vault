// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.loot.LootModifierAutoSmelt;
import iskallia.vault.loot.LootModifierDestructive;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModLootModifiers
{
    public static void registerGlobalModifiers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        final IForgeRegistry<GlobalLootModifierSerializer<?>> registry = (IForgeRegistry<GlobalLootModifierSerializer<?>>)event.getRegistry();
        registry.register(new LootModifierAutoSmelt.Serializer().setRegistryName(Vault.id("paxel_auto_smelt")));
        registry.register(new LootModifierDestructive.Serializer().setRegistryName(Vault.id("paxel_destructive")));
    }
}
