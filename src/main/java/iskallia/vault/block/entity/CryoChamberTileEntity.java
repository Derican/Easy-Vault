package iskallia.vault.block.entity;

import com.mojang.authlib.GameProfile;
import iskallia.vault.client.ClientEternalData;
import iskallia.vault.container.inventory.CryochamberContainer;
import iskallia.vault.entity.eternal.EternalData;
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.SkinProfile;
import iskallia.vault.vending.TraderCore;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CryoChamberTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    protected SkinProfile skin;
    private UUID owner;
    public List<String> coreNames;
    private int maxCores;
    private boolean infusing;
    private int infusionTimeRemaining;
    private boolean growingEternal;
    private int growEternalTimeRemaining;
    protected UUID eternalId;
    public float lastCoreCount;
    private final ItemStackHandler itemHandler;
    private final LazyOptional<IItemHandler> handler;

    protected CryoChamberTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.coreNames = new ArrayList<String>();
        this.maxCores = 0;
        this.infusing = false;
        this.infusionTimeRemaining = 0;
        this.growingEternal = false;
        this.growEternalTimeRemaining = 0;
        this.itemHandler = new ItemStackHandler(1) {
            protected void onContentsChanged(final int slot) {
                if (this.getStackInSlot(slot).getItem() == ModItems.TRADER_CORE) {
                    CryoChamberTileEntity.this.addTraderCore(ItemTraderCore.getCoreFromStack(this.getStackInSlot(slot)));
                    this.setStackInSlot(slot, ItemStack.EMPTY);
                }
                CryoChamberTileEntity.this.sendUpdates();
            }

            public boolean isItemValid(final int slot, final ItemStack stack) {
                return stack.getItem() == ModItems.TRADER_CORE && !CryoChamberTileEntity.this.isFull() && !CryoChamberTileEntity.this.isInfusing();
            }
        };
        this.handler = LazyOptional.of(() -> this.itemHandler);
        this.skin = new SkinProfile();
    }

    public CryoChamberTileEntity() {
        this(ModBlocks.CRYO_CHAMBER_TILE_ENTITY);
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(final UUID owner) {
        this.owner = owner;
    }

    public int getMaxCores() {
        return this.maxCores;
    }

    public void setMaxCores(final int maxCores) {
        this.maxCores = maxCores;
    }

    public boolean isInfusing() {
        return this.infusing;
    }

    public int getInfusionTimeRemaining() {
        return this.infusionTimeRemaining;
    }

    public boolean isGrowingEternal() {
        return this.growingEternal;
    }

    public int getGrowEternalTimeRemaining() {
        return this.growEternalTimeRemaining;
    }

    public SkinProfile getSkin() {
        return this.skin;
    }

    public int getCoreCount() {
        return this.coreNames.size();
    }

    public List<String> getCoreNames() {
        return this.coreNames;
    }

    public boolean addTraderCore(final TraderCore core) {
        if (this.isFull() || this.isInfusing() || this.getOwner() == null) {
            return false;
        }
        if (!(this.level instanceof ServerWorld)) {
            return false;
        }
        final ServerWorld sWorld = (ServerWorld) this.level;
        sWorld.playSound(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.SOUL_ESCAPE, SoundCategory.PLAYERS, 1.0f, 1.0f);
        final GameProfile knownProfile = sWorld.getServer().getProfileCache().get(this.getOwner());
        if (knownProfile == null) {
            return false;
        }
        final int eternals = EternalsData.get(sWorld).getEternals(this.getOwner()).getNonAncientEternalCount();
        final int cores = this.getMaxCores();
        final int newCores = ModConfigs.CRYO_CHAMBER.getPlayerCoreCount(knownProfile.getName(), eternals);
        if (cores != newCores) {
            this.setMaxCores(newCores);
            this.sendUpdates();
        }
        this.coreNames.add(core.getName());
        if (core.getTrade() != null && !core.getTrade().wasTradeUsed() && sWorld.random.nextFloat() < ModConfigs.CRYO_CHAMBER.getUnusedTraderRewardChance()) {
            final PlayerEntity player = sWorld.getNearestPlayer(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), 3.0, false);
            if (player instanceof ServerPlayerEntity) {
                MiscUtils.giveItem((ServerPlayerEntity) player, new ItemStack(ModItems.PANDORAS_BOX));
            } else {
                BlockPos.findClosestMatch(this.getBlockPos(), 7, 2, sWorld::isEmptyBlock).ifPresent(airPos -> Block.popResource(sWorld, airPos, new ItemStack(ModItems.PANDORAS_BOX)));
            }
        }
        this.infusing = true;
        this.infusionTimeRemaining = ModConfigs.CRYO_CHAMBER.getInfusionTime();
        this.sendUpdates();
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateSkin() {
        if (this.infusing && !this.coreNames.isEmpty()) {
            this.skin.updateSkin(this.coreNames.get(this.coreNames.size() - 1));
            return;
        }
        final EternalDataSnapshot snapshot = ClientEternalData.getSnapshot(this.getEternalId());
        if (snapshot == null || snapshot.getName() == null) {
            return;
        }
        this.skin.updateSkin(snapshot.getName());
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.setChanged();
    }

    @Nullable
    public EternalData getEternal() {
        if (this.getLevel() == null) {
            return null;
        }
        if (this.getLevel().isClientSide()) {
            return null;
        }
        if (this.eternalId == null) {
            return null;
        }
        return EternalsData.get((ServerWorld) this.getLevel()).getEternals(this.owner).get(this.eternalId);
    }

    public UUID getEternalId() {
        return this.eternalId;
    }

    protected boolean isFull() {
        return !this.coreNames.isEmpty() && this.coreNames.size() >= this.maxCores;
    }

    public void tick() {
        if (this.level == null || this.level.isClientSide || this.owner == null) {
            return;
        }
        if (this.isFull() && !this.growingEternal && this.eternalId == null) {
            this.growingEternal = true;
            this.growEternalTimeRemaining = ModConfigs.CRYO_CHAMBER.getGrowEternalTime();
        }
        if (this.isFull() && !this.growingEternal && this.level.getGameTime() % 40L == 0L) {
            this.level.playSound(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.CONDUIT_AMBIENT, SoundCategory.PLAYERS, 0.25f, 1.0f);
        }
        if (this.infusing) {
            if (this.infusionTimeRemaining-- <= 0) {
                this.infusionTimeRemaining = 0;
                this.infusing = false;
            }
            this.sendUpdates();
        } else if (this.growingEternal) {
            if (this.growEternalTimeRemaining-- <= 0) {
                this.growEternalTimeRemaining = 0;
                this.growingEternal = false;
                this.createEternal();
            }
            this.sendUpdates();
        }
    }

    private void createEternal() {
        final EternalsData.EternalGroup eternals = EternalsData.get((ServerWorld) this.getLevel()).getEternals(this.owner);
        int attempts = 100;
        String name;
        do {
            --attempts;
            name = this.coreNames.get(this.getLevel().getRandom().nextInt(this.coreNames.size()));
        } while (attempts > 0 && eternals.containsEternal(name));
        this.eternalId = EternalsData.get((ServerWorld) this.getLevel()).add(this.owner, name, false);
    }

    public ITextComponent getDisplayName() {
        final EternalData eternal = this.getEternal();
        if (eternal != null) {
            return new StringTextComponent(eternal.getName());
        }
        return new StringTextComponent("Cryo Chamber");
    }

    @Nullable
    public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player) {
        if (this.getLevel() == null) {
            return null;
        }
        return new CryochamberContainer(windowId, this.getLevel(), this.getBlockPos(), playerInventory);
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        super.save(nbt);
        if (this.owner != null) {
            nbt.putUUID("Owner", this.owner);
        }
        if (this.eternalId != null) {
            nbt.putUUID("EternalId", this.eternalId);
        }
        if (!this.coreNames.isEmpty()) {
            final ListNBT list = new ListNBT();
            for (int i = 0; i < this.coreNames.size(); ++i) {
                final CompoundNBT nameNbt = new CompoundNBT();
                final String name = this.coreNames.get(i);
                nameNbt.putString("name" + i, name);
                list.add(nameNbt);
            }
            nbt.put("CoresList", list);
        }
        nbt.putInt("MaxCoreCount", this.maxCores);
        nbt.putBoolean("Infusing", this.infusing);
        nbt.putInt("InfusionTimeRemaining", this.infusionTimeRemaining);
        nbt.putBoolean("GrowingEternal", this.growingEternal);
        nbt.putInt("GrowEternalTimeRemaining", this.growEternalTimeRemaining);
        nbt.put("Inventory", this.itemHandler.serializeNBT());
        return nbt;
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.contains("Owner")) {
            this.owner = nbt.getUUID("Owner");
        }
        if (nbt.contains("EternalId")) {
            this.eternalId = nbt.getUUID("EternalId");
        }
        if (nbt.contains("CoresList")) {
            final ListNBT list = nbt.getList("CoresList", 10);
            this.coreNames = new LinkedList<String>();
            for (int i = 0; i < list.size(); ++i) {
                final CompoundNBT nameTag = list.getCompound(i);
                this.coreNames.add(nameTag.getString("name" + i));
            }
        }
        this.maxCores = nbt.getInt("MaxCoreCount");
        this.infusing = nbt.getBoolean("Infusing");
        this.infusionTimeRemaining = nbt.getInt("InfusionTimeRemaining");
        this.growingEternal = nbt.getBoolean("GrowingEternal");
        this.growEternalTimeRemaining = nbt.getInt("GrowEternalTimeRemaining");
        this.itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        return (LazyOptional<T>) ((cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? this.handler.cast() : super.getCapability((Capability) cap, side));
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
        this.load(state, tag);
    }

    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT nbt = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), nbt);
    }

    public CompoundNBT getRenameNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        final EternalData eternal = this.getEternal();
        if (eternal == null) {
            return nbt;
        }
        nbt.put("BlockPos", NBTUtil.writeBlockPos(this.getBlockPos()));
        nbt.putString("EternalName", eternal.getName());
        return nbt;
    }

    public void renameEternal(final String name) {
        if (this.getEternal() != null) {
            this.getEternal().setName(name);
        }
    }
}
