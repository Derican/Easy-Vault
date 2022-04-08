package iskallia.vault.world.vault.logic.objective;

import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.network.message.BossMusicMessage;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.VaultUtils;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = "the_vault", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SummonAndKillBossObjective extends VaultObjective {
    protected int targetCount = rand.nextInt(4) + 3;
    protected int progressCount;
    protected UUID bossId = null;
    protected ITextComponent bossName = null;
    protected Vector3d bossPos = null;
    protected boolean isBossDead = false;

    public SummonAndKillBossObjective(ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
    }

    public boolean allObelisksClicked() {
        return (this.progressCount >= this.targetCount);
    }

    public void addObelisk() {
        this.progressCount++;
    }

    public UUID getBossId() {
        return this.bossId;
    }

    public boolean isBossDead() {
        return this.isBossDead;
    }

    public boolean isBossSpawned() {
        return (this.bossId != null);
    }

    public ITextComponent getBossName() {
        return this.bossName;
    }

    public Vector3d getBossPos() {
        return this.bossPos;
    }

    public void setBoss(LivingEntity boss) {
        this.bossId = boss.getUUID();
    }


    public void setObjectiveTargetCount(int amount) {
        this.targetCount = amount;
    }


    @Nullable
    public ITextComponent getObjectiveTargetDescription(int amount) {
        return (ITextComponent) (new StringTextComponent("Required Obelisks: "))
                .append((ITextComponent) (new StringTextComponent(String.valueOf(amount))).withStyle(TextFormatting.GOLD));
    }


    @Nonnull
    public BlockState getObjectiveRelevantBlock() {
        return ModBlocks.OBELISK.defaultBlockState();
    }


    @Nullable
    public LootTable getRewardLootTable(VaultRaid vault, Function<ResourceLocation, LootTable> tblResolver) {
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        return (config != null) ? tblResolver.apply(config.getBossCrate()) : LootTable.EMPTY;
    }


    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) (new StringTextComponent("Kill the Boss")).withStyle(TextFormatting.GOLD);
    }


    public int modifyMinimumObjectiveCount(int objectives, int requiredAmount) {
        return Math.max(objectives, requiredAmount);
    }


    public void tick(VaultRaid vault, PlayerFilter filter, ServerWorld world) {
        super.tick(vault, filter, world);
        if (isCompleted()) {
            return;
        }

        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> {
            vPlayer.runIfPresent(world.getServer(), ());


            if (isBossSpawned()) {
                vPlayer.sendIfPresent(world.getServer(), new BossMusicMessage(true));
            }
        });


        if (this.isBossDead) {
            setCompleted();
        }
    }


    public void complete(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        super.complete(vault, player, world);
        player.sendIfPresent(world.getServer(), new BossMusicMessage(false));
        player.sendIfPresent(world.getServer(), VaultGoalMessage.clear());
    }


    public void complete(VaultRaid vault, ServerWorld world) {
        super.complete(vault, world);
        vault.getPlayers().forEach(player -> {
            player.sendIfPresent(world.getServer(), new BossMusicMessage(false));
            player.sendIfPresent(world.getServer(), VaultGoalMessage.clear());
        });
    }

    public void spawnBossLoot(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.random).withParameter(LootParameters.THIS_ENTITY, playerEntity).withParameter(LootParameters.ORIGIN, getBossPos()).withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.playerAttack((PlayerEntity) playerEntity)).withOptionalParameter(LootParameters.KILLER_ENTITY, playerEntity).withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY, playerEntity).withParameter(LootParameters.LAST_DAMAGE_PLAYER, playerEntity).withLuck(playerEntity.getLuck());
            LootContext ctx = builder.create(LootParameterSets.ENTITY);
            dropBossCrate(world, vault, player, ctx);
            for (int i = 1; i < vault.getPlayers().size(); i++) {
                if (rand.nextFloat() < 0.5F) {
                    dropBossCrate(world, vault, player, ctx);
                }
            }
            world.getServer().getPlayerList().broadcastMessage(getBossKillMessage((PlayerEntity) playerEntity), ChatType.CHAT, player.getPlayerId());
        });
    }


    private ITextComponent getBossKillMessage(PlayerEntity player) {
        IFormattableTextComponent msgContainer = (new StringTextComponent("")).withStyle(TextFormatting.WHITE);
        IFormattableTextComponent playerName = player.getDisplayName().copy();
        playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));

        return (ITextComponent) msgContainer.append((ITextComponent) playerName).append(" defeated ").append(getBossName()).append("!");
    }

    private void dropBossCrate(ServerWorld world, VaultRaid vault, VaultPlayer rewardPlayer, LootContext context) {
        NonNullList<ItemStack> stacks = createLoot(world, vault, context);

        vault.getProperties().getBase(VaultRaid.IS_RAFFLE).ifPresent(isRaffle -> {
            if (!isRaffle.booleanValue()) {
                return;
            }


            vault.getPlayers().stream().filter(()).min(Comparator.comparing(())).ifPresent(());
        });


        BlockPos dropPos = rewardPlayer.getServerPlayer(world.getServer()).map(Entity::blockPosition).orElse(new BlockPos(getBossPos()));

        ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);
        ItemEntity item = new ItemEntity((World) world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), crate);
        item.setDefaultPickUpDelay();
        world.addFreshEntity((Entity) item);

        this.crates.add(new VaultObjective.Crate((List<ItemStack>) stacks));
    }


    protected void addSpecialLoot(ServerWorld world, VaultRaid vault, LootContext context, NonNullList<ItemStack> stacks) {
        super.addSpecialLoot(world, vault, context, stacks);

        boolean isCowVault = ((Boolean) vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, Boolean.valueOf(false))).booleanValue();
        if (isCowVault) {
            stacks.add(new ItemStack((IItemProvider) ModItems.ARMOR_CRATE_HELLCOW));
        }
    }

    protected void onBossDeath(LivingDeathEvent event, VaultRaid vault, ServerWorld world, boolean dropCrate) {
        LivingEntity boss = event.getEntityLiving();
        if (!boss.getUUID().equals(getBossId()))
            return;
        this.bossName = boss.getCustomName();
        this.bossPos = boss.position();
        this.isBossDead = true;

        if (dropCrate) {
            Optional<UUID> source = Optional.<Entity>ofNullable(event.getSource().getEntity()).map(Entity::getUUID);
            Optional<VaultPlayer> killer = source.flatMap(vault::getPlayer);
            Optional<VaultPlayer> host = vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer);

            if (killer.isPresent()) {
                spawnBossLoot(vault, killer.get(), world);
            } else if (host.isPresent() && host.get() instanceof iskallia.vault.world.vault.player.VaultRunner) {
                spawnBossLoot(vault, host.get(), world);
            } else {
                vault.getPlayers().stream()
                        .filter(player -> player instanceof iskallia.vault.world.vault.player.VaultRunner)
                        .findFirst()
                        .ifPresent(player -> spawnBossLoot(vault, player, world));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBossDeath(LivingDeathEvent event) {
        if ((event.getEntity()).level.isClientSide())
            return;
        ServerWorld world = (ServerWorld) (event.getEntity()).level;

        VaultRaid vault = VaultRaidData.get(world).getAt(world, event.getEntity().blockPosition());
        if (!VaultUtils.inVault(vault, event.getEntity())) {
            return;
        }


        List<SummonAndKillBossObjective> matchingObjectives = (List<SummonAndKillBossObjective>) vault.getPlayers().stream().map(player -> player.getActiveObjective(SummonAndKillBossObjective.class)).filter(Optional::isPresent).map(Optional::get).filter(o -> !o.isCompleted()).filter(SummonAndKillBossObjective::allObelisksClicked).filter(o -> o.getBossId().equals(event.getEntity().getUUID())).collect(Collectors.toList());

        if (matchingObjectives.isEmpty()) {
            vault.getActiveObjective(SummonAndKillBossObjective.class).ifPresent(objective -> objective.onBossDeath(event, vault, world, true));
        } else {

            matchingObjectives.forEach(objective -> objective.onBossDeath(event, vault, world, false));
        }
    }


    public static boolean isBossInVault(VaultRaid vault, LivingEntity entity) {
        List<SummonAndKillBossObjective> matchingObjectives = (List<SummonAndKillBossObjective>) vault.getPlayers().stream().map(player -> player.getActiveObjective(SummonAndKillBossObjective.class)).filter(Optional::isPresent).map(Optional::get).filter(o -> !o.isCompleted()).filter(SummonAndKillBossObjective::allObelisksClicked).filter(o -> o.getBossId().equals(entity.getUUID())).collect(Collectors.toList());

        vault.getActiveObjective(SummonAndKillBossObjective.class).ifPresent(matchingObjectives::add);
        return matchingObjectives.stream().anyMatch(o -> entity.getUUID().equals(o.getBossId()));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("ProgressCount", this.progressCount);
        nbt.putInt("TargetCount", this.targetCount);

        if (getBossId() != null) {
            nbt.putString("BossId", getBossId().toString());
        }

        if (getBossName() != null) {
            nbt.putString("BossName", ITextComponent.Serializer.toJson(getBossName()));
        }

        if (getBossPos() != null) {
            nbt.putDouble("BossPosX", getBossPos().x());
            nbt.putDouble("BossPosY", getBossPos().y());
            nbt.putDouble("BossPosZ", getBossPos().z());
        }

        nbt.putBoolean("IsBossDead", isBossDead());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.progressCount = nbt.getInt("ProgressCount");
        this.targetCount = nbt.getInt("TargetCount");

        if (nbt.contains("BossId", 8)) {
            this.bossId = UUID.fromString(nbt.getString("BossId"));
        }

        if (nbt.contains("BossName", 8)) {
            this.bossName = (ITextComponent) ITextComponent.Serializer.fromJson(nbt.getString("BossName"));
        }

        this.bossPos = new Vector3d(nbt.getDouble("BossPosX"), nbt.getDouble("BossPosY"), nbt.getDouble("BossPosZ"));
        this.isBossDead = nbt.getBoolean("IsBossDead");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\SummonAndKillBossObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */