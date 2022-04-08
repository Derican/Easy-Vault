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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.Property;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
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
import java.util.*;
import java.util.function.Predicate;

public class StatueCauldronTileEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> this.itemHandler);

    private final Predicate<ItemEntity> itemPredicate;
    private UUID owner;
    private int statueCount;
    private int requiredAmount;
    private List<String> names;

    public List<String> getNames() {
        return this.names;
    }

    public StatueCauldronTileEntity() {
        super(ModBlocks.STATUE_CAULDRON_TILE_ENTITY);
        this.itemPredicate = (itemEntity -> itemEntity.getItem().getItem() instanceof LootStatueBlockItem);
        this.names = new ArrayList<>();
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setStatueCount(int statueCount) {
        this.statueCount = statueCount;
    }

    public int getStatueCount() {
        return this.statueCount;
    }

    public void setRequiredAmount(int requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public int getRequiredAmount() {
        return this.requiredAmount;
    }

    public void addName(String name) {
        this.names.add(name);
    }


    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            List<ItemEntity> statues = this.level.getLoadedEntitiesOfClass(ItemEntity.class, (new AxisAlignedBB(

                    getBlockPos())).inflate(1.0D, 1.0D, 1.0D), this.itemPredicate);


            for (Iterator<ItemEntity> iterator = statues.iterator(); iterator.hasNext(); ) {
                ItemEntity e = iterator.next();
                this.handler.ifPresent(h -> {
                    if (h.insertItem(0, e.getItem(), true).isEmpty())
                        e.remove();
                    setChanged();
                    sendUpdates();
                });
            }

            if (this.statueCount >= this.requiredAmount) {
                List<String> nameList = new ArrayList<>(this.names);
                Collections.shuffle(nameList);
                String name = (nameList.size() == 0) ? "iGoodie" : nameList.get(0);
                if (name == null || name.isEmpty()) name = "iGoodie";
                ItemStack statue = LootStatueBlockItem.getStatueBlockItem(name, StatueType.OMEGA);


                ItemEntity itemEntity = new ItemEntity(this.level, getBlockPos().getX() + 0.5D, getBlockPos().getY() + 1.2D, getBlockPos().getZ() + 0.5D, statue);


                this.level.addFreshEntity((Entity) itemEntity);


                this.level.setBlockAndUpdate(getBlockPos(), (BlockState) ModBlocks.STATUE_CAULDRON.defaultBlockState().setValue((Property) StatueCauldronBlock.LEVEL, Integer.valueOf(0)));
                this.statueCount = 0;
                this.names.clear();
                sendUpdates();
            }
        }
    }

    private void bubbleCauldron(ServerWorld world) {
        int particleCount = 100;

        world.playSound(null, this.worldPosition
                        .getX(), this.worldPosition.getY(), this.worldPosition.getZ(), ModSounds.CAULDRON_BUBBLES_SFX, SoundCategory.MASTER, 1.0F,

                (float) Math.random());

        world.sendParticles((IParticleData) ParticleTypes.WITCH, this.worldPosition
                .getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D, particleCount, 0.0D, 0.0D, 0.0D, Math.PI);
    }


    private ItemStackHandler createHandler() {
        return new ItemStackHandler(64) {
            protected void onContentsChanged(int slot) {
                StatueCauldronTileEntity.this.sendUpdates();
            }


            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (((Integer) StatueCauldronTileEntity.this.getBlockState().getValue((Property) CauldronBlock.LEVEL)).intValue() != 3)
                    return false;
                if (StatueCauldronTileEntity.this.getStatueCount() >= StatueCauldronTileEntity.this.getRequiredAmount())
                    return false;
                if (stack.getItem() instanceof LootStatueBlockItem) {
                    StatueType type = (StatueType) MiscUtils.getEnumEntry(StatueType.class, stack.getOrCreateTag().getCompound("BlockEntityTag").getInt("StatueType"));
                    return type.doesStatueCauldronAccept();
                }
                return false;
            }


            @Nonnull
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (simulate && isItemValid(slot, stack)) {
                    int amount = ModConfigs.STATUE_RECYCLING.getItemValue(stack.getItem().getRegistryName().toString());
                    StatueCauldronTileEntity.this.statueCount = Math.min(StatueCauldronTileEntity.this.statueCount + amount, StatueCauldronTileEntity.this.requiredAmount);
                    CompoundNBT tag = stack.getOrCreateTag();
                    CompoundNBT blockData = tag.getCompound("BlockEntityTag");
                    String name = blockData.getString("PlayerNickname");
                    if (!name.isEmpty())
                        for (int i = 0; i < amount; ) {
                            StatueCauldronTileEntity.this.addName(name);
                            i++;
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.handler.cast();
        }
        return super.getCapability(cap, side);
    }


    public CompoundNBT save(CompoundNBT compound) {
        if (this.owner != null) compound.putUUID("Owner", this.owner);
        ListNBT nameList = new ListNBT();
        if (this.names != null && !this.names.isEmpty()) {
            int i = 0;
            for (String name : this.names) {
                CompoundNBT nameNbt = new CompoundNBT();
                nameNbt.putString("name" + i++, name);
                nameList.add(nameNbt);
            }
        }
        compound.put("NameList", (INBT) nameList);
        compound.putInt("StatueCount", this.statueCount);
        compound.putInt("RequiredAmount", this.requiredAmount);
        return super.save(compound);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("Owner", 11)) {
            this.owner = nbt.getUUID("Owner");
        }

        ListNBT nameList = nbt.getList("NameList", 10);
        int i = 0;
        this.names.clear();
        for (INBT nameNbt : nameList) {
            this.names.add(((CompoundNBT) nameNbt).getString("name" + i++));
        }

        this.statueCount = nbt.getInt("StatueCount");
        this.requiredAmount = nbt.getInt("RequiredAmount");
        super.load(state, nbt);
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        if (this.owner != null) compound.putUUID("Owner", this.owner);
        ListNBT nameList = new ListNBT();
        if (this.names != null && !this.names.isEmpty()) {
            int i = 0;
            for (String name : this.names) {
                CompoundNBT nameNbt = new CompoundNBT();
                nameNbt.putString("name" + i++, name);
                nameList.add(nameNbt);
            }
        }

        compound.put("NameList", (INBT) nameList);
        compound.putInt("StatueCount", this.statueCount);
        compound.putInt("RequiredAmount", this.requiredAmount);
        return compound;
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

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 11);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
        setChanged();
    }

    public void setNames(ListNBT nameList) {
        this.names.clear();
        int i = 0;
        for (INBT nameNbt : nameList)
            this.names.add(((CompoundNBT) nameNbt).getString("name" + i++));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\StatueCauldronTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */