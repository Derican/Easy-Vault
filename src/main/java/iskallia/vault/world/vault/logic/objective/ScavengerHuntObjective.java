package iskallia.vault.world.vault.logic.objective;

import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.config.ScavengerHuntConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.BasicScavengerItem;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ScavengerHuntObjective extends VaultObjective {
    public static final int INVENTORY_SIZE = 45;
    private final ChestWatcher chestWatcher = new ChestWatcher();
    private final Inventory inventoryMirror = new Inventory();

    private final List<ItemSubmission> submissions = new ArrayList<>();
    private int requiredSubmissions;
    private NonNullList<ItemStack> chestInventory = NonNullList.withSize(45, ItemStack.EMPTY);

    public ScavengerHuntObjective(ResourceLocation id) {
        this(id, ModConfigs.SCAVENGER_HUNT.getTotalRequiredItems());
    }

    private ScavengerHuntObjective(ResourceLocation id, int requiredSubmissions) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.requiredSubmissions = requiredSubmissions;
    }

    public IInventory getScavengerChestInventory() {
        return this.inventoryMirror;
    }

    public ChestWatcher getChestWatcher() {
        return this.chestWatcher;
    }

    private Stream<ItemSubmission> getActiveSubmissionsFilter() {
        return getAllSubmissions().stream()
                .filter(submission -> !submission.isFinished());
    }

    public List<ItemSubmission> getActiveSubmissions() {
        return getActiveSubmissionsFilter().collect((Collector) Collectors.toList());
    }

    public List<ItemSubmission> getAllSubmissions() {
        return Collections.unmodifiableList(this.submissions);
    }

    public Predicate<ScavengerHuntConfig.ItemEntry> getGenerationDropFilter() {
        List<ItemSubmission> submissions = getActiveSubmissions();
        return entry -> {
            Item generatedItem = entry.getItem();
            for (ItemSubmission submission : submissions) {
                if (generatedItem.equals(submission.getRequiredItem())) {
                    return true;
                }
            }
            return false;
        };
    }

    public boolean trySubmitItem(UUID vaultIdentifier, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (!vaultIdentifier.equals(BasicScavengerItem.getVaultIdentifier(stack))) {
            return false;
        }
        Item providedItem = stack.getItem();


        boolean addedItem = ((Boolean) getActiveSubmissionsFilter().filter(submission -> providedItem.equals(submission.requiredItem)).findFirst().map(submission -> {
            int add = Math.min(stack.getCount(), submission.requiredAmount - submission.currentAmount);
            ItemSubmission itemSubmission = submission;
            itemSubmission.currentAmount = itemSubmission.currentAmount + add;
            stack.shrink(add);
            return Boolean.valueOf(true);
        }).orElse(Boolean.valueOf(false))).booleanValue();

        if (getAllSubmissions().stream().filter(ItemSubmission::isFinished).count() >= this.requiredSubmissions) {
            setCompleted();
        }
        return addedItem;
    }


    public void setObjectiveTargetCount(int amount) {
        this.requiredSubmissions = amount;
    }


    @Nullable
    public ITextComponent getObjectiveTargetDescription(int amount) {
        return (ITextComponent) (new StringTextComponent("Total required Item Types: "))
                .append((ITextComponent) (new StringTextComponent(String.valueOf(amount))).withStyle(TextFormatting.GREEN));
    }


    @Nonnull
    public BlockState getObjectiveRelevantBlock() {
        return ModBlocks.SCAVENGER_CHEST.defaultBlockState();
    }


    @Nullable
    public LootTable getRewardLootTable(VaultRaid vault, Function<ResourceLocation, LootTable> tblResolver) {
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        return (config != null) ? tblResolver.apply(config.getScavengerCrate()) : LootTable.EMPTY;
    }


    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) (new StringTextComponent("Scavenger Hunt")).withStyle(TextFormatting.GREEN);
    }


    public ITextComponent getVaultName() {
        return (ITextComponent) new StringTextComponent("Scavenger Vault");
    }


    public void tick(VaultRaid vault, PlayerFilter filter, ServerWorld world) {
        super.tick(vault, filter, world);
        MinecraftServer srv = world.getServer();

        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));


        if (isCompleted())
            return;
        long activeSubmissions = getActiveSubmissionsFilter().count();
        if (world.getGameTime() % 20L == 0L) {


            boolean addedAnyItem = ((Boolean) vault.getProperties().getBase(VaultRaid.IDENTIFIER).map(identifier -> {
                boolean addedItem = false;
                NonNullList<ItemStack> inventory = this.chestInventory;
                for (int slot = 0; slot < inventory.size(); slot++) {
                    ItemStack stack = (ItemStack) inventory.get(slot);
                    if (!stack.isEmpty()) if (trySubmitItem(identifier, stack)) {
                        this.chestInventory.set(slot, stack);
                        updateOpenContainers(srv, vault, slot, stack);
                        addedItem = true;
                    }
                }
                return Boolean.valueOf(addedItem);
            }).orElse(Boolean.valueOf(false))).booleanValue();

            if (activeSubmissions > getActiveSubmissionsFilter().count()) {
                vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));


            } else if (addedAnyItem) {
                vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));
            }
        }


        if (isCompleted()) {
            spawnRewards(world, vault);
        }

        if (getAllSubmissions().size() < this.requiredSubmissions) {
            ItemSubmission newEntry = getNewEntry(vault);
            if (newEntry != null) {
                this.submissions.add(newEntry);
            }
        }
    }

    private void updateOpenContainers(MinecraftServer srv, VaultRaid vault, int slot, ItemStack stack) {
        vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));
    }


    public void complete(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        super.complete(vault, player, world);
        player.sendIfPresent(world.getServer(), VaultGoalMessage.clear());
    }


    public void complete(VaultRaid vault, ServerWorld world) {
        super.complete(vault, world);
        vault.getPlayers().forEach(player -> player.sendIfPresent(world.getServer(), VaultGoalMessage.clear()));
    }


    public void spawnRewards(ServerWorld world, VaultRaid vault) {
        VaultPlayer rewardPlayer = vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer).filter(vPlayer -> vPlayer instanceof iskallia.vault.world.vault.player.VaultRunner).orElseGet(() -> (VaultPlayer) vault.getPlayers().stream().filter(()).findAny().orElse(null));


        if (rewardPlayer == null) {
            return;
        }
        rewardPlayer.runIfPresent(world.getServer(), sPlayer -> {
            BlockPos pos = sPlayer.blockPosition();
            LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.random).withParameter(LootParameters.THIS_ENTITY, sPlayer).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf((Vector3i) pos)).withLuck(sPlayer.getLuck());
            LootContext ctx = builder.create(LootParameterSets.CHEST);
            dropRewardCrate(world, vault, pos, ctx);
            for (int i = 1; i < vault.getPlayers().size(); i++) {
                if (rand.nextFloat() < 0.5F) {
                    dropRewardCrate(world, vault, pos, ctx);
                }
            }
            IFormattableTextComponent msgContainer = (new StringTextComponent("")).withStyle(TextFormatting.WHITE);
            IFormattableTextComponent playerName = sPlayer.getDisplayName().copy();
            playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
            MiscUtils.broadcast((ITextComponent) msgContainer.append((ITextComponent) playerName).append(" finished a Scavenger Hunt!"));
        });
    }


    private void dropRewardCrate(ServerWorld world, VaultRaid vault, BlockPos pos, LootContext context) {
        NonNullList<ItemStack> stacks = createLoot(world, vault, context);

        ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE_SCAVENGER, stacks);
        ItemEntity item = new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(), crate);
        item.setDefaultPickUpDelay();
        world.addFreshEntity((Entity) item);

        this.crates.add(new VaultObjective.Crate((List<ItemStack>) stacks));
    }


    @Nullable
    private ItemSubmission getNewEntry(VaultRaid vault) {
        List<Item> currentItems = (List<Item>) this.submissions.stream().map(submission -> submission.requiredItem).collect(Collectors.toList());
        int players = vault.getPlayers().size();
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        float multiplier = 1.0F + (players - 1) * 0.5F;
        ScavengerHuntConfig.ItemEntry newEntry = ModConfigs.SCAVENGER_HUNT.getRandomRequiredItem(currentItems::contains);
        if (newEntry == null) {
            return null;
        }
        ScavengerHuntConfig.SourceType sourceType = ModConfigs.SCAVENGER_HUNT.getRequirementSource(newEntry.createItemStack());
        switch (sourceType) {
            case MOB:
                multiplier *= 1.0F + level / 100.0F;
                break;
            case CHEST:
                multiplier *= 1.0F + level / 100.0F / 1.5F;
                break;
        }
        return ItemSubmission.fromConfigEntry(newEntry, multiplier);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();

        ListNBT list = new ListNBT();
        for (ItemSubmission submission : this.submissions) {
            list.add(submission.serialize());
        }
        tag.put("submissions", (INBT) list);

        tag.putInt("requiredSubmissions", this.requiredSubmissions);

        ListNBT inventoryList = new ListNBT();
        for (int slot = 0; slot < this.chestInventory.size(); slot++) {
            ItemStack stack = (ItemStack) this.chestInventory.get(slot);
            if (!stack.isEmpty()) {
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putInt("slot", slot);
                itemTag.put("item", (INBT) stack.serializeNBT());
                inventoryList.add(itemTag);
            }
        }
        tag.put("inventory", (INBT) inventoryList);

        return tag;
    }

    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.submissions.clear();
        ListNBT list = tag.getList("submissions", 10);
        for (int index = 0; index < list.size(); index++) {
            this.submissions.add(ItemSubmission.deserialize(list.getCompound(index)));
        }

        this.requiredSubmissions = tag.getInt("requiredSubmissions");

        this.chestInventory = NonNullList.withSize(45, ItemStack.EMPTY);
        ListNBT inventoryList = tag.getList("inventory", 10);
        for (int i = 0; i < inventoryList.size(); i++) {
            CompoundNBT itemTag = inventoryList.getCompound(i);
            int slot = itemTag.getInt("slot");
            ItemStack stack = ItemStack.of(itemTag.getCompound("item"));
            this.chestInventory.set(slot, stack);
        }
    }


    public static class ItemSubmission {
        private final Item requiredItem;
        private final int requiredAmount;
        private int currentAmount = 0;

        public ItemSubmission(Item requiredItem, int requiredAmount) {
            this.requiredItem = requiredItem;
            this.requiredAmount = requiredAmount;
        }

        private static ItemSubmission fromConfigEntry(ScavengerHuntConfig.ItemEntry entry, float multiplyAmount) {
            return new ItemSubmission(entry.getItem(), MathHelper.ceil(entry.getRandomAmount() * multiplyAmount));
        }

        public boolean isFinished() {
            return (this.currentAmount >= this.requiredAmount);
        }

        public Item getRequiredItem() {
            return this.requiredItem;
        }

        public int getRequiredAmount() {
            return this.requiredAmount;
        }

        public int getCurrentAmount() {
            return this.currentAmount;
        }

        public CompoundNBT serialize() {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("item", this.requiredItem.getRegistryName().toString());
            tag.putInt("required", this.requiredAmount);
            tag.putInt("current", this.currentAmount);
            return tag;
        }

        public static ItemSubmission deserialize(CompoundNBT tag) {
            Item requiredItem = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("item")));
            int requiredAmount = tag.getInt("required");
            int currentAmount = tag.getInt("current");
            ItemSubmission submitted = new ItemSubmission(requiredItem, requiredAmount);
            submitted.currentAmount = currentAmount;
            return submitted;
        }
    }


    public class ChestWatcher
            implements IContainerListener {
        public void refreshContainer(Container container, NonNullList<ItemStack> items) {
        }

        public void slotChanged(Container container, int slotId, ItemStack stack) {
            if (slotId >= 0 && slotId < 45) {
                ScavengerHuntObjective.this.chestInventory.set(slotId, stack);
            }
        }

        public void setContainerData(Container containerIn, int varToUpdate, int newValue) {
        }
    }

    private class Inventory
            implements IInventory {
        private Inventory() {
        }

        public int getContainerSize() {
            return ScavengerHuntObjective.this.chestInventory.size();
        }


        public boolean isEmpty() {
            return ScavengerHuntObjective.this.chestInventory.isEmpty();
        }


        public ItemStack getItem(int index) {
            return (ItemStack) ScavengerHuntObjective.this.chestInventory.get(index);
        }


        public ItemStack removeItem(int index, int count) {
            return ItemStackHelper.removeItem((List) ScavengerHuntObjective.this.chestInventory, index, count);
        }


        public ItemStack removeItemNoUpdate(int index) {
            ItemStack existing = getItem(index);
            setItem(index, ItemStack.EMPTY);
            return existing;
        }


        public void setItem(int index, ItemStack stack) {
            ScavengerHuntObjective.this.chestInventory.set(index, stack);
        }


        public void setChanged() {
        }


        public boolean stillValid(PlayerEntity player) {
            return true;
        }


        public void clearContent() {
            ScavengerHuntObjective.this.chestInventory.clear();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\ScavengerHuntObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */