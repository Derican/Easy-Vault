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

public class CryoChamberTileEntity
        extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    protected SkinProfile skin;
    private UUID owner;
    public List<String> coreNames = new ArrayList<>();
    private int maxCores = 0;

    private boolean infusing = false;
    private int infusionTimeRemaining = 0;
    private boolean growingEternal = false;
    private int growEternalTimeRemaining = 0;

    protected UUID eternalId;
    public float lastCoreCount;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        protected void onContentsChanged(int slot) {
            if (getStackInSlot(slot).getItem() == ModItems.TRADER_CORE) {
                CryoChamberTileEntity.this.addTraderCore(ItemTraderCore.getCoreFromStack(getStackInSlot(slot)));
                setStackInSlot(slot, ItemStack.EMPTY);
            }

            CryoChamberTileEntity.this.sendUpdates();
        }


        public boolean isItemValid(int slot, ItemStack stack) {
            return (stack.getItem() == ModItems.TRADER_CORE && !CryoChamberTileEntity.this.isFull() && !CryoChamberTileEntity.this.isInfusing());
        }
    };

    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> this.itemHandler);

    protected CryoChamberTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.skin = new SkinProfile();
    }

    public CryoChamberTileEntity() {
        this(ModBlocks.CRYO_CHAMBER_TILE_ENTITY);
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public int getMaxCores() {
        return this.maxCores;
    }

    public void setMaxCores(int maxCores) {
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

    public boolean addTraderCore(TraderCore core) {
        if (isFull() || isInfusing() || getOwner() == null) {
            return false;
        }
        if (!(this.level instanceof ServerWorld)) {
            return false;
        }
        ServerWorld sWorld = (ServerWorld) this.level;
        sWorld.playSound(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.SOUL_ESCAPE, SoundCategory.PLAYERS, 1.0F, 1.0F);

        GameProfile knownProfile = sWorld.getServer().getProfileCache().get(getOwner());
        if (knownProfile == null) {
            return false;
        }
        int eternals = EternalsData.get(sWorld).getEternals(getOwner()).getNonAncientEternalCount();

        int cores = getMaxCores();
        int newCores = ModConfigs.CRYO_CHAMBER.getPlayerCoreCount(knownProfile.getName(), eternals);
        if (cores != newCores) {
            setMaxCores(newCores);
            sendUpdates();
        }

        this.coreNames.add(core.getName());

        if (core.getTrade() != null && !core.getTrade().wasTradeUsed() && sWorld.random.nextFloat() < ModConfigs.CRYO_CHAMBER.getUnusedTraderRewardChance()) {
            PlayerEntity player = sWorld.getNearestPlayer(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), 3.0D, false);
            if (player instanceof ServerPlayerEntity) {
                MiscUtils.giveItem((ServerPlayerEntity) player, new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));
            } else {
                BlockPos.findClosestMatch(getBlockPos(), 7, 2, sWorld::isEmptyBlock).ifPresent(airPos -> Block.popResource((World) sWorld, airPos, new ItemStack((IItemProvider) ModItems.PANDORAS_BOX)));
            }
        }


        this.infusing = true;
        this.infusionTimeRemaining = ModConfigs.CRYO_CHAMBER.getInfusionTime();
        sendUpdates();
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateSkin() {
        if (this.infusing && !this.coreNames.isEmpty()) {
            this.skin.updateSkin(this.coreNames.get(this.coreNames.size() - 1));
            return;
        }
        EternalDataSnapshot snapshot = ClientEternalData.getSnapshot(getEternalId());
        if (snapshot == null || snapshot.getName() == null) {
            return;
        }
        this.skin.updateSkin(snapshot.getName());
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
        setChanged();
    }

    @Nullable
    public EternalData getEternal() {
        if (getLevel() == null) {
            return null;
        }
        if (!getLevel().isClientSide()) {
            if (this.eternalId == null) {
                return null;
            }
            return EternalsData.get((ServerWorld) getLevel()).getEternals(this.owner).get(this.eternalId);
        }
        return null;
    }

    public UUID getEternalId() {
        return this.eternalId;
    }

    protected boolean isFull() {
        return (!this.coreNames.isEmpty() && this.coreNames.size() >= this.maxCores);
    }


    public void tick() {
        if (this.level == null || this.level.isClientSide || this.owner == null)
            return;
        if (isFull() && !this.growingEternal && this.eternalId == null) {
            this.growingEternal = true;
            this.growEternalTimeRemaining = ModConfigs.CRYO_CHAMBER.getGrowEternalTime();
        }

        if (isFull() && !this.growingEternal && this.level.getGameTime() % 40L == 0L) {
            this.level.playSound(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.CONDUIT_AMBIENT, SoundCategory.PLAYERS, 0.25F, 1.0F);
        }

        if (this.infusing) {
            if (this.infusionTimeRemaining-- <= 0) {
                this.infusionTimeRemaining = 0;
                this.infusing = false;
            }

            sendUpdates();
        } else if (this.growingEternal) {
            if (this.growEternalTimeRemaining-- <= 0) {
                this.growEternalTimeRemaining = 0;
                this.growingEternal = false;
                createEternal();
            }

            sendUpdates();
        }
    }

    private void createEternal() {
        String name;
        EternalsData.EternalGroup eternals = EternalsData.get((ServerWorld) getLevel()).getEternals(this.owner);

        int attempts = 100;
        do {
            attempts--;
            name = this.coreNames.get(getLevel().getRandom().nextInt(this.coreNames.size()));
        } while (attempts > 0 && eternals.containsEternal(name));
        this.eternalId = EternalsData.get((ServerWorld) getLevel()).add(this.owner, name, false);
    }


    public ITextComponent getDisplayName() {
        EternalData eternal = getEternal();
        if (eternal != null) {
            return (ITextComponent) new StringTextComponent(eternal.getName());
        }
        return (ITextComponent) new StringTextComponent("Cryo Chamber");
    }


    @Nullable
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        if (getLevel() == null) {
            return null;
        }
        return (Container) new CryochamberContainer(windowId, getLevel(), getBlockPos(), playerInventory);
    }


    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if (this.owner != null) nbt.putUUID("Owner", this.owner);
        if (this.eternalId != null) nbt.putUUID("EternalId", this.eternalId);

        if (!this.coreNames.isEmpty()) {
            ListNBT list = new ListNBT();
            for (int i = 0; i < this.coreNames.size(); i++) {
                CompoundNBT nameNbt = new CompoundNBT();
                String name = this.coreNames.get(i);
                nameNbt.putString("name" + i, name);
                list.add(nameNbt);
            }
            nbt.put("CoresList", (INBT) list);
        }
        nbt.putInt("MaxCoreCount", this.maxCores);
        nbt.putBoolean("Infusing", this.infusing);
        nbt.putInt("InfusionTimeRemaining", this.infusionTimeRemaining);
        nbt.putBoolean("GrowingEternal", this.growingEternal);
        nbt.putInt("GrowEternalTimeRemaining", this.growEternalTimeRemaining);
        nbt.put("Inventory", (INBT) this.itemHandler.serializeNBT());
        return nbt;
    }


    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.contains("Owner")) this.owner = nbt.getUUID("Owner");
        if (nbt.contains("EternalId")) this.eternalId = nbt.getUUID("EternalId");

        if (nbt.contains("CoresList")) {
            ListNBT list = nbt.getList("CoresList", 10);
            this.coreNames = new LinkedList<>();
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT nameTag = list.getCompound(i);
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? this.handler.cast() : super.getCapability(cap, side);
    }


    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }


    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }


    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        handleUpdateTag(getBlockState(), nbt);
    }

    public CompoundNBT getRenameNBT() {
        CompoundNBT nbt = new CompoundNBT();
        EternalData eternal = getEternal();
        if (eternal == null) {
            return nbt;
        }
        nbt.put("BlockPos", (INBT) NBTUtil.writeBlockPos(getBlockPos()));
        nbt.putString("EternalName", eternal.getName());
        return nbt;
    }

    public void renameEternal(String name) {
        if (getEternal() != null)
            getEternal().setName(name);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\CryoChamberTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */