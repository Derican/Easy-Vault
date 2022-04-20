package iskallia.vault.container.slot.player;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
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

public class ArmorEditSlot extends Slot {
    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES;
    private final EquipmentSlotType slotType;

    public ArmorEditSlot(final IInventory inventory, final EquipmentSlotType slotType, final int index, final int xPosition, final int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.slotType = slotType;
    }

    public int getMaxStackSize() {
        return 1;
    }

    public boolean mayPlace(final ItemStack stack) {
        try {
            return stack.canEquip(this.slotType, (Entity) null);
        } catch (final Exception exc) {
            return MobEntity.getEquipmentSlotForItem(stack) == this.slotType;
        }
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        if (this.slotType.getType() != EquipmentSlotType.Group.ARMOR) {
            return null;
        }
        return (Pair<ResourceLocation, ResourceLocation>) Pair.of(PlayerContainer.BLOCK_ATLAS, ArmorEditSlot.ARMOR_SLOT_TEXTURES[this.slotType.getIndex()]);
    }

    static {
        ARMOR_SLOT_TEXTURES = new ResourceLocation[]{PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS, PlayerContainer.EMPTY_ARMOR_SLOT_LEGGINGS, PlayerContainer.EMPTY_ARMOR_SLOT_CHESTPLATE, PlayerContainer.EMPTY_ARMOR_SLOT_HELMET};
    }
}
