package iskallia.vault.block.entity;

import iskallia.vault.container.ScavengerChestContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ScavengerChestTileEntity extends ChestTileEntity {
    private static final Random rand;

    protected ScavengerChestTileEntity(final TileEntityType<?> typeIn) {
        super((TileEntityType) typeIn);
        this.setItems(NonNullList.withSize(45, ItemStack.EMPTY));
    }

    public ScavengerChestTileEntity() {
        this(ModBlocks.SCAVENGER_CHEST_TILE_ENTITY);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide()) {
            this.playEffects();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        final ParticleManager mgr = Minecraft.getInstance().particleEngine;
        final BlockPos pos = this.getBlockPos();
        final Vector3d rPos = new Vector3d(pos.getX() + 0.5 + (ScavengerChestTileEntity.rand.nextFloat() - ScavengerChestTileEntity.rand.nextFloat()) * (ScavengerChestTileEntity.rand.nextFloat() * 3.0f), pos.getY() + 0.5 + (ScavengerChestTileEntity.rand.nextFloat() - ScavengerChestTileEntity.rand.nextFloat()) * (ScavengerChestTileEntity.rand.nextFloat() * 7.0f), pos.getZ() + 0.5 + (ScavengerChestTileEntity.rand.nextFloat() - ScavengerChestTileEntity.rand.nextFloat()) * (ScavengerChestTileEntity.rand.nextFloat() * 3.0f));
        final SimpleAnimatedParticle p = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, rPos.x, rPos.y, rPos.z, 0.0, 0.0, 0.0);
        if (p != null) {
//            TODO: check if the omit is acceptable
//            p.baseGravity = 0.0f;
            p.setColor(2347008);
        }
    }

    public int getContainerSize() {
        return 45;
    }

    protected Container createMenu(final int id, final PlayerInventory playerInventory) {
        Container ct = new ScavengerChestContainer(id, playerInventory, (IInventory) this, (IInventory) this);
        if (this.level instanceof ServerWorld) {
            final ServerWorld sWorld = (ServerWorld) this.level;
            final VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, this.getBlockPos());
            if (vault != null) {
                ct = vault.getActiveObjective(ScavengerHuntObjective.class).map(objective -> {
                    final Container linkedCt = new ScavengerChestContainer(id, playerInventory, (IInventory) this, objective.getScavengerChestInventory());
                    linkedCt.addSlotListener((IContainerListener) objective.getChestWatcher());
                    return linkedCt;
                }).orElse(ct);
            }
        }
        return ct;
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) new TranslationTextComponent(ModBlocks.SCAVENGER_CHEST.getDescriptionId());
    }

    static {
        rand = new Random();
    }
}
