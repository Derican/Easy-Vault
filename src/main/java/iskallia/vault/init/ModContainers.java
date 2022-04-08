package iskallia.vault.init;

import iskallia.vault.container.*;
import iskallia.vault.container.inventory.*;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
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

    public static void register(RegistryEvent.Register<ContainerType<?>> event) {
        SKILL_TREE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            UUID uniqueID = inventory.player.getUUID();

            AbilityTree abilityTree = new AbilityTree(uniqueID);
            abilityTree.deserializeNBT(Optional.<CompoundNBT>ofNullable(buffer.readNbt()).orElse(new CompoundNBT()));
            TalentTree talentTree = new TalentTree(uniqueID);
            talentTree.deserialize(Optional.<CompoundNBT>ofNullable(buffer.readNbt()).orElse(new CompoundNBT()), false);
            ResearchTree researchTree = new ResearchTree(uniqueID);
            researchTree.deserializeNBT(Optional.<CompoundNBT>ofNullable(buffer.readNbt()).orElse(new CompoundNBT()));
            return new SkillTreeContainer(windowId, abilityTree, talentTree, researchTree);
        });
        VAULT_CRATE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            World world = inventory.player.getCommandSenderWorld();

            BlockPos pos = buffer.readBlockPos();
            return new VaultCrateContainer(windowId, world, pos, inventory, inventory.player);
        });
        VENDING_MACHINE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            World world = inventory.player.getCommandSenderWorld();

            BlockPos pos = buffer.readBlockPos();
            return new VendingMachineContainer(windowId, world, pos, inventory, inventory.player);
        });
        ADVANCED_VENDING_MACHINE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            World world = inventory.player.getCommandSenderWorld();

            BlockPos pos = buffer.readBlockPos();
            return new AdvancedVendingContainer(windowId, world, pos, inventory, inventory.player);
        });
        RENAMING_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            CompoundNBT nbt = buffer.readNbt();

            return new RenamingContainer(windowId, (nbt == null) ? new CompoundNBT() : nbt);
        });
        KEY_PRESS_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            PlayerEntity player = inventory.player;

            return new KeyPressContainer(windowId, player);
        });
        OMEGA_STATUE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            CompoundNBT nbt = buffer.readNbt();

            return new OmegaStatueContainer(windowId, (nbt == null) ? new CompoundNBT() : nbt);
        });
        TRANSMOG_TABLE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            PlayerEntity player = inventory.player;

            return new TransmogTableContainer(windowId, player);
        });
        SCAVENGER_CHEST_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            Inventory inv = new Inventory(45);

            return new ScavengerChestContainer(windowId, inventory, (IInventory) inv, (IInventory) inv);
        });
        CATALYST_DECRYPTION_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            World world = inventory.player.getCommandSenderWorld();

            BlockPos pos = buffer.readBlockPos();
            return new CatalystDecryptionContainer(windowId, world, pos, inventory);
        });
        SHARD_POUCH_CONTAINER = createContainerType((windowId, inventory, data) -> {
            int pouchSlot = data.readInt();

            return new ShardPouchContainer(windowId, inventory, pouchSlot);
        });
        SHARD_TRADE_CONTAINER = createContainerType((windowId, inventory, data) -> new ShardTradeContainer(windowId, inventory));


        CRYOCHAMBER_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            World world = inventory.player.getCommandSenderWorld();

            BlockPos pos = buffer.readBlockPos();
            return new CryochamberContainer(windowId, world, pos, inventory);
        });
        GLOBAL_DIFFICULTY_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            CompoundNBT data = buffer.readNbt();

            return new GlobalDifficultyContainer(windowId, data);
        });
        ETCHING_TRADE_CONTAINER = createContainerType((windowId, inventory, data) -> new EtchingTradeContainer(windowId, inventory, data.readInt()));


        VAULT_CHARM_CONTROLLER_CONTAINER = createContainerType((windowId, inventory, data) -> new VaultCharmControllerContainer(windowId, inventory, data.readNbt()));


        event.getRegistry().registerAll(SKILL_TREE_CONTAINER
                .setRegistryName("ability_tree"), (ContainerType) VAULT_CRATE_CONTAINER
                .setRegistryName("vault_crate"), (ContainerType) VENDING_MACHINE_CONTAINER
                .setRegistryName("vending_machine"), (ContainerType) ADVANCED_VENDING_MACHINE_CONTAINER
                .setRegistryName("advanced_vending_machine"), (ContainerType) RENAMING_CONTAINER
                .setRegistryName("renaming_container"), (ContainerType) KEY_PRESS_CONTAINER
                .setRegistryName("key_press_container"), (ContainerType) OMEGA_STATUE_CONTAINER
                .setRegistryName("omega_statue_container"), (ContainerType) TRANSMOG_TABLE_CONTAINER
                .setRegistryName("transmog_table_container"), (ContainerType) SCAVENGER_CHEST_CONTAINER
                .setRegistryName("scavenger_chest_container"), (ContainerType) CATALYST_DECRYPTION_CONTAINER
                .setRegistryName("catalyst_decryption_container"), (ContainerType) SHARD_POUCH_CONTAINER
                .setRegistryName("shard_pouch_container"), (ContainerType) SHARD_TRADE_CONTAINER
                .setRegistryName("shard_trade_container"), (ContainerType) CRYOCHAMBER_CONTAINER
                .setRegistryName("cryochamber_container"), (ContainerType) GLOBAL_DIFFICULTY_CONTAINER
                .setRegistryName("global_difficulty_container"), (ContainerType) ETCHING_TRADE_CONTAINER
                .setRegistryName("etching_trade_container"), (ContainerType) VAULT_CHARM_CONTROLLER_CONTAINER
                .setRegistryName("looter_charm_controller_container"));
    }

    public static ContainerType<ScavengerChestContainer> SCAVENGER_CHEST_CONTAINER;
    public static ContainerType<CatalystDecryptionContainer> CATALYST_DECRYPTION_CONTAINER;
    public static ContainerType<ShardPouchContainer> SHARD_POUCH_CONTAINER;
    public static ContainerType<ShardTradeContainer> SHARD_TRADE_CONTAINER;
    public static ContainerType<CryochamberContainer> CRYOCHAMBER_CONTAINER;
    public static ContainerType<GlobalDifficultyContainer> GLOBAL_DIFFICULTY_CONTAINER;
    public static ContainerType<EtchingTradeContainer> ETCHING_TRADE_CONTAINER;
    public static ContainerType<VaultCharmControllerContainer> VAULT_CHARM_CONTROLLER_CONTAINER;

    private static <T extends net.minecraft.inventory.container.Container> ContainerType<T> createContainerType(IContainerFactory<T> factory) {
        return new ContainerType((ContainerType.IFactory) factory);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModContainers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */