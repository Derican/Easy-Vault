package iskallia.vault.world.vault.logic.objective;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;

import java.util.function.IntSupplier;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.CompoundNBT;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSetSlotPacket;
import iskallia.vault.container.ScavengerChestContainer;

import java.util.Set;

import net.minecraft.util.Util;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.vault.modifier.VaultModifier;
import iskallia.vault.world.vault.modifier.ArtifactChanceModifier;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import iskallia.vault.world.vault.modifier.TimerModifier;
import iskallia.vault.world.vault.modifier.NoExitModifier;
import iskallia.vault.config.VaultModifiersConfig;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import iskallia.vault.world.vault.time.extension.SandExtension;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import iskallia.vault.entity.VaultSandEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.server.MinecraftServer;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultRoom;

import java.util.Collection;
import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkDirection;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.PlayerFilter;
import net.minecraft.loot.LootTable;

import java.util.function.Function;
import javax.annotation.Nonnull;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import iskallia.vault.item.BasicScavengerItem;

import java.util.UUID;
import javax.annotation.Nullable;

import iskallia.vault.world.vault.gen.layout.SquareRoomLayout;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;

import java.util.Iterator;

import net.minecraft.item.Item;
import iskallia.vault.config.ScavengerHuntConfig;

import java.util.function.Predicate;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.inventory.IInventory;
import iskallia.vault.Vault;

import java.util.ArrayList;

import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.init.ModConfigs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class TreasureHuntObjective extends VaultObjective {
    public static final int INVENTORY_SIZE = 45;
    private final ChestWatcher chestWatcher;
    private final Inventory inventoryMirror;
    private final List<ItemSubmission> submissions;
    private int requiredSubmissions;
    private int addedSand;
    private int sandPerModifier;
    private NonNullList<ItemStack> chestInventory;
    private ResourceLocation roomPool;
    private ResourceLocation tunnelPool;

    public TreasureHuntObjective(final ResourceLocation id) {
        this(id, ModConfigs.TREASURE_HUNT.getTotalRequiredItems());
    }

    private TreasureHuntObjective(final ResourceLocation id, final int requiredSubmissions) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.chestWatcher = new ChestWatcher();
        this.inventoryMirror = new Inventory();
        this.submissions = new ArrayList<ItemSubmission>();
        this.sandPerModifier = -1;
        this.chestInventory = NonNullList.withSize(45, ItemStack.EMPTY);
        this.roomPool = Vault.id("raid/rooms");
        this.tunnelPool = Vault.id("vault/tunnels");
        this.requiredSubmissions = requiredSubmissions;
    }

    public IInventory getScavengerChestInventory() {
        return this.inventoryMirror;
    }

    public ChestWatcher getChestWatcher() {
        return this.chestWatcher;
    }

    private Stream<ItemSubmission> getActiveSubmissionsFilter() {
        return this.getAllSubmissions().stream().filter(submission -> !submission.isFinished());
    }

    public List<ItemSubmission> getActiveSubmissions() {
        return this.getActiveSubmissionsFilter().collect(Collectors.toList());
    }

    public List<ItemSubmission> getAllSubmissions() {
        return Collections.unmodifiableList(this.submissions);
    }

    public Predicate<ScavengerHuntConfig.ItemEntry> getGenerationDropFilter() {
        final List<ItemSubmission> submissions = this.getActiveSubmissions();
        return entry -> {
            final Item generatedItem = entry.getItem();

            for (ItemSubmission submission : submissions) {
                if (generatedItem.equals(submission.getRequiredItem())) {
                    return true;
                }
            }
            return false;
        };
    }

    public void setSandPerModifier(final int sandPerModifier) {
        this.sandPerModifier = sandPerModifier;
    }

    public void setRoomPool(final ResourceLocation roomPool) {
        this.roomPool = roomPool;
    }

    public void setTunnelPool(final ResourceLocation tunnelPool) {
        this.tunnelPool = tunnelPool;
    }

    @Nullable
    @Override
    public VaultRoomLayoutGenerator getCustomLayout() {
        final SquareRoomLayout layout = new SquareRoomLayout(19);
        layout.setRoomId(this.roomPool);
        layout.setTunnelId(this.tunnelPool);
        return layout;
    }

    public boolean trySubmitItem(final UUID vaultIdentifier, final ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (!vaultIdentifier.equals(BasicScavengerItem.getVaultIdentifier(stack))) {
            return false;
        }
        final Item providedItem = stack.getItem();
        final boolean addedItem = this.getActiveSubmissionsFilter().filter(submission -> providedItem.equals(submission.requiredItem)).findFirst().map(submission -> {
            final int add = Math.min(stack.getCount(), submission.requiredAmount - submission.currentAmount);
            submission.currentAmount += add;
            stack.shrink(add);
            return true;
        }).orElse(false);
        if (this.getAllSubmissions().stream().filter(ItemSubmission::isFinished).count() >= this.requiredSubmissions) {
            this.setCompleted();
        }
        return addedItem;
    }

    @Override
    public void setObjectiveTargetCount(final int amount) {
        this.requiredSubmissions = amount;
    }

    @Nullable
    @Override
    public ITextComponent getObjectiveTargetDescription(final int amount) {
        return new StringTextComponent("Total required Item Types: ").append(new StringTextComponent(String.valueOf(amount)).withStyle(TextFormatting.GREEN));
    }

    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock(final VaultRaid vault, final ServerWorld world, final BlockPos pos) {
        return ModBlocks.SCAVENGER_CHEST.defaultBlockState();
    }

    @Nullable
    @Override
    public LootTable getRewardLootTable(final VaultRaid vault, final Function<ResourceLocation, LootTable> tblResolver) {
        return null;
    }

    @Override
    public ITextComponent getObjectiveDisplayName() {
        return new StringTextComponent("Treasure Hunt").withStyle(TextFormatting.GREEN);
    }

    @Override
    public ITextComponent getVaultName() {
        return new StringTextComponent("Treasure Vault");
    }

    @Override
    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        super.tick(vault, filter, world);
        final MinecraftServer srv = world.getServer();
        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, playerEntity -> {
            final VaultGoalMessage pkt = VaultGoalMessage.treasureHunt(this.getActiveSubmissions());
            ModNetwork.CHANNEL.sendTo(pkt, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }));
        if (this.isCompleted()) {
            return;
        }
        final long activeSubmissions = this.getActiveSubmissionsFilter().count();
        if (world.getGameTime() % 20L == 0L) {
            final boolean addedAnyItem = vault.getProperties().getBase(VaultRaid.IDENTIFIER).map(identifier -> {
                boolean addedItem = false;
                final NonNullList<ItemStack> inventory = this.chestInventory;
                for (int slot = 0; slot < inventory.size(); ++slot) {
                    final ItemStack stack = inventory.get(slot);
                    if (!stack.isEmpty()) {
                        if (this.trySubmitItem(identifier, stack)) {
                            this.chestInventory.set(slot, stack);
                            this.updateOpenContainers(srv, vault, slot, stack);
                            addedItem = true;
                        }
                    }
                }
                return Boolean.valueOf(addedItem);
            }).orElse(false);
            if (activeSubmissions > this.getActiveSubmissionsFilter().count()) {
                vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(srv, sPlayer -> world.playSound(null, sPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0f, 1.0f)));
            } else if (addedAnyItem) {
                vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(srv, sPlayer -> world.playSound(null, sPlayer.blockPosition(), SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.1f, 1.4f)));
            }
        }
        if (this.getAllSubmissions().size() < this.requiredSubmissions) {
            final ItemSubmission newEntry = this.getNewEntry(vault);
            if (newEntry != null) {
                this.submissions.add(newEntry);
            }
        }
        vault.getPlayers().stream().map(vPlayer -> vPlayer.getServerPlayer(world.getServer())).filter(Optional::isPresent).map(Optional::get).map(player -> vault.getGenerator().getPiecesAt(player.blockPosition())).flatMap(Collection::stream).filter(piece -> piece instanceof VaultRoom).map(piece -> piece).forEach(room -> {
            if (((VaultRoom) room).getSandId().size() < ModConfigs.TREASURE_HUNT.sandPerRoom) {
                this.spawnSand(vault, world, ((VaultRoom) room));
            }
        });
    }

    public void spawnSand(final VaultRaid vault, final ServerWorld world, final VaultRoom room) {
        for (int i = 0; i < 200; ++i) {
            final int x = TreasureHuntObjective.rand.nextInt(room.getBoundingBox().x1 - room.getBoundingBox().x0 + 1) + room.getBoundingBox().x0;
            final int y = TreasureHuntObjective.rand.nextInt(room.getBoundingBox().y1 - room.getBoundingBox().y0 + 1) + room.getBoundingBox().y0;
            final int z = TreasureHuntObjective.rand.nextInt(room.getBoundingBox().z1 - room.getBoundingBox().z0 + 1) + room.getBoundingBox().z0;
            final BlockPos pos = new BlockPos(x, y, z);
            if (world.isAreaLoaded(pos, 1)) {
                final BlockState state = world.getBlockState(pos);
                if (state.isAir(world, pos) && state.isAir(world, pos.below()) && state.isAir(world, pos.above())) {
                    final VaultSandEntity sand = VaultSandEntity.create(world, pos);
                    final ItemStack stack = sand.getItem();
                    stack.getOrCreateTag().putUUID("vault_id", vault.getProperties().getValue(VaultRaid.IDENTIFIER));
                    sand.setItem(stack);
                    room.addSandId(sand.getUUID());
                    world.addFreshEntity(sand);
                    return;
                }
            }
        }
    }

    public void depositSand(final VaultRaid vault, final ServerPlayerEntity player, final int amount) {
        final long extraTime = ModConfigs.TREASURE_HUNT.ticksPerSand * (long) amount;
        final SandExtension extension = new SandExtension(player.getUUID(), amount, extraTime);
        vault.getPlayers().forEach(vPlayer -> vPlayer.getTimer().addTime(extension, 0));
        if (this.sandPerModifier > 0) {
            for (int i = 1; i <= amount; ++i) {
                final int count = this.addedSand + i;
                if (count % this.sandPerModifier == 0) {
                    this.addRandomModifier(vault, player.getLevel(), player);
                }
            }
        }
        this.addedSand += amount;
    }

    private void addRandomModifier(final VaultRaid vault, final ServerWorld sWorld, final ServerPlayerEntity player) {
        final int level = vault.getProperties().getValue(VaultRaid.LEVEL);
        final Set<VaultModifier> modifiers = ModConfigs.VAULT_MODIFIERS.getRandom(TreasureHuntObjective.rand, level, VaultModifiersConfig.ModifierPoolType.FINAL_WENDARR_ADDS, null);
        modifiers.removeIf(mod -> mod instanceof NoExitModifier);
        modifiers.removeIf(mod -> mod instanceof TimerModifier);
        modifiers.removeIf(mod -> mod instanceof InventoryRestoreModifier);
        if (sWorld.getRandom().nextFloat() < 0.65f) {
            modifiers.removeIf(mod -> mod instanceof ArtifactChanceModifier);
        }
        final List<VaultModifier> modifierList = new ArrayList<VaultModifier>(modifiers);
        Collections.shuffle(modifierList);
        final VaultModifier modifier = MiscUtils.getRandomEntry(modifierList, TreasureHuntObjective.rand);
        if (modifier != null) {
            final ITextComponent c0 = player.getDisplayName().copy().withStyle(TextFormatting.LIGHT_PURPLE);
            final ITextComponent c2 = new StringTextComponent(" deposited ").withStyle(TextFormatting.GRAY);
            final ITextComponent c3 = new StringTextComponent("sand").withStyle(TextFormatting.YELLOW);
            final ITextComponent c4 = new StringTextComponent(" and added ").withStyle(TextFormatting.GRAY);
            final ITextComponent c5 = modifier.getNameComponent();
            final ITextComponent c6 = new StringTextComponent(".").withStyle(TextFormatting.GRAY);
            final ITextComponent ct = new StringTextComponent("").append(c0).append(c2).append(c3).append(c4).append(c5).append(c6);
            vault.getModifiers().addPermanentModifier(modifier);
            vault.getPlayers().forEach(vPlayer -> {
                modifier.apply(vault, vPlayer, sWorld, sWorld.getRandom());
                vPlayer.runIfPresent(sWorld.getServer(), sPlayer -> sPlayer.sendMessage(ct, Util.NIL_UUID));
            });
        }
    }

    public int getAddedSand() {
        return this.addedSand;
    }

    private void updateOpenContainers(final MinecraftServer srv, final VaultRaid vault, final int slot, final ItemStack stack) {
        vault.getPlayers().forEach(vPlayer -> vPlayer.runIfPresent(srv, sPlayer -> {
            if (sPlayer.containerMenu instanceof ScavengerChestContainer) {
                sPlayer.containerMenu.setItem(slot, stack);
                sPlayer.connection.send(new SSetSlotPacket(sPlayer.containerMenu.containerId, slot, stack));
            }
        }));
    }

    @Override
    public void complete(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        super.complete(vault, player, world);
        player.sendIfPresent(world.getServer(), VaultGoalMessage.clear());
    }

    @Override
    public void complete(final VaultRaid vault, final ServerWorld world) {
        super.complete(vault, world);
        vault.getPlayers().forEach(player -> player.sendIfPresent(world.getServer(), VaultGoalMessage.clear()));
    }

    @Nullable
    private ItemSubmission getNewEntry(final VaultRaid vault) {
        final List<Item> currentItems = this.submissions.stream().map(submission -> submission.requiredItem).collect(Collectors.toList());
        final int players = vault.getPlayers().size();
        final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        float multiplier = 1.0f + (players - 1) * 0.5f;
        final ScavengerHuntConfig.ItemEntry newEntry = ModConfigs.TREASURE_HUNT.getRandomRequiredItem(currentItems::contains);
        if (newEntry == null) {
            return null;
        }
        final ScavengerHuntConfig.SourceType sourceType = ModConfigs.TREASURE_HUNT.getRequirementSource(newEntry.createItemStack());
        switch (sourceType) {
            case MOB: {
                multiplier *= 1.0f + level / 100.0f;
                break;
            }
            case CHEST: {
                multiplier *= 1.0f + level / 100.0f / 1.5f;
                break;
            }
        }
        return ItemSubmission.fromConfigEntry(newEntry, multiplier);
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        final ListNBT list = new ListNBT();
        for (final ItemSubmission submission : this.submissions) {
            list.add(submission.serialize());
        }
        tag.put("submissions", list);
        tag.putInt("requiredSubmissions", this.requiredSubmissions);
        tag.putInt("sandPerModifier", this.sandPerModifier);
        tag.putInt("addedSand", this.addedSand);
        final ListNBT inventoryList = new ListNBT();
        for (int slot = 0; slot < this.chestInventory.size(); ++slot) {
            final ItemStack stack = this.chestInventory.get(slot);
            if (!stack.isEmpty()) {
                final CompoundNBT itemTag = new CompoundNBT();
                itemTag.putInt("slot", slot);
                itemTag.put("item", stack.serializeNBT());
                inventoryList.add(itemTag);
            }
        }
        tag.put("inventory", inventoryList);
        tag.putString("roomPool", this.roomPool.toString());
        tag.putString("tunnelPool", this.tunnelPool.toString());
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.submissions.clear();
        final ListNBT list = tag.getList("submissions", 10);
        for (int index = 0; index < list.size(); ++index) {
            this.submissions.add(ItemSubmission.deserialize(list.getCompound(index)));
        }
        this.requiredSubmissions = tag.getInt("requiredSubmissions");
        this.sandPerModifier = tag.getInt("sandPerModifier");
        this.addedSand = tag.getInt("addedSand");
        this.chestInventory = NonNullList.withSize(45, ItemStack.EMPTY);
        final ListNBT inventoryList = tag.getList("inventory", 10);
        for (int i = 0; i < inventoryList.size(); ++i) {
            final CompoundNBT itemTag = inventoryList.getCompound(i);
            final int slot = itemTag.getInt("slot");
            final ItemStack stack = ItemStack.of(itemTag.getCompound("item"));
            this.chestInventory.set(slot, stack);
        }
        if (tag.contains("roomPool", 8)) {
            this.roomPool = new ResourceLocation(tag.getString("roomPool"));
        }
        if (tag.contains("tunnelPool", 8)) {
            this.tunnelPool = new ResourceLocation(tag.getString("tunnelPool"));
        }
    }

    public static class ItemSubmission {
        private final Item requiredItem;
        private final int requiredAmount;
        private int currentAmount;

        public ItemSubmission(final Item requiredItem, final int requiredAmount) {
            this.currentAmount = 0;
            this.requiredItem = requiredItem;
            this.requiredAmount = requiredAmount;
        }

        private static ItemSubmission fromConfigEntry(final ScavengerHuntConfig.ItemEntry entry, final float multiplyAmount) {
            return new ItemSubmission(entry.getItem(), MathHelper.ceil(entry.getRandomAmount() * multiplyAmount));
        }

        public boolean isFinished() {
            return this.currentAmount >= this.requiredAmount;
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
            final CompoundNBT tag = new CompoundNBT();
            tag.putString("item", this.requiredItem.getRegistryName().toString());
            tag.putInt("required", this.requiredAmount);
            tag.putInt("current", this.currentAmount);
            return tag;
        }

        public static ItemSubmission deserialize(final CompoundNBT tag) {
            final Item requiredItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("item")));
            final int requiredAmount = tag.getInt("required");
            final int currentAmount = tag.getInt("current");
            final ItemSubmission submitted = new ItemSubmission(requiredItem, requiredAmount);
            submitted.currentAmount = currentAmount;
            return submitted;
        }
    }

    public static class Config {
        private final IntSupplier requiredSubmissionsGen;
        private final Function<Predicate<Item>, ScavengerHuntConfig.ItemEntry> itemGen;
        private final Function<ItemStack, ScavengerHuntConfig.SourceType> sourceGen;

        public Config(final IntSupplier requiredSubmissionsGen, final Function<Predicate<Item>, ScavengerHuntConfig.ItemEntry> itemGen, final Function<ItemStack, ScavengerHuntConfig.SourceType> sourceGen) {
            this.requiredSubmissionsGen = requiredSubmissionsGen;
            this.itemGen = itemGen;
            this.sourceGen = sourceGen;
        }
    }

    public class ChestWatcher implements IContainerListener {
        public void refreshContainer(final Container container, final NonNullList<ItemStack> items) {
        }

        public void slotChanged(final Container container, final int slotId, final ItemStack stack) {
            if (slotId >= 0 && slotId < 45) {
                TreasureHuntObjective.this.chestInventory.set(slotId, stack);
            }
        }

        public void setContainerData(final Container containerIn, final int varToUpdate, final int newValue) {
        }
    }

    private class Inventory implements IInventory {
        public int getContainerSize() {
            return TreasureHuntObjective.this.chestInventory.size();
        }

        public boolean isEmpty() {
            return TreasureHuntObjective.this.chestInventory.isEmpty();
        }

        public ItemStack getItem(final int index) {
            return TreasureHuntObjective.this.chestInventory.get(index);
        }

        public ItemStack removeItem(final int index, final int count) {
            return ItemStackHelper.removeItem(TreasureHuntObjective.this.chestInventory, index, count);
        }

        public ItemStack removeItemNoUpdate(final int index) {
            final ItemStack existing = this.getItem(index);
            this.setItem(index, ItemStack.EMPTY);
            return existing;
        }

        public void setItem(final int index, final ItemStack stack) {
            TreasureHuntObjective.this.chestInventory.set(index, stack);
        }

        public void setChanged() {
        }

        public boolean stillValid(final PlayerEntity player) {
            return true;
        }

        public void clearContent() {
            TreasureHuntObjective.this.chestInventory.clear();
        }
    }
}
