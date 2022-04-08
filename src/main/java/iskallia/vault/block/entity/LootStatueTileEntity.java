package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.StatueType;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class LootStatueTileEntity
        extends SkinnableTileEntity
        implements ITickableTileEntity {
    private StatueType statueType;
    private int currentTick = 0;
    private int itemsRemaining = 0;
    private int totalItems = 0;

    private ItemStack lootItem = ItemStack.EMPTY;

    private boolean master;
    private BlockPos masterPos;
    private int chipCount = 0;
    private float playerScale;

    protected LootStatueTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public LootStatueTileEntity() {
        this(ModBlocks.LOOT_STATUE_TILE_ENTITY);
    }

    public float getPlayerScale() {
        return this.playerScale;
    }

    public void setPlayerScale(float playerScale) {
        this.playerScale = playerScale;
    }

    public boolean isMaster() {
        return this.master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public BlockPos getMasterPos() {
        return this.masterPos;
    }

    public void setMasterPos(BlockPos masterPos) {
        this.masterPos = masterPos;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    @Nonnull
    public ItemStack getLootItem() {
        return this.lootItem;
    }

    public void setLootItem(@Nonnull ItemStack stack) {
        this.lootItem = stack;
        setChanged();
        sendUpdates();
    }


    protected void updateSkin() {
    }


    public StatueType getStatueType() {
        return this.statueType;
    }

    public void setStatueType(StatueType statueType) {
        this.statueType = statueType;
    }

    public boolean addChip() {
        if (!this.statueType.isOmega() || this.chipCount >= ModConfigs.STATUE_LOOT.getMaxAccelerationChips()) {
            return false;
        }
        this.chipCount++;
        sendUpdates();
        return true;
    }

    public ItemStack removeChip() {
        ItemStack stack = ItemStack.EMPTY;
        if (this.chipCount > 0) {
            this.chipCount--;
            stack = new ItemStack((IItemProvider) ModItems.ACCELERATION_CHIP, 1);
            sendUpdates();
        }
        return stack;
    }

    public int getChipCount() {
        return this.chipCount;
    }

    public int getItemsRemaining() {
        return this.itemsRemaining;
    }

    public void setItemsRemaining(int itemsRemaining) {
        this.itemsRemaining = itemsRemaining;
    }

    public int getTotalItems() {
        return this.totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }


    public void tick() {
        if (this.level == null || this.level.isClientSide || this.itemsRemaining == 0 || !this.statueType.dropsItems())
            return;
        if (this.statueType == StatueType.OMEGA &&
                !this.master) {
            return;
        }
        if (this.currentTick++ >= getModifiedInterval()) {
            this.currentTick = 0;
            if (!this.lootItem.isEmpty()) {
                ItemStack stack = this.lootItem.copy();
                if (poopItem(stack, false)) {
                    setChanged();
                    getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
        }
    }

    private int getModifiedInterval() {
        int interval = ModConfigs.STATUE_LOOT.getInterval(getStatueType());
        if (getChipCount() == 0 || !getStatueType().isOmega()) {
            return interval;
        }
        return interval - ModConfigs.STATUE_LOOT.getIntervalDecrease(getChipCount());
    }

    public boolean poopItem(ItemStack stack, boolean simulate) {
        assert this.level != null;
        BlockPos down = getBlockPos().below();
        if (this.statueType == StatueType.OMEGA) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos offset = down.offset(x, 0, z);
                    TileEntity tileEntity = this.level.getBlockEntity(offset);
                    if (tileEntity != null) {
                        LazyOptional<IItemHandler> handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
                        if (handler.isPresent()) {
                            ItemStack remainder = ItemHandlerHelper.insertItemStacked((IItemHandler) handler.orElse(null), stack, true);
                            if (remainder.isEmpty()) {
                                ItemHandlerHelper.insertItemStacked((IItemHandler) handler.orElse(null), stack, false);
                                if (this.itemsRemaining != -1) {
                                    this.itemsRemaining--;
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            TileEntity tileEntity = this.level.getBlockEntity(down);
            if (tileEntity != null) {
                LazyOptional<IItemHandler> handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
                handler.ifPresent(h -> {
                    ItemHandlerHelper.insertItemStacked(h, stack, simulate);
                    if (this.itemsRemaining != -1) {
                        this.itemsRemaining--;
                    }
                });
                return true;
            }
        }
        return false;
    }


    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0.0D, 6.0D, 0.0D);
    }


    public CompoundNBT save(CompoundNBT nbt) {
        if (this.statueType == null) return super.save(nbt);

        nbt.putInt("StatueType", getStatueType().ordinal());

        if (this.statueType == StatueType.OMEGA) {
            if (this.master) {
                nbt.putBoolean("Master", true);
                nbt.put("MasterPos", (INBT) NBTUtil.writeBlockPos(getBlockPos()));
            } else {

                nbt.putBoolean("Master", false);
                nbt.put("MasterPos", (INBT) NBTUtil.writeBlockPos(this.masterPos));
                return super.save(nbt);
            }
        }

        String nickname = this.skin.getLatestNickname();
        nbt.putString("PlayerNickname", (nickname == null) ? "" : nickname);
        nbt.putInt("CurrentTick", getCurrentTick());
        nbt.put("LootItem", (INBT) getLootItem().serializeNBT());
        nbt.putInt("ChipCount", this.chipCount);
        nbt.putInt("ItemsRemaining", this.itemsRemaining);
        nbt.putInt("TotalItems", this.totalItems);
        nbt.putFloat("playerScale", this.playerScale);

        return super.save(nbt);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        if (!nbt.contains("StatueType", 3)) {
            throw new IllegalStateException("Invalid State NBT " + nbt.toString());
        }

        setStatueType(StatueType.values()[nbt.getInt("StatueType")]);

        if (this.statueType == StatueType.OMEGA) {
            if (nbt.getBoolean("Master")) {
                this.master = true;
                this.masterPos = getBlockPos();
            } else {
                this.master = false;
                this.masterPos = NBTUtil.readBlockPos(nbt.getCompound("MasterPos"));
                super.load(state, nbt);

                return;
            }
        }
        String nickname = nbt.getString("PlayerNickname");
        this.skin.updateSkin(nickname);
        this.lootItem = ItemStack.of(nbt.getCompound("LootItem"));
        setCurrentTick(nbt.getInt("CurrentTick"));
        this.chipCount = nbt.getInt("ChipCount");
        this.itemsRemaining = nbt.getInt("ItemsRemaining");
        this.totalItems = nbt.getInt("TotalItems");

        if (nbt.contains("playerScale")) {
            this.playerScale = nbt.getFloat("playerScale");
        } else {
            this.playerScale = MathUtilities.randomFloat(2.0F, 4.0F);
        }


        super.load(state, nbt);
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        if (getStatueType() == null) {
            return nbt;
        }

        nbt.putInt("StatueType", getStatueType().ordinal());

        if (this.statueType == StatueType.OMEGA) {
            if (this.master) {
                nbt.putBoolean("Master", true);
                nbt.put("MasterPos", (INBT) NBTUtil.writeBlockPos(getBlockPos()));
            } else {
                nbt.putBoolean("Master", false);
                nbt.put("MasterPos", (INBT) NBTUtil.writeBlockPos(this.masterPos));
                return nbt;
            }
        }

        String nickname = this.skin.getLatestNickname();
        nbt.putString("PlayerNickname", (nickname == null) ? "" : nickname);
        nbt.putInt("CurrentTick", getCurrentTick());
        nbt.put("LootItem", (INBT) getLootItem().serializeNBT());
        nbt.putInt("ChipCount", this.chipCount);
        nbt.putInt("ItemsRemaining", this.itemsRemaining);
        nbt.putInt("TotalItems", this.totalItems);
        nbt.putFloat("playerScale", this.playerScale);

        return nbt;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\LootStatueTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */