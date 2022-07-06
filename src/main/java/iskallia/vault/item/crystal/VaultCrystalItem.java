package iskallia.vault.item.crystal;

import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.block.VaultPortalSize;
import iskallia.vault.block.entity.VaultPortalTileEntity;
import iskallia.vault.container.RenamingContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.RenameType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class VaultCrystalItem extends Item {
    private static final Random rand;
    public static final String SHOULD_EXHAUST = "ShouldExhaust";
    public static final String SHOULD_APPLY_ECHO = "ShouldApplyEcho";
    public static final String SHOULD_CLONE = "ShouldClone";
    public static final String CLONED = "Cloned";

    public VaultCrystalItem(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).stacksTo(1));
        this.setRegistryName(id);
    }

    public static CrystalData getData(final ItemStack stack) {
        return new CrystalData(stack);
    }

    public static ItemStack getCrystalWithBoss(final String playerBossName) {
        final ItemStack stack = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        final CrystalData data = new CrystalData(stack);
        data.setPlayerBossName(playerBossName);
        data.setType(CrystalData.Type.RAFFLE);
        return stack;
    }

    public static ItemStack getCrystalWithObjective(final ResourceLocation objectiveKey) {
        final ItemStack stack = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        final CrystalData data = new CrystalData(stack);
        data.setSelectedObjective(objectiveKey);
        if (VaultCrystalItem.rand.nextBoolean()) {
            data.setType(CrystalData.Type.COOP);
        }
        return stack;
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            for (final CrystalData.Type crystalType : CrystalData.Type.values()) {
                if (crystalType.visibleInCreative()) {
                    final ItemStack crystal = new ItemStack((IItemProvider) this);
                    getData(crystal).setType(crystalType);
                    items.add(crystal);
                }
            }
        }
    }

    public ITextComponent getName(final ItemStack stack) {
        final CrystalData data = getData(stack);
        if (data.getEchoData().getEchoCount() > 0) {
            return (ITextComponent) new StringTextComponent("Echoed Vault Crystal").withStyle(TextFormatting.DARK_PURPLE);
        }
        return super.getName(stack);
    }

    public ActionResultType useOn(final ItemUseContext context) {
        if (context.getLevel().isClientSide || context.getPlayer() == null) {
            return super.useOn(context);
        }
        final ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        final CrystalData data = new CrystalData(stack);
        if (data.getEchoData().getEchoCount() > 0) {
            return super.useOn(context);
        }
        final BlockPos pos = context.getClickedPos();
        if (this.tryCreatePortal(context.getLevel(), pos, context.getClickedFace(), data)) {
            context.getLevel().playSound((PlayerEntity) null, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), ModSounds.VAULT_PORTAL_OPEN, SoundCategory.BLOCKS, 1.0f, 1.0f);
            final IFormattableTextComponent playerName = context.getPlayer().getDisplayName().copy();
            playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
            final String suffix = (data.getType() == CrystalData.Type.FINAL_LOBBY) ? " opened the Final Vault!" : " opened a Vault!";
            final StringTextComponent suffixComponent = new StringTextComponent(suffix);
            context.getLevel().getServer().getPlayerList().broadcastMessage((ITextComponent)new StringTextComponent("").append((ITextComponent)playerName).append((ITextComponent)suffixComponent), ChatType.CHAT, context.getPlayer().getUUID());
            context.getItemInHand().shrink(1);
            return ActionResultType.SUCCESS;
        }
        return super.useOn(context);
    }

    private boolean tryCreatePortal(final World world, final BlockPos pos, final Direction facing, final CrystalData data) {
        final Optional<VaultPortalSize> optional = VaultPortalSize.getPortalSize((IWorld)world, pos.relative(facing), Direction.Axis.X, VaultPortalBlock.FRAME);
        if (optional.isPresent()) {
            final VaultPortalSize portal = optional.get();
            final BlockState state = (BlockState)ModBlocks.VAULT_PORTAL.defaultBlockState().setValue(VaultPortalBlock.AXIS, portal.getAxis());
            data.frameData = new FrameData();
            for (int i = -1; i <= portal.getWidth(); ++i) {
                for (int j = -1; j <= portal.getHeight(); ++j) {
                    if (i == -1 || j == -1 || i == portal.getWidth() || j == portal.getHeight()) {
                        final BlockPos p = portal.getBottomLeft().relative(portal.getRightDir(), i).above(j);
                        final TileEntity te = world.getBlockEntity(p);
                        data.frameData.tiles.add(new FrameData.Tile(world.getBlockState(p).getBlock(), (te == null) ? new CompoundNBT() : te.serializeNBT(), p));
                    }
                }
            }
            data.updateDelegate();
            portal.placePortalBlocks(blockPos -> {
                world.setBlock(blockPos, state, 3);
                final TileEntity te2 = world.getBlockEntity(blockPos);
                if (!(te2 instanceof VaultPortalTileEntity)) {
                    return;
                }
                else {
                    final VaultPortalTileEntity portalTE = (VaultPortalTileEntity)te2;
                    portalTE.setCrystalData(data);
                    return;
                }
            });
            return true;
        }
        return false;
    }

    public static long getSeed(final ItemStack stack) {
        if (!(stack.getItem() instanceof VaultCrystalItem)) {
            return 0L;
        }
        final CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("Seed", 4)) {
            setRandomSeed(stack);
        }
        return nbt.getLong("Seed");
    }

    public static void setRandomSeed(final ItemStack stack) {
        if (!(stack.getItem() instanceof VaultCrystalItem)) {
            return;
        }
        stack.getOrCreateTag().putLong("Seed", VaultCrystalItem.rand.nextLong());
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        getData(stack).addInformation(world, tooltip, flag);
        super.appendHoverText(stack, world, (List) tooltip, flag);
    }

    public ActionResult<ItemStack> use(final World worldIn, final PlayerEntity player, final Hand handIn) {
        if (worldIn.isClientSide) {
            return (ActionResult<ItemStack>) super.use(worldIn, player, handIn);
        }
        if (handIn == Hand.OFF_HAND) {
            return (ActionResult<ItemStack>) super.use(worldIn, player, handIn);
        }
        final ItemStack stack = player.getMainHandItem();
        final CrystalData data = getData(stack);
        if (!data.getPlayerBossName().isEmpty() && player.isShiftKeyDown()) {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("RenameType", RenameType.VAULT_CRYSTAL.ordinal());
            nbt.put("Data", (INBT) stack.serializeNBT());
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return (ITextComponent) new StringTextComponent("Rename Raffle Boss");
                }

                @Nullable
                public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                    return new RenamingContainer(windowId, nbt);
                }
            }, buffer -> buffer.writeNbt(nbt));
        }
        return (ActionResult<ItemStack>) super.use(worldIn, player, handIn);
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (entity instanceof ServerPlayerEntity) {
            this.attemptExhaust(stack);
            this.attemptApplyEcho((ServerPlayerEntity) entity, stack);
            this.handleCloning((ServerPlayerEntity) entity, stack);
        }
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
    }

    public static void markAttemptExhaust(final ItemStack stack) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putBoolean("ShouldExhaust", true);
    }

    private static boolean shouldExhaust(final ItemStack stack) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.getBoolean("ShouldExhaust") && getData(stack).canBeModified();
    }

    private void attemptExhaust(final ItemStack stack) {
        if (shouldExhaust(stack)) {
            final CrystalData data = getData(stack);
            if (Math.random() < ModConfigs.VAULT_INHIBITOR.CHANCE_TO_EXHAUST) {
                data.setModifiable(false);
            }
            markEhaustAttempted(stack);
        }
    }

    private static void markEhaustAttempted(final ItemStack stack) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putBoolean("ShouldExhaust", false);
    }

    public static void markAttemptApplyEcho(final ItemStack stack, final int amount) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("ShouldApplyEcho", amount + nbt.getInt("ShouldApplyEcho"));
    }

    private static boolean shouldApplyEcho(final ItemStack stack) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.getInt("ShouldApplyEcho") > 0;
    }

    private void attemptApplyEcho(final ServerPlayerEntity player, final ItemStack stack) {
        if (shouldApplyEcho(stack)) {
            final CompoundNBT nbt = stack.getOrCreateTag();
            final int amount = nbt.getInt("ShouldApplyEcho");
            final CrystalData data = getData(stack);
            final int remainder = data.addEchoGems(amount);
            if (remainder > 0) {
                EntityHelper.giveItem((PlayerEntity) player, new ItemStack((IItemProvider) ModItems.ECHO_GEM, remainder));
            }
            markApplyEchoAttempted(stack);
        }
    }

    private static void markApplyEchoAttempted(final ItemStack stack) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("ShouldApplyEcho", 0);
    }

    public static void markForClone(final ItemStack stack, final boolean success) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("ShouldClone", success ? 1 : -1);
    }

    private void handleCloning(final ServerPlayerEntity player, final ItemStack stack) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("ShouldClone")) {
            return;
        }
        final CrystalData data = getData(stack);
        if (nbt.getInt("ShouldClone") == -1) {
            data.setModifiable(false);
            player.getCommandSenderWorld().playSound((PlayerEntity) null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1.0f, 1.0f);
            stack.getOrCreateTag().remove("ShouldClone");
        } else {
            data.setModifiable(false);
            nbt.putBoolean("Cloned", true);
            data.setModifiable(false);
            stack.getOrCreateTag().remove("ShouldClone");
            EntityHelper.giveItem((PlayerEntity) player, stack.copy());
            player.getCommandSenderWorld().playSound((PlayerEntity) null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.8f, 1.5f);
        }
    }

    static {
        rand = new Random();
    }
}
