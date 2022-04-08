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
import net.minecraft.state.Property;
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
    private static final Random rand = new Random();
    public static final String SHOULD_EXHAUST = "ShouldExhaust";
    public static final String SHOULD_APPLY_ECHO = "ShouldApplyEcho";
    public static final String SHOULD_CLONE = "ShouldClone";
    public static final String CLONED = "Cloned";

    public VaultCrystalItem(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties()).tab(group).stacksTo(1));
        setRegistryName(id);
    }

    public static CrystalData getData(ItemStack stack) {
        return new CrystalData(stack);
    }

    public static ItemStack getCrystalWithBoss(String playerBossName) {
        ItemStack stack = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        CrystalData data = new CrystalData(stack);
        data.setPlayerBossName(playerBossName);
        data.setType(CrystalData.Type.RAFFLE);
        return stack;
    }

    public static ItemStack getCrystalWithObjective(ResourceLocation objectiveKey) {
        ItemStack stack = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        CrystalData data = new CrystalData(stack);
        data.setSelectedObjective(objectiveKey);
        if (rand.nextBoolean()) {
            data.setType(CrystalData.Type.COOP);
        }
        return stack;
    }


    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            for (CrystalData.Type crystalType : CrystalData.Type.values()) {
                if (crystalType.visibleInCreative()) {


                    ItemStack crystal = new ItemStack((IItemProvider) this);
                    getData(crystal).setType(crystalType);
                    items.add(crystal);
                }
            }
        }
    }

    public ITextComponent getName(ItemStack stack) {
        CrystalData data = getData(stack);
        if (data.getEchoData().getEchoCount() > 0) {
            return (ITextComponent) (new StringTextComponent("Echoed Vault Crystal")).withStyle(TextFormatting.DARK_PURPLE);
        }
        return super.getName(stack);
    }


    public ActionResultType useOn(ItemUseContext context) {
        if ((context.getLevel()).isClientSide || context.getPlayer() == null) {
            return super.useOn(context);
        }

        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        CrystalData data = new CrystalData(stack);

        if (data.getEchoData().getEchoCount() > 0) return super.useOn(context);

        BlockPos pos = context.getClickedPos();

        if (tryCreatePortal(context.getLevel(), pos, context.getClickedFace(), data)) {
            context.getLevel().playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.VAULT_PORTAL_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F);


            IFormattableTextComponent playerName = context.getPlayer().getDisplayName().copy();
            playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
            StringTextComponent suffix = new StringTextComponent(" opened a Vault!");

            context.getLevel().getServer().getPlayerList().broadcastMessage((ITextComponent) (new StringTextComponent(""))
                    .append((ITextComponent) playerName).append((ITextComponent) suffix), ChatType.CHAT, context.getPlayer().getUUID());
            context.getItemInHand().shrink(1);
            return ActionResultType.SUCCESS;
        }

        return super.useOn(context);
    }

    private boolean tryCreatePortal(World world, BlockPos pos, Direction facing, CrystalData data) {
        Optional<VaultPortalSize> optional = VaultPortalSize.getPortalSize((IWorld) world, pos.relative(facing), Direction.Axis.X, VaultPortalBlock.FRAME);

        if (optional.isPresent()) {
            BlockState state = (BlockState) ModBlocks.VAULT_PORTAL.defaultBlockState().setValue((Property) VaultPortalBlock.AXIS, (Comparable) ((VaultPortalSize) optional.get()).getAxis());

            ((VaultPortalSize) optional.get()).placePortalBlocks(blockPos -> {
                world.setBlock(blockPos, state, 3);
                TileEntity te = world.getBlockEntity(blockPos);
                if (!(te instanceof VaultPortalTileEntity))
                    return;
                VaultPortalTileEntity portal = (VaultPortalTileEntity) te;
                portal.setCrystalData(data);
            });
            return true;
        }

        return false;
    }

    public static long getSeed(ItemStack stack) {
        if (!(stack.getItem() instanceof VaultCrystalItem)) {
            return 0L;
        }
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("Seed", 4)) {
            setRandomSeed(stack);
        }

        return nbt.getLong("Seed");
    }

    public static void setRandomSeed(ItemStack stack) {
        if (!(stack.getItem() instanceof VaultCrystalItem)) {
            return;
        }
        stack.getOrCreateTag().putLong("Seed", rand.nextLong());
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        getData(stack).addInformation(world, tooltip, flag);
        super.appendHoverText(stack, world, tooltip, flag);
    }


    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand handIn) {
        if (worldIn.isClientSide) return super.use(worldIn, player, handIn);
        if (handIn == Hand.OFF_HAND) return super.use(worldIn, player, handIn);

        ItemStack stack = player.getMainHandItem();
        CrystalData data = getData(stack);

        if (!data.getPlayerBossName().isEmpty() &&
                player.isShiftKeyDown()) {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("RenameType", RenameType.VAULT_CRYSTAL.ordinal());
            nbt.put("Data", (INBT) stack.serializeNBT());
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

                public ITextComponent getDisplayName() {
                    return (ITextComponent) new StringTextComponent("Rename Raffle Boss");
                }


                @Nullable
                public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return (Container) new RenamingContainer(windowId, nbt);
                }
            },buffer -> buffer.writeNbt(nbt));
        }


        return super.use(worldIn, player, handIn);
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof ServerPlayerEntity) {
            attemptExhaust(stack);
            attemptApplyEcho((ServerPlayerEntity) entity, stack);
            handleCloning((ServerPlayerEntity) entity, stack);
        }
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
    }

    public static void markAttemptExhaust(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putBoolean("ShouldExhaust", true);
    }

    private static boolean shouldExhaust(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        return (nbt.getBoolean("ShouldExhaust") && getData(stack).canBeModified());
    }

    private void attemptExhaust(ItemStack stack) {
        if (shouldExhaust(stack)) {
            CrystalData data = getData(stack);
            if (Math.random() < ModConfigs.VAULT_INHIBITOR.CHANCE_TO_EXHAUST) {
                data.setModifiable(false);
            }
            markEhaustAttempted(stack);
        }
    }

    private static void markEhaustAttempted(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putBoolean("ShouldExhaust", false);
    }

    public static void markAttemptApplyEcho(ItemStack stack, int amount) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("ShouldApplyEcho", amount + nbt.getInt("ShouldApplyEcho"));
    }

    private static boolean shouldApplyEcho(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        return (nbt.getInt("ShouldApplyEcho") > 0);
    }

    private void attemptApplyEcho(ServerPlayerEntity player, ItemStack stack) {
        if (shouldApplyEcho(stack)) {
            CompoundNBT nbt = stack.getOrCreateTag();
            int amount = nbt.getInt("ShouldApplyEcho");
            CrystalData data = getData(stack);
            int remainder = data.addEchoGems(amount);
            if (remainder > 0)
                EntityHelper.giveItem((PlayerEntity) player, new ItemStack((IItemProvider) ModItems.ECHO_GEM, remainder));
            markApplyEchoAttempted(stack);
        }
    }

    private static void markApplyEchoAttempted(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("ShouldApplyEcho", 0);
    }

    public static void markForClone(ItemStack stack, boolean success) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("ShouldClone", success ? 1 : -1);
    }

    private void handleCloning(ServerPlayerEntity player, ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("ShouldClone"))
            return;
        CrystalData data = getData(stack);
        if (nbt.getInt("ShouldClone") == -1) {
            data.setModifiable(false);
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            stack.getOrCreateTag().remove("ShouldClone");
        } else {
            data.setModifiable(false);
            nbt.putBoolean("Cloned", true);
            data.setModifiable(false);
            stack.getOrCreateTag().remove("ShouldClone");
            EntityHelper.giveItem((PlayerEntity) player, stack.copy());
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.8F, 1.5F);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\crystal\VaultCrystalItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */