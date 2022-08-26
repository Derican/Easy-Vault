// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.data;

import iskallia.vault.world.vault.player.VaultPlayer;

import java.util.function.Supplier;

import net.minecraft.nbt.ListNBT;

import java.util.Map;

import net.minecraft.util.Util;

import java.util.HashMap;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.event.TickEvent;

import java.util.Collection;
import java.util.ArrayList;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.backup.BackupManager;
import net.minecraft.world.World;

import java.util.function.Consumer;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.util.NonNullList;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import net.minecraft.nbt.INBT;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.util.IItemProvider;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import iskallia.vault.world.vault.player.VaultMember;
import net.minecraft.util.RegistryKey;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;

import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.UUID;

import iskallia.vault.nbt.VMapNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.nbt.VListNBT;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.storage.WorldSavedData;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultRaidData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_VaultRaid";
    private final VListNBT<VaultRaid, CompoundNBT> vaults;
    private BlockPos.Mutable nextVaultPos;
    @Deprecated
    private final VMapNBT<UUID, VaultRaid> activeVaults;

    public VaultRaidData() {
        this("the_vault_VaultRaid");
    }

    public VaultRaidData(final String name) {
        super(name);
        this.vaults = VListNBT.of(VaultRaid::new);
        this.nextVaultPos = BlockPos.ZERO.mutable();
        this.activeVaults = VMapNBT.ofUUID(VaultRaid::new);
    }

    public VaultRaid get(final UUID vaultId) {
        if (vaultId == null) {
            return null;
        }
        return this.vaults.stream().filter(vault -> vaultId.equals(vault.getProperties().getBaseOrDefault(VaultRaid.IDENTIFIER, (UUID) null))).findFirst().orElse(null);
    }

    public VaultRaid getActiveFor(final ServerPlayerEntity player) {
        return this.getActiveFor(player.getUUID());
    }

    public VaultRaid getActiveFor(final UUID playerId) {
        return this.vaults.stream().filter(vault -> vault.getPlayer(playerId).isPresent()).findFirst().orElse(null);
    }

    public VaultRaid getAt(final ServerWorld world, final BlockPos pos) {
        return this.vaults.stream().filter(vault -> world.dimension() == vault.getProperties().getValue(VaultRaid.DIMENSION)).filter(vault -> {
            final Optional<MutableBoundingBox> box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX);
            return box.isPresent() && box.get().isInside(pos);
        }).findFirst().orElse(null);
    }

    public void remove(final UUID vaultId) {
        this.vaults.removeIf(vault -> vault.getProperties().getValue(VaultRaid.IDENTIFIER).equals(vaultId));
    }

    public void remove(final MinecraftServer server, final UUID playerId, final VaultRaid vault) {
        if (vault == null) {
            return;
        }
        final ServerWorld world = server.getLevel(vault.getProperties().getValue(VaultRaid.DIMENSION));
        vault.getPlayer(playerId).ifPresent(player -> {
            if (player.hasExited() || player instanceof VaultMember) {
                return;
            } else {
                VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.EXIT_SAFELY).execute(vault, player, world);
                return;
            }
        });
        PlayerStatsData.get(world).onVaultFinished(playerId, vault);
    }

    public static ItemStack generateRaidRewardCrate() {
        final ItemStack stack = new ItemStack(Items.RED_SHULKER_BOX);
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
        final ItemStack miner = new ItemStack(ModItems.VAULT_CRYSTAL);
        miner.getOrCreateTag().put("CrystalData", minerData.serializeNBT());
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
        final ItemStack digsite = new ItemStack(ModItems.VAULT_CRYSTAL);
        digsite.getOrCreateTag().put("CrystalData", digsiteData.serializeNBT());
        final NonNullList<ItemStack> raidContents = NonNullList.create();
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.KNOWLEDGE_STAR));
        raidContents.add(new ItemStack(ModItems.KNOWLEDGE_STAR));
        raidContents.add(new ItemStack(ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack(ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack(ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.UNIDENTIFIED_TREASURE_KEY));
        raidContents.add(new ItemStack(ModItems.LEGENDARY_TREASURE_OMEGA));
        raidContents.add(miner);
        raidContents.add(new ItemStack(ModItems.UNIDENTIFIED_ARTIFACT));
        raidContents.add(digsite);
        raidContents.add(new ItemStack(ModItems.LEGENDARY_TREASURE_OMEGA));
        raidContents.add(new ItemStack(ModItems.UNIDENTIFIED_TREASURE_KEY));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack(ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack(ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack(ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack(ModItems.SKILL_ORB));
        raidContents.add(new ItemStack(ModItems.SKILL_ORB));
        raidContents.add(new ItemStack(ModItems.PANDORAS_BOX));
        stack.getOrCreateTag().put("BlockEntityTag", new CompoundNBT());
        ItemStackHelper.saveAllItems(stack.getOrCreateTag().getCompound("BlockEntityTag"), raidContents);
        return stack;
    }

    public VaultRaid startVault(final ServerWorld world, final VaultRaid.Builder builder) {
        return this.startVault(world, builder, vault -> {
        });
    }

    public VaultRaid startVault(ServerWorld world, final VaultRaid.Builder builder, final Consumer<VaultRaid> onBuild) {
        final MinecraftServer server = world.getServer();
        final VaultRaid vault = builder.build();
        onBuild.accept(vault);
        builder.getLevelInitializer().executeForAllPlayers(vault, world);
        final Optional<RegistryKey<World>> dimension = vault.getProperties().getBase(VaultRaid.DIMENSION);
        if (dimension.isPresent()) {
            world = server.getLevel(dimension.get());
        } else {
            vault.getProperties().create(VaultRaid.DIMENSION, world.dimension());
        }
        final ServerWorld destination = dimension.isPresent() ? server.getLevel(dimension.get()) : world;
        server.submit(() -> {
            vault.getGenerator().generate(destination, vault, this.nextVaultPos);
            vault.getPlayers().forEach(player -> {
                player.runIfPresent(server, sPlayer -> {
                    BackupManager.createPlayerInventorySnapshot(sPlayer);
                    if (PlayerSet.isActive(VaultGear.Set.PHOENIX, sPlayer)) {
                        final PhoenixSetSnapshotData phoenixSetData = PhoenixSetSnapshotData.get(server);
                        if (phoenixSetData.hasSnapshot(sPlayer)) {
                            phoenixSetData.removeSnapshot(sPlayer);
                        }
                        phoenixSetData.createSnapshot(sPlayer);
                    }
                    return;
                });
                vault.getInitializer().execute(vault, player, destination);
                return;
            });
            this.vaults.add(vault);
            return;
        });
        return vault;
    }

    public void tick(final ServerWorld world) {
        new ArrayList<>(this.vaults).stream().filter(vault -> vault.getProperties().getValue(VaultRaid.DIMENSION) == world.dimension()).forEach(vault -> vault.tick(world));
        this.vaults.removeIf(vault -> {
            if (vault.isFinished()) {
                vault.getPlayers().forEach(player -> this.remove(world.getServer(), player.getPlayerId(), vault));
            }
            return vault.isFinished();
        });
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            get((ServerWorld) event.world).tick((ServerWorld) event.world);
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
            } else {
                foundVaults.put(vaultId, vault);
            }
            this.activeVaults.put(playerId, vault);
        }
        this.vaults.deserializeNBT(nbt.getList("Vaults", 10));
        this.vaults.addAll(this.activeVaults.values());
        final int[] pos = nbt.getIntArray("NextVaultPos");
        this.nextVaultPos = new BlockPos.Mutable(pos[0], pos[1], pos[2]);
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        nbt.put("Vaults", this.vaults.serializeNBT());
        nbt.putIntArray("NextVaultPos", new int[]{this.nextVaultPos.getX(), this.nextVaultPos.getY(), this.nextVaultPos.getZ()});
        return nbt;
    }

    public static VaultRaidData get(final ServerWorld world) {
        return (VaultRaidData) world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier) VaultRaidData::new, "the_vault_VaultRaid");
    }
}
