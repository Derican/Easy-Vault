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

public class TrophyStatueTileEntity extends LootStatueTileEntity {
    private static final Random rand;
    private WeekKey week;
    private PlayerVaultStatsData.PlayerRecordEntry recordEntry;

    public TrophyStatueTileEntity() {
        super(ModBlocks.TROPHY_STATUE_TILE_ENTITY);
        this.week = null;
        this.recordEntry = null;
    }

    public WeekKey getWeek() {
        return this.week;
    }

    public void setWeek(final WeekKey week) {
        this.week = week;
    }

    public PlayerVaultStatsData.PlayerRecordEntry getRecordEntry() {
        return this.recordEntry;
    }

    public void setRecordEntry(final PlayerVaultStatsData.PlayerRecordEntry recordEntry) {
        this.recordEntry = recordEntry;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide()) {
            this.playEffects();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        if (TrophyStatueTileEntity.rand.nextInt(4) == 0) {
            final ParticleManager mgr = Minecraft.getInstance().particleEngine;
            final BlockPos pos = this.getBlockPos();
            final Vector3d rPos = new Vector3d(pos.getX() + 0.5 + (TrophyStatueTileEntity.rand.nextFloat() - TrophyStatueTileEntity.rand.nextFloat()) * (0.1 + TrophyStatueTileEntity.rand.nextFloat() * 0.6), pos.getY() + 0.5 + (TrophyStatueTileEntity.rand.nextFloat() - TrophyStatueTileEntity.rand.nextFloat()) * (TrophyStatueTileEntity.rand.nextFloat() * 0.2), pos.getZ() + 0.5 + (TrophyStatueTileEntity.rand.nextFloat() - TrophyStatueTileEntity.rand.nextFloat()) * (0.1 + TrophyStatueTileEntity.rand.nextFloat() * 0.6));
            final SimpleAnimatedParticle p = (SimpleAnimatedParticle) mgr.createParticle(ParticleTypes.FIREWORK, rPos.x, rPos.y, rPos.z, 0.0, 0.0, 0.0);
            if (p != null) {
//                TODO: check if the omit is acceptable
//                p.baseGravity = 0.0f;
                p.setColor(-3229440);
            }
        }
    }

    @Override
    public CompoundNBT save(final CompoundNBT nbt) {
        if (this.week != null) {
            nbt.put("trophyWeek", this.week.serialize());
        }
        if (this.recordEntry != null) {
            nbt.put("recordEntry", this.recordEntry.serialize());
        }
        return super.save(nbt);
    }

    @Override
    public void load(final BlockState state, final CompoundNBT nbt) {
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

    @Override
    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        if (this.week != null) {
            nbt.put("trophyWeek", this.week.serialize());
        }
        if (this.recordEntry != null) {
            nbt.put("recordEntry", this.recordEntry.serialize());
        }
        return nbt;
    }

    static {
        rand = new Random();
    }
}
