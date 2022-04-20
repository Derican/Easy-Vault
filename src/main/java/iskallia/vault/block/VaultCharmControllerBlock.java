package iskallia.vault.block;

import iskallia.vault.block.entity.VaultCharmControllerTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.VaultCharmUpgrade;
import iskallia.vault.world.data.VaultCharmData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class VaultCharmControllerBlock extends Block {
    public VaultCharmControllerBlock() {
        super(AbstractBlock.Properties.of(Material.METAL).strength(2.0f, 3600000.0f).noOcclusion());
    }

    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.VAULT_CHARM_CONTROLLER_TILE_ENTITY.create();
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        }
        if (hand != Hand.MAIN_HAND) {
            return ActionResultType.SUCCESS;
        }
        final TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof VaultCharmControllerTileEntity)) {
            return ActionResultType.SUCCESS;
        }
        if (!(player instanceof ServerPlayerEntity)) {
            return ActionResultType.SUCCESS;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
        final VaultCharmData data = VaultCharmData.get(sPlayer.getLevel());
        final VaultCharmData.VaultCharmInventory inventory = data.getInventory(sPlayer);
        final ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof VaultCharmUpgrade)) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buffer -> buffer.writeNbt(data.getInventory(sPlayer).serializeNBT()));
            return ActionResultType.SUCCESS;
        }
        final VaultCharmUpgrade item = (VaultCharmUpgrade) heldItem.getItem();
        final int newSize = item.getTier().getSlotAmount();
        if (inventory.canUpgrade(newSize)) {
            player.level.playSound((PlayerEntity) null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0f, 1.0f);
            data.upgradeInventorySize(sPlayer, item.getTier().getSlotAmount());
            heldItem.shrink(1);
            return ActionResultType.SUCCESS;
        }
        player.level.playSound((PlayerEntity) null, pos, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.0f);
        return ActionResultType.SUCCESS;
    }

    public VoxelShape getShape(final BlockState p_220053_1_, final IBlockReader p_220053_2_, final BlockPos p_220053_3_, final ISelectionContext p_220053_4_) {
        return VoxelShapes.or(Block.box(5.0, 0.0, 5.0, 11.0, 1.0, 11.0), new VoxelShape[]{Block.box(5.0, 0.0, 5.0, 11.0, 1.0, 11.0), Block.box(6.0, 1.0, 6.0, 10.0, 4.0, 10.0), Block.box(5.0, 4.0, 5.0, 11.0, 7.0, 11.0), Block.box(4.0, 7.0, 4.0, 12.0, 9.0, 12.0), Block.box(1.0, 9.0, 1.0, 15.0, 11.0, 15.0), Block.box(5.0, 11.0, 5.0, 11.0, 15.0, 11.0)});
    }
}
