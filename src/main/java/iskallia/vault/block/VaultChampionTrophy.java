// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import iskallia.vault.block.entity.VaultChampionTrophyTileEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItemUseContext;
import javax.annotation.Nullable;
import iskallia.vault.init.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import javax.annotation.Nonnull;
import net.minecraft.state.StateContainer;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.block.Block;

public class VaultChampionTrophy extends Block
{
    public static final DirectionProperty FACING;
    public static final EnumProperty<Variant> VARIANT;
    public static final VoxelShape SHAPE;
    
    public VaultChampionTrophy() {
        super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(5.0f, 6.0f));
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)VaultChampionTrophy.FACING, (Comparable)Direction.SOUTH)).setValue((Property)VaultChampionTrophy.VARIANT, (Comparable)Variant.SILVER));
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { (Property)VaultChampionTrophy.FACING, (Property)VaultChampionTrophy.VARIANT });
    }
    
    @Nonnull
    public VoxelShape getShape(@Nonnull final BlockState state, @Nonnull final IBlockReader worldIn, @Nonnull final BlockPos pos, @Nonnull final ISelectionContext context) {
        return VaultChampionTrophy.SHAPE;
    }
    
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }
    
    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.VAULT_CHAMPION_TROPHY_TILE_ENTITY.create();
    }
    
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final ItemStack stack = context.getItemInHand();
        final CompoundNBT blockEntityTag = stack.getOrCreateTagElement("BlockEntityTag");
        final String variantId = blockEntityTag.contains("Variant", 8) ? blockEntityTag.getString("Variant") : Variant.SILVER.getSerializedName();
        final Variant variant = Variant.valueOf(variantId.toUpperCase());
        return (BlockState)((BlockState)this.defaultBlockState().setValue((Property)VaultChampionTrophy.FACING, (Comparable)context.getHorizontalDirection())).setValue((Property)VaultChampionTrophy.VARIANT, (Comparable)variant);
    }
    
    public void playerWillDestroy(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final BlockState state, @Nonnull final PlayerEntity player) {
        if (!world.isClientSide && !player.isCreative()) {
            final ItemStack itemStack = new ItemStack((IItemProvider)this.getBlock());
            final CompoundNBT nbt = itemStack.getOrCreateTag();
            final CompoundNBT blockEntityTag = itemStack.getOrCreateTagElement("BlockEntityTag");
            final TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof VaultChampionTrophyTileEntity) {
                final VaultChampionTrophyTileEntity trophy = (VaultChampionTrophyTileEntity)tileEntity;
                trophy.writeToEntityTag(blockEntityTag);
            }
            final Variant variant = (Variant)state.getValue((Property)VaultChampionTrophy.VARIANT);
            nbt.putInt("CustomModelData", variant.ordinal());
            blockEntityTag.putString("Variant", variant.getSerializedName());
            final ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity((Entity)itemEntity);
        }
        super.playerWillDestroy(world, pos, state, player);
    }
    
    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        VARIANT = EnumProperty.create("variant", (Class)Variant.class);
        SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    }
    
    public enum Variant implements IStringSerializable
    {
        SILVER, 
        BLUE_SILVER, 
        GOLDEN, 
        PLATINUM;
        
        @Nonnull
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
