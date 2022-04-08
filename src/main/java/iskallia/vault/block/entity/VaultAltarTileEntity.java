package iskallia.vault.block.entity;

import iskallia.vault.altar.AltarInfusionRecipe;
import iskallia.vault.altar.RequiredItem;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.VectorHelper;
import iskallia.vault.world.data.PlayerStatsData;
import iskallia.vault.world.data.PlayerVaultAltarData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class VaultAltarTileEntity extends TileEntity implements ITickableTileEntity {
    private UUID owner;
    private int infusionTimer = -666;
    private AltarInfusionRecipe recipe;
    private ItemStackHandler itemHandler = createHandler();
    private AltarState altarState;
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> this.itemHandler);

    public VaultAltarTileEntity() {
        super(ModBlocks.VAULT_ALTAR_TILE_ENTITY);
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setRecipe(AltarInfusionRecipe recipe) {
        this.recipe = recipe;
    }

    public AltarInfusionRecipe getRecipe() {
        return this.recipe;
    }

    public void setAltarState(AltarState state) {
        this.altarState = state;
    }

    public AltarState getAltarState() {
        return this.altarState;
    }

    public void sendUpdates() {
        if (this.level == null)
            return;
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
        setChanged();
    }


    public void tick() {
        World world = getLevel();
        if (world == null || world.isClientSide)
            return;
        if (this.altarState == AltarState.IDLE) {
            return;
        }
        if (PlayerVaultAltarData.get((ServerWorld) world).getRecipe(this.owner) != null &&
                PlayerVaultAltarData.get((ServerWorld) world).getRecipe(this.owner).isComplete() &&
                this.altarState != AltarState.INFUSING) this.altarState = AltarState.COMPLETE;


        switch (this.altarState) {
            case ACCEPTING:
                pullNearbyItems(world,
                        PlayerVaultAltarData.get((ServerWorld) world),
                        getBlockPos().getX() + 0.5D,
                        getBlockPos().getY() + 0.5D,
                        getBlockPos().getZ() + 0.5D, ModConfigs.VAULT_ALTAR.ITEM_RANGE_CHECK);
                break;


            case INFUSING:
                playInfusionEffects((ServerWorld) world);
                if (this.infusionTimer-- <= 0) {
                    completeInfusion(world);
                    sendUpdates();
                }
                break;
        }

        this.recipe = PlayerVaultAltarData.get((ServerWorld) world).getRecipe(this.owner);

        if (world.getGameTime() % 20L == 0L) sendUpdates();

    }

    public void onAltarPowered() {
        if (!(this.level instanceof ServerWorld) || getAltarState() != AltarState.COMPLETE)
            return;
        ServerWorld serverWorld = (ServerWorld) this.level;

        PlayerVaultAltarData.get(serverWorld).getAltars(this.owner).forEach(altarPos -> {
            if (!getBlockPos().equals(altarPos)) {
                TileEntity te = this.level.getBlockEntity(altarPos);
                if (te instanceof VaultAltarTileEntity) {
                    VaultAltarTileEntity altar = (VaultAltarTileEntity) te;
                    if (altar.getAltarState() != AltarState.IDLE)
                        altar.onRemoveVaultRock();
                }
            }
        });
        serverWorld.playSound(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), SoundEvents.BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0F, 0.5F);
        this.infusionTimer = ModConfigs.VAULT_ALTAR.INFUSION_TIME * 20;
        this.altarState = AltarState.INFUSING;
    }


    public ActionResultType onAddVaultRock(ServerPlayerEntity player, ItemStack heldItem) {
        if (this.level == null) return ActionResultType.FAIL;
        ServerWorld world = (ServerWorld) this.level;

        List<BlockPos> altarPositions = PlayerVaultAltarData.get(world).getAltars(player.getUUID());
        for (BlockPos altarPosition : altarPositions) {
            TileEntity te = world.getBlockEntity(altarPosition);
            if (te instanceof VaultAltarTileEntity) {
                VaultAltarTileEntity altar = (VaultAltarTileEntity) te;
                if (altar.altarState == AltarState.INFUSING) {
                    return ActionResultType.FAIL;
                }
            }
        }

        PlayerVaultAltarData altarData = PlayerVaultAltarData.get(world);
        this.recipe = altarData.getRecipe(world, this.worldPosition, player);

        setAltarState(AltarState.ACCEPTING);

        if (!player.isCreative()) heldItem.shrink(1);
        sendUpdates();

        return ActionResultType.SUCCESS;
    }

    public ActionResultType onRemoveVaultRock() {
        setAltarState(AltarState.IDLE);
        this.recipe = null;
        this.infusionTimer = -666;
        if (getLevel() != null) {
            getLevel().addFreshEntity((Entity) new ItemEntity(getLevel(),
                    getBlockPos().getX() + 0.5D,
                    getBlockPos().getY() + 1.5D,
                    getBlockPos().getZ() + 0.5D, new ItemStack((IItemProvider) ModItems.VAULT_ROCK)));
        }


        sendUpdates();
        return ActionResultType.SUCCESS;
    }

    private void completeInfusion(World world) {
        ServerWorld serverWorld = (ServerWorld) world;
        ItemStack crystal = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        world.addFreshEntity((Entity) new ItemEntity(world, getBlockPos().getX() + 0.5D, this.worldPosition.getY() + 1.5D, this.worldPosition.getZ() + 0.5D, crystal));

        CrystalData data = new CrystalData(crystal);
        int level = PlayerVaultStatsData.get((ServerWorld) world).getVaultStats(this.owner).getVaultLevel();
        data.setType((CrystalData.Type) (ModConfigs.LOOT_TABLES.getForLevel(level)).CRYSTAL_TYPE.getRandom(world.getRandom()));

        PlayerStatsData.get((ServerWorld) world).onCrystalCrafted(this.owner, this.recipe.getRequiredItems(), data.getType());
        resetAltar((ServerWorld) world);

        playCompletionEffects(serverWorld);
    }

    private void playInfusionEffects(ServerWorld world) {
        float speed = this.infusionTimer * 0.01F - 0.5F;
        if (speed > 0.0F) {
            world.sendParticles((IParticleData) ParticleTypes.PORTAL, this.worldPosition.getX() + 0.5D, getBlockPos().getY() + 1.6D, getBlockPos().getZ() + 0.5D, 3, 0.0D, 0.0D, 0.0D, speed);
        }
    }

    private void playCompletionEffects(ServerWorld serverWorld) {
        RedstoneParticleData particleData = new RedstoneParticleData(0.0F, 1.0F, 0.0F, 1.0F);
        for (int i = 0; i < 10; i++) {
            float offset = 0.1F * i;
            if (serverWorld.random.nextFloat() < 0.5F) offset *= -1.0F;
            serverWorld.sendParticles((IParticleData) particleData, this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 1.6D, this.worldPosition.getZ() + 0.5D, 10, offset, offset, offset, 1.0D);
        }

        serverWorld.playSound(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), SoundEvents.PLAYER_LEVELUP, SoundCategory.BLOCKS, 0.7F, 1.5F);
    }

    private void resetAltar(ServerWorld world) {
        this.recipe = null;
        this.infusionTimer = -666;
        PlayerVaultAltarData.get(world).removeRecipe(this.owner);
        this.altarState = AltarState.IDLE;
    }

    private void pullNearbyItems(World world, PlayerVaultAltarData data, double x, double y, double z, double range) {
        if (data.getRecipe(this.owner) == null)
            return;
        if (data.getRecipe(this.owner).getRequiredItems().isEmpty())
            return;
        float speed = ModConfigs.VAULT_ALTAR.PULL_SPEED / 20.0F;

        List<ItemEntity> entities = world.getEntitiesOfClass(ItemEntity.class, getAABB(range, x, y, z));
        for (ItemEntity itemEntity : entities) {
            List<RequiredItem> itemsToPull = data.getRecipe(this.owner).getRequiredItems();
            if (itemsToPull == null)
                return;
            for (RequiredItem required : itemsToPull) {

                if (required.reachedAmountRequired())
                    continue;
                if (required.isItemEqual(itemEntity.getItem())) {
                    int excess = required.getRemainder(itemEntity.getItem().getCount());
                    moveItemTowardPedestal(itemEntity, speed);
                    if (isItemInRange(itemEntity.blockPosition())) {
                        if (excess > 0) {
                            required.setCurrentAmount(required.getAmountRequired());
                            itemEntity.getItem().setCount(excess);
                        } else {
                            required.addAmount(itemEntity.getItem().getCount());
                            itemEntity.getItem().setCount(excess);
                            itemEntity.remove();
                        }
                        data.setDirty();
                        sendUpdates();
                    }
                }
            }
        }
    }

    private void moveItemTowardPedestal(ItemEntity itemEntity, float speed) {
        Vector3d target = VectorHelper.getVectorFromPos(getBlockPos());
        Vector3d current = VectorHelper.getVectorFromPos(itemEntity.blockPosition());

        Vector3d velocity = VectorHelper.getMovementVelocity(current, target, speed);

        itemEntity.push(velocity.x, velocity.y, velocity.z);
    }

    private boolean isItemInRange(BlockPos itemPos) {
        return (itemPos.distSqr((Vector3i) getBlockPos()) <= 4.0D);
    }

    public AxisAlignedBB getAABB(double range, double x, double y, double z) {
        return new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range);
    }


    public CompoundNBT save(CompoundNBT compound) {
        if (this.altarState != null) compound.putInt("AltarState", this.altarState.ordinal());
        if (this.owner != null) compound.putUUID("Owner", this.owner);
        if (this.recipe != null) compound.put("Recipe", (INBT) AltarInfusionRecipe.serialize(this.recipe));
        compound.putInt("InfusionTimer", this.infusionTimer);
        return super.save(compound);
    }


    public void load(BlockState state, CompoundNBT compound) {
        if (!compound.contains("AltarState")) migrate(compound.getBoolean("containsVaultRock"));

        if (compound.contains("AltarState")) this.altarState = AltarState.values()[compound.getInt("AltarState")];
        if (compound.contains("Owner")) this.owner = compound.getUUID("Owner");
        if (compound.contains("Recipe")) this.recipe = AltarInfusionRecipe.deserialize(compound.getCompound("Recipe"));
        if (compound.contains("InfusionTimer")) this.infusionTimer = compound.getInt("InfusionTimer");
        super.load(state, compound);
    }

    private void migrate(boolean containsVaultRock) {
        this.altarState = containsVaultRock ? AltarState.ACCEPTING : AltarState.IDLE;
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (this.altarState != null) tag.putInt("AltarState", this.altarState.ordinal());
        if (this.owner != null) tag.putUUID("Owner", this.owner);
        if (this.recipe != null) tag.put("Recipe", (INBT) AltarInfusionRecipe.serialize(this.recipe));
        tag.putInt("InfusionTimer", this.infusionTimer);
        return tag;
    }


    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        handleUpdateTag(getBlockState(), tag);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            protected void onContentsChanged(int slot) {
                VaultAltarTileEntity.this.sendUpdates();
            }


            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (PlayerVaultAltarData.get((ServerWorld) VaultAltarTileEntity.this.level).getRecipe(VaultAltarTileEntity.this.owner) != null &&
                        !PlayerVaultAltarData.get((ServerWorld) VaultAltarTileEntity.this.level).getRecipe(VaultAltarTileEntity.this.owner).isComplete()) {
                    List<RequiredItem> items = PlayerVaultAltarData.get((ServerWorld) VaultAltarTileEntity.this.level).getRecipe(VaultAltarTileEntity.this.owner).getRequiredItems();
                    for (RequiredItem item : items) {
                        if (item.isItemEqual(stack)) {
                            return true;
                        }
                    }
                }

                return false;
            }


            @Nonnull
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                PlayerVaultAltarData data = PlayerVaultAltarData.get((ServerWorld) VaultAltarTileEntity.this.level);
                if (data.getRecipe(VaultAltarTileEntity.this.owner) != null &&
                        !data.getRecipe(VaultAltarTileEntity.this.owner).isComplete()) {
                    List<RequiredItem> items = data.getRecipe(VaultAltarTileEntity.this.owner).getRequiredItems();
                    for (RequiredItem item : items) {
                        if (item.reachedAmountRequired()) {
                            continue;
                        }
                        if (item.isItemEqual(stack)) {
                            int amount = stack.getCount();
                            int excess = item.getRemainder(amount);
                            if (excess > 0) {
                                if (!simulate) {
                                    item.setCurrentAmount(item.getAmountRequired());
                                    data.setDirty();
                                }
                                return ItemHandlerHelper.copyStackWithSize(stack, excess);
                            }
                            if (!simulate) {
                                item.addAmount(amount);
                                data.setDirty();
                            }
                            return ItemStack.EMPTY;
                        }
                    }
                }


                return stack;
            }
        };
    }


    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public enum AltarState {
        IDLE,
        ACCEPTING,
        COMPLETE,
        INFUSING;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultAltarTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */