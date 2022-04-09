// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.data;

import iskallia.vault.backup.BackupManager;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.nbt.VMapNBT;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.util.calc.PlayerStatisticsCollector;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultRaidData extends WorldSavedData
{
    protected static final String DATA_NAME = "the_vault_VaultRaid";
    private final VMapNBT<UUID, VaultRaid> activeVaults;
    private BlockPos.Mutable nextVaultPos;
    
    public VaultRaidData() {
        this("the_vault_VaultRaid");
    }
    
    public VaultRaidData(final String name) {
        super(name);
        this.activeVaults = VMapNBT.ofUUID(VaultRaid::new);
        this.nextVaultPos = BlockPos.ZERO.mutable();
    }
    
    public VaultRaid getActiveFor(final ServerPlayerEntity player) {
        return this.getActiveFor(player.getUUID());
    }
    
    public VaultRaid getActiveFor(final UUID playerId) {
        return this.activeVaults.get(playerId);
    }
    
    public VaultRaid getAt(final ServerWorld world, final BlockPos pos) {
        synchronized (this.activeVaults) {
            return this.activeVaults.values().stream().filter(vault -> world.dimension() == vault.getProperties().getValue(VaultRaid.DIMENSION)).filter(vault -> {
                final Optional<MutableBoundingBox> box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX);
                return box.isPresent() && box.get().isInside((Vector3i)pos);
            }).findFirst().orElse(null);
        }
    }
    
    public void remove(final MinecraftServer server, final UUID playerId) {
        final VaultRaid vault;
        synchronized (this.activeVaults) {
            vault = this.activeVaults.remove(playerId);
            if (vault == null) {
                return;
            }
        }
        final ServerWorld world = server.getLevel((RegistryKey)vault.getProperties().getValue(VaultRaid.DIMENSION));
        vault.getPlayer(playerId).ifPresent(player -> {
            if (player.hasExited()) {
                return;
            }
            else {
                VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.GRANT_EXP_COMPLETE).then(VaultRaid.EXIT_SAFELY).execute(vault, player, world);
                return;
            }
        });
        PlayerStatsData.get(server).onVaultFinished(playerId, vault);
        if (!PlayerStatsData.get(server).get(playerId).hasFinishedRaidReward()) {
            final int raids = PlayerStatisticsCollector.getFinishedRaids(server, playerId);
            if (raids >= 5 && raids >= PlayerVaultStatsData.get(server).getVaultStats(playerId).getVaultLevel()) {
                ScheduledItemDropData.get(server).addDrop(playerId, generateRaidRewardCrate());
                PlayerStatsData.get(server).setRaidRewardReceived(playerId);
            }
        }
    }
    
    public static ItemStack generateRaidRewardCrate() {
        final ItemStack stack = new ItemStack((IItemProvider)Items.RED_SHULKER_BOX);
        final CrystalData minerData = new CrystalData();
        minerData.setModifiable(false);
        minerData.setCanTriggerInfluences(false);
        minerData.setPreventsRandomModifiers(true);
        minerData.setSelectedObjective(VaultRaid.ARCHITECT_EVENT.get().getId());
        minerData.setTargetObjectiveCount(20);
        minerData.addModifier("Copious");
        minerData.addModifier("Rich");
        minerData.addModifier("Plentiful");
        minerData.addModifier("Endless");
        final ItemStack miner = new ItemStack((IItemProvider)ModItems.VAULT_CRYSTAL);
        miner.getOrCreateTag().put("CrystalData", (INBT)minerData.serializeNBT());
        final CrystalData digsiteData = new CrystalData();
        digsiteData.setModifiable(false);
        digsiteData.setCanTriggerInfluences(false);
        digsiteData.setPreventsRandomModifiers(true);
        digsiteData.setSelectedObjective(VaultRaid.SCAVENGER_HUNT.get().getId());
        digsiteData.setTargetObjectiveCount(6);
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addGuaranteedRoom("digsite");
        digsiteData.addModifier("Super Lucky");
        digsiteData.addModifier("Super Lucky");
        digsiteData.addModifier("Locked");
        final ItemStack digsite = new ItemStack((IItemProvider)ModItems.VAULT_CRYSTAL);
        digsite.getOrCreateTag().put("CrystalData", (INBT)digsiteData.serializeNBT());
        final NonNullList<ItemStack> raidContents = NonNullList.create();
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.KNOWLEDGE_STAR));
        raidContents.add(new ItemStack((IItemProvider)ModItems.KNOWLEDGE_STAR));
        raidContents.add(new ItemStack((IItemProvider)ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider)ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider)ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.UNIDENTIFIED_TREASURE_KEY));
        raidContents.add(new ItemStack((IItemProvider)ModItems.LEGENDARY_TREASURE_OMEGA));
        raidContents.add(miner);
        raidContents.add(new ItemStack((IItemProvider)ModItems.UNIDENTIFIED_ARTIFACT));
        raidContents.add(digsite);
        raidContents.add(new ItemStack((IItemProvider)ModItems.LEGENDARY_TREASURE_OMEGA));
        raidContents.add(new ItemStack((IItemProvider)ModItems.UNIDENTIFIED_TREASURE_KEY));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider)ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider)ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider)ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider)ModItems.SKILL_ORB));
        raidContents.add(new ItemStack((IItemProvider)ModItems.SKILL_ORB));
        raidContents.add(new ItemStack((IItemProvider)ModItems.PANDORAS_BOX));
        stack.getOrCreateTag().put("BlockEntityTag", (INBT)new CompoundNBT());
        ItemStackHelper.saveAllItems(stack.getOrCreateTag().getCompound("BlockEntityTag"), (NonNullList)raidContents);
        return stack;
    }
    
    public VaultRaid startVault(ServerWorld world, final VaultRaid.Builder builder) {
        final MinecraftServer server = world.getServer();
        final VaultRaid vault = builder.build();
        builder.getLevelInitializer().executeForAllPlayers(vault, world);
        final Optional<RegistryKey<World>> dimension = vault.getProperties().getBase(VaultRaid.DIMENSION);
        if (dimension.isPresent()) {
            world = server.getLevel((RegistryKey)dimension.get());
        }
        else {
            vault.getProperties().create(VaultRaid.DIMENSION, (RegistryKey<World>)world.dimension());
        }
        final ServerWorld destination = dimension.isPresent() ? server.getLevel((RegistryKey)dimension.get()) : world;
        server.submit(() -> {
            vault.getGenerator().generate(destination, vault, this.nextVaultPos);
            vault.getPlayers().forEach(player -> {
                player.runIfPresent(server, sPlayer -> {
                    BackupManager.createPlayerInventorySnapshot(sPlayer);
                    if (PlayerSet.isActive(VaultGear.Set.PHOENIX, (LivingEntity)sPlayer)) {
                        final PhoenixSetSnapshotData phoenixSetData = PhoenixSetSnapshotData.get(server);
                        if (phoenixSetData.hasSnapshot((PlayerEntity)sPlayer)) {
                            phoenixSetData.removeSnapshot((PlayerEntity)sPlayer);
                        }
                        phoenixSetData.createSnapshot((PlayerEntity)sPlayer);
                    }
                    return;
                });
                this.remove(server, player.getPlayerId());
                synchronized (this.activeVaults) {
                    this.activeVaults.put(player.getPlayerId(), vault);
                }
                vault.getInitializer().execute(vault, player, destination);
            });
            return;
        });
        return vault;
    }
    
    public void tick(final ServerWorld world) {
        final Set<VaultRaid> vaults;
        synchronized (this.activeVaults) {
            vaults = new HashSet<VaultRaid>(this.activeVaults.values());
        }
        vaults.stream().filter(vault -> vault.getProperties().getValue(VaultRaid.DIMENSION) == world.dimension()).forEach(vault -> vault.tick(world));
        final Set<UUID> completed = new HashSet<UUID>();
        vaults.forEach(vault -> {
            if (vault.isFinished()) {
                vault.getPlayers().forEach(player -> completed.add(player.getPlayerId()));
            }
            return;
        });
        completed.forEach(uuid -> this.remove(world.getServer(), uuid));
    }
    
    @SubscribeEvent
    public static void onTick(final TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            get((ServerWorld)event.world).tick((ServerWorld)event.world);
        }
    }
    
    public boolean isDirty() {
        return true;
    }
    
    public void load(final CompoundNBT nbt) {
        final Map<UUID, VaultRaid> foundVaults = new HashMap<UUID, VaultRaid>();
        final ListNBT vaults = nbt.getList("ActiveVaults", 10);
        for (int i = 0; i < vaults.size(); ++i) {
            final CompoundNBT tag = vaults.getCompound(i);
            final UUID playerId = UUID.fromString(tag.getString("Key"));
            VaultRaid vault = new VaultRaid();
            vault.deserializeNBT(tag.getCompound("Value"));
            final UUID vaultId = vault.getProperties().getBaseOrDefault(VaultRaid.IDENTIFIER, Util.NIL_UUID);
            if (foundVaults.containsKey(vaultId)) {
                vault = foundVaults.get(vaultId);
            }
            else {
                foundVaults.put(vaultId, vault);
            }
            this.activeVaults.put(playerId, vault);
        }
        final int[] pos = nbt.getIntArray("NextVaultPos");
        this.nextVaultPos = new BlockPos.Mutable(pos[0], pos[1], pos[2]);
    }
    
    public CompoundNBT save(final CompoundNBT nbt) {
        nbt.put("ActiveVaults", (INBT)this.activeVaults.serializeNBT());
        nbt.putIntArray("NextVaultPos", new int[] { this.nextVaultPos.getX(), this.nextVaultPos.getY(), this.nextVaultPos.getZ() });
        return nbt;
    }
    
    public static VaultRaidData get(final ServerWorld world) {
        return (VaultRaidData)world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier)VaultRaidData::new, "the_vault_VaultRaid");
    }
}
