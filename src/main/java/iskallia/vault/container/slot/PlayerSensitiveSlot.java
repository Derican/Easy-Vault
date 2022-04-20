package iskallia.vault.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;

public interface PlayerSensitiveSlot {
    default ItemStack modifyTakenStack(final PlayerEntity player, final ItemStack taken, final boolean simulate) {
        return this.modifyTakenStack(player, taken, player.getCommandSenderWorld().isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER, simulate);
    }

    ItemStack modifyTakenStack(final PlayerEntity p0, final ItemStack p1, final LogicalSide p2, final boolean p3);
}
