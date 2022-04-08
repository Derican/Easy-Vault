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
    private final Set<String> voted = new HashSet<>();
    private final BlockPos stabilizerPos;
    private final List<DirectionChoice> directions = new ArrayList<>();
    private int voteTicks;

    VotingSession(BlockPos stabilizerPos, Collection<DirectionChoice> directions) {
        this.stabilizerPos = stabilizerPos;
        this.voteTicks = 410;
        this.directions.addAll(directions);
    }

    VotingSession(CompoundNBT tag) {
        this.stabilizerPos = (BlockPos) CodecUtils.readNBT(BlockPos.CODEC, tag, "pos", BlockPos.ZERO);
        this.voteTicks = tag.getInt("voteTicks");
        ListNBT directions = tag.getList("directions", 10);
        for (int i = 0; i < directions.size(); i++) {
            this.directions.add(new DirectionChoice(directions.getCompound(i)));
        }
    }

    boolean acceptVote(String voter, Direction dir) {
        if (this.voted.add(voter)) {
            for (DirectionChoice choice : this.directions) {
                if (choice.getDirection() == dir) {
                    choice.addVote();
                    return true;
                }
            }
        }
        return false;
    }

    void tick(ServerWorld world) {
        if (!isFinished()) {
            this.voteTicks--;

            if (world.hasChunkAt(getStabilizerPos())) {
                TileEntity tile = world.getBlockEntity(getStabilizerPos());
                if (tile instanceof StabilizerTileEntity) {
                    ((StabilizerTileEntity) tile).setActive();
                }
            }
        }
    }

    public BlockPos getStabilizerPos() {
        return this.stabilizerPos;
    }

    public boolean isFinished() {
        return (this.voteTicks <= 0);
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

    public float getChoicePercentage(DirectionChoice choice) {
        float total = 0.0F;
        for (DirectionChoice anyChoice : getDirections()) {
            total += anyChoice.getVotes();
        }
        return choice.getVotes() / total;
    }

    public DirectionChoice getVotedDirection() {
        List<DirectionChoice> choices = new ArrayList<>(getDirections());
        Collections.shuffle(choices);

        DirectionChoice votedChoice = null;
        for (DirectionChoice choice : choices) {
            if (votedChoice == null || choice.getVotes() > votedChoice.getVotes()) {
                votedChoice = choice;
            }
        }
        return votedChoice;
    }

    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        CodecUtils.writeNBT(BlockPos.CODEC, this.stabilizerPos, tag, "pos");
        tag.putInt("voteTicks", this.voteTicks);
        ListNBT directions = new ListNBT();
        for (DirectionChoice choice : this.directions) {
            directions.add(choice.serialize());
        }
        tag.put("directions", (INBT) directions);
        return tag;
    }

    public static VotingSession deserialize(CompoundNBT tag) {
        return new VotingSession(tag);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\VotingSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */