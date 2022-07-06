package iskallia.vault.world.vault.logic.objective;

import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import iskallia.vault.world.vault.gen.layout.SingularVaultRoomLayout;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

import net.minecraft.loot.LootTable;

import java.util.function.Function;
import javax.annotation.Nonnull;

import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

import java.util.Optional;

import net.minecraft.entity.Entity;
import iskallia.vault.world.vault.gen.piece.FinalVaultBoss;
import net.minecraft.world.World;
import iskallia.vault.init.ModEntities;
import iskallia.vault.entity.EyesoreEntity;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.task.VaultTask;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class KillTheBossObjective extends VaultObjective {
    private UUID bossId;
    private boolean bossDead;

    public KillTheBossObjective(final ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
    }

    public void setBossId(final UUID bossId) {
        this.bossId = bossId;
    }

    @Override
    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        super.tick(vault, filter, world);
        if (this.bossId == null) {
            final EyesoreEntity boss = (EyesoreEntity) ModEntities.EYESORE.create((World) world);
            final Optional<VaultPiece> room = vault.getGenerator().getPieces(FinalVaultBoss.class).stream().findFirst();
            if (room.isPresent()) {
                final Vector3i pos = room.get().getCenter();
                boss.moveTo(pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 0.0f, 0.0f);
                world.addWithUUID((Entity) boss);
                this.bossId = boss.getUUID();
            } else {
                this.bossDead = true;
            }
        } else {
            final Entity boss2 = world.getEntity(this.bossId);
            if (boss2 == null || !boss2.isAlive()) {
                this.bossDead = true;
            }
        }
        if (this.bossDead) {
            this.setCompleted();
        }
    }

    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock(final VaultRaid vault, final ServerWorld world, final BlockPos pos) {
        return Blocks.AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public LootTable getRewardLootTable(final VaultRaid vault, final Function<ResourceLocation, LootTable> tblResolver) {
        return null;
    }

    @Override
    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) new StringTextComponent("Kill the Boss").withStyle(TextFormatting.OBFUSCATED);
    }

    @Nullable
    @Override
    public ITextComponent getObjectiveTargetDescription(final int amount) {
        return (ITextComponent) new StringTextComponent("Kill the Boss").withStyle(TextFormatting.OBFUSCATED);
    }

    @Nullable
    @Override
    public VaultRoomLayoutGenerator getCustomLayout() {
        return new SingularVaultRoomLayout();
    }

    @Override
    public boolean shouldPauseTimer(final MinecraftServer srv, final VaultRaid vault) {
        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        if (this.bossId != null) {
            tag.putString("BossId", this.bossId.toString());
        }
        tag.putBoolean("BossDead", this.bossDead);
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("BossId", 8)) {
            this.bossId = UUID.fromString(nbt.getString("BossId"));
        }
        this.bossDead = nbt.getBoolean("BossDead");
    }
}
