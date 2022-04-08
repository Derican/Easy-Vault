package iskallia.vault.container.slot.player;

import com.mojang.datafixers.util.Pair;
import iskallia.vault.container.slot.ReadOnlySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OffHandSlot extends ReadOnlySlot {
    public OffHandSlot(PlayerEntity player, int xPosition, int yPosition) {
        super((IInventory) player.inventory, 40, xPosition, yPosition);
    }

    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(PlayerContainer.BLOCK_ATLAS, PlayerContainer.EMPTY_ARMOR_SLOT_SHIELD);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\player\OffHandSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */