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


public class ScavengerChestTileEntity
        extends ChestTileEntity {
    private static final Random rand = new Random();

    protected ScavengerChestTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
        setItems(NonNullList.withSize(45, ItemStack.EMPTY));
    }

    public ScavengerChestTileEntity() {
        this(ModBlocks.SCAVENGER_CHEST_TILE_ENTITY);
    }


    public void tick() {
        super.tick();

        if (this.level.isClientSide()) {
            playEffects();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
        BlockPos pos = getBlockPos();


        Vector3d rPos = new Vector3d(pos.getX() + 0.5D + ((rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 3.0F), pos.getY() + 0.5D + ((rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 7.0F), pos.getZ() + 0.5D + ((rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 3.0F));
        SimpleAnimatedParticle p = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, rPos.x, rPos.y, rPos.z, 0.0D, 0.0D, 0.0D);


        if (p != null) {
//            p.baseGravity = 0.0F;
            p.setColor(2347008);
        }
    }


    public int getContainerSize() {
        return 45;
    }

    protected Container createMenu(int id, PlayerInventory playerInventory) {
        Container container = null;
        ScavengerChestContainer scavengerChestContainer = new ScavengerChestContainer(id, playerInventory, (IInventory) this, (IInventory) this);
        if (this.level instanceof ServerWorld) {
            ServerWorld sWorld = (ServerWorld) this.level;
            VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, getBlockPos());
            if (vault != null) {


                container = (Container) vault.getActiveObjective(ScavengerHuntObjective.class).map(objective -> {
                    ScavengerChestContainer scavengerChestContainer1 = new ScavengerChestContainer(id, playerInventory, (IInventory) this, objective.getScavengerChestInventory());
                    scavengerChestContainer1.addSlotListener((IContainerListener) objective.getChestWatcher());
                    return (Container) scavengerChestContainer1;
                }).orElse(scavengerChestContainer);
            }
        }
        return container;
    }


    public ITextComponent getDisplayName() {
        return (ITextComponent) new TranslationTextComponent(ModBlocks.SCAVENGER_CHEST.getDescriptionId());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\ScavengerChestTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */