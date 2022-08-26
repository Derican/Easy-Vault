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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VaultCharmControllerTileEntity extends TileEntity implements INamedContainerProvider {
    public VaultCharmControllerTileEntity() {
        super(ModBlocks.VAULT_CHARM_CONTROLLER_TILE_ENTITY);
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Vault Charm Inscription Table");
    }

    @Nullable
    public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
        if (!(this.getLevel() instanceof ServerWorld)) {
            return null;
        }
        final ServerWorld world = (ServerWorld) this.getLevel();
        if (!(playerEntity instanceof ServerPlayerEntity)) {
            return null;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) playerEntity;
        final CompoundNBT inventoryNbt = VaultCharmData.get(world).getInventory(player).serializeNBT();
        return new VaultCharmControllerContainer(windowId, playerInventory, inventoryNbt);
    }
}
