package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.StepHeightMessage;
import iskallia.vault.skill.talent.TalentGroup;
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
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StepTalent extends PlayerTalent {
    private static final Set<UUID> stepTrackList;
    @Expose
    private final float stepHeightAddend;

    public StepTalent(final int cost, final float stepHeightAddend) {
        super(cost);
        this.stepHeightAddend = stepHeightAddend;
    }

    public float getStepHeightAddend() {
        return this.stepHeightAddend;
    }

    @SubscribeEvent
    public static void onClone(final PlayerEvent.Clone event) {
        refresh((ServerPlayerEntity) event.getOriginal());
    }

    @SubscribeEvent
    public static void onTeleport(final PlayerEvent.PlayerChangedDimensionEvent event) {
        refresh((ServerPlayerEntity) event.getPlayer());
    }

    private static void refresh(final ServerPlayerEntity player) {
        player.getServer().tell(new TickDelayedTask(2, () -> set(player, player.maxUpStep)));
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.PlayerTickEvent event) {
        final PlayerEntity player = event.player;
        if (player.getCommandSenderWorld().isClientSide() || !(player.getCommandSenderWorld() instanceof ServerWorld) || !(player instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerWorld sWorld = (ServerWorld) player.getCommandSenderWorld();
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
        final UUID playerUUID = player.getUUID();
        final TalentTree talentTree = PlayerTalentsData.get(sWorld).getTalents(player);
        final TalentNode<?> node = talentTree.getNodeOf((TalentGroup<?>) ModConfigs.TALENTS.STEP);
        if (!(node.getTalent() instanceof StepTalent)) {
            return;
        }
        final StepTalent talent = (StepTalent) node.getTalent();
        if (node.isLearned() && !player.isCrouching()) {
            StepTalent.stepTrackList.add(playerUUID);
            final float targetHeight = 1.0f + talent.getStepHeightAddend();
            if (sPlayer.maxUpStep < targetHeight) {
                set(sPlayer, targetHeight);
            }
        } else if (StepTalent.stepTrackList.contains(playerUUID)) {
            set(sPlayer, 1.0f);
            StepTalent.stepTrackList.remove(playerUUID);
        }
    }

    private static void set(final ServerPlayerEntity player, final float stepHeight) {
        ModNetwork.CHANNEL.sendTo(new StepHeightMessage(stepHeight - 0.4f), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        player.maxUpStep = stepHeight;
    }

    static {
        stepTrackList = new HashSet<UUID>();
    }
}
