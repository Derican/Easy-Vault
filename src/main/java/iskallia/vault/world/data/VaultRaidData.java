package iskallia.vault.world.data;

import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.nbt.VMapNBT;
import iskallia.vault.util.calc.PlayerStatisticsCollector;
import iskallia.vault.world.vault.VaultRaid;
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

import java.util.*;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultRaidData extends WorldSavedData {
    private final VMapNBT<UUID, VaultRaid> activeVaults = VMapNBT.ofUUID(VaultRaid::new);
    protected static final String DATA_NAME = "the_vault_VaultRaid";
    private BlockPos.Mutable nextVaultPos = BlockPos.ZERO.mutable();

    public VaultRaidData() {
        this("the_vault_VaultRaid");
    }

    public VaultRaidData(String name) {
        super(name);
    }

    public VaultRaid getActiveFor(ServerPlayerEntity player) {
        return getActiveFor(player.getUUID());
    }

    public VaultRaid getActiveFor(UUID playerId) {
        return (VaultRaid) this.activeVaults.get(playerId);
    }

    public VaultRaid getAt(ServerWorld world, BlockPos pos) {
        synchronized (this.activeVaults) {
            return this.activeVaults.values().stream()
                    .filter(vault -> (world.dimension() == vault.getProperties().getValue(VaultRaid.DIMENSION)))
                    .filter(vault -> {
                        Optional<MutableBoundingBox> box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX);
                        return (box.isPresent() && ((MutableBoundingBox) box.get()).isInside((Vector3i) pos));

                    }).findFirst()
                    .orElse(null);
        }
    }

    public void remove(MinecraftServer server, UUID playerId) {
        VaultRaid vault;
        synchronized (this.activeVaults) {
            vault = (VaultRaid) this.activeVaults.remove(playerId);
            if (vault == null) {
                return;
            }
        }

        ServerWorld world = server.getLevel((RegistryKey) vault.getProperties().getValue(VaultRaid.DIMENSION));

        vault.getPlayer(playerId).ifPresent(player -> {
            if (player.hasExited()) {
                return;
            }


            VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.GRANT_EXP_COMPLETE).then(VaultRaid.EXIT_SAFELY).execute(vault, player, world);
        });

        PlayerStatsData.get(server).onVaultFinished(playerId, vault);
        if (!PlayerStatsData.get(server).get(playerId).hasFinishedRaidReward()) {
            int raids = PlayerStatisticsCollector.getFinishedRaids(server, playerId);
            if (raids >= 5 && raids >= PlayerVaultStatsData.get(server).getVaultStats(playerId).getVaultLevel()) {
                ScheduledItemDropData.get(server).addDrop(playerId, generateRaidRewardCrate());
                PlayerStatsData.get(server).setRaidRewardReceived(playerId);
            }
        }
    }

    public static ItemStack generateRaidRewardCrate() {
        ItemStack stack = new ItemStack((IItemProvider) Items.RED_SHULKER_BOX);

        CrystalData minerData = new CrystalData();
        minerData.setModifiable(false);
        minerData.setCanTriggerInfluences(false);
        minerData.setPreventsRandomModifiers(true);
        minerData.setSelectedObjective(((ArchitectObjective) VaultRaid.ARCHITECT_EVENT.get()).getId());
        minerData.setTargetObjectiveCount(20);
        minerData.addModifier("Copious");
        minerData.addModifier("Rich");
        minerData.addModifier("Plentiful");
        minerData.addModifier("Endless");
        ItemStack miner = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        miner.getOrCreateTag().put("CrystalData", (INBT) minerData.serializeNBT());

        CrystalData digsiteData = new CrystalData();
        digsiteData.setModifiable(false);
        digsiteData.setCanTriggerInfluences(false);
        digsiteData.setPreventsRandomModifiers(true);
        digsiteData.setSelectedObjective(((ScavengerHuntObjective) VaultRaid.SCAVENGER_HUNT.get()).getId());
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
        ItemStack digsite = new ItemStack((IItemProvider) ModItems.VAULT_CRYSTAL);
        digsite.getOrCreateTag().put("CrystalData", (INBT) digsiteData.serializeNBT());

        NonNullList<ItemStack> raidContents = NonNullList.create();

        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider) ModItems.KNOWLEDGE_STAR));
        raidContents.add(new ItemStack((IItemProvider) ModItems.KNOWLEDGE_STAR));
        raidContents.add(new ItemStack((IItemProvider) ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider) ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider) ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));

        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider) ModItems.UNIDENTIFIED_TREASURE_KEY));
        raidContents.add(new ItemStack((IItemProvider) ModItems.LEGENDARY_TREASURE_OMEGA));
        raidContents.add(miner);
        raidContents.add(new ItemStack((IItemProvider) ModItems.UNIDENTIFIED_ARTIFACT));
        raidContents.add(digsite);
        raidContents.add(new ItemStack((IItemProvider) ModItems.LEGENDARY_TREASURE_OMEGA));
        raidContents.add(new ItemStack((IItemProvider) ModItems.UNIDENTIFIED_TREASURE_KEY));
        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));

        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));
        raidContents.add(new ItemStack((IItemProvider) ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider) ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider) ModItems.VAULT_PLATINUM));
        raidContents.add(new ItemStack((IItemProvider) ModItems.SKILL_ORB));
        raidContents.add(new ItemStack((IItemProvider) ModItems.SKILL_ORB));
        raidContents.add(new ItemStack((IItemProvider) ModItems.PANDORAS_BOX));

        stack.getOrCreateTag().put("BlockEntityTag", (INBT) new CompoundNBT());
        ItemStackHelper.saveAllItems(stack.getOrCreateTag().getCompound("BlockEntityTag"), raidContents);
        return stack;
    }

    public VaultRaid startVault(ServerWorld world, VaultRaid.Builder builder) {
        MinecraftServer server = world.getServer();
        VaultRaid vault = builder.build();
        builder.getLevelInitializer().executeForAllPlayers(vault, world);

        Optional<RegistryKey<World>> dimension = vault.getProperties().getBase(VaultRaid.DIMENSION);

        if (dimension.isPresent()) {
            world = server.getLevel(dimension.get());
        } else {
            vault.getProperties().create(VaultRaid.DIMENSION, world.dimension());
        }

        ServerWorld destination = dimension.isPresent() ? server.getLevel(dimension.get()) : world;

        server.submit(() -> {
            vault.getGenerator().generate(destination, vault, this.nextVaultPos);


            vault.getPlayers().forEach(());
        });


        return vault;
    }

    public void tick(ServerWorld world) {
        Set<VaultRaid> vaults;
        synchronized (this.activeVaults) {
            vaults = new HashSet<>(this.activeVaults.values());
        }

        vaults.stream().filter(vault -> (vault.getProperties().getValue(VaultRaid.DIMENSION) == world.dimension()))

                .forEach(vault -> vault.tick(world));

        Set<UUID> completed = new HashSet<>();

        vaults.forEach(vault -> {
            if (vault.isFinished()) {
                vault.getPlayers().forEach(());
            }
        });

        completed.forEach(uuid -> remove(world.getServer(), uuid));
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            get((ServerWorld) event.world).tick((ServerWorld) event.world);
        }
    }


    public boolean isDirty() {
        return true;
    }


    public void load(CompoundNBT nbt) {
        Map<UUID, VaultRaid> foundVaults = new HashMap<>();
        ListNBT vaults = nbt.getList("ActiveVaults", 10);
        for (int i = 0; i < vaults.size(); i++) {
            CompoundNBT tag = vaults.getCompound(i);

            UUID playerId = UUID.fromString(tag.getString("Key"));

            VaultRaid vault = new VaultRaid();
            vault.deserializeNBT(tag.getCompound("Value"));
            UUID vaultId = (UUID) vault.getProperties().getBaseOrDefault(VaultRaid.IDENTIFIER, Util.NIL_UUID);
            if (foundVaults.containsKey(vaultId)) {
                vault = foundVaults.get(vaultId);
            } else {
                foundVaults.put(vaultId, vault);
            }
            this.activeVaults.put(playerId, vault);
        }

        int[] pos = nbt.getIntArray("NextVaultPos");
        this.nextVaultPos = new BlockPos.Mutable(pos[0], pos[1], pos[2]);
    }


    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("ActiveVaults", (INBT) this.activeVaults.serializeNBT());
        nbt.putIntArray("NextVaultPos", new int[]{this.nextVaultPos.getX(), this.nextVaultPos.getY(), this.nextVaultPos.getZ()});
        return nbt;
    }

    public static VaultRaidData get(ServerWorld world) {
        return (VaultRaidData) world.getServer().overworld().getDataStorage().computeIfAbsent(VaultRaidData::new, "the_vault_VaultRaid");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\VaultRaidData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */