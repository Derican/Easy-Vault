package iskallia.vault.entity;

import iskallia.vault.Vault;
import iskallia.vault.block.entity.EtchingVendorControllerTileEntity;
import iskallia.vault.container.inventory.EtchingTradeContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class EtchingVendorEntity extends MobEntity {
    private static final DataParameter<BlockPos> VENDOR_POS;

    public EtchingVendorEntity(final EntityType<? extends MobEntity> type, final World world) {
        super(type, world);
        this.setInvulnerable(true);
        this.setNoGravity(true);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EtchingVendorEntity.VENDOR_POS, BlockPos.ZERO);
    }

    protected void registerGoals() {
        super.registerGoals();
//        this.goalSelector = new GoalSelector(this.level.getProfilerSupplier());
//        this.targetSelector = new GoalSelector(this.level.getProfilerSupplier());
        this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
    }

    public void setVendorPos(final BlockPos pos) {
        this.entityData.set(EtchingVendorEntity.VENDOR_POS, pos);
    }

    public BlockPos getVendorPos() {
        return (BlockPos) this.entityData.get((DataParameter) EtchingVendorEntity.VENDOR_POS);
    }

    public void tick() {
        super.tick();
        this.dropLeash(true, false);
        if (this.level.isClientSide()) {
            return;
        }
        if (!this.isValid()) {
            this.remove();
        }
    }

    public boolean isValid() {
        if (this.level.dimension() != Vault.VAULT_KEY) {
            return false;
        }
        if (!this.level.isAreaLoaded(this.getVendorPos(), 1)) {
            return false;
        }
        if (this.distanceToSqr(Vector3d.atCenterOf(this.getVendorPos())) > 4.0) {
            return false;
        }
        final TileEntity te = this.level.getBlockEntity(this.getVendorPos());
        return te instanceof EtchingVendorControllerTileEntity && ((EtchingVendorControllerTileEntity) te).getMonitoredEntityId() == this.getId();
    }

    @Nullable
    public EtchingVendorControllerTileEntity getControllerTile() {
        return (EtchingVendorControllerTileEntity) this.level.getBlockEntity(this.getVendorPos());
    }

    protected ActionResultType mobInteract(final PlayerEntity player, final Hand hand) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return new StringTextComponent("Etching Trader");
                }

                @Nullable
                public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player) {
                    return new EtchingTradeContainer(windowId, playerInventory, EtchingVendorEntity.this.getId());
                }
            }, buf -> buf.writeInt(this.getId()));
        }
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    public boolean removeWhenFarAway(final double distanceToClosestPlayer) {
        return false;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    static {
        VENDOR_POS = EntityDataManager.defineId(EtchingVendorEntity.class, DataSerializers.BLOCK_POS);
    }
}
