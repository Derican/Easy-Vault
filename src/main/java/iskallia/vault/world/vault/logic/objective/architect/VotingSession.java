package iskallia.vault.world.vault.logic.objective.architect;

import iskallia.vault.block.entity.StabilizerTileEntity;
import iskallia.vault.util.CodecUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public class VotingSession {
    private static final int VOTING_DURATION = 410;
    private final BlockPos stabilizerPos;
    private final Set<String> voted;
    private final List<DirectionChoice> directions;
    private int voteTicks;

    VotingSession(final BlockPos stabilizerPos, final Collection<DirectionChoice> directions) {
        this.voted = new HashSet<String>();
        this.directions = new ArrayList<DirectionChoice>();
        this.stabilizerPos = stabilizerPos;
        this.voteTicks = 410;
        this.directions.addAll(directions);
    }

    VotingSession(final CompoundNBT tag) {
        this.voted = new HashSet<String>();
        this.directions = new ArrayList<DirectionChoice>();
        this.stabilizerPos = CodecUtils.readNBT((com.mojang.serialization.Codec<BlockPos>) BlockPos.CODEC, tag, "pos", BlockPos.ZERO);
        this.voteTicks = tag.getInt("voteTicks");
        final ListNBT directions = tag.getList("directions", 10);
        for (int i = 0; i < directions.size(); ++i) {
            this.directions.add(new DirectionChoice(directions.getCompound(i)));
        }
    }

    boolean acceptVote(final String voter, final Direction dir) {
        if (this.voted.add(voter)) {
            for (final DirectionChoice choice : this.directions) {
                if (choice.getDirection() == dir) {
                    choice.addVote();
                    return true;
                }
            }
        }
        return false;
    }

    void tick(final ServerWorld world) {
        if (!this.isFinished()) {
            --this.voteTicks;
            if (world.hasChunkAt(this.getStabilizerPos())) {
                final TileEntity tile = world.getBlockEntity(this.getStabilizerPos());
                if (tile instanceof StabilizerTileEntity) {
                    this.setStabilizerActive((StabilizerTileEntity)tile);
                }
            }
        }
    }

    protected void setStabilizerActive(final StabilizerTileEntity tile) {
        tile.setActive();
    }

    public BlockPos getStabilizerPos() {
        return this.stabilizerPos;
    }

    public boolean isFinished() {
        return this.voteTicks <= 0;
    }

    public int getTotalVoteTicks() {
        return 410;
    }

    public int getRemainingVoteTicks() {
        return this.voteTicks;
    }

    public List<DirectionChoice> getDirections() {
        return this.directions;
    }

    public boolean hasDirectionChoice(final Direction direction) {
        for (final DirectionChoice directionChoice : this.getDirections()) {
            if (directionChoice.getDirection() == direction) {
                return true;
            }
        }
        return false;
    }

    public float getChoicePercentage(final DirectionChoice choice) {
        float total = 0.0f;
        for (final DirectionChoice anyChoice : this.getDirections()) {
            total += anyChoice.getVotes();
        }
        return choice.getVotes() / total;
    }

    public DirectionChoice getVotedDirection() {
        final List<DirectionChoice> choices = new ArrayList<DirectionChoice>(this.getDirections());
        Collections.shuffle(choices);
        DirectionChoice votedChoice = null;
        for (final DirectionChoice choice : choices) {
            if (votedChoice == null || choice.getVotes() > votedChoice.getVotes()) {
                votedChoice = choice;
            }
        }
        return votedChoice;
    }

    public CompoundNBT serialize() {
        final CompoundNBT tag = new CompoundNBT();
        CodecUtils.writeNBT((com.mojang.serialization.Codec<BlockPos>) BlockPos.CODEC, this.stabilizerPos, tag, "pos");
        tag.putInt("voteTicks", this.voteTicks);
        final ListNBT directions = new ListNBT();
        for (final DirectionChoice choice : this.directions) {
            directions.add(choice.serialize());
        }
        tag.put("directions", (INBT) directions);
        return tag;
    }

    public static VotingSession deserialize(final CompoundNBT tag) {
        if (tag.getBoolean("isFinal")) {
            return new SummonAndKillBossesVotingSession(tag);
        }
        return new VotingSession(tag);
    }
}
