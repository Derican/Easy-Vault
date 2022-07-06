// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity;

import iskallia.vault.attribute.CompoundAttribute;
import iskallia.vault.world.vault.logic.VaultSandEvent;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.IItemProvider;
import iskallia.vault.init.ModItems;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemStack;
import iskallia.vault.init.ModEntities;
import net.minecraft.world.World;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.EntityType;

public class VaultSandEntity extends FloatingItemEntity
{
    public VaultSandEntity(final EntityType<? extends ItemEntity> type, final World world) {
        super(type, world);
        this.setColor(-3241472, -3229440);
    }
    
    public VaultSandEntity(final World worldIn, final double x, final double y, final double z) {
        this(ModEntities.VAULT_SAND, worldIn);
        this.setPos(x, y, z);
        this.yRot = this.random.nextFloat() * 360.0f;
        this.setDeltaMovement(this.random.nextDouble() * 0.2 - 0.1, 0.2, this.random.nextDouble() * 0.2 - 0.1);
    }
    
    public VaultSandEntity(final World worldIn, final double x, final double y, final double z, final ItemStack stack) {
        this(worldIn, x, y, z);
        this.setItem(stack);
        this.lifespan = Integer.MAX_VALUE;
    }
    
    public static VaultSandEntity create(final World world, final BlockPos pos) {
        return new VaultSandEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack((IItemProvider)ModItems.VAULT_SAND));
    }
    
    @Override
    public void playerTouch(final PlayerEntity player) {
        final boolean wasAlive = this.isAlive();
        super.playerTouch(player);
        if (wasAlive && !this.isAlive() && player instanceof ServerPlayerEntity) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity)player;
            final VaultRaid activeRaid = VaultRaidData.get(sPlayer.getLevel()).getActiveFor(sPlayer);
            if (activeRaid != null) {
                activeRaid.getProperties().get(VaultRaid.SAND_EVENT).ifPresent(eventData -> {
                    ((VaultSandEvent)eventData.getBaseValue()).pickupSand(sPlayer);
                    eventData.updateNBT();
                });
            }
        }
    }
}
