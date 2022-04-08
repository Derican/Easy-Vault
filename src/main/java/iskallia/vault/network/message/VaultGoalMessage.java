package iskallia.vault.network.message;

import iskallia.vault.client.vault.goal.VaultGoalData;
import iskallia.vault.network.message.base.OpcodeMessage;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import iskallia.vault.world.vault.logic.objective.architect.VotingSession;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class VaultGoalMessage extends OpcodeMessage<VaultGoalMessage.VaultGoal> {
    public enum VaultGoal {
        OBELISK_GOAL,
        OBELISK_MESSAGE,
        SCAVENGER_GOAL,
        ARCHITECT_GOAL,
        ANCIENTS_GOAL,
        RAID_GOAL,
        CAKE_HUNT_GOAL,
        CLEAR;
    }


    public static void encode(VaultGoalMessage message, PacketBuffer buffer) {
        message.encodeSelf(message, buffer);
    }

    public static VaultGoalMessage decode(PacketBuffer buffer) {
        VaultGoalMessage message = new VaultGoalMessage();
        message.decodeSelf(buffer, VaultGoal.class);
        return message;
    }

    public static void handle(VaultGoalMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> VaultGoalData.create(message));
        context.setPacketHandled(true);
    }


    public static VaultGoalMessage obeliskGoal(int touched, int max) {
        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Obelisks")).withStyle(TextFormatting.BOLD);
        return (VaultGoalMessage) composeMessage(new VaultGoalMessage(), VaultGoal.OBELISK_GOAL, payload -> {
            payload.putString("Message", ITextComponent.Serializer.toJson(text));
            payload.putInt("TouchedObelisks", touched);
            payload.putInt("MaxObelisks", max);
        });
    }

    public static VaultGoalMessage killBossGoal() {
        IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Boss Battle")).withStyle(TextFormatting.BOLD).withStyle(TextFormatting.GREEN);
        return (VaultGoalMessage) composeMessage(new VaultGoalMessage(), VaultGoal.OBELISK_MESSAGE, payload -> payload.putString("Message", ITextComponent.Serializer.toJson(text)));
    }


    public static VaultGoalMessage scavengerHunt(List<ScavengerHuntObjective.ItemSubmission> activeSubmissions) {
        return (VaultGoalMessage) composeMessage(new VaultGoalMessage(), VaultGoal.SCAVENGER_GOAL, payload -> {
            ListNBT list = new ListNBT();
            for (ScavengerHuntObjective.ItemSubmission submission : activeSubmissions) {
                list.add(submission.serialize());
            }
            payload.put("scavengerItems", (INBT) list);
        });
    }

    public static VaultGoalMessage architectEvent(float completedPercent, int ticksUntilNextVote, int totalTicksUntilNextVote, @Nullable VotingSession activeVotingSession) {
        return (VaultGoalMessage) composeMessage(new VaultGoalMessage(), VaultGoal.ARCHITECT_GOAL, payload -> {
            payload.putFloat("completedPercent", completedPercent);
            payload.putInt("ticksUntilNextVote", ticksUntilNextVote);
            payload.putInt("totalTicksUntilNextVote", totalTicksUntilNextVote);
            if (activeVotingSession != null) {
                payload.put("votingSession", (INBT) activeVotingSession.serialize());
            }
        });
    }

    public static VaultGoalMessage ancientsHunt(int totalAncients, int foundAncients) {
        return (VaultGoalMessage) composeMessage(new VaultGoalMessage(), VaultGoal.ANCIENTS_GOAL, payload -> {
            payload.putInt("total", totalAncients);
            payload.putInt("found", foundAncients);
        });
    }


    public static VaultGoalMessage raidChallenge(int wave, int totalWaves, int aliveMobs, int totalMobs, int tickWaveDelay, int completed, List<ITextComponent> positiveModifiers, List<ITextComponent> negativeModifiers) {
        return (VaultGoalMessage) composeMessage(new VaultGoalMessage(), VaultGoal.RAID_GOAL, payload -> {
            payload.putInt("wave", wave);
            payload.putInt("totalWaves", totalWaves);
            payload.putInt("aliveMobs", aliveMobs);
            payload.putInt("totalMobs", totalMobs);
            payload.putInt("tickWaveDelay", tickWaveDelay);
            payload.putInt("completedRaids", completed);
            ListNBT positives = new ListNBT();
            positiveModifiers.forEach(());
            payload.put("positives", (INBT) positives);
            ListNBT negatives = new ListNBT();
            negativeModifiers.forEach(());
            payload.put("negatives", (INBT) negatives);
        });
    }


    public static VaultGoalMessage cakeHunt(int totalCakes, int foundCakes) {
        return (VaultGoalMessage) composeMessage(new VaultGoalMessage(), VaultGoal.CAKE_HUNT_GOAL, payload -> {
            payload.putInt("total", totalCakes);
            payload.putInt("found", foundCakes);
        });
    }

    public static VaultGoalMessage clear() {
        return (VaultGoalMessage) composeMessage(new VaultGoalMessage(), VaultGoal.CLEAR, payload -> {

        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\VaultGoalMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */