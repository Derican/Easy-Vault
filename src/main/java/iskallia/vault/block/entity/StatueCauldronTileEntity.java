package iskallia.vault.block.entity;

import iskallia.vault.block.StatueCauldronBlock;
import iskallia.vault.block.item.LootStatueBlockItem;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.StatueType;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class StatueCauldronTileEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler itemHandler;
    private final LazyOptional<IItemHandler> handler;
    private final Predicate<ItemEntity> itemPredicate;
    private UUID owner;
    private int statueCount;
    private int requiredAmount;
    private final List<String> names;

    public List<String> getNames() {
        return this.names;
    }

    public StatueCauldronTileEntity() {
        super(ModBlocks.STATUE_CAULDRON_TILE_ENTITY);
        this.itemHandler = this.createHandler();
        this.handler = LazyOptional.of(() -> this.itemHandler);
        this.itemPredicate = (itemEntity -> itemEntity.getItem().getItem() instanceof LootStatueBlockItem);
        this.names = new ArrayList<String>();
    }

    public void setOwner(final UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setStatueCount(final int statueCount) {
        this.statueCount = statueCount;
    }

    public int getStatueCount() {
        return this.statueCount;
    }

    public void setRequiredAmount(final int requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public int getRequiredAmount() {
        return this.requiredAmount;
    }

    public void addName(final String name) {
        this.names.add(name);
    }

    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            final List<ItemEntity> statues = this.level.getLoadedEntitiesOfClass((Class) ItemEntity.class, new AxisAlignedBB(this.getBlockPos()).inflate(1.0, 1.0, 1.0), (Predicate) this.itemPredicate);
            for (final ItemEntity e : statues) {
                this.handler.ifPresent(h -> {
                    if (h.insertItem(0, e.getItem(), true).isEmpty()) {
                        e.remove();
                    }
                    this.setChanged();
                    this.sendUpdates();
                });
            }
            if (this.statueCount >= this.requiredAmount) {
                final List<String> nameList = new ArrayList<String>(this.names);
                Collections.shuffle(nameList);
                String name = (nameList.size() == 0) ? "iGoodie" : nameList.get(0);
                if (name == null || name.isEmpty()) {
                    name = "iGoodie";
                }
                final ItemStack statue = LootStatueBlockItem.getStatueBlockItem(name, StatueType.OMEGA);
                final ItemEntity itemEntity = new ItemEntity(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.2, this.getBlockPos().getZ() + 0.5, statue);
                this.level.addFreshEntity(itemEntity);
                this.level.setBlockAndUpdate(this.getBlockPos(), ModBlocks.STATUE_CAULDRON.defaultBlockState().setValue(StatueCauldronBlock.LEVEL, 0));
                this.statueCount = 0;
                this.names.clear();
                this.sendUpdates();
            }
        }
    }

    private void bubbleCauldron(final ServerWorld world) {
        final int particleCount = 100;
        world.playSound(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), ModSounds.CAULDRON_BUBBLES_SFX, SoundCategory.MASTER, 1.0f, (float) Math.random());
        world.sendParticles((IParticleData) ParticleTypes.WITCH, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, particleCount, 0.0, 0.0, 0.0, 3.141592653589793);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(64) {
            protected void onContentsChanged(final int slot) {
                StatueCauldronTileEntity.this.sendUpdates();
            }

            public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
                if (StatueCauldronTileEntity.this.getBlockState().getValue(CauldronBlock.LEVEL) != 3) {
                    return false;
                }
                if (StatueCauldronTileEntity.this.getStatueCount() >= StatueCauldronTileEntity.this.getRequiredAmount()) {
                    return false;
                }
                if (stack.getItem() instanceof LootStatueBlockItem) {
                    final StatueType type = MiscUtils.getEnumEntry(StatueType.class, stack.getOrCreateTag().getCompound("BlockEntityTag").getInt("StatueType"));
                    return type.doesStatueCauldronAccept();
                }
                return false;
            }

            @Nonnull
            public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
                if (simulate && this.isItemValid(slot, stack)) {
                    final int amount = ModConfigs.STATUE_RECYCLING.getItemValue(stack.getItem().getRegistryName().toString());
                    StatueCauldronTileEntity.this.statueCount = Math.min(StatueCauldronTileEntity.this.statueCount + amount, StatueCauldronTileEntity.this.requiredAmount);
                    final CompoundNBT tag = stack.getOrCreateTag();
                    final CompoundNBT blockData = tag.getCompound("BlockEntityTag");
                    final String name = blockData.getString("PlayerNickname");
                    if (!name.isEmpty()) {
                        for (int i = 0; i < amount; ++i) {
                            StatueCauldronTileEntity.this.addName(name);
                        }
                    }
                    if (StatueCauldronTileEntity.this.level != null && !StatueCauldronTileEntity.this.level.isClientSide) {
                        StatueCauldronTileEntity.this.bubbleCauldron((ServerWorld) StatueCauldronTileEntity.this.level);
                    }
                    StatueCauldronTileEntity.this.sendUpdates();
                    return ItemStack.EMPTY;
                }
                return stack;
            }
        };
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.handler.cast();
        }
        return (LazyOptional<T>) super.getCapability((Capability) cap, side);
    }

    public CompoundNBT save(final CompoundNBT compound) {
        if (this.owner != null) {
            compound.putUUID("Owner", this.owner);
        }
        final ListNBT nameList = new ListNBT();
        if (this.names != null && !this.names.isEmpty()) {
            int i = 0;
            for (final String name : this.names) {
                final CompoundNBT nameNbt = new CompoundNBT();
                nameNbt.putString("name" + i++, name);
                nameList.add(nameNbt);
            }
        }
        compound.put("NameList", nameList);
        compound.putInt("StatueCount", this.statueCount);
        compound.putInt("RequiredAmount", this.requiredAmount);
        return super.save(compound);
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        if (nbt.contains("Owner", 11)) {
            this.owner = nbt.getUUID("Owner");
        }
        final ListNBT nameList = nbt.getList("NameList", 10);
        int i = 0;
        this.names.clear();
        for (final INBT nameNbt : nameList) {
            this.names.add(((CompoundNBT) nameNbt).getString("name" + i++));
        }
        this.statueCount = nbt.getInt("StatueCount");
        this.requiredAmount = nbt.getInt("RequiredAmount");
        super.load(state, nbt);
    }

    public CompoundNBT getUpdateTag() {
        final CompoundNBT compound = super.getUpdateTag();
        if (this.owner != null) {
            compound.putUUID("Owner", this.owner);
        }
        final ListNBT nameList = new ListNBT();
        if (this.names != null && !this.names.isEmpty()) {
            int i = 0;
            for (final String name : this.names) {
                final CompoundNBT nameNbt = new CompoundNBT();
                nameNbt.putString("name" + i++, name);
                nameList.add(nameNbt);
            }
        }
        compound.put("NameList", nameList);
        compound.putInt("StatueCount", this.statueCount);
        compound.putInt("RequiredAmount", this.requiredAmount);
        return compound;
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

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 11);
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.setChanged();
    }

    public void setNames(final ListNBT nameList) {
        this.names.clear();
        int i = 0;
        for (final INBT nameNbt : nameList) {
            this.names.add(((CompoundNBT) nameNbt).getString("name" + i++));
        }
    }
}
