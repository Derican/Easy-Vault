package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.WeekKey;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;


public class TrophyStatueTileEntity
        extends LootStatueTileEntity {
    private static final Random rand = new Random();

    private WeekKey week = null;
    private PlayerVaultStatsData.PlayerRecordEntry recordEntry = null;

    public TrophyStatueTileEntity() {
        super(ModBlocks.TROPHY_STATUE_TILE_ENTITY);
    }

    public WeekKey getWeek() {
        return this.week;
    }

    public void setWeek(WeekKey week) {
        this.week = week;
    }

    public PlayerVaultStatsData.PlayerRecordEntry getRecordEntry() {
        return this.recordEntry;
    }

    public void setRecordEntry(PlayerVaultStatsData.PlayerRecordEntry recordEntry) {
        this.recordEntry = recordEntry;
    }


    public void tick() {
        super.tick();

        if (this.level.isClientSide()) {
            playEffects();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        if (rand.nextInt(4) == 0) {
            ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
            BlockPos pos = getBlockPos();


            Vector3d rPos = new Vector3d(pos.getX() + 0.5D + (rand.nextFloat() - rand.nextFloat()) * (0.1D + rand.nextFloat() * 0.6D), pos.getY() + 0.5D + (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 0.2D, pos.getZ() + 0.5D + (rand.nextFloat() - rand.nextFloat()) * (0.1D + rand.nextFloat() * 0.6D));
            SimpleAnimatedParticle p = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, rPos.x, rPos.y, rPos.z, 0.0D, 0.0D, 0.0D);


            if (p != null) {
//                p.baseGravity = 0.0F;
                p.setColor(-3229440);
            }
        }
    }


    public CompoundNBT save(CompoundNBT nbt) {
        if (this.week != null) {
            nbt.put("trophyWeek", (INBT) this.week.serialize());
        }
        if (this.recordEntry != null) {
            nbt.put("recordEntry", (INBT) this.recordEntry.serialize());
        }
        return super.save(nbt);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("trophyWeek", 10)) {
            this.week = WeekKey.deserialize(nbt.getCompound("trophyWeek"));
        } else {
            this.week = null;
        }
        if (nbt.contains("recordEntry", 10)) {
            this.recordEntry = PlayerVaultStatsData.PlayerRecordEntry.deserialize(nbt.getCompound("recordEntry"));
        } else {
            this.recordEntry = null;
        }
        super.load(state, nbt);
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        if (this.week != null) {
            nbt.put("trophyWeek", (INBT) this.week.serialize());
        }
        if (this.recordEntry != null) {
            nbt.put("recordEntry", (INBT) this.recordEntry.serialize());
        }
        return nbt;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\TrophyStatueTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */