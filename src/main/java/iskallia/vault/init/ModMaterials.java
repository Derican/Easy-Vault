package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModMaterials {
    public static final ResourceLocation VAULT_CHEST_LOCATION = Vault.id("entity/chest/vault_chest");
    public static final ResourceLocation VAULT_TREASURE_CHEST_LOCATION = Vault.id("entity/chest/vault_treasure_chest");
    public static final ResourceLocation VAULT_ALTER_CHEST_LOCATION = Vault.id("entity/chest/vault_altar_chest");
    public static final ResourceLocation VAULT_COOP_CHEST_LOCATION = Vault.id("entity/chest/vault_coop_chest");
    public static final ResourceLocation VAULT_BONUS_CHEST_LOCATION = Vault.id("entity/chest/vault_bonus_chest");
    public static final ResourceLocation SCAVENGER_CHEST_LOCATION = Vault.id("entity/chest/scavenger_chest");

    @SubscribeEvent
    public static void registerMaterials(TextureStitchEvent.Pre event){
        event.addSprite(VAULT_CHEST_LOCATION);
        event.addSprite(VAULT_TREASURE_CHEST_LOCATION);
        event.addSprite(VAULT_ALTER_CHEST_LOCATION);
        event.addSprite(VAULT_COOP_CHEST_LOCATION);
        event.addSprite(VAULT_BONUS_CHEST_LOCATION);
        event.addSprite(SCAVENGER_CHEST_LOCATION);
    }

}
