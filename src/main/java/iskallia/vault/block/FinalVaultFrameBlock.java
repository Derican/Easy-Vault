// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import net.minecraft.block.HorizontalBlock;
import iskallia.vault.init.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.material.PushReaction;
import javax.annotation.Nonnull;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IBlockReader;
import iskallia.vault.block.entity.FinalVaultFrameTileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import javax.annotation.Nullable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.state.DirectionProperty;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.block.Block;

@Mod.EventBusSubscriber
public class FinalVaultFrameBlock extends Block
{
    public static final DirectionProperty FACING;
    
    public FinalVaultFrameBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.STONE).strength(2.0f, 3600000.0f).noOcclusion());
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { (Property)FinalVaultFrameBlock.FACING });
    }
    
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return (BlockState)this.defaultBlockState().setValue((Property)FinalVaultFrameBlock.FACING, (Comparable)context.getHorizontalDirection().getOpposite());
    }
    
    @SubscribeEvent
    public static void onBlockHit(final PlayerInteractEvent.LeftClickBlock event) {
        if (!event.isCancelable()) {
            return;
        }
        final PlayerEntity player = event.getPlayer();
        if (player.isCreative()) {
            return;
        }
        final FinalVaultFrameTileEntity tileEntity = FinalVaultFrameTileEntity.get((IBlockReader)player.level, event.getPos());
        if (tileEntity == null) {
            return;
        }
        if (!tileEntity.getOwnerUUID().equals(player.getUUID())) {
            event.setCanceled(true);
        }
    }
    
    public boolean isToolEffective(final BlockState state, final ToolType tool) {
        return tool == ToolType.PICKAXE;
    }
    
    @Nonnull
    public PushReaction getPistonPushReaction(@Nonnull final BlockState state) {
        return PushReaction.BLOCK;
    }
    
    public ItemStack getPickBlock(final BlockState state, final RayTraceResult target, final IBlockReader world, final BlockPos pos, final PlayerEntity player) {
        final ItemStack itemStack = new ItemStack((IItemProvider)this.getBlock());
        final FinalVaultFrameTileEntity tileEntity = FinalVaultFrameTileEntity.get(world, pos);
        final CompoundNBT entityNBT = new CompoundNBT();
        if (tileEntity != null) {
            tileEntity.writeToEntityTag(entityNBT);
        }
        itemStack.getOrCreateTag().put("BlockEntityTag", (INBT)entityNBT);
        return itemStack;
    }
    
    public void setPlacedBy(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final BlockState state, @Nullable final LivingEntity placer, @Nonnull final ItemStack stack) {
        if (world.isClientSide()) {
            return;
        }
        final CompoundNBT tag = stack.getTagElement("BlockEntityTag");
        if (tag == null) {
            return;
        }
        final FinalVaultFrameTileEntity tileEntity = FinalVaultFrameTileEntity.get((IBlockReader)world, pos);
        if (tileEntity == null) {
            return;
        }
        tileEntity.loadFromNBT(tag);
        super.setPlacedBy(world, pos, state, placer, stack);
    }
    
    public void playerWillDestroy(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final BlockState state, @Nonnull final PlayerEntity player) {
        if (!world.isClientSide && !player.isCreative()) {
            final FinalVaultFrameTileEntity tileEntity = FinalVaultFrameTileEntity.get((IBlockReader)world, pos);
            if (tileEntity != null) {
                final ItemStack itemStack = new ItemStack((IItemProvider)this.getBlock());
                final CompoundNBT entityNBT = new CompoundNBT();
                tileEntity.writeToEntityTag(entityNBT);
                itemStack.getOrCreateTag().put("BlockEntityTag", (INBT)entityNBT);
                final ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity((Entity)itemEntity);
            }
        }
        super.playerWillDestroy(world, pos, state, player);
    }
    
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }
    
    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.FINAL_VAULT_FRAME_TILE_ENTITY.create();
    }
    
    static {
        FACING = HorizontalBlock.FACING;
    }
}
