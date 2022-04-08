package iskallia.vault.world.vault.logic.objective.architect;

import com.google.common.collect.Iterables;
import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.nbt.NBTHelper;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.gen.decorator.BreadcrumbFeature;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.VaultGenerator;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.VaultBossSpawner;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.architect.modifier.VoteModifier;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultPieceProcessor;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = "the_vault", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArchitectObjective extends VaultObjective {
    private final List<VotingSession> completedSessions = new ArrayList<>();
    private VotingSession activeSession = null;

    private boolean votingLocked = false;
    private int totalRequiredVotes;
    private int voteDowntimeTicks = 400;
    private int ticksUntilNextVote = 0;

    private UUID bossId = null;
    private boolean isBossDead = false;
    private final VListNBT<BlockPos, CompoundNBT> exitPortalLocations = VListNBT.ofCodec(BlockPos.CODEC, BlockPos.ZERO);
    private boolean collidedWithExitPortal = false;

    public ArchitectObjective(ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.totalRequiredVotes = ModConfigs.ARCHITECT_EVENT.getRandomTotalRequiredPolls();
    }

    public boolean createVotingSession(VaultRaid vault, ServerWorld world, BlockPos origin) {
        if (this.activeSession != null || this.ticksUntilNextVote > 0 || isVotingLocked()) {
            return false;
        }
        VaultRoom room = (VaultRoom) Iterables.getFirst(vault.getGenerator().getPiecesAt(origin, VaultRoom.class), null);
        if (room == null) {
            return false;
        }

        List<Direction> availableDirections = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (dir.getAxis().isHorizontal() && VaultJigsawHelper.canExpand(vault, room, dir)) {
                availableDirections.add(dir);
            }
        }
        if (availableDirections.size() <= 1) {
            return false;
        }


        Direction bossDir = null;
        if (this.completedSessions.size() >= this.totalRequiredVotes) {
            bossDir = (Direction) MiscUtils.getRandomEntry(availableDirections, rand);
        }
        List<DirectionChoice> choices = new ArrayList<>();
        for (Direction dir : availableDirections) {
            DirectionChoice choice = new DirectionChoice(dir);
            if (dir == bossDir) {
                choice.addModifier(ModConfigs.ARCHITECT_EVENT.getBossModifier());
            } else {
                VoteModifier randomModifier = ModConfigs.ARCHITECT_EVENT.generateRandomModifier();
                if (randomModifier != null) {
                    choice.addModifier(randomModifier);
                }
            }
            choices.add(choice);
        }

        this.activeSession = new VotingSession(origin, choices);
        if (this.completedSessions.isEmpty()) {

            IFormattableTextComponent display = (new StringTextComponent("")).append("Vote with ");
            List<DirectionChoice> directions = this.activeSession.getDirections();
            for (int i = 0; i < directions.size(); i++) {
                if (i != 0) {
                    display.append(", ");
                }
                DirectionChoice choice = directions.get(i);
                display.append(choice.getDirectionDisplay("/"));
            }
            display.append("!");

            vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(world.getServer(), ()));
        }


        return true;
    }

    @Nullable
    public VotingSession getActiveSession() {
        return this.activeSession;
    }

    public boolean handleVote(String sender, Direction dir) {
        if (this.activeSession == null) {
            return false;
        }
        return this.activeSession.acceptVote(sender, dir);
    }


    public boolean shouldPauseTimer(MinecraftServer srv, VaultRaid vault) {
        return (super.shouldPauseTimer(srv, vault) || (this.activeSession == null && this.completedSessions
                .isEmpty()));
    }


    public void tick(VaultRaid vault, PlayerFilter filter, ServerWorld world) {
        super.tick(vault, filter, world);
        MinecraftServer srv = world.getServer();

        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));


        if (isCompleted())
            return;
        if (this.ticksUntilNextVote > 0) {
            this.ticksUntilNextVote--;
        }

        if (this.activeSession != null) {
            this.activeSession.tick(world);

            if (this.activeSession.isFinished()) {
                finishVote(vault, this.activeSession, world);
                this.completedSessions.add(this.activeSession);
                this.activeSession = null;

                if (!isVotingLocked()) {
                    this.ticksUntilNextVote = this.voteDowntimeTicks;
                }
            }
        }

        if (!this.exitPortalLocations.isEmpty()) {
            vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));
        }


        if (this.bossId != null && this.isBossDead) {
            setCompleted();
        }
        if (!this.exitPortalLocations.isEmpty() && this.collidedWithExitPortal) {
            setCompleted();
        }
    }

    private void finishVote(VaultRaid vault, VotingSession session, ServerWorld world) {
        vault.getGenerator().getPiecesAt(session.getStabilizerPos(), VaultRoom.class).stream()
                .findFirst()
                .ifPresent(room -> {
                    STitlePacket subtitlePacket;
                    DirectionChoice choice = session.getVotedDirection();
                    List<VoteModifier> modifiers = new ArrayList<>();
                    choice.getModifiers().forEach(());
                    JigsawPiece roomPiece = modifiers.stream().map(()).filter(Objects::nonNull).findFirst().orElse(null);
                    List<VaultPiece> generatedPieces = VaultJigsawHelper.expandVault(vault, world, room, choice.getDirection(), roomPiece);
                    BreadcrumbFeature.generateVaultBreadcrumb(vault, world, generatedPieces);
                    List<VaultPieceProcessor> postProcessors = (List<VaultPieceProcessor>) modifiers.stream().map(()).filter(Objects::nonNull).collect(Collectors.toList());
                    generatedPieces.forEach(());
                    modifiers.forEach(());
                    choice.getModifiers().forEach(());
                    this.voteDowntimeTicks = Math.max(0, this.voteDowntimeTicks);
                    STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.TITLE, choice.getDirectionDisplay());
                    VoteModifier displayModifier = (VoteModifier) Iterables.getFirst(modifiers, null);
                    if (displayModifier != null) {
                        subtitlePacket = new STitlePacket(STitlePacket.Type.SUBTITLE, displayModifier.getDescription());
                    } else {
                        subtitlePacket = null;
                    }
                    vault.getPlayers().forEach(());
                });
    }


    public void buildPortal(List<BlockPos> portalLocations) {
        this.exitPortalLocations.addAll(portalLocations);
    }

    public void spawnBoss(VaultRaid vault, ServerWorld world, BlockPos pos) {
        LivingEntity boss = VaultBossSpawner.spawnBoss(vault, world, pos);
        this.bossId = boss.getUUID();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBossDeath(LivingDeathEvent event) {
        LivingEntity died = event.getEntityLiving();
        if (died.getCommandSenderWorld().isClientSide() || !(died.getCommandSenderWorld() instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = (ServerWorld) died.getCommandSenderWorld();

        VaultRaid vault = VaultRaidData.get(world).getAt(world, died.blockPosition());
        if (vault == null) {
            return;
        }


        List<ArchitectObjective> matchingObjectives = (List<ArchitectObjective>) vault.getPlayers().stream().map(player -> player.getActiveObjective(ArchitectObjective.class)).filter(Optional::isPresent).map(Optional::get).filter(objective -> !objective.isCompleted()).filter(objective -> died.getUUID().equals(objective.bossId)).collect(Collectors.toList());

        if (matchingObjectives.isEmpty()) {
            vault.getActiveObjective(ArchitectObjective.class).ifPresent(objective -> {
                if (objective.onBossKill(died)) {
                    objective.dropBossCrate(died, event.getSource(), world, vault);
                }
            });
        } else {
            matchingObjectives.forEach(objective -> objective.onBossKill(died));
        }
    }


    private boolean onBossKill(LivingEntity boss) {
        if (this.isBossDead || !boss.getUUID().equals(this.bossId)) {
            return false;
        }
        this.isBossDead = true;
        return true;
    }

    private void dropBossCrate(LivingEntity boss, DamageSource killSrc, ServerWorld world, VaultRaid vault) {
        Optional<UUID> source = Optional.<Entity>ofNullable(killSrc.getEntity()).map(Entity::getUUID);
        Optional<VaultPlayer> killer = source.flatMap(vault::getPlayer);
        Optional<VaultPlayer> host = vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer);

        if (killer.isPresent()) {
            spawnBossLoot(vault, boss.blockPosition(), killer.get(), world, true);
        } else if (host.isPresent() && host.get() instanceof iskallia.vault.world.vault.player.VaultRunner) {
            spawnBossLoot(vault, boss.blockPosition(), host.get(), world, true);
        } else {
            vault.getPlayers().stream()
                    .filter(player -> player instanceof iskallia.vault.world.vault.player.VaultRunner)
                    .findFirst()
                    .ifPresent(player -> spawnBossLoot(vault, boss.blockPosition(), player, world, true));
        }
    }

    public void spawnBossLoot(VaultRaid vault, BlockPos bossPos, VaultPlayer player, ServerWorld world, boolean isBossKill) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.random).withParameter(LootParameters.THIS_ENTITY, playerEntity).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf((Vector3i) bossPos)).withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.playerAttack((PlayerEntity) playerEntity)).withOptionalParameter(LootParameters.KILLER_ENTITY, playerEntity).withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY, playerEntity).withParameter(LootParameters.LAST_DAMAGE_PLAYER, playerEntity).withLuck(playerEntity.getLuck());
            LootContext ctx = builder.create(LootParameterSets.ENTITY);
            spawnRewardCrate(world, (Vector3i) bossPos, vault, ctx);
            for (int i = 1; i < vault.getPlayers().size(); i++) {
                if (rand.nextFloat() < 0.5F) {
                    spawnRewardCrate(world, (Vector3i) bossPos, vault, ctx);
                }
            }
            MiscUtils.broadcast(isBossKill ? getBossKillMessage((PlayerEntity) playerEntity) : getEscapeMessage((PlayerEntity) playerEntity));
            vault.getPlayers().forEach(());
        });
    }


    protected void addSpecialLoot(ServerWorld world, VaultRaid vault, LootContext context, NonNullList<ItemStack> stacks) {
        super.addSpecialLoot(world, vault, context, stacks);

        if (ModConfigs.ARCHITECT_EVENT.isEnabled()) {
            stacks.add(new ItemStack((IItemProvider) ModItems.VAULT_GEAR));
        }
    }

    private ITextComponent getBossKillMessage(PlayerEntity player) {
        IFormattableTextComponent msgContainer = (new StringTextComponent("")).withStyle(TextFormatting.WHITE);
        IFormattableTextComponent playerName = player.getDisplayName().copy();
        playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));

        return (ITextComponent) msgContainer.append((ITextComponent) playerName).append(" defeated Boss!");
    }

    private ITextComponent getEscapeMessage(PlayerEntity player) {
        IFormattableTextComponent msgContainer = (new StringTextComponent("")).withStyle(TextFormatting.WHITE);
        IFormattableTextComponent playerName = player.getDisplayName().copy();
        playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));

        return (ITextComponent) msgContainer.append((ITextComponent) playerName).append(" successfully escaped from the Vault!");
    }

    private ITextComponent getCompletionMessage(PlayerEntity player) {
        IFormattableTextComponent msgContainer = (new StringTextComponent("")).withStyle(TextFormatting.WHITE);
        IFormattableTextComponent playerName = player.getDisplayName().copy();
        playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));

        return (ITextComponent) msgContainer.append((ITextComponent) playerName).append(" finished building a Vault!");
    }

    private void spawnRewardCrate(ServerWorld world, Vector3i pos, VaultRaid vault, LootContext context) {
        NonNullList<ItemStack> stacks = createLoot(world, vault, context);
        ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);
        ItemEntity item = new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(), crate);
        item.setDefaultPickUpDelay();
        world.addFreshEntity((Entity) item);

        this.crates.add(new VaultObjective.Crate((List) stacks));
    }

    public int getTicksUntilNextVote() {
        return this.ticksUntilNextVote;
    }

    public float getCompletedPercent() {
        return MathHelper.clamp(this.completedSessions.size() / this.totalRequiredVotes, 0.0F, 1.0F);
    }

    public void setVotingLocked() {
        this.votingLocked = true;
    }

    public boolean isVotingLocked() {
        return this.votingLocked;
    }


    public void setObjectiveTargetCount(int amount) {
        this.totalRequiredVotes = amount;
    }


    @Nullable
    public ITextComponent getObjectiveTargetDescription(int amount) {
        return (ITextComponent) (new StringTextComponent("Required amount of votes: "))
                .append((ITextComponent) (new StringTextComponent(String.valueOf(amount))).withStyle(TextFormatting.AQUA));
    }


    @Nonnull
    public BlockState getObjectiveRelevantBlock() {
        return ModBlocks.STABILIZER.defaultBlockState();
    }


    @Nullable
    public LootTable getRewardLootTable(VaultRaid vault, Function<ResourceLocation, LootTable> tblResolver) {
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        return (config != null) ? tblResolver.apply(config.getBossCrate()) : LootTable.EMPTY;
    }


    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) (new StringTextComponent("Build a Vault")).withStyle(TextFormatting.AQUA);
    }


    public ITextComponent getVaultName() {
        return (ITextComponent) new StringTextComponent("Architect Vault");
    }


    @Nonnull
    public Supplier<? extends VaultGenerator> getVaultGenerator() {
        return VaultRaid.ARCHITECT_GENERATOR;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        if (this.activeSession != null) {
            tag.put("activeSession", (INBT) this.activeSession.serialize());
        }

        ListNBT sessions = new ListNBT();
        for (VotingSession session : this.completedSessions) {
            sessions.add(session.serialize());
        }
        tag.put("completedSessions", (INBT) sessions);

        tag.putInt("totalRequiredVotes", this.totalRequiredVotes);
        tag.putInt("voteDowntimeTicks", this.voteDowntimeTicks);
        tag.putInt("ticksUntilNextVote", this.ticksUntilNextVote);
        tag.putBoolean("votingLocked", this.votingLocked);

        NBTHelper.writeOptional(tag, "bossId", this.bossId, (nbt, uuid) -> nbt.putUUID("bossId", uuid));
        tag.putBoolean("isBossDead", this.isBossDead);
        tag.put("exitPortalLocations", (INBT) this.exitPortalLocations.serializeNBT());
        tag.putBoolean("collidedWithExitPortal", this.collidedWithExitPortal);
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);
        if (tag.contains("activeSession", 10)) {
            this.activeSession = new VotingSession(tag.getCompound("activeSession"));
        } else {
            this.activeSession = null;
        }

        this.completedSessions.clear();
        ListNBT sessions = tag.getList("completedSessions", 10);
        for (int i = 0; i < sessions.size(); i++) {
            this.completedSessions.add(new VotingSession(sessions.getCompound(i)));
        }

        this.totalRequiredVotes = tag.getInt("totalRequiredVotes");
        this.voteDowntimeTicks = tag.getInt("voteDowntimeTicks");
        this.ticksUntilNextVote = tag.getInt("ticksUntilNextVote");
        this.votingLocked = tag.getBoolean("votingLocked");

        this.bossId = (UUID) NBTHelper.readOptional(tag, "bossId", nbt -> nbt.getUUID("bossId"));
        this.isBossDead = tag.getBoolean("isBossDead");
        this.exitPortalLocations.deserializeNBT(tag.getList("exitPortalLocations", 10));
        this.collidedWithExitPortal = tag.getBoolean("collidedWithExitPortal");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\ArchitectObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */