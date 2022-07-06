package iskallia.vault.network.message;

import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.network.message.base.OpcodeMessage;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import iskallia.vault.world.vault.logic.objective.TreasureHuntObjective;
import iskallia.vault.world.vault.logic.objective.architect.VotingSession;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class VaultGoalMessage extends OpcodeMessage<VaultGoalMessage.VaultGoal> {
    private VaultGoalMessage() {
    }

    public static void encode(final VaultGoalMessage message, final PacketBuffer buffer) {
        message.encodeSelf(message, buffer);
    }

    public static VaultGoalMessage decode(final PacketBuffer buffer) {
        final VaultGoalMessage message = new VaultGoalMessage();
        message.decodeSelf(buffer, VaultGoal.class);
        return message;
    }

    public static void handle(final VaultGoalMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> VaultGoalData.create(message));
        context.setPacketHandled(true);
    }

    public static VaultGoalMessage obeliskGoal(final int touched, final int max) {
        final ITextComponent text = (ITextComponent) new StringTextComponent("Obelisks").withStyle(TextFormatting.BOLD);
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.OBELISK_GOAL, payload -> {
            payload.putString("Message", ITextComponent.Serializer.toJson(text));
            payload.putInt("TouchedObelisks", touched);
            payload.putInt("MaxObelisks", max);
        });
    }

    public static VaultGoalMessage killBossGoal() {
        final ITextComponent text = (ITextComponent) new StringTextComponent("Boss Battle").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.GREEN);
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.OBELISK_MESSAGE, payload -> payload.putString("Message", ITextComponent.Serializer.toJson(text)));
    }

    public static VaultGoalMessage scavengerHunt(final List<ScavengerHuntObjective.ItemSubmission> activeSubmissions) {
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.SCAVENGER_GOAL, payload -> {
            final ListNBT list = new ListNBT();

            for (ScavengerHuntObjective.ItemSubmission submission : activeSubmissions) {
                list.add(submission.serialize());
            }
            payload.put("scavengerItems", (INBT) list);
        });
    }

    public static VaultGoalMessage treasureHunt(final List<TreasureHuntObjective.ItemSubmission> activeSubmissions) {
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.SCAVENGER_GOAL, payload -> {
            final ListNBT list = new ListNBT();

            for (TreasureHuntObjective.ItemSubmission submission : activeSubmissions) {
                list.add(submission.serialize());
            }
            payload.put("scavengerItems", (INBT) list);
        });
    }

    public static VaultGoalMessage architectEvent(final float completedPercent, final int ticksUntilNextVote, final int totalTicksUntilNextVote, @Nullable final VotingSession activeVotingSession) {
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.ARCHITECT_GOAL, payload -> {
            payload.putFloat("completedPercent", completedPercent);
            payload.putInt("ticksUntilNextVote", ticksUntilNextVote);
            payload.putInt("totalTicksUntilNextVote", totalTicksUntilNextVote);
            if (activeVotingSession != null) {
                payload.put("votingSession", (INBT) activeVotingSession.serialize());
            }
        });
    }

    public static VaultGoalMessage architectFinalEvent(final int killedBosses, final int totalBosses, final int knowledge, final int totalKnowledge, @Nullable final VotingSession activeVotingSession, final boolean killCurrentBoss) {
        ITextComponent text;
        if (killCurrentBoss) {
            text = (ITextComponent) new StringTextComponent("Kill the Boss!").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.RED);
        } else {
            text = (ITextComponent) new StringTextComponent("Gather Knowledge!").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.AQUA);
        }
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.FINAL_ARCHITECT_GOAL, payload -> {
            payload.putString("message", ITextComponent.Serializer.toJson(text));
            payload.putInt("killedBosses", killedBosses);
            payload.putInt("totalBosses", totalBosses);
            payload.putInt("knowledge", knowledge);
            payload.putInt("totalKnowledge", totalKnowledge);
            if (activeVotingSession != null) {
                payload.put("votingSession", (INBT) activeVotingSession.serialize());
            }
        });
    }

    public static VaultGoalMessage ancientsHunt(final int totalAncients, final int foundAncients) {
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.ANCIENTS_GOAL, payload -> {
            payload.putInt("total", totalAncients);
            payload.putInt("found", foundAncients);
        });
    }

    public static VaultGoalMessage raidChallenge(final int wave, final int totalWaves, final int aliveMobs, final int totalMobs, final int tickWaveDelay, final int completed, final int target, final List<ITextComponent> positiveModifiers, final List<ITextComponent> negativeModifiers) {
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.RAID_GOAL, payload -> {
            payload.putInt("wave", wave);
            payload.putInt("totalWaves", totalWaves);
            payload.putInt("aliveMobs", aliveMobs);
            payload.putInt("totalMobs", totalMobs);
            payload.putInt("tickWaveDelay", tickWaveDelay);
            payload.putInt("completedRaids", completed);
            payload.putInt("targetRaids", target);
            final ListNBT positives = new ListNBT();
            positiveModifiers.forEach(modifier -> positives.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(modifier))));
            payload.put("positives", (INBT) positives);
            final ListNBT negatives = new ListNBT();
            negativeModifiers.forEach(modifier -> negatives.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(modifier))));
            payload.put("negatives", (INBT) negatives);
        });
    }

    public static VaultGoalMessage cakeHunt(final int totalCakes, final int foundCakes) {
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.CAKE_HUNT_GOAL, payload -> {
            payload.putInt("total", totalCakes);
            payload.putInt("found", foundCakes);
        });
    }

    public static VaultGoalMessage clear() {
        return OpcodeMessage.composeMessage(new VaultGoalMessage(), VaultGoal.CLEAR, payload -> {
        });
    }

    public enum VaultGoal {
        OBELISK_GOAL,
        OBELISK_MESSAGE,
        SCAVENGER_GOAL,
        ARCHITECT_GOAL,
        ANCIENTS_GOAL,
        RAID_GOAL,
        CAKE_HUNT_GOAL,
        CLEAR,
        FINAL_ARCHITECT_GOAL;
    }
}
