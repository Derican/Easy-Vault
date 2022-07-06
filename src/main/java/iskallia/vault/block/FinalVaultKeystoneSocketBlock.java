// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.world.IWorldReader;
import iskallia.vault.item.FinalVaultKeystoneItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.Objects;
import iskallia.vault.init.ModBlocks;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.state.Property;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.block.pattern.BlockPattern;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.BooleanProperty;
import net.minecraft.block.Block;

public class FinalVaultKeystoneSocketBlock extends Block
{
    public static BooleanProperty ACTIVATED;
    public static EnumProperty<PlayerFavourData.VaultGodType> ASSOCIATED_GOD;
    private static BlockPattern portalShape;
    public static final VoxelShape SHAPE;
    
    public FinalVaultKeystoneSocketBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).strength(3.6E7f).sound(SoundType.STONE));
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FinalVaultKeystoneSocketBlock.ACTIVATED, (Comparable)false)).setValue((Property)FinalVaultKeystoneSocketBlock.ASSOCIATED_GOD, (Comparable)PlayerFavourData.VaultGodType.BENEVOLENT));
    }
    
    public BlockPattern getOrCreatePortalShape() {
        if (FinalVaultKeystoneSocketBlock.portalShape == null) {
            FinalVaultKeystoneSocketBlock.portalShape = BlockPatternBuilder.start().aisle(new String[] { "?xx?", "x??x", "x??x", "?xx?" }).where('?', CachedBlockInfo.hasState(BlockStateMatcher.ANY)).where('x', CachedBlockInfo.hasState((Predicate)BlockStateMatcher.forBlock((Block)ModBlocks.FINAL_VAULT_KEYSTONE_SOCKET).where((Property)FinalVaultKeystoneSocketBlock.ACTIVATED, o -> Objects.equals(o, true)))).build();
        }
        return FinalVaultKeystoneSocketBlock.portalShape;
    }
    
    @Nonnull
    public VoxelShape getShape(@Nonnull final BlockState state, @Nonnull final IBlockReader worldIn, @Nonnull final BlockPos pos, @Nonnull final ISelectionContext context) {
        return FinalVaultKeystoneSocketBlock.SHAPE;
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { (Property)FinalVaultKeystoneSocketBlock.ACTIVATED }).add(new Property[] { (Property)FinalVaultKeystoneSocketBlock.ASSOCIATED_GOD });
    }
    
    @Nonnull
    public ActionResultType use(@Nonnull final BlockState state, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final PlayerEntity player, @Nonnull final Hand hand, @Nonnull final BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            final ItemStack heldItem = player.getItemInHand(hand);
            final Item item = heldItem.getItem();
            if (item instanceof FinalVaultKeystoneItem) {
                final FinalVaultKeystoneItem keystoneItem = (FinalVaultKeystoneItem)item;
                final PlayerFavourData.VaultGodType blockGodType = (PlayerFavourData.VaultGodType)state.getValue((Property)FinalVaultKeystoneSocketBlock.ASSOCIATED_GOD);
                final boolean activated = (boolean)state.getValue((Property)FinalVaultKeystoneSocketBlock.ACTIVATED);
                if (!activated && keystoneItem.getAssociatedGod() == blockGodType) {
                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                    }
                    final BlockState newState = (BlockState)state.setValue((Property)FinalVaultKeystoneSocketBlock.ACTIVATED, (Comparable)true);
                    world.setBlock(pos, newState, 3);
                    final BlockPattern.PatternHelper patternHelper = this.getOrCreatePortalShape().find((IWorldReader)world, pos);
                    if (patternHelper != null) {
                        final int portalSize = 2;
                        final BlockPos portalStart = patternHelper.getFrontTopLeft().offset(-portalSize, 0, -portalSize);
                        for (int x = 0; x < portalSize; ++x) {
                            for (int z = 0; z < portalSize; ++z) {
                                world.setBlock(portalStart.offset(x, 0, z), Blocks.DANDELION.defaultBlockState(), 2);
                            }
                        }
                        world.globalLevelEvent(1038, portalStart.offset(1, 0, 1), 0);
                    }
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }
    
    static {
        FinalVaultKeystoneSocketBlock.ACTIVATED = BooleanProperty.create("activated");
        FinalVaultKeystoneSocketBlock.ASSOCIATED_GOD = (EnumProperty<PlayerFavourData.VaultGodType>)EnumProperty.create("associated_god", (Class)PlayerFavourData.VaultGodType.class);
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
    }
}
