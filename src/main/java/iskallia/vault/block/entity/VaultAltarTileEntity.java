package iskallia.vault.block.entity;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

import net.minecraft.network.NetworkManager;

import javax.annotation.Nullable;

import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.vector.Vector3d;
import iskallia.vault.util.VectorHelper;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import iskallia.vault.world.data.PlayerStatsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.init.ModAttributes;
import net.minecraft.util.IItemProvider;
import iskallia.vault.init.ModItems;
import iskallia.vault.altar.RequiredItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ActionResultType;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.PlayerVaultAltarData;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.tileentity.TileEntityType;
import iskallia.vault.init.ModBlocks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import iskallia.vault.altar.AltarInfusionRecipe;

import java.util.UUID;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class VaultAltarTileEntity extends TileEntity implements ITickableTileEntity {
    private UUID owner;
    private AltarInfusionRecipe recipe;
    private AltarState altarState;
    private int infusionTimer;
    private ItemStackHandler itemHandler;
    private LazyOptional<IItemHandler> handler;

    public VaultAltarTileEntity() {
        super((TileEntityType) ModBlocks.VAULT_ALTAR_TILE_ENTITY);
        this.infusionTimer = -666;
        this.itemHandler = this.createHandler();
        this.handler = LazyOptional.of(() -> this.itemHandler);
    }

    public void setOwner(final UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setRecipe(final AltarInfusionRecipe recipe) {
        this.recipe = recipe;
    }

    public AltarInfusionRecipe getRecipe() {
        return this.recipe;
    }

    public void setAltarState(final AltarState state) {
        this.altarState = state;
    }

    public AltarState getAltarState() {
        return this.altarState;
    }

    public int getInfusionTimer() {
        return this.infusionTimer;
    }

    public void sendUpdates() {
        if (this.level == null) {
            return;
        }
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.setChanged();
    }

    public void tick() {
        final World world = this.getLevel();
        if (world == null || world.isClientSide) {
            return;
        }
        if (this.altarState == AltarState.IDLE) {
            return;
        }
        if (PlayerVaultAltarData.get((ServerWorld) world).getRecipe(this.owner) != null && PlayerVaultAltarData.get((ServerWorld) world).getRecipe(this.owner).isComplete() && this.altarState != AltarState.INFUSING) {
            this.altarState = AltarState.COMPLETE;
        }
        switch (this.altarState) {
            case ACCEPTING: {
                this.pullNearbyItems(world, PlayerVaultAltarData.get((ServerWorld) world), this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, ModConfigs.VAULT_ALTAR.ITEM_RANGE_CHECK);
                break;
            }
            case INFUSING: {
                this.playInfusionEffects((ServerWorld) world);
                if (this.infusionTimer-- <= 0) {
                    this.completeInfusion(world);
                    this.sendUpdates();
                    break;
                }
                break;
            }
        }
        this.recipe = PlayerVaultAltarData.get((ServerWorld) world).getRecipe(this.owner);
        if (world.getGameTime() % 20L == 0L) {
            this.sendUpdates();
        }
    }

    public void onAltarPowered() {
        if (!(this.level instanceof ServerWorld) || this.getAltarState() != AltarState.COMPLETE) {
            return;
        }
        final ServerWorld serverWorld = (ServerWorld) this.level;
        PlayerVaultAltarData.get(serverWorld).getAltars(this.owner).forEach(altarPos -> {
            if (!this.getBlockPos().equals((Object) altarPos)) {
                final TileEntity te = this.level.getBlockEntity(altarPos);
                if (te instanceof VaultAltarTileEntity) {
                    final VaultAltarTileEntity altar = (VaultAltarTileEntity) te;
                    if (altar.getAltarState() != AltarState.IDLE) {
                        altar.onRemoveVaultRock();
                    }
                }
            }
            return;
        });
        serverWorld.playSound((PlayerEntity) null, (double) this.getBlockPos().getX(), (double) this.getBlockPos().getY(), (double) this.getBlockPos().getZ(), SoundEvents.BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 0.5f);
        this.infusionTimer = ModConfigs.VAULT_ALTAR.INFUSION_TIME * 20;
        this.altarState = AltarState.INFUSING;
    }

    public ActionResultType onAddVaultRock(final ServerPlayerEntity player, final ItemStack heldItem) {
        if (this.level == null) {
            return ActionResultType.FAIL;
        }
        final ServerWorld world = (ServerWorld) this.level;
        final List<BlockPos> altarPositions = PlayerVaultAltarData.get(world).getAltars(player.getUUID());
        for (final BlockPos altarPosition : altarPositions) {
            final TileEntity te = world.getBlockEntity(altarPosition);
            if (te instanceof VaultAltarTileEntity) {
                final VaultAltarTileEntity altar = (VaultAltarTileEntity) te;
                if (altar.altarState == AltarState.INFUSING) {
                    return ActionResultType.FAIL;
                }
                continue;
            }
        }
        final PlayerVaultAltarData altarData = PlayerVaultAltarData.get(world);
        this.recipe = altarData.getRecipe(world, this.worldPosition, player);
        this.setAltarState(AltarState.ACCEPTING);
        if (!player.isCreative()) {
            heldItem.shrink(1);
        }
        this.sendUpdates();
        return ActionResultType.SUCCESS;
    }

    public ActionResultType onPogRightClick(final ServerPlayerEntity player, final ItemStack heldItem) {
        if (this.level == null) {
            return ActionResultType.FAIL;
        }
        final ServerWorld world = (ServerWorld) this.level;
        if (this.recipe == null) {
            return ActionResultType.SUCCESS;
        }
        final List<BlockPos> altarPositions = PlayerVaultAltarData.get(world).getAltars(player.getUUID());
        for (final BlockPos altarPosition : altarPositions) {
            final TileEntity te = world.getBlockEntity(altarPosition);
            if (te instanceof VaultAltarTileEntity) {
                final VaultAltarTileEntity altar = (VaultAltarTileEntity) te;
                if (altar.altarState == AltarState.INFUSING) {
                    return ActionResultType.FAIL;
                }
                continue;
            }
        }
        final List<RequiredItem> idolRequirements = new ArrayList<RequiredItem>();
        final ItemStack benevolent = new ItemStack((IItemProvider) ModItems.IDOL_BENEVOLENT);
        final ItemStack malevolence = new ItemStack((IItemProvider) ModItems.IDOL_MALEVOLENCE);
        final ItemStack omniscient = new ItemStack((IItemProvider) ModItems.IDOL_OMNISCIENT);
        final ItemStack timekeeper = new ItemStack((IItemProvider) ModItems.IDOL_TIMEKEEPER);
        ModAttributes.IDOL_TYPE.create(benevolent, PlayerFavourData.VaultGodType.BENEVOLENT);
        ModAttributes.GEAR_STATE.create(benevolent, VaultGear.State.IDENTIFIED);
        ModAttributes.GEAR_MODEL.create(benevolent, 0);
        ModAttributes.IDOL_TYPE.create(malevolence, PlayerFavourData.VaultGodType.MALEVOLENCE);
        ModAttributes.GEAR_STATE.create(malevolence, VaultGear.State.IDENTIFIED);
        ModAttributes.GEAR_MODEL.create(malevolence, 0);
        ModAttributes.IDOL_TYPE.create(omniscient, PlayerFavourData.VaultGodType.OMNISCIENT);
        ModAttributes.GEAR_STATE.create(omniscient, VaultGear.State.IDENTIFIED);
        ModAttributes.GEAR_MODEL.create(omniscient, 0);
        ModAttributes.IDOL_TYPE.create(timekeeper, PlayerFavourData.VaultGodType.TIMEKEEPER);
        ModAttributes.GEAR_STATE.create(timekeeper, VaultGear.State.IDENTIFIED);
        ModAttributes.GEAR_MODEL.create(timekeeper, 0);
        idolRequirements.add(new RequiredItem(benevolent, 0, 1));
        idolRequirements.add(new RequiredItem(malevolence, 0, 1));
        idolRequirements.add(new RequiredItem(omniscient, 0, 1));
        idolRequirements.add(new RequiredItem(timekeeper, 0, 1));
        System.out.println(benevolent.getTag());
        this.recipe.cacheRequiredItems(idolRequirements);
        this.recipe.setPogInfused(true);
        PlayerVaultAltarData.get(world).setDirty();
        if (!player.isCreative()) {
            heldItem.shrink(1);
        }
        this.sendUpdates();
        world.playSound((PlayerEntity) null, (double) this.getBlockPos().getX(), (double) this.getBlockPos().getY(), (double) this.getBlockPos().getZ(), SoundEvents.END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1.0f, 2.0f);
        return ActionResultType.SUCCESS;
    }

    public ActionResultType onRemoveVaultRock() {
        this.setAltarState(AltarState.IDLE);
        this.recipe = null;
        this.infusionTimer = -666;
        if (this.getLevel() != null) {
            this.getLevel().addFreshEntity((Entity) new ItemEntity(this.getLevel(), this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.5, this.getBlockPos().getZ() + 0.5, new ItemStack((IItemProvider) ModItems.VAULT_ROCK)));
        }
        this.sendUpdates();
        return ActionResultType.SUCCESS;
    }

    public ActionResultType onRemovePogInfusion() {
        this.setAltarState(AltarState.ACCEPTING);
        this.recipe.revertCache();
        this.recipe.setPogInfused(false);
        if (this.level != null) {
            this.level.playSound((PlayerEntity) null, (double) this.getBlockPos().getX(), (double) this.getBlockPos().getY(), (double) this.getBlockPos().getZ(), SoundEvents.WITHER_DEATH, SoundCategory.BLOCKS, 0.7f, 1.5f);
        }
        this.sendUpdates();
        return ActionResultType.SUCCESS;
    }

    private void completeInfusion(final World world) {
        final ServerWorld serverWorld = (ServerWorld) world;
        final ItemStack crystal = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        final CrystalData data = new CrystalData(crystal);
        if (this.recipe.isPogInfused()) {
            data.setType(CrystalData.Type.FINAL_LOBBY);
            serverWorld.playSound((PlayerEntity) null, (double) this.getBlockPos().getX(), (double) this.getBlockPos().getY(), (double) this.getBlockPos().getZ(), SoundEvents.END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1.0f, 0.8f);
        } else {
            final int level = PlayerVaultStatsData.get((ServerWorld) world).getVaultStats(this.owner).getVaultLevel();
            data.setType(ModConfigs.LOOT_TABLES.getForLevel(level).CRYSTAL_TYPE.getRandom(world.getRandom()));
        }
        world.addFreshEntity((Entity) new ItemEntity(world, this.getBlockPos().getX() + 0.5, this.worldPosition.getY() + 1.5, this.worldPosition.getZ() + 0.5, crystal));
        PlayerStatsData.get((ServerWorld) world).onCrystalCrafted(this.owner, this.recipe.getRequiredItems(), data.getType());
        this.resetAltar((ServerWorld) world);
        this.playCompletionEffects(serverWorld);
    }

    private void playInfusionEffects(final ServerWorld world) {
        final float speed = this.infusionTimer * 0.01f - 0.5f;
        if (speed > 0.0f) {
            world.sendParticles((IParticleData) ParticleTypes.PORTAL, this.worldPosition.getX() + 0.5, this.getBlockPos().getY() + 1.6, this.getBlockPos().getZ() + 0.5, 3, 0.0, 0.0, 0.0, (double) speed);
        }
    }

    private void playCompletionEffects(final ServerWorld serverWorld) {
        final RedstoneParticleData particleData = new RedstoneParticleData(0.0f, 1.0f, 0.0f, 1.0f);
        for (int i = 0; i < 10; ++i) {
            float offset = 0.1f * i;
            if (serverWorld.random.nextFloat() < 0.5f) {
                offset *= -1.0f;
            }
            serverWorld.sendParticles((IParticleData) particleData, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.6, this.worldPosition.getZ() + 0.5, 10, (double) offset, (double) offset, (double) offset, 1.0);
        }
        serverWorld.playSound((PlayerEntity) null, (double) this.getBlockPos().getX(), (double) this.getBlockPos().getY(), (double) this.getBlockPos().getZ(), SoundEvents.PLAYER_LEVELUP, SoundCategory.BLOCKS, 0.7f, 1.5f);
    }

    private void resetAltar(final ServerWorld world) {
        this.infusionTimer = -666;
        if (this.recipe.isPogInfused()) {
            this.recipe.revertCache();
            this.recipe.setPogInfused(false);
            PlayerVaultAltarData.get(world).setDirty();
            this.altarState = AltarState.ACCEPTING;
            this.sendUpdates();
        } else {
            this.recipe = null;
            PlayerVaultAltarData.get(world).removeRecipe(this.owner);
            this.altarState = AltarState.IDLE;
        }
    }

    private void pullNearbyItems(final World world, final PlayerVaultAltarData data, final double x, final double y, final double z, final double range) {
        if (data.getRecipe(this.owner) == null) {
            return;
        }
        if (data.getRecipe(this.owner).getRequiredItems().isEmpty()) {
            return;
        }
        final float speed = ModConfigs.VAULT_ALTAR.PULL_SPEED / 20.0f;
        final List<ItemEntity> entities = world.getEntitiesOfClass((Class) ItemEntity.class, this.getAABB(range, x, y, z));
        for (final ItemEntity itemEntity : entities) {
            final List<RequiredItem> itemsToPull = data.getRecipe(this.owner).getRequiredItems();
            if (itemsToPull == null) {
                return;
            }
            for (final RequiredItem required : itemsToPull) {
                if (required.reachedAmountRequired()) {
                    continue;
                }
                if (!required.isItemEqual(itemEntity.getItem())) {
                    continue;
                }
                final int excess = required.getRemainder(itemEntity.getItem().getCount());
                this.moveItemTowardPedestal(itemEntity, speed);
                if (!this.isItemInRange(itemEntity.blockPosition())) {
                    continue;
                }
                if (excess > 0) {
                    required.setCurrentAmount(required.getAmountRequired());
                    itemEntity.getItem().setCount(excess);
                } else {
                    required.addAmount(itemEntity.getItem().getCount());
                    itemEntity.getItem().setCount(excess);
                    itemEntity.remove();
                }
                data.setDirty();
                this.sendUpdates();
            }
        }
    }

    private void moveItemTowardPedestal(final ItemEntity itemEntity, final float speed) {
        final Vector3d target = VectorHelper.getVectorFromPos(this.getBlockPos());
        final Vector3d current = VectorHelper.getVectorFromPos(itemEntity.blockPosition());
        final Vector3d velocity = VectorHelper.getMovementVelocity(current, target, speed);
        itemEntity.push(velocity.x, velocity.y, velocity.z);
    }

    private boolean isItemInRange(final BlockPos itemPos) {
        return itemPos.distSqr((Vector3i) this.getBlockPos()) <= 4.0;
    }

    public AxisAlignedBB getAABB(final double range, final double x, final double y, final double z) {
        return new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range);
    }

    public CompoundNBT save(final CompoundNBT compound) {
        if (this.altarState != null) {
            compound.putInt("AltarState", this.altarState.ordinal());
        }
        if (this.owner != null) {
            compound.putUUID("Owner", this.owner);
        }
        if (this.recipe != null) {
            compound.put("Recipe", (INBT) this.recipe.serialize());
        }
        compound.putInt("InfusionTimer", this.infusionTimer);
        return super.save(compound);
    }

    public void load(final BlockState state, final CompoundNBT compound) {
        if (!compound.contains("AltarState")) {
            this.migrate(compound.getBoolean("containsVaultRock"));
        }
        if (compound.contains("AltarState")) {
            this.altarState = AltarState.values()[compound.getInt("AltarState")];
        }
        if (compound.contains("Owner")) {
            this.owner = compound.getUUID("Owner");
        }
        if (compound.contains("Recipe")) {
            this.recipe = AltarInfusionRecipe.deserialize(compound.getCompound("Recipe"));
        }
        if (compound.contains("InfusionTimer")) {
            this.infusionTimer = compound.getInt("InfusionTimer");
        }
        super.load(state, compound);
    }

    private void migrate(final boolean containsVaultRock) {
        this.altarState = (containsVaultRock ? AltarState.ACCEPTING : AltarState.IDLE);
    }

    public CompoundNBT getUpdateTag() {
        final CompoundNBT tag = super.getUpdateTag();
        if (this.altarState != null) {
            tag.putInt("AltarState", this.altarState.ordinal());
        }
        if (this.owner != null) {
            tag.putUUID("Owner", this.owner);
        }
        if (this.recipe != null) {
            tag.put("Recipe", (INBT) this.recipe.serialize());
        }
        tag.putInt("InfusionTimer", this.infusionTimer);
        return tag;
    }

    public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
        this.load(state, tag);
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT tag = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), tag);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            protected void onContentsChanged(final int slot) {
                VaultAltarTileEntity.this.sendUpdates();
            }

            public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
                if (PlayerVaultAltarData.get((ServerWorld) VaultAltarTileEntity.this.level).getRecipe(VaultAltarTileEntity.this.owner) != null && !PlayerVaultAltarData.get((ServerWorld) VaultAltarTileEntity.this.level).getRecipe(VaultAltarTileEntity.this.owner).isComplete()) {
                    final List<RequiredItem> items = PlayerVaultAltarData.get((ServerWorld) VaultAltarTileEntity.this.level).getRecipe(VaultAltarTileEntity.this.owner).getRequiredItems();
                    for (final RequiredItem item : items) {
                        if (item.isItemEqual(stack)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Nonnull
            public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
                final PlayerVaultAltarData data = PlayerVaultAltarData.get((ServerWorld) VaultAltarTileEntity.this.level);
                final AltarInfusionRecipe recipe = data.getRecipe(VaultAltarTileEntity.this.owner);
                if (recipe != null && !recipe.isComplete()) {
                    final List<RequiredItem> items = recipe.getRequiredItems();
                    for (final RequiredItem item : items) {
                        if (item.reachedAmountRequired()) {
                            continue;
                        }
                        if (!item.isItemEqual(stack)) {
                            continue;
                        }
                        final int amount = stack.getCount();
                        final int excess = item.getRemainder(amount);
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
                return stack;
            }
        };
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) this.handler.cast();
        }
        return (LazyOptional<T>) super.getCapability((Capability) cap, side);
    }

    public enum AltarState {
        IDLE,
        ACCEPTING,
        COMPLETE,
        INFUSING;
    }
}
