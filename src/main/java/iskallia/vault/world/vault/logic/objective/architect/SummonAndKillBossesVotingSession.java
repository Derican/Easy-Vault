package iskallia.vault.world.vault.logic.objective.architect;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;

import iskallia.vault.block.entity.StabilizerTileEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.Collection;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;

public class SummonAndKillBossesVotingSession extends VotingSession {
    private Direction votedDirection;

    SummonAndKillBossesVotingSession(final BlockPos stabilizerPos, final Collection<DirectionChoice> directions) {
        super(stabilizerPos, directions);
        this.votedDirection = null;
    }

    SummonAndKillBossesVotingSession(final CompoundNBT tag) {
        super(tag);
        this.votedDirection = null;
        if (tag.contains("votedDirection", 3)) {
            this.votedDirection = Direction.values()[tag.getInt("votedDirection")];
        }
    }

    @Override
    protected void setStabilizerActive(final StabilizerTileEntity tile) {
        super.setStabilizerActive(tile);
        tile.setHighlightDirections((Collection<Direction>) this.getDirections().stream().map(DirectionChoice::getDirection).collect(Collectors.toList()));
    }

    @Override
    public boolean isFinished() {
        return this.votedDirection != null;
    }

    @Override
    public float getChoicePercentage(final DirectionChoice choice) {
        return 0.0f;
    }

    public void setVotedDirection(final Direction votedDirection) {
        for (final DirectionChoice dir : this.getDirections()) {
            if (dir.getDirection() == votedDirection) {
                this.votedDirection = votedDirection;
                break;
            }
        }
    }

    @Override
    public DirectionChoice getVotedDirection() {
        if (this.votedDirection != null) {
            for (final DirectionChoice dir : this.getDirections()) {
                if (dir.getDirection() == this.votedDirection) {
                    return dir;
                }
            }
        }
        return super.getVotedDirection();
    }

    @Override
    public CompoundNBT serialize() {
        final CompoundNBT nbt = super.serialize();
        nbt.putBoolean("isFinal", true);
        if (this.votedDirection != null) {
            nbt.putInt("votedDirection", this.votedDirection.ordinal());
        }
        return nbt;
    }
}
