package iskallia.vault.init;

import iskallia.vault.container.*;
import iskallia.vault.container.inventory.*;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.network.IContainerFactory;

import java.util.Optional;
import java.util.UUID;

public class ModContainers {
    public static ContainerType<SkillTreeContainer> SKILL_TREE_CONTAINER;
    public static ContainerType<VaultCrateContainer> VAULT_CRATE_CONTAINER;
    public static ContainerType<VendingMachineContainer> VENDING_MACHINE_CONTAINER;
    public static ContainerType<AdvancedVendingContainer> ADVANCED_VENDING_MACHINE_CONTAINER;
    public static ContainerType<RenamingContainer> RENAMING_CONTAINER;
    public static ContainerType<KeyPressContainer> KEY_PRESS_CONTAINER;
    public static ContainerType<OmegaStatueContainer> OMEGA_STATUE_CONTAINER;
    public static ContainerType<TransmogTableContainer> TRANSMOG_TABLE_CONTAINER;
    public static ContainerType<ScavengerChestContainer> SCAVENGER_CHEST_CONTAINER;
    public static ContainerType<CatalystDecryptionContainer> CATALYST_DECRYPTION_CONTAINER;
    public static ContainerType<ShardPouchContainer> SHARD_POUCH_CONTAINER;
    public static ContainerType<ShardTradeContainer> SHARD_TRADE_CONTAINER;
    public static ContainerType<CryochamberContainer> CRYOCHAMBER_CONTAINER;
    public static ContainerType<GlobalDifficultyContainer> GLOBAL_DIFFICULTY_CONTAINER;
    public static ContainerType<EtchingTradeContainer> ETCHING_TRADE_CONTAINER;
    public static ContainerType<VaultCharmControllerContainer> VAULT_CHARM_CONTROLLER_CONTAINER;

    public static void register(final RegistryEvent.Register<ContainerType<?>> event) {
        ModContainers.SKILL_TREE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final UUID uniqueID = inventory.player.getUUID();
            final AbilityTree abilityTree = new AbilityTree(uniqueID);
            abilityTree.deserializeNBT(Optional.ofNullable(buffer.readNbt()).orElse(new CompoundNBT()));
            final TalentTree talentTree = new TalentTree(uniqueID);
            talentTree.deserialize(Optional.ofNullable(buffer.readNbt()).orElse(new CompoundNBT()), false);
            return new SkillTreeContainer(windowId, abilityTree, talentTree);
        });
        ModContainers.VAULT_CRATE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final World world = inventory.player.getCommandSenderWorld();
            final BlockPos pos = buffer.readBlockPos();
            return new VaultCrateContainer(windowId, world, pos, inventory, inventory.player);
        });
        ModContainers.VENDING_MACHINE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final World world = inventory.player.getCommandSenderWorld();
            final BlockPos pos = buffer.readBlockPos();
            return new VendingMachineContainer(windowId, world, pos, inventory, inventory.player);
        });
        ModContainers.ADVANCED_VENDING_MACHINE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final World world = inventory.player.getCommandSenderWorld();
            final BlockPos pos = buffer.readBlockPos();
            return new AdvancedVendingContainer(windowId, world, pos, inventory, inventory.player);
        });
        ModContainers.RENAMING_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final CompoundNBT nbt = buffer.readNbt();
            return new RenamingContainer(windowId, (nbt == null) ? new CompoundNBT() : nbt);
        });
        ModContainers.KEY_PRESS_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final PlayerEntity player = inventory.player;
            return new KeyPressContainer(windowId, player);
        });
        ModContainers.OMEGA_STATUE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final CompoundNBT nbt = buffer.readNbt();
            return new OmegaStatueContainer(windowId, (nbt == null) ? new CompoundNBT() : nbt);
        });
        ModContainers.TRANSMOG_TABLE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final PlayerEntity player = inventory.player;
            return new TransmogTableContainer(windowId, player);
        });
        ModContainers.SCAVENGER_CHEST_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final Inventory inv = new Inventory(45);
            return new ScavengerChestContainer(windowId, inventory, inv, inv);
        });
        ModContainers.CATALYST_DECRYPTION_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final World world = inventory.player.getCommandSenderWorld();
            final BlockPos pos = buffer.readBlockPos();
            return new CatalystDecryptionContainer(windowId, world, pos, inventory);
        });
        ModContainers.SHARD_POUCH_CONTAINER = createContainerType((windowId, inventory, data) -> {
            final int pouchSlot = data.readInt();
            return new ShardPouchContainer(windowId, inventory, pouchSlot);
        });
        ModContainers.SHARD_TRADE_CONTAINER = createContainerType((windowId, inventory, data) -> new ShardTradeContainer(windowId, inventory));
        ModContainers.CRYOCHAMBER_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final World world = inventory.player.getCommandSenderWorld();
            final BlockPos pos = buffer.readBlockPos();
            return new CryochamberContainer(windowId, world, pos, inventory);
        });
        ModContainers.GLOBAL_DIFFICULTY_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            final CompoundNBT data = buffer.readNbt();
            return new GlobalDifficultyContainer(windowId, data);
        });
        ModContainers.ETCHING_TRADE_CONTAINER = createContainerType((windowId, inventory, data) -> new EtchingTradeContainer(windowId, inventory, data.readInt()));
        ModContainers.VAULT_CHARM_CONTROLLER_CONTAINER = createContainerType((windowId, inventory, data) -> new VaultCharmControllerContainer(windowId, inventory, data.readNbt()));
        event.getRegistry().registerAll(ModContainers.SKILL_TREE_CONTAINER.setRegistryName("ability_tree"), ModContainers.VAULT_CRATE_CONTAINER.setRegistryName("vault_crate"), ModContainers.VENDING_MACHINE_CONTAINER.setRegistryName("vending_machine"), ModContainers.ADVANCED_VENDING_MACHINE_CONTAINER.setRegistryName("advanced_vending_machine"), ModContainers.RENAMING_CONTAINER.setRegistryName("renaming_container"), ModContainers.KEY_PRESS_CONTAINER.setRegistryName("key_press_container"), ModContainers.OMEGA_STATUE_CONTAINER.setRegistryName("omega_statue_container"), ModContainers.TRANSMOG_TABLE_CONTAINER.setRegistryName("transmog_table_container"), ModContainers.SCAVENGER_CHEST_CONTAINER.setRegistryName("scavenger_chest_container"), ModContainers.CATALYST_DECRYPTION_CONTAINER.setRegistryName("catalyst_decryption_container"), ModContainers.SHARD_POUCH_CONTAINER.setRegistryName("shard_pouch_container"), ModContainers.SHARD_TRADE_CONTAINER.setRegistryName("shard_trade_container"), ModContainers.CRYOCHAMBER_CONTAINER.setRegistryName("cryochamber_container"), ModContainers.GLOBAL_DIFFICULTY_CONTAINER.setRegistryName("global_difficulty_container"), ModContainers.ETCHING_TRADE_CONTAINER.setRegistryName("etching_trade_container"), ModContainers.VAULT_CHARM_CONTROLLER_CONTAINER.setRegistryName("looter_charm_controller_container"));
    }

    private static <T extends Container> ContainerType<T> createContainerType(final IContainerFactory<T> factory) {
        return (ContainerType<T>) new ContainerType(factory);
    }
}
