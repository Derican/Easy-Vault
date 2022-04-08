package iskallia.vault.container.slot.player;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;


public class ArmorEditSlot
        extends Slot {
    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS, PlayerContainer.EMPTY_ARMOR_SLOT_LEGGINGS, PlayerContainer.EMPTY_ARMOR_SLOT_CHESTPLATE, PlayerContainer.EMPTY_ARMOR_SLOT_HELMET};


    private final EquipmentSlotType slotType;


    public ArmorEditSlot(IInventory inventory, EquipmentSlotType slotType, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.slotType = slotType;
    }


    public int getMaxStackSize() {
        return 1;
    }


    public boolean mayPlace(ItemStack stack) {
        try {
            return stack.canEquip(this.slotType, null);
        } catch (Exception exc) {
            return (MobEntity.getEquipmentSlotForItem(stack) == this.slotType);
        }
    }


    @Nullable
    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        if (this.slotType.getType() != EquipmentSlotType.Group.ARMOR) {
            return null;
        }
        return Pair.of(PlayerContainer.BLOCK_ATLAS, ARMOR_SLOT_TEXTURES[this.slotType.getIndex()]);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\player\ArmorEditSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */