package iskallia.vault.world.vault.logic.objective;

import com.google.common.collect.Iterables;
import iskallia.vault.block.entity.VaultLootableTileEntity;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.world.data.EternalsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.VaultGenerator;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import iskallia.vault.world.vault.logic.task.IVaultTask;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.modifier.ArtifactChanceModifier;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.time.VaultTimer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public abstract class VaultObjective implements INBTSerializable<CompoundNBT>, IVaultTask {
    public static final Map<ResourceLocation, Supplier<? extends VaultObjective>> REGISTRY = new HashMap<>();
    public static final float COOP_DOUBLE_CRATE_CHANCE = 0.5F;
    protected static final Random rand = new Random();

    private ResourceLocation id;

    private VaultTask onTick;
    private VaultTask onComplete;
    private boolean completed;
    private int completionTime = -1;

    protected VListNBT<Crate, CompoundNBT> crates = VListNBT.of(() -> new Crate());


    public VaultObjective(ResourceLocation id, VaultTask onTick, VaultTask onComplete) {
        this.id = id;
        this.onTick = onTick;
        this.onComplete = onComplete;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public void initialize(MinecraftServer srv, VaultRaid vault) {
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

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public void setObjectiveTargetCount(int amount) {
    }

    @Nullable
    public ITextComponent getObjectiveTargetDescription(int amount) {
        return null;
    }


    public void postProcessObjectiveRelevantBlock(ServerWorld world, BlockPos pos) {
    }


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

    public int modifyObjectiveCount(int objectives) {
        return objectives;
    }

    public int modifyMinimumObjectiveCount(int objectives, int requiredAmount) {
        return objectives;
    }

    public Collection<Crate> getCrates() {
        return (Collection) this.crates;
    }

    public boolean shouldPauseTimer(MinecraftServer srv, VaultRaid vault) {
        return vault.getPlayers().stream().noneMatch(vPlayer -> vPlayer.isOnline(srv));
    }

    public int getVaultTimerStart(int vaultTime) {
        return vaultTime;
    }

    public boolean preventsEatingExtensionFruit(MinecraftServer srv, VaultRaid vault) {
        return isCompleted();
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

    public void notifyBail(VaultRaid vault, VaultPlayer player, ServerWorld world) {
    }

    public void tick(VaultRaid vault, PlayerFilter filter, ServerWorld world) {
        if (isCompleted())
            return;
        vault.getPlayers().forEach(vPlayer -> {
            if (filter.test(vPlayer.getPlayerId())) {
                this.onTick.execute(vault, vPlayer, world);
            }
        });
    }


    public void execute(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        this.onComplete.execute(vault, player, world);
    }

    public void complete(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        this.onComplete.execute(vault, player, world);
    }

    public void complete(VaultRaid vault, ServerWorld world) {
        vault.getPlayers().forEach(player -> this.onComplete.execute(vault, player, world));
    }

    public VaultObjective thenTick(VaultTask task) {
        this.onTick = (this.onTick == VaultTask.EMPTY) ? task : this.onTick.then(task);
        return this;
    }

    public VaultObjective thenComplete(VaultTask task) {
        this.onComplete = (this.onComplete == VaultTask.EMPTY) ? task : this.onComplete.then(task);
        return this;
    }

    protected NonNullList<ItemStack> createLoot(ServerWorld world, VaultRaid vault, LootContext context) {
        LootTable rewardLootTable = getRewardLootTable(vault, world.getServer().getLootTables()::get);
        if (rewardLootTable == null) {
            return NonNullList.create();
        }

        NonNullList<ItemStack> stacks = NonNullList.create();
        NonNullList<ItemStack> specialLoot = NonNullList.create();

        addSpecialLoot(world, vault, context, specialLoot);
        stacks.addAll(rewardLootTable.getRandomItems(context));

        vault.getPlayers().stream()
                .filter(player -> player instanceof iskallia.vault.world.vault.player.VaultRunner)
                .findAny()
                .ifPresent(vPlayer -> {
                    VaultTimer timer = vPlayer.getTimer();

                    float pTimeLeft = MathHelper.clamp(1.0F - timer.getRunTime() / timer.getTotalTime(), 0.0F, 1.0F);

                    List<ItemStack> additionalLoot = new ArrayList<>();
                    additionalLoot.addAll(rewardLootTable.getRandomItems(context));
                    additionalLoot.addAll(rewardLootTable.getRandomItems(context));
                    int rolls = Math.round(additionalLoot.size() * pTimeLeft);
                    if (rolls > 0) {
                        stacks.addAll(additionalLoot.subList(0, rolls));
                    }
                });
        stacks.removeIf(ItemStack::isEmpty);


        for (int i = 0; i < stacks.size() - 54 + specialLoot.size(); i++) {
            stacks.remove(world.random.nextInt(stacks.size()));
        }
        stacks.addAll((Collection) specialLoot);
        Collections.shuffle((List<?>) stacks);
        return stacks;
    }

    protected void addSpecialLoot(ServerWorld world, VaultRaid vault, LootContext context, NonNullList<ItemStack> stacks) {
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);


        int eternals = EternalsData.get(world).getTotalEternals();
        if (eternals > 0) {
            stacks.add(new ItemStack((IItemProvider) ModItems.ETERNAL_SOUL, Math.min(world.random.nextInt(eternals) + 1, 64)));
        }


        if (((Boolean) vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(Boolean.valueOf(false))).booleanValue()) {
            String name = (String) vault.getProperties().getValue(VaultRaid.PLAYER_BOSS_NAME);
            stacks.add(LootStatueBlockItem.getStatueBlockItem(name, StatueType.VAULT_BOSS));

            if (world.random.nextInt(4) != 0) ;
        }


        int traders = ModConfigs.SCALING_CHEST_REWARDS.traderCount(getId(), VaultRarity.COMMON, level);
        for (int i = 0; i < traders; i++) {
            stacks.add(new ItemStack((IItemProvider) ModItems.TRADER_CORE));
        }
        int statues = ModConfigs.SCALING_CHEST_REWARDS.statueCount(getId(), VaultRarity.COMMON, level);
        for (int j = 0; j < statues; j++) {
            ItemStack statue = new ItemStack((IItemProvider) ModBlocks.GIFT_NORMAL_STATUE);
            if (ModConfigs.SCALING_CHEST_REWARDS.isMegaStatue()) {
                statue = new ItemStack((IItemProvider) ModBlocks.GIFT_MEGA_STATUE);
            }
            stacks.add(statue);
        }


        boolean cannotGetArtifact = vault.getActiveModifiersFor(PlayerFilter.any(), InventoryRestoreModifier.class).stream().anyMatch(InventoryRestoreModifier::preventsArtifact);
        if (!cannotGetArtifact && config != null) {
            float chance = config.getArtifactChance();

            for (ArtifactChanceModifier modifier : vault.getActiveModifiersFor(PlayerFilter.any(), ArtifactChanceModifier.class)) {
                chance += modifier.getArtifactChanceIncrease();
            }

            if (((Boolean) vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, Boolean.valueOf(false))).booleanValue()) {
                chance *= 2.0F;
            }
            if (world.getRandom().nextFloat() < chance) {
                stacks.add(new ItemStack((IItemProvider) ModItems.UNIDENTIFIED_ARTIFACT));
            }
        }
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", getId().toString());
        nbt.putBoolean("Completed", isCompleted());
        nbt.put("OnTick", (INBT) this.onTick.serializeNBT());
        nbt.put("OnComplete", (INBT) this.onComplete.serializeNBT());

        if (getCompletionTime() != -1) {
            nbt.putInt("CompletionTime", getCompletionTime());
        }

        nbt.put("Crates", (INBT) this.crates.serializeNBT());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.completed = nbt.getBoolean("Completed");
        this.onTick = VaultTask.fromNBT(nbt.getCompound("OnTick"));
        this.onComplete = VaultTask.fromNBT(nbt.getCompound("OnComplete"));

        if (nbt.contains("CompletionTime", 3)) {
            this.completionTime = nbt.getInt("CompletionTime");
        }
        this.crates.deserializeNBT(nbt.getList("Crates", 10));
    }

    public static VaultObjective fromNBT(CompoundNBT nbt) {
        VaultObjective objective = ((Supplier<VaultObjective>) REGISTRY.get(new ResourceLocation(nbt.getString("Id")))).get();
        objective.deserializeNBT(nbt);
        return objective;
    }

    @Nullable
    public static VaultObjective getObjective(ResourceLocation key) {
        return ((Supplier<VaultObjective>) REGISTRY.getOrDefault(key, () -> null)).get();
    }

    public static <T extends VaultObjective> Supplier<T> register(Supplier<T> objective) {
        REGISTRY.put(((VaultObjective) objective.get()).getId(), objective);
        return objective;
    }

    public static VaultLootableTileEntity.ExtendedGenerator getObjectiveBlock() {
        return new VaultLootableTileEntity.ExtendedGenerator() {
            @Nonnull
            public BlockState generate(ServerWorld world, BlockPos pos, Random random, String poolName, UUID playerUUID) {
                VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
                VaultObjective objective = (VaultObjective) Iterables.getFirst(vault.getAllObjectives(), null);
                if (objective == null) {
                    return Blocks.AIR.defaultBlockState();
                }
                return objective.getObjectiveRelevantBlock();
            }


            public void postProcess(ServerWorld world, BlockPos pos) {
                VaultRaid vault = VaultRaidData.get(world).getAt(world, pos);
                VaultObjective objective = (VaultObjective) Iterables.getFirst(vault.getAllObjectives(), null);
                if (objective != null)
                    objective.postProcessObjectiveRelevantBlock(world, pos);
            }
        };
    }

    protected VaultObjective() {
    }

    @Nonnull
    public abstract BlockState getObjectiveRelevantBlock();

    @Nullable
    public abstract LootTable getRewardLootTable(VaultRaid paramVaultRaid, Function<ResourceLocation, LootTable> paramFunction);

    public abstract ITextComponent getObjectiveDisplayName();

    public static class Crate implements INBTSerializable<CompoundNBT> {
        private List<ItemStack> contents = new ArrayList<>();


        public Crate(List<ItemStack> contents) {
            this.contents = contents;
        }

        public List<ItemStack> getContents() {
            return this.contents;
        }


        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT contentsList = new ListNBT();
            this.contents.forEach(stack -> contentsList.add(stack.save(new CompoundNBT())));
            nbt.put("Contents", (INBT) contentsList);
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.contents.clear();
            ListNBT contentsList = nbt.getList("Contents", 10);

            contentsList.stream().map(inbt -> (CompoundNBT) inbt).forEach(compoundNBT -> this.contents.add(ItemStack.of(compoundNBT)));
        }

        private Crate() {
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\VaultObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */