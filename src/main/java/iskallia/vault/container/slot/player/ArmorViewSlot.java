package iskallia.vault.container.slot.player;

import com.mojang.datafixers.util.Pair;
import iskallia.vault.container.slot.ReadOnlySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArmorViewSlot
        extends ReadOnlySlot {
    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{PlayerContainer.EMPTY_ARMOR_SLOT_HELMET, PlayerContainer.EMPTY_ARMOR_SLOT_CHESTPLATE, PlayerContainer.EMPTY_ARMOR_SLOT_LEGGINGS, PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS};


    private final EquipmentSlotType equipmentSlotType;


    public ArmorViewSlot(PlayerEntity player, EquipmentSlotType equipmentSlotType, int xPosition, int yPosition) {
        super((IInventory) player.inventory, 39 - equipmentSlotType.getIndex(), xPosition, yPosition);
        this.equipmentSlotType = equipmentSlotType;
    }

    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(PlayerContainer.BLOCK_ATLAS, ARMOR_SLOT_TEXTURES[this.equipmentSlotType.getIndex()]);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\player\ArmorViewSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */