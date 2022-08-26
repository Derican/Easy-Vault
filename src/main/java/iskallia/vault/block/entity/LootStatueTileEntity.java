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

public class LootStatueTileEntity extends SkinnableTileEntity implements ITickableTileEntity {
    private StatueType statueType;
    private int currentTick;
    private int itemsRemaining;
    private int totalItems;
    private ItemStack lootItem;
    private boolean master;
    private BlockPos masterPos;
    private int chipCount;
    private float playerScale;

    protected LootStatueTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.currentTick = 0;
        this.itemsRemaining = 0;
        this.totalItems = 0;
        this.lootItem = ItemStack.EMPTY;
        this.chipCount = 0;
    }

    public LootStatueTileEntity() {
        this(ModBlocks.LOOT_STATUE_TILE_ENTITY);
    }

    public float getPlayerScale() {
        return this.playerScale;
    }

    public void setPlayerScale(final float playerScale) {
        this.playerScale = playerScale;
    }

    public boolean isMaster() {
        return this.master;
    }

    public void setMaster(final boolean master) {
        this.master = master;
    }

    public BlockPos getMasterPos() {
        return this.masterPos;
    }

    public void setMasterPos(final BlockPos masterPos) {
        this.masterPos = masterPos;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public void setCurrentTick(final int currentTick) {
        this.currentTick = currentTick;
    }

    @Nonnull
    public ItemStack getLootItem() {
        return this.lootItem;
    }

    public void setLootItem(@Nonnull final ItemStack stack) {
        this.lootItem = stack;
        this.setChanged();
        this.sendUpdates();
    }

    @Override
    protected void updateSkin() {
    }

    public StatueType getStatueType() {
        return this.statueType;
    }

    public void setStatueType(final StatueType statueType) {
        this.statueType = statueType;
    }

    public boolean addChip() {
        if (!this.statueType.isOmega() || this.chipCount >= ModConfigs.STATUE_LOOT.getMaxAccelerationChips()) {
            return false;
        }
        ++this.chipCount;
        this.sendUpdates();
        return true;
    }

    public ItemStack removeChip() {
        ItemStack stack = ItemStack.EMPTY;
        if (this.chipCount > 0) {
            --this.chipCount;
            stack = new ItemStack(ModItems.ACCELERATION_CHIP, 1);
            this.sendUpdates();
        }
        return stack;
    }

    public int getChipCount() {
        return this.chipCount;
    }

    public int getItemsRemaining() {
        return this.itemsRemaining;
    }

    public void setItemsRemaining(final int itemsRemaining) {
        this.itemsRemaining = itemsRemaining;
    }

    public int getTotalItems() {
        return this.totalItems;
    }

    public void setTotalItems(final int totalItems) {
        this.totalItems = totalItems;
    }

    public void tick() {
        if (this.level == null || this.level.isClientSide || this.itemsRemaining == 0 || !this.statueType.dropsItems()) {
            return;
        }
        if (this.statueType == StatueType.OMEGA && !this.master) {
            return;
        }
        if (this.currentTick++ >= this.getModifiedInterval()) {
            this.currentTick = 0;
            if (!this.lootItem.isEmpty()) {
                final ItemStack stack = this.lootItem.copy();
                if (this.poopItem(stack, false)) {
                    this.setChanged();
                    this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
                }
            }
        }
    }

    private int getModifiedInterval() {
        final int interval = ModConfigs.STATUE_LOOT.getInterval(this.getStatueType());
        if (this.getChipCount() == 0 || !this.getStatueType().isOmega()) {
            return interval;
        }
        return interval - ModConfigs.STATUE_LOOT.getIntervalDecrease(this.getChipCount());
    }

    public boolean poopItem(final ItemStack stack, final boolean simulate) {
        assert this.level != null;
        final BlockPos down = this.getBlockPos().below();
        if (this.statueType == StatueType.OMEGA) {
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    final BlockPos offset = down.offset(x, 0, z);
                    final TileEntity tileEntity = this.level.getBlockEntity(offset);
                    if (tileEntity != null) {
                        final LazyOptional<IItemHandler> handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
                        if (handler.isPresent()) {
                            final ItemStack remainder = ItemHandlerHelper.insertItemStacked(handler.orElse(null), stack, true);
                            if (remainder.isEmpty()) {
                                ItemHandlerHelper.insertItemStacked(handler.orElse(null), stack, false);
                                if (this.itemsRemaining != -1) {
                                    --this.itemsRemaining;
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            final TileEntity tileEntity2 = this.level.getBlockEntity(down);
            if (tileEntity2 != null) {
                final LazyOptional<IItemHandler> handler2 = tileEntity2.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
                handler2.ifPresent(h -> {
                    ItemHandlerHelper.insertItemStacked(h, stack, simulate);
                    if (this.itemsRemaining != -1) {
                        --this.itemsRemaining;
                    }
                });
                return true;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0.0, 6.0, 0.0);
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        if (this.statueType == null) {
            return super.save(nbt);
        }
        nbt.putInt("StatueType", this.getStatueType().ordinal());
        if (this.statueType == StatueType.OMEGA) {
            if (!this.master) {
                nbt.putBoolean("Master", false);
                nbt.put("MasterPos", NBTUtil.writeBlockPos(this.masterPos));
                return super.save(nbt);
            }
            nbt.putBoolean("Master", true);
            nbt.put("MasterPos", NBTUtil.writeBlockPos(this.getBlockPos()));
        }
        final String nickname = this.skin.getLatestNickname();
        nbt.putString("PlayerNickname", (nickname == null) ? "" : nickname);
        nbt.putInt("CurrentTick", this.getCurrentTick());
        nbt.put("LootItem", this.getLootItem().serializeNBT());
        nbt.putInt("ChipCount", this.chipCount);
        nbt.putInt("ItemsRemaining", this.itemsRemaining);
        nbt.putInt("TotalItems", this.totalItems);
        nbt.putFloat("playerScale", this.playerScale);
        return super.save(nbt);
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        if (!nbt.contains("StatueType", 3)) {
            throw new IllegalStateException("Invalid State NBT " + nbt);
        }
        this.setStatueType(StatueType.values()[nbt.getInt("StatueType")]);
        if (this.statueType == StatueType.OMEGA) {
            if (!nbt.getBoolean("Master")) {
                this.master = false;
                this.masterPos = NBTUtil.readBlockPos(nbt.getCompound("MasterPos"));
                super.load(state, nbt);
                return;
            }
            this.master = true;
            this.masterPos = this.getBlockPos();
        }
        final String nickname = nbt.getString("PlayerNickname");
        this.skin.updateSkin(nickname);
        this.lootItem = ItemStack.of(nbt.getCompound("LootItem"));
        this.setCurrentTick(nbt.getInt("CurrentTick"));
        this.chipCount = nbt.getInt("ChipCount");
        this.itemsRemaining = nbt.getInt("ItemsRemaining");
        this.totalItems = nbt.getInt("TotalItems");
        if (nbt.contains("playerScale")) {
            this.playerScale = nbt.getFloat("playerScale");
        } else {
            this.playerScale = MathUtilities.randomFloat(2.0f, 4.0f);
        }
        super.load(state, nbt);
    }

    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        if (this.getStatueType() == null) {
            return nbt;
        }
        nbt.putInt("StatueType", this.getStatueType().ordinal());
        if (this.statueType == StatueType.OMEGA) {
            if (!this.master) {
                nbt.putBoolean("Master", false);
                nbt.put("MasterPos", NBTUtil.writeBlockPos(this.masterPos));
                return nbt;
            }
            nbt.putBoolean("Master", true);
            nbt.put("MasterPos", NBTUtil.writeBlockPos(this.getBlockPos()));
        }
        final String nickname = this.skin.getLatestNickname();
        nbt.putString("PlayerNickname", (nickname == null) ? "" : nickname);
        nbt.putInt("CurrentTick", this.getCurrentTick());
        nbt.put("LootItem", this.getLootItem().serializeNBT());
        nbt.putInt("ChipCount", this.chipCount);
        nbt.putInt("ItemsRemaining", this.itemsRemaining);
        nbt.putInt("TotalItems", this.totalItems);
        nbt.putFloat("playerScale", this.playerScale);
        return nbt;
    }
}
