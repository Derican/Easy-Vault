package iskallia.vault.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;

public interface PlayerSensitiveSlot {
    default ItemStack modifyTakenStack(PlayerEntity player, ItemStack taken, boolean simulate) {
        return modifyTakenStack(player, taken, player.getCommandSenderWorld().isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER, simulate);
    }

    ItemStack modifyTakenStack(PlayerEntity paramPlayerEntity, ItemStack paramItemStack, LogicalSide paramLogicalSide, boolean paramBoolean);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\PlayerSensitiveSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */