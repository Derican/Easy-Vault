package iskallia.vault.world.vault.logic.objective;

import net.minecraft.nbt.ListNBT;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Blocks;
import com.google.common.collect.Iterables;
import iskallia.vault.world.data.VaultRaidData;

import java.util.UUID;

import iskallia.vault.block.entity.VaultLootableTileEntity;
import net.minecraft.nbt.INBT;

import java.util.Iterator;

import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.world.vault.modifier.ArtifactChanceModifier;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.block.item.LootStatueBlockItem;
import iskallia.vault.util.StatueType;
import net.minecraft.util.IItemProvider;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.data.EternalsData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.time.VaultTimer;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import net.minecraft.util.math.MathHelper;
import iskallia.vault.world.vault.player.VaultRunner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.loot.LootContext;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.vault.player.VaultPlayer;

import java.util.Collection;

import net.minecraft.util.text.StringTextComponent;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import iskallia.vault.world.vault.gen.VaultGenerator;
import net.minecraft.loot.LootTable;

import java.util.function.Function;
import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

import net.minecraft.util.text.ITextComponent;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.server.MinecraftServer;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.world.vault.logic.task.VaultTask;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import com.google.common.collect.BiMap;
import iskallia.vault.world.vault.logic.task.IVaultTask;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class VaultObjective implements INBTSerializable<CompoundNBT>, IVaultTask {
    public static final BiMap<ResourceLocation, Supplier<? extends VaultObjective>> REGISTRY;
    public static final float COOP_DOUBLE_CRATE_CHANCE = 0.5f;
    protected static final Random rand;
    private ResourceLocation id;
    protected VaultTask onTick;
    private VaultTask onComplete;
    private boolean completed;
    private int completionTime;
    protected VListNBT<Crate, CompoundNBT> crates;

    protected VaultObjective() {
        this.completionTime = -1;
        this.crates = VListNBT.of(() -> new Crate());
    }

    public VaultObjective(final ResourceLocation id, final VaultTask onTick, final VaultTask onComplete) {
        this.completionTime = -1;
        this.crates = VListNBT.of(() -> new Crate());
        this.id = id;
        this.onTick = onTick;
        this.onComplete = onComplete;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public void initialize(final MinecraftServer srv, final VaultRaid vault) {
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public int getCompletionTime() {
        return this.completionTime;
    }

    public void setCompleted() {
        this.completed = true;
    }

    public void setCompletionTime(final int completionTime) {
        this.completionTime = completionTime;
    }

    public void setObjectiveTargetCount(final int amount) {
    }

    @Nullable
    public ITextComponent getObjectiveTargetDescription(final int amount) {
        return null;
    }

    @Nonnull
    public abstract BlockState getObjectiveRelevantBlock(final VaultRaid p0, final ServerWorld p1, final BlockPos p2);

    public void postProcessObjectiveRelevantBlock(final ServerWorld world, final BlockPos pos) {
    }

    @Nullable
    public abstract LootTable getRewardLootTable(final VaultRaid p0, final Function<ResourceLocation, LootTable> p1);

    public abstract ITextComponent getObjectiveDisplayName();

    @Nonnull
    public Supplier<? extends VaultGenerator> getVaultGenerator() {
        return VaultRaid.SINGLE_STAR;
    }

    @Nullable
    public VaultRoomLayoutGenerator getCustomLayout() {
        return null;
    }

    public ITextComponent getVaultName() {
        return (ITextComponent) new StringTextComponent("Vault");
    }

    @Deprecated
    public int getMaxObjectivePlacements() {
        return 10;
    }

    public int modifyObjectiveCount(final int objectives) {
        return objectives;
    }

    public int modifyMinimumObjectiveCount(final int objectives, final int requiredAmount) {
        return objectives;
    }

    public Collection<Crate> getCrates() {
        return this.crates;
    }

    public boolean shouldPauseTimer(final MinecraftServer srv, final VaultRaid vault) {
        return vault.getPlayers().stream().noneMatch(vPlayer -> vPlayer.isOnline(srv));
    }

    public int getVaultTimerStart(final int vaultTime) {
        return vaultTime;
    }

    public boolean preventsEatingExtensionFruit(final MinecraftServer srv, final VaultRaid vault) {
        return this.isCompleted();
    }

    public boolean preventsMobSpawning() {
        return false;
    }

    public boolean preventsTrappedChests() {
        return false;
    }

    public boolean preventsInfluences() {
        return false;
    }

    public boolean preventsNormalMonsterDrops() {
        return false;
    }

    public boolean preventsCatalystFragments() {
        return false;
    }

    public void notifyBail(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
    }

    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        if (this.isCompleted()) {
            return;
        }
        vault.getPlayers().forEach(vPlayer -> {
            if (filter.test(vPlayer.getPlayerId())) {
                this.onTick.execute(vault, vPlayer, world);
            }
        });
    }

    public void execute(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        this.onComplete.execute(vault, player, world);
    }

    public void complete(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        this.onComplete.execute(vault, player, world);
    }

    public void complete(final VaultRaid vault, final ServerWorld world) {
        vault.getPlayers().forEach(player -> this.onComplete.execute(vault, player, world));
    }

    public VaultObjective thenTick(final VaultTask task) {
        this.onTick = ((this.onTick == VaultTask.EMPTY) ? task : this.onTick.then(task));
        return this;
    }

    public VaultObjective thenComplete(final VaultTask task) {
        this.onComplete = ((this.onComplete == VaultTask.EMPTY) ? task : this.onComplete.then(task));
        return this;
    }

    protected NonNullList<ItemStack> createLoot(final ServerWorld world, final VaultRaid vault, final LootContext context) {
        final LootTable rewardLootTable = this.getRewardLootTable(vault, world.getServer().getLootTables()::get);
        if (rewardLootTable == null) {
            return NonNullList.create();
        }
        final NonNullList<ItemStack> stacks = NonNullList.create();
        final NonNullList<ItemStack> specialLoot = NonNullList.create();
        this.addSpecialLoot(world, vault, context, specialLoot);
        stacks.addAll((Collection) rewardLootTable.getRandomItems(context));
        vault.getPlayers().stream().filter(player -> player instanceof VaultRunner).findAny().ifPresent(vPlayer -> {
            final VaultTimer timer = vPlayer.getTimer();
            final float pTimeLeft = MathHelper.clamp(1.0f - timer.getRunTime() / (float) timer.getTotalTime(), 0.0f, 1.0f);
            final List<ItemStack> additionalLoot = new ArrayList<ItemStack>();
            additionalLoot.addAll(rewardLootTable.getRandomItems(context));
            additionalLoot.addAll(rewardLootTable.getRandomItems(context));
            final int rolls = Math.round(additionalLoot.size() * pTimeLeft);
            if (rolls > 0) {
                stacks.addAll((Collection) additionalLoot.subList(0, rolls));
            }
            return;
        });
        stacks.removeIf(ItemStack::isEmpty);
        for (int i = 0; i < stacks.size() - 54 + specialLoot.size(); ++i) {
            stacks.remove(world.random.nextInt(stacks.size()));
        }
        stacks.addAll((Collection) specialLoot);
        Collections.shuffle((List<?>) stacks);
        return stacks;
    }

    protected void addSpecialLoot(final ServerWorld world, final VaultRaid vault, final LootContext context, final NonNullList<ItemStack> stacks) {
        final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        final LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        final int eternals = EternalsData.get(world).getTotalEternals();
        if (eternals > 0) {
            stacks.add(new ItemStack((IItemProvider) ModItems.ETERNAL_SOUL, Math.min(world.random.nextInt(eternals) + 1, 64)));
        }
        if (vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(false)) {
            final String name = vault.getProperties().getValue(VaultRaid.PLAYER_BOSS_NAME);
            stacks.add(LootStatueBlockItem.getStatueBlockItem(name, StatueType.VAULT_BOSS));
            if (world.random.nextInt(4) != 0) {
            }
        }
        for (int traders = ModConfigs.SCALING_CHEST_REWARDS.traderCount(this.getId(), VaultRarity.COMMON, level), i = 0; i < traders; ++i) {
            stacks.add(new ItemStack((IItemProvider) ModItems.TRADER_CORE));
        }
        for (int statues = ModConfigs.SCALING_CHEST_REWARDS.statueCount(this.getId(), VaultRarity.COMMON, level), j = 0; j < statues; ++j) {
            ItemStack statue = new ItemStack((IItemProvider) ModBlocks.GIFT_NORMAL_STATUE);
            if (ModConfigs.SCALING_CHEST_REWARDS.isMegaStatue()) {
                statue = new ItemStack((IItemProvider) ModBlocks.GIFT_MEGA_STATUE);
            }
            stacks.add(statue);
        }
        final boolean cannotGetArtifact = vault.getActiveModifiersFor(PlayerFilter.any(), InventoryRestoreModifier.class).stream().anyMatch(InventoryRestoreModifier::preventsArtifact);
        if (!cannotGetArtifact && config != null) {
            float chance = config.getArtifactChance();
            for (final ArtifactChanceModifier modifier : vault.getActiveModifiersFor(PlayerFilter.any(), ArtifactChanceModifier.class)) {
                chance += modifier.getArtifactChanceIncrease();
            }
            if (vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, false)) {
                chance *= 2.0f;
            }
            if (world.getRandom().nextFloat() < chance) {
                stacks.add(new ItemStack((IItemProvider) ModItems.UNIDENTIFIED_ARTIFACT));
            }
        }
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.getId().toString());
        nbt.putBoolean("Completed", this.isCompleted());
        nbt.put("OnTick", (INBT) this.onTick.serializeNBT());
        nbt.put("OnComplete", (INBT) this.onComplete.serializeNBT());
        if (this.getCompletionTime() != -1) {
            nbt.putInt("CompletionTime", this.getCompletionTime());
        }
        nbt.put("Crates", (INBT) this.crates.serializeNBT());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.completed = nbt.getBoolean("Completed");
        this.onTick = VaultTask.fromNBT(nbt.getCompound("OnTick"));
        this.onComplete = VaultTask.fromNBT(nbt.getCompound("OnComplete"));
        if (nbt.contains("CompletionTime", 3)) {
            this.completionTime = nbt.getInt("CompletionTime");
        }
        this.crates.deserializeNBT(nbt.getList("Crates", 10));
    }

    public static VaultObjective fromNBT(final CompoundNBT nbt) {
        final VaultObjective objective = (VaultObjective.REGISTRY.get(new ResourceLocation(nbt.getString("Id")))).get();
        objective.deserializeNBT(nbt);
        return objective;
    }

    @Nullable
    public static VaultObjective getObjective(final ResourceLocation key) {
        return (VaultObjective.REGISTRY.getOrDefault(key, () -> null)).get();
    }

    public static <T extends VaultObjective> Supplier<T> register(final Supplier<T> objective) {
        VaultObjective.REGISTRY.put(objective.get().getId(), objective);
        return objective;
    }

    public static VaultLootableTileEntity.ExtendedGenerator getObjectiveBlock() {
        return new VaultLootableTileEntity.ExtendedGenerator() {
            @Nonnull
            @Override
            public BlockState generate(final ServerWorld world, final BlockPos pos, final Random random, final String poolName, final UUID playerUUID) {
                final VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
                final VaultObjective objective = (VaultObjective) Iterables.getFirst((Iterable) vault.getAllObjectives(), null);
                if (objective == null) {
                    return Blocks.AIR.defaultBlockState();
                }
                return objective.getObjectiveRelevantBlock(vault, world, pos);
            }

            @Override
            public void postProcess(final ServerWorld world, final BlockPos pos) {
                final VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
                final VaultObjective objective = (VaultObjective) Iterables.getFirst((Iterable) vault.getAllObjectives(), null);
                if (objective != null) {
                    objective.postProcessObjectiveRelevantBlock(world, pos);
                }
            }
        };
    }

    static {
        REGISTRY = (BiMap) HashBiMap.create();
        rand = new Random();
    }

    public static class Crate implements INBTSerializable<CompoundNBT> {
        private List<ItemStack> contents;

        private Crate() {
            this.contents = new ArrayList<ItemStack>();
        }

        public Crate(final List<ItemStack> contents) {
            this.contents = new ArrayList<ItemStack>();
            this.contents = contents;
        }

        public List<ItemStack> getContents() {
            return this.contents;
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            final ListNBT contentsList = new ListNBT();
            this.contents.forEach(stack -> contentsList.add(stack.save(new CompoundNBT())));
            nbt.put("Contents", (INBT) contentsList);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.contents.clear();
            final ListNBT contentsList = nbt.getList("Contents", 10);
            contentsList.stream().map(inbt -> (CompoundNBT) inbt).forEach(compoundNBT -> this.contents.add(ItemStack.of(compoundNBT)));
        }
    }
}
