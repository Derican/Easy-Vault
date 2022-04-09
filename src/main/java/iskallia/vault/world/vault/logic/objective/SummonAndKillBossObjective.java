// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective;

import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.BossMusicMessage;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.VaultUtils;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.player.VaultRunner;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = "the_vault", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SummonAndKillBossObjective extends VaultObjective
{
    protected int progressCount;
    protected int targetCount;
    protected UUID bossId;
    protected ITextComponent bossName;
    protected Vector3d bossPos;
    protected boolean isBossDead;
    
    public SummonAndKillBossObjective(final ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.targetCount = SummonAndKillBossObjective.rand.nextInt(4) + 3;
        this.bossId = null;
        this.bossName = null;
        this.bossPos = null;
        this.isBossDead = false;
    }
    
    public boolean allObelisksClicked() {
        return this.progressCount >= this.targetCount;
    }
    
    public void addObelisk() {
        ++this.progressCount;
    }
    
    public UUID getBossId() {
        return this.bossId;
    }
    
    public boolean isBossDead() {
        return this.isBossDead;
    }
    
    public boolean isBossSpawned() {
        return this.bossId != null;
    }
    
    public ITextComponent getBossName() {
        return this.bossName;
    }
    
    public Vector3d getBossPos() {
        return this.bossPos;
    }
    
    public void setBoss(final LivingEntity boss) {
        this.bossId = boss.getUUID();
    }
    
    @Override
    public void setObjectiveTargetCount(final int amount) {
        this.targetCount = amount;
    }
    
    @Nullable
    @Override
    public ITextComponent getObjectiveTargetDescription(final int amount) {
        return (ITextComponent)new StringTextComponent("Required Obelisks: ").append((ITextComponent)new StringTextComponent(String.valueOf(amount)).withStyle(TextFormatting.GOLD));
    }
    
    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock() {
        return ModBlocks.OBELISK.defaultBlockState();
    }
    
    @Nullable
    @Override
    public LootTable getRewardLootTable(final VaultRaid vault, final Function<ResourceLocation, LootTable> tblResolver) {
        final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        final LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        return (config != null) ? tblResolver.apply(config.getBossCrate()) : LootTable.EMPTY;
    }
    
    @Override
    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent)new StringTextComponent("Kill the Boss").withStyle(TextFormatting.GOLD);
    }
    
    @Override
    public int modifyMinimumObjectiveCount(final int objectives, final int requiredAmount) {
        return Math.max(objectives, requiredAmount);
    }
    
    @Override
    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        super.tick(vault, filter, world);
        if (this.isCompleted()) {
            return;
        }
        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> {
            vPlayer.runIfPresent(world.getServer(), playerEntity -> {
                final VaultGoalMessage pkt = this.allObelisksClicked() ? VaultGoalMessage.killBossGoal() : VaultGoalMessage.obeliskGoal(this.progressCount, this.targetCount);
                ModNetwork.CHANNEL.sendTo(pkt, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                return;
            });
            if (this.isBossSpawned()) {
                vPlayer.sendIfPresent(world.getServer(), new BossMusicMessage(true));
            }
            return;
        });
        if (this.isBossDead) {
            this.setCompleted();
        }
    }
    
    @Override
    public void complete(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        super.complete(vault, player, world);
        player.sendIfPresent(world.getServer(), new BossMusicMessage(false));
        player.sendIfPresent(world.getServer(), VaultGoalMessage.clear());
    }
    
    @Override
    public void complete(final VaultRaid vault, final ServerWorld world) {
        super.complete(vault, world);
        vault.getPlayers().forEach(player -> {
            player.sendIfPresent(world.getServer(), new BossMusicMessage(false));
            player.sendIfPresent(world.getServer(), VaultGoalMessage.clear());
        });
    }
    
    public void spawnBossLoot(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            final LootContext.Builder builder = new LootContext.Builder(world).withRandom(world.random).withParameter(LootParameters.THIS_ENTITY, playerEntity).withParameter(LootParameters.ORIGIN, this.getBossPos()).withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.playerAttack((PlayerEntity)playerEntity)).withOptionalParameter(LootParameters.KILLER_ENTITY, playerEntity).withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY, playerEntity).withParameter(LootParameters.LAST_DAMAGE_PLAYER, playerEntity).withLuck(playerEntity.getLuck());
            final LootContext ctx = builder.create(LootParameterSets.ENTITY);
            this.dropBossCrate(world, vault, player, ctx);
            for (int i = 1; i < vault.getPlayers().size(); ++i) {
                if (SummonAndKillBossObjective.rand.nextFloat() < 0.5f) {
                    this.dropBossCrate(world, vault, player, ctx);
                }
            }
            world.getServer().getPlayerList().broadcastMessage(this.getBossKillMessage((PlayerEntity)playerEntity), ChatType.CHAT, player.getPlayerId());
        });
    }
    
    private ITextComponent getBossKillMessage(final PlayerEntity player) {
        final IFormattableTextComponent msgContainer = new StringTextComponent("").withStyle(TextFormatting.WHITE);
        final IFormattableTextComponent playerName = player.getDisplayName().copy();
        playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
        return (ITextComponent)msgContainer.append((ITextComponent)playerName).append(" defeated ").append(this.getBossName()).append("!");
    }
    
    private void dropBossCrate(final ServerWorld world, final VaultRaid vault, final VaultPlayer rewardPlayer, final LootContext context) {
        final NonNullList<ItemStack> stacks = this.createLoot(world, vault, context);
        vault.getProperties().getBase(VaultRaid.IS_RAFFLE).ifPresent(isRaffle -> {
            if (!isRaffle) {
                return;
            }
            else {
                vault.getPlayers().stream().filter(player -> player instanceof VaultRunner).min(Comparator.comparing(vPlayer -> vPlayer.getTimer().getTimeLeft())).ifPresent(vPlayer -> {
                    final VaultTimer timer = vPlayer.getTimer();
                    final PlayerVaultStatsData data = PlayerVaultStatsData.get(world);
                    if (timer.getRunTime() < data.getFastestVaultTime().getTickCount()) {
                        vPlayer.runIfPresent(world.getServer(), sPlayer -> data.updateFastestVaultTime((PlayerEntity)sPlayer, timer.getRunTime()));
                    }
                });
                return;
            }
        });
        final BlockPos dropPos = rewardPlayer.getServerPlayer(world.getServer()).map((Function<? super ServerPlayerEntity, ? extends BlockPos>)Entity::blockPosition).orElse(new BlockPos(this.getBossPos()));
        final ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);
        final ItemEntity item = new ItemEntity((World)world, (double)dropPos.getX(), (double)dropPos.getY(), (double)dropPos.getZ(), crate);
        item.setDefaultPickUpDelay();
        world.addFreshEntity((Entity)item);
        this.crates.add(new Crate((List<ItemStack>)stacks));
    }
    
    @Override
    protected void addSpecialLoot(final ServerWorld world, final VaultRaid vault, final LootContext context, final NonNullList<ItemStack> stacks) {
        super.addSpecialLoot(world, vault, context, stacks);
        final boolean isCowVault = vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, false);
        if (isCowVault) {
            stacks.add(new ItemStack((IItemProvider)ModItems.ARMOR_CRATE_HELLCOW));
        }
    }
    
    protected void onBossDeath(final LivingDeathEvent event, final VaultRaid vault, final ServerWorld world, final boolean dropCrate) {
        final LivingEntity boss = event.getEntityLiving();
        if (!boss.getUUID().equals(this.getBossId())) {
            return;
        }
        this.bossName = boss.getCustomName();
        this.bossPos = boss.position();
        this.isBossDead = true;
        if (dropCrate) {
            final Optional<UUID> source = Optional.ofNullable(event.getSource().getEntity()).map((Function<? super Entity, ? extends UUID>)Entity::getUUID);
            final Optional<VaultPlayer> killer = source.flatMap(vault::getPlayer);
            final Optional<VaultPlayer> host = vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer);
            if (killer.isPresent()) {
                this.spawnBossLoot(vault, killer.get(), world);
            }
            else if (host.isPresent() && host.get() instanceof VaultRunner) {
                this.spawnBossLoot(vault, host.get(), world);
            }
            else {
                vault.getPlayers().stream().filter(player -> player instanceof VaultRunner).findFirst().ifPresent(player -> this.spawnBossLoot(vault, player, world));
            }
        }
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
        final List<SummonAndKillBossObjective> matchingObjectives = vault.getPlayers().stream().map(player -> player.getActiveObjective(SummonAndKillBossObjective.class)).filter(Optional::isPresent).map(Optional::get).filter(o -> !o.isCompleted()).filter(SummonAndKillBossObjective::allObelisksClicked).filter(o -> o.getBossId().equals(event.getEntity().getUUID())).collect(Collectors.toList());
        if (matchingObjectives.isEmpty()) {
            vault.getActiveObjective(SummonAndKillBossObjective.class).ifPresent(objective -> objective.onBossDeath(event, vault, world, true));
        }
        else {
            matchingObjectives.forEach(objective -> objective.onBossDeath(event, vault, world, false));
        }
    }
    
    public static boolean isBossInVault(final VaultRaid vault, final LivingEntity entity) {
        final List<SummonAndKillBossObjective> matchingObjectives = vault.getPlayers().stream().map(player -> player.getActiveObjective(SummonAndKillBossObjective.class)).filter(Optional::isPresent).map(Optional::get).filter(o -> !o.isCompleted()).filter(SummonAndKillBossObjective::allObelisksClicked).filter(o -> o.getBossId().equals(entity.getUUID())).collect(Collectors.toList());
        vault.getActiveObjective(SummonAndKillBossObjective.class).ifPresent(matchingObjectives::add);
        return matchingObjectives.stream().anyMatch(o -> entity.getUUID().equals(o.getBossId()));
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("ProgressCount", this.progressCount);
        nbt.putInt("TargetCount", this.targetCount);
        if (this.getBossId() != null) {
            nbt.putString("BossId", this.getBossId().toString());
        }
        if (this.getBossName() != null) {
            nbt.putString("BossName", ITextComponent.Serializer.toJson(this.getBossName()));
        }
        if (this.getBossPos() != null) {
            nbt.putDouble("BossPosX", this.getBossPos().x());
            nbt.putDouble("BossPosY", this.getBossPos().y());
            nbt.putDouble("BossPosZ", this.getBossPos().z());
        }
        nbt.putBoolean("IsBossDead", this.isBossDead());
        return nbt;
    }
    
    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.progressCount = nbt.getInt("ProgressCount");
        this.targetCount = nbt.getInt("TargetCount");
        if (nbt.contains("BossId", 8)) {
            this.bossId = UUID.fromString(nbt.getString("BossId"));
        }
        if (nbt.contains("BossName", 8)) {
            this.bossName = (ITextComponent)ITextComponent.Serializer.fromJson(nbt.getString("BossName"));
        }
        this.bossPos = new Vector3d(nbt.getDouble("BossPosX"), nbt.getDouble("BossPosY"), nbt.getDouble("BossPosZ"));
        this.isBossDead = nbt.getBoolean("IsBossDead");
    }
}
