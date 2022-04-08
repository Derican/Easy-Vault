package iskallia.vault.block.entity;

import iskallia.vault.container.VaultCharmControllerContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.VaultCharmData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VaultCharmControllerTileEntity
        extends TileEntity implements INamedContainerProvider {
    public VaultCharmControllerTileEntity() {
        super(ModBlocks.VAULT_CHARM_CONTROLLER_TILE_ENTITY);
    }


    @Nonnull
    public ITextComponent getDisplayName() {
        return (ITextComponent) new StringTextComponent("Vault Charm Inscription Table");
    }


    @Nullable
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        if (!(getLevel() instanceof ServerWorld)) return null;
        ServerWorld world = (ServerWorld) getLevel();

        if (!(playerEntity instanceof ServerPlayerEntity)) return null;
        ServerPlayerEntity player = (ServerPlayerEntity) playerEntity;

        CompoundNBT inventoryNbt = VaultCharmData.get(world).getInventory(player).serializeNBT();

        return (Container) new VaultCharmControllerContainer(windowId, playerInventory, inventoryNbt);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultCharmControllerTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */