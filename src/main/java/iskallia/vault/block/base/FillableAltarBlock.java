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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public abstract class FillableAltarBlock<T extends FillableAltarTileEntity> extends FacedBlock {
    protected static final Random rand;
    public static final float FAVOUR_CHANCE = 0.05f;
    public static final VoxelShape SHAPE;

    public FillableAltarBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).strength(-1.0f, 3600000.0f).noDrops().noOcclusion());
    }

    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return FillableAltarBlock.SHAPE;
    }

    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    public abstract T createTileEntity(final BlockState p0, final IBlockReader p1);

    public abstract IParticleData getFlameParticle();

    public abstract PlayerFavourData.VaultGodType getAssociatedVaultGod();

    public abstract ActionResultType rightClicked(final BlockState p0, final ServerWorld p1, final BlockPos p2, final T p3, final ServerPlayerEntity p4, final ItemStack p5);

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        final TileEntity tileEntity = world.getBlockEntity(pos);
        final ItemStack heldStack = player.getItemInHand(hand);
        if (tileEntity != null) {
            try {
                if (((FillableAltarTileEntity) tileEntity).isMaxedOut()) {
                    world.setBlockAndUpdate(pos, this.getSuccessChestState(state));
                    return ActionResultType.SUCCESS;
                }
                return this.rightClicked(state, (ServerWorld) world, pos, (T) tileEntity, (ServerPlayerEntity) player, heldStack);
            } catch (final ClassCastException ex) {
            }
        }
        return ActionResultType.FAIL;
    }

    protected BlockState getSuccessChestState(final BlockState altarState) {
        return ModBlocks.VAULT_ALTAR_CHEST.defaultBlockState().setValue(ChestBlock.FACING, altarState.getValue(FillableAltarBlock.FACING));
    }

    public static float getFavourChance(final PlayerEntity player, final PlayerFavourData.VaultGodType favourType) {
        final ItemStack offHand = player.getItemInHand(Hand.OFF_HAND);
        if (offHand.isEmpty() || !(offHand.getItem() instanceof IdolItem)) {
            return 0.05f;
        }
        if (favourType != ((IdolItem) offHand.getItem()).getType()) {
            return 0.05f;
        }
        int multiplier = 2;
        if (ModAttributes.IDOL_AUGMENTED.exists(offHand)) {
            multiplier = 3;
        }
        return 0.05f * multiplier;
    }

    public static void playFavourInfo(final ServerPlayerEntity sPlayer) {
        final BlockPos pos = sPlayer.blockPosition();
        sPlayer.level.playSound((PlayerEntity) null, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), ModSounds.FAVOUR_UP, SoundCategory.PLAYERS, 0.4f, 0.7f);
        final ITextComponent msg = (ITextComponent) new StringTextComponent("You gained a favour!").withStyle(TextFormatting.DARK_GREEN).withStyle(TextFormatting.BOLD);
        sPlayer.displayClientMessage(msg, true);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(final BlockState stateIn, final World world, final BlockPos pos, final Random rand) {
        this.addFlameParticle(world, pos, 1.0, 17.0, 15.0);
        this.addFlameParticle(world, pos, 15.0, 17.0, 15.0);
        this.addFlameParticle(world, pos, 15.0, 17.0, 1.0);
        this.addFlameParticle(world, pos, 1.0, 17.0, 1.0);
    }

    @OnlyIn(Dist.CLIENT)
    public void addFlameParticle(final World world, final BlockPos pos, final double xOffset, final double yOffset, final double zOffset) {
        final double x = pos.getX() + xOffset / 16.0;
        final double y = pos.getY() + yOffset / 16.0;
        final double z = pos.getZ() + zOffset / 16.0;
        world.addParticle(this.getFlameParticle(), x, y, z, 0.0, 0.0, 0.0);
    }

    static {
        rand = new Random();
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);
    }
}
