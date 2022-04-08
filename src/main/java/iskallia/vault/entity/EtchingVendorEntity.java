package iskallia.vault.entity;

import iskallia.vault.Vault;
import iskallia.vault.block.entity.EtchingVendorControllerTileEntity;
import iskallia.vault.container.inventory.EtchingTradeContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
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
    private static final DataParameter<BlockPos> VENDOR_POS = EntityDataManager.defineId(EtchingVendorEntity.class, DataSerializers.BLOCK_POS);

    public EtchingVendorEntity(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
        setInvulnerable(true);
        setNoGravity(true);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VENDOR_POS, BlockPos.ZERO);
    }


    protected void registerGoals() {
        super.registerGoals();

//        this.goalSelector = new GoalSelector(this.level.getProfilerSupplier());
//        this.targetSelector = new GoalSelector(this.level.getProfilerSupplier());

        this.goalSelector.addGoal(1, (Goal) new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, (Goal) new LookRandomlyGoal(this));
    }

    public void setVendorPos(BlockPos pos) {
        this.entityData.set(VENDOR_POS, pos);
    }

    public BlockPos getVendorPos() {
        return (BlockPos) this.entityData.get(VENDOR_POS);
    }


    public void tick() {
        super.tick();

        dropLeash(true, false);
        if (this.level.isClientSide()) {
            return;
        }
        if (!isValid()) {
            remove();
        }
    }

    public boolean isValid() {
        if (this.level.dimension() != Vault.VAULT_KEY) {
            return false;
        }

        if (!this.level.isAreaLoaded(getVendorPos(), 1)) {
            return false;
        }
        if (distanceToSqr(Vector3d.atCenterOf((Vector3i) getVendorPos())) > 4.0D) {
            return false;
        }

        TileEntity te = this.level.getBlockEntity(getVendorPos());
        if (!(te instanceof EtchingVendorControllerTileEntity)) {
            return false;
        }
        if (((EtchingVendorControllerTileEntity) te).getMonitoredEntityId() != getId()) {
            return false;
        }
        return true;
    }

    @Nullable
    public EtchingVendorControllerTileEntity getControllerTile() {
        return (EtchingVendorControllerTileEntity) this.level.getBlockEntity(getVendorPos());
    }


    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return (ITextComponent) new StringTextComponent("Etching Trader");
                }


                @Nullable
                public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
                    return (Container) new EtchingTradeContainer(windowId, playerInventory, EtchingVendorEntity.this.getId());
                }
            },buf -> buf.writeInt(getId()));
        }
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\EtchingVendorEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */