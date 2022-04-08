package iskallia.vault.block.base;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.gear.IdolItem;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;


public abstract class FillableAltarBlock<T extends FillableAltarTileEntity>
        extends FacedBlock {
    protected static final Random rand = new Random();
    public static final float FAVOUR_CHANCE = 0.05F;
    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);

    public FillableAltarBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
                .strength(-1.0F, 3600000.0F)
                .noDrops()
                .noOcclusion());
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;

        TileEntity tileEntity = world.getBlockEntity(pos);
        ItemStack heldStack = player.getItemInHand(hand);

        if (tileEntity != null) {
            try {
                if (((FillableAltarTileEntity) tileEntity).isMaxedOut()) {
                    world.setBlockAndUpdate(pos, getSuccessChestState(state));
                    return ActionResultType.SUCCESS;
                }
                return rightClicked(state, (ServerWorld) world, pos, (T) tileEntity, (ServerPlayerEntity) player, heldStack);
            } catch (ClassCastException classCastException) {
            }
        }

        return ActionResultType.FAIL;
    }

    protected BlockState getSuccessChestState(BlockState altarState) {
        return (BlockState) ModBlocks.VAULT_ALTAR_CHEST.defaultBlockState().setValue((Property) ChestBlock.FACING, altarState.getValue((Property) FACING));
    }

    public static float getFavourChance(PlayerEntity player, PlayerFavourData.VaultGodType favourType) {
        ItemStack offHand = player.getItemInHand(Hand.OFF_HAND);
        if (offHand.isEmpty() || !(offHand.getItem() instanceof IdolItem)) {
            return 0.05F;
        }
        if (favourType != ((IdolItem) offHand.getItem()).getType()) {
            return 0.05F;
        }
        int multiplier = 2;
        if (ModAttributes.IDOL_AUGMENTED.exists(offHand)) {
            multiplier = 3;
        }
        return 0.05F * multiplier;
    }

    public static void playFavourInfo(ServerPlayerEntity sPlayer) {
        BlockPos pos = sPlayer.blockPosition();
        sPlayer.level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.FAVOUR_UP, SoundCategory.PLAYERS, 0.4F, 0.7F);


        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("You gained a favour!")).withStyle(TextFormatting.DARK_GREEN).withStyle(TextFormatting.BOLD);
        sPlayer.displayClientMessage((ITextComponent) iFormattableTextComponent, true);
    }


    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
        addFlameParticle(world, pos, 1.0D, 17.0D, 15.0D);
        addFlameParticle(world, pos, 15.0D, 17.0D, 15.0D);
        addFlameParticle(world, pos, 15.0D, 17.0D, 1.0D);
        addFlameParticle(world, pos, 1.0D, 17.0D, 1.0D);
    }

    @OnlyIn(Dist.CLIENT)
    public void addFlameParticle(World world, BlockPos pos, double xOffset, double yOffset, double zOffset) {
        double x = pos.getX() + xOffset / 16.0D;
        double y = pos.getY() + yOffset / 16.0D;
        double z = pos.getZ() + zOffset / 16.0D;
        world.addParticle(getFlameParticle(), x, y, z, 0.0D, 0.0D, 0.0D);
    }

    public abstract T createTileEntity(BlockState paramBlockState, IBlockReader paramIBlockReader);

    public abstract IParticleData getFlameParticle();

    public abstract PlayerFavourData.VaultGodType getAssociatedVaultGod();

    public abstract ActionResultType rightClicked(BlockState paramBlockState, ServerWorld paramServerWorld, BlockPos paramBlockPos, T paramT, ServerPlayerEntity paramServerPlayerEntity, ItemStack paramItemStack);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\base\FillableAltarBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */