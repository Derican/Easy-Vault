package iskallia.vault.world.vault.logic.objective.architect;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import java.util.Set;
import net.minecraft.util.text.TextFormatting;
import iskallia.vault.util.MiscUtils;
import java.util.Collections;
import iskallia.vault.world.vault.modifier.VaultModifier;
import iskallia.vault.config.VaultModifiersConfig;
import net.minecraft.entity.player.PlayerEntity;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.Entity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.util.function.Function;
import java.util.Optional;
import iskallia.vault.world.vault.VaultUtils;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraft.entity.LivingEntity;
import javax.annotation.Nullable;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.STitlePacket;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultPieceProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import java.util.function.Predicate;
import java.util.Objects;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import iskallia.vault.world.vault.logic.objective.architect.modifier.RandomVoteModifier;
import iskallia.vault.world.vault.logic.objective.architect.modifier.VoteModifier;
import iskallia.vault.config.FinalArchitectEventConfig;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.network.PacketDistributor;
import iskallia.vault.network.message.EffectMessage;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import java.util.Collection;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import net.minecraft.util.Direction;
import java.util.ArrayList;
import com.google.common.collect.Iterables;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkDirection;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultGoalMessage;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.Vault;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "the_vault", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArchitectSummonAndKillBossesObjective extends ArchitectObjective
{
    private ResourceLocation roomPool;
    private ResourceLocation tunnelPool;
    private int killedBosses;
    private int totalKilledBossesNeeded;
    private int knowledge;
    private int totalKnowledgeNeeded;
    protected UUID currentBossId;
    private float combinedMobHealthMultiplier;

    public ArchitectSummonAndKillBossesObjective(final ResourceLocation id) {
        super(id);
        this.roomPool = Vault.id("raid/rooms");
        this.tunnelPool = Vault.id("vault/tunnels");
        this.killedBosses = 0;
        this.totalKilledBossesNeeded = 0;
        this.knowledge = 0;
        this.totalKnowledgeNeeded = 0;
        this.currentBossId = null;
        this.combinedMobHealthMultiplier = 0.0f;
        this.voteDowntimeTicks = 0;
        this.totalKilledBossesNeeded = ModConfigs.FINAL_ARCHITECT.getBossKillsNeeded();
        this.totalKnowledgeNeeded = ModConfigs.FINAL_ARCHITECT.getTotalKnowledgeNeeded();
    }

    @Override
    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        if (!this.isCompleted()) {
            vault.getPlayers().forEach(vPlayer -> {
                if (filter.test(vPlayer.getPlayerId())) {
                    this.onTick.execute(vault, vPlayer, world);
                }
                return;
            });
        }
        final MinecraftServer srv = world.getServer();
        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, playerEntity -> {
            final VaultGoalMessage pkt = VaultGoalMessage.architectFinalEvent(this.killedBosses, this.totalKilledBossesNeeded, this.knowledge, this.totalKnowledgeNeeded, this.activeSession, this.currentBossId != null);
            ModNetwork.CHANNEL.sendTo((Object)pkt, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }));
        if (this.isCompleted()) {
            return;
        }
        if (this.activeSession != null) {
            this.activeSession.tick(world);
            if (this.activeSession.isFinished()) {
                this.finishVote(vault, this.activeSession, world);
                this.completedSessions.add(this.activeSession);
                this.activeSession = null;
            }
        }
        if (this.hasFulfilledObjective()) {
            this.setCompleted();
        }
    }

    private boolean hasFulfilledObjective() {
        return this.killedBosses >= this.totalKilledBossesNeeded;
    }

    @Override
    public boolean createVotingSession(final ServerWorld world, final BlockPos origin) {
        if (this.getActiveSession() != null || this.currentBossId != null || this.hasFulfilledObjective()) {
            return false;
        }
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, origin);
        if (vault == null) {
            return false;
        }
        final VaultRoom room = (VaultRoom)Iterables.getFirst(vault.getGenerator().getPiecesAt(origin, VaultRoom.class), (Object)null);
        if (room == null) {
            return false;
        }
        final List<Direction> availableDirections = new ArrayList<Direction>();
        for (final Direction dir : Direction.values()) {
            if (dir.getAxis().isHorizontal() && VaultJigsawHelper.canExpand(vault, room, dir)) {
                availableDirections.add(dir);
            }
        }
        if (availableDirections.size() <= 1) {
            return false;
        }
        final List<DirectionChoice> choices = new ArrayList<DirectionChoice>();
        for (final Direction dir2 : availableDirections) {
            final DirectionChoice choice = new DirectionChoice(dir2);
            final FinalArchitectEventConfig.ModifierPair pair = ModConfigs.FINAL_ARCHITECT.getRandomPair();
            if (pair != null) {
                VoteModifier modifier = ModConfigs.FINAL_ARCHITECT.getModifier(pair.getPositive());
                if (modifier != null) {
                    choice.addModifier(modifier);
                }
                modifier = ModConfigs.FINAL_ARCHITECT.getModifier(pair.getNegative());
                if (modifier != null) {
                    choice.addModifier(modifier);
                }
            }
            choices.add(choice);
        }
        this.activeSession = new SummonAndKillBossesVotingSession(origin, choices);
        final EffectMessage msg = EffectMessage.playSound(SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.6f, 1.0f);
        vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(world.getServer(), sPlayer -> ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sPlayer), (Object)msg)));
        return true;
    }

    @Override
    protected void finishVote(final VaultRaid vault, final VotingSession session, final ServerWorld world) {
        vault.getGenerator().getPiecesAt(session.getStabilizerPos(), VaultRoom.class).stream().findFirst().ifPresent(room -> {
            final DirectionChoice choice = session.getVotedDirection();
            final ArrayList<VoteModifier> modifiers = new ArrayList<>();
            VoteModifier modifier = null;
            choice.getFinalArchitectModifiers().forEach(modifier1 -> {
                if (modifier1 instanceof RandomVoteModifier) {
                    modifiers.add(((RandomVoteModifier)modifier1).rollModifier());
                }
                else {
                    modifiers.add(modifier1);
                }
                return;
            });
            final EffectMessage msg = EffectMessage.playSound(SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.6f, 1.0f);
            vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(world.getServer(), sPlayer -> ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sPlayer), (Object)msg)));
            final JigsawPiece roomPiece = modifiers.stream().map(modifier2 -> modifier2.getSpecialRoom(this, vault)).filter(Objects::nonNull).findFirst().orElse(null);
            final IFormattableTextComponent txt = new StringTextComponent("").append(choice.getDirectionDisplay()).append(": ");
            for (int i = 0; i < modifiers.size(); ++i) {
                modifier = modifiers.get(i);
                if (i != 0) {
                    txt.append(", ");
                }
                txt.append(modifier.getDescription());
            }
            vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(world.getServer(), sPlayer -> sPlayer.sendMessage(txt, Util.NIL_UUID)));
            modifiers.forEach(modifier3 -> modifier3.onApply(this, vault, world));
            final List<VaultPiece> generatedPieces = this.expandVault(vault, world, (VaultRoom) room, session, choice.getDirection(), roomPiece, null);
            final List<VaultPieceProcessor> postProcessors = modifiers.stream().map(modifier4 -> modifier4.getPostProcessor(this, vault)).filter(Objects::nonNull).collect(Collectors.toList());
            generatedPieces.forEach(piece -> postProcessors.forEach(processor -> processor.postProcess(vault, world, piece, choice.getDirection())));
            final STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.TITLE, choice.getDirectionDisplay());
            vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(world.getServer(), sPlayer -> sPlayer.connection.send(titlePacket)));
        });
    }

    @Override
    protected List<VaultPiece> expandVault(final VaultRaid vault, final ServerWorld world, final VaultRoom room, final VotingSession session, final Direction direction, @Nullable final JigsawPiece roomToGenerate, @Nullable final JigsawPiece tunnelToGenerate) {
        final JigsawPiece roomPiece = (this.roomPool == null) ? null : VaultJigsawHelper.getRandomPiece(this.roomPool);
        final JigsawPiece tunnelPiece = (this.tunnelPool == null) ? null : VaultJigsawHelper.getRandomPiece(this.tunnelPool);
        boolean generateObelisk = false;
        if (this.knowledge >= this.totalKnowledgeNeeded) {
            generateObelisk = true;
            this.knowledge = 0;
            final EffectMessage msg = EffectMessage.playSound(SoundEvents.PLAYER_LEVELUP, SoundCategory.NEUTRAL, 0.8f, 0.4f);
            vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(world.getServer(), sPlayer -> ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sPlayer), (Object)msg)));
        }
        return VaultJigsawHelper.expandTenosFinalVault(vault, world, room, direction, roomPiece, tunnelPiece, generateObelisk);
    }

    public void setBoss(final LivingEntity boss) {
        this.currentBossId = boss.getUUID();
    }

    public void setRoomPool(final ResourceLocation roomPool) {
        this.roomPool = roomPool;
    }

    public void setTunnelPool(final ResourceLocation tunnelPool) {
        this.tunnelPool = tunnelPool;
    }

    public void addKnowledge(final int knowledge) {
        this.knowledge = Math.max(0, this.knowledge + knowledge);
    }

    public void addMobHealthMultiplier(final float combinedMobHealthMultiplier) {
        this.combinedMobHealthMultiplier = Math.max(0.0f, this.combinedMobHealthMultiplier + combinedMobHealthMultiplier);
    }

    public float getCombinedMobHealthMultiplier() {
        return this.combinedMobHealthMultiplier;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBossDeath(final LivingDeathEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        final ServerWorld world = (ServerWorld)event.getEntity().level;
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, event.getEntity().blockPosition());
        if (!VaultUtils.inVault(vault, event.getEntity())) {
            return;
        }
        final List<ArchitectSummonAndKillBossesObjective> matchingObjectives = vault.getPlayers().stream().map(player -> player.getActiveObjective(ArchitectSummonAndKillBossesObjective.class)).filter(Optional::isPresent).map(Optional::get).filter(o -> !o.isCompleted()).filter(o -> o.currentBossId != null).filter(o -> o.currentBossId.equals(event.getEntity().getUUID())).collect(Collectors.toList());
        if (matchingObjectives.isEmpty()) {
            vault.getActiveObjective(ArchitectSummonAndKillBossesObjective.class).ifPresent(objective -> objective.onBossDeath(event, vault, world));
        }
        else {
            matchingObjectives.forEach(objective -> objective.onBossDeath(event, vault, world));
        }
    }

    protected void onBossDeath(final LivingDeathEvent event, final VaultRaid vault, final ServerWorld world) {
        final LivingEntity boss = event.getEntityLiving();
        if (!boss.getUUID().equals(this.currentBossId)) {
            return;
        }
        final Optional<UUID> source = Optional.ofNullable(event.getSource().getEntity()).map((Function<? super Entity, ? extends UUID>)Entity::getUUID);
        final Optional<VaultPlayer> killer = source.flatMap(vault::getPlayer);
        killer.ifPresent(kPlayer -> kPlayer.runIfPresent(world.getServer(), playerEntity -> vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(world.getServer(), recipient -> recipient.sendMessage(this.getBossKillMessage(playerEntity), Util.NIL_UUID)))));
        this.currentBossId = null;
        ++this.killedBosses;
        if (!this.hasFulfilledObjective()) {
            this.addModifier(vault, world);
        }
    }

    private void addModifier(final VaultRaid vault, final ServerWorld world) {
        final int level = vault.getProperties().getValue(VaultRaid.LEVEL);
        final Set<VaultModifier> modifiers = ModConfigs.VAULT_MODIFIERS.getRandom(ArchitectSummonAndKillBossesObjective.rand, level, VaultModifiersConfig.ModifierPoolType.FINAL_TENOS_ADDS, this.getId());
        final List<VaultModifier> modifierList = new ArrayList<VaultModifier>(modifiers);
        Collections.shuffle(modifierList);
        final VaultModifier modifier = MiscUtils.getRandomEntry(modifierList, ArchitectSummonAndKillBossesObjective.rand);
        if (modifier != null) {
            final ITextComponent ct = new StringTextComponent("Added ").withStyle(TextFormatting.GRAY).append(modifier.getNameComponent());
            vault.getModifiers().addPermanentModifier(modifier);
            vault.getPlayers().forEach(vPlayer -> {
                modifier.apply(vault, vPlayer, world, world.getRandom());
                vPlayer.runIfPresent(world.getServer(), sPlayer -> sPlayer.sendMessage(ct, Util.NIL_UUID));
            });
        }
    }

    private ITextComponent getBossKillMessage(final PlayerEntity player) {
        final IFormattableTextComponent msgContainer = new StringTextComponent("").withStyle(TextFormatting.WHITE);
        final IFormattableTextComponent playerName = player.getDisplayName().copy();
        playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
        return msgContainer.append(playerName).append(" defeated a Boss!");
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("killedBosses", this.killedBosses);
        nbt.putInt("totalKilledBossesNeeded", this.totalKilledBossesNeeded);
        nbt.putInt("knowledge", this.knowledge);
        nbt.putInt("totalKnowledgeNeeded", this.totalKnowledgeNeeded);
        nbt.putFloat("combinedMobHealthMultiplier", this.combinedMobHealthMultiplier);
        if (this.currentBossId != null) {
            nbt.putUUID("currentBossId", this.currentBossId);
        }
        nbt.putString("roomPool", this.roomPool.toString());
        nbt.putString("tunnelPool", this.tunnelPool.toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.killedBosses = nbt.getInt("killedBosses");
        this.totalKilledBossesNeeded = nbt.getInt("totalKilledBossesNeeded");
        this.knowledge = nbt.getInt("knowledge");
        this.totalKnowledgeNeeded = nbt.getInt("totalKnowledgeNeeded");
        this.combinedMobHealthMultiplier = nbt.getFloat("combinedMobHealthMultiplier");
        if (nbt.hasUUID("currentBossId")) {
            this.currentBossId = nbt.getUUID("currentBossId");
        }
        if (nbt.contains("roomPool", 8)) {
            this.roomPool = new ResourceLocation(nbt.getString("roomPool"));
        }
        if (nbt.contains("tunnelPool", 8)) {
            this.tunnelPool = new ResourceLocation(nbt.getString("tunnelPool"));
        }
    }
}
