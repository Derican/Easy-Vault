package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.StepHeightMessage;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StepTalent
        extends PlayerTalent {
    private static final Set<UUID> stepTrackList = new HashSet<>();
    @Expose
    private final float stepHeightAddend;

    public StepTalent(int cost, float stepHeightAddend) {
        super(cost);
        this.stepHeightAddend = stepHeightAddend;
    }

    public float getStepHeightAddend() {
        return this.stepHeightAddend;
    }


    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        refresh((ServerPlayerEntity) event.getOriginal());
    }


    @SubscribeEvent
    public static void onTeleport(PlayerEvent.PlayerChangedDimensionEvent event) {
        refresh((ServerPlayerEntity) event.getPlayer());
    }

    private static void refresh(ServerPlayerEntity player) {
        player.getServer().tell((Runnable) new TickDelayedTask(2, () -> set(player, player.maxUpStep)));
    }


    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.getCommandSenderWorld().isClientSide() ||
                !(player.getCommandSenderWorld() instanceof ServerWorld) || !(player instanceof ServerPlayerEntity)) {
            return;
        }

        ServerWorld sWorld = (ServerWorld) player.getCommandSenderWorld();
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;

        UUID playerUUID = player.getUUID();
        TalentTree talentTree = PlayerTalentsData.get(sWorld).getTalents(player);
        TalentNode<?> node = talentTree.getNodeOf(ModConfigs.TALENTS.STEP);
        if (!(node.getTalent() instanceof StepTalent)) {
            return;
        }
        StepTalent talent = (StepTalent) node.getTalent();

        if (node.isLearned() && !player.isCrouching()) {
            stepTrackList.add(playerUUID);
            float targetHeight = 1.0F + talent.getStepHeightAddend();
            if (sPlayer.maxUpStep < targetHeight) {
                set(sPlayer, targetHeight);
            }
        } else if (stepTrackList.contains(playerUUID)) {
            set(sPlayer, 1.0F);
            stepTrackList.remove(playerUUID);
        }
    }


    private static void set(ServerPlayerEntity player, float stepHeight) {
        ModNetwork.CHANNEL.sendTo(new StepHeightMessage(stepHeight - 0.4F), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        player.maxUpStep = stepHeight;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\StepTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */