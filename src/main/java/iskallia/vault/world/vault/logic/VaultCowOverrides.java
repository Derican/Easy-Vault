package iskallia.vault.world.vault.logic;

import iskallia.vault.entity.AggressiveCowEntity;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.init.ModEntities;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.UUID;


public class VaultCowOverrides {
    private static final UUID DAMAGE_NERF_MULTIPLIER = UUID.fromString("384df991-f603-344c-a090-3693adfa984a");

    public static final String ENTITY_TAG = "replaced_entity";

    public static void setupVault(VaultRaid vault) {
        vault.getEvents().add(VaultRaid.REPLACE_WITH_COW);
    }

    @Nullable
    public static AggressiveCowEntity replaceVaultEntity(VaultRaid vault, LivingEntity spawned, ServerWorld world) {
        if (spawned instanceof net.minecraft.entity.monster.SilverfishEntity || spawned instanceof iskallia.vault.entity.EtchingVendorEntity || spawned instanceof iskallia.vault.entity.EternalEntity) {
            spawned.addTag("replaced_entity");
            return null;
        }
        EntityScaler.scaleVaultEntity(vault, (Entity) spawned);

        AggressiveCowEntity override = (AggressiveCowEntity) ModEntities.AGGRESSIVE_COW.create((World) world);

        AttributeModifierManager mgr = override.getAttributes();
        for (Attribute attr : ForgeRegistries.ATTRIBUTES) {
            if (spawned.getAttributes().hasAttribute(attr) && mgr.hasAttribute(attr)) {
                override.getAttribute(attr).setBaseValue(spawned.getAttributeValue(attr));
            }
        }
        mgr.getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(DAMAGE_NERF_MULTIPLIER, "Scaling Damage Reduction", 0.4D, AttributeModifier.Operation.MULTIPLY_TOTAL));


        if (spawned instanceof net.minecraft.entity.MobEntity) {
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                ItemStack has = override.getItemBySlot(slot);
                if (!has.isEmpty()) {
                    spawned.setItemSlot(slot, has.copy());
                } else {
                    spawned.setItemSlot(slot, ItemStack.EMPTY);
                }
            }
        }

        override.addTag("replaced_entity");
        return override;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\VaultCowOverrides.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */