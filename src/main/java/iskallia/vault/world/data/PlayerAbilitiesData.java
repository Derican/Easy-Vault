package iskallia.vault.world.data;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerAbilitiesData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerAbilities";
    private final Map<UUID, AbilityTree> playerMap = new HashMap<>();

    public PlayerAbilitiesData() {
        super("the_vault_PlayerAbilities");
    }

    public PlayerAbilitiesData(String name) {
        super(name);
    }

    public AbilityTree getAbilities(PlayerEntity player) {
        return getAbilities(player.getUUID());
    }

    public AbilityTree getAbilities(UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, AbilityTree::new);
    }


    public PlayerAbilitiesData add(ServerPlayerEntity player, AbilityNode<?, ?>... nodes) {
        getAbilities((PlayerEntity) player).add(player.getServer(), (AbilityNode[]) nodes);

        setDirty();
        return this;
    }

    public PlayerAbilitiesData remove(ServerPlayerEntity player, AbilityNode<?, ?>... nodes) {
        getAbilities((PlayerEntity) player).remove(player.getServer(), (AbilityNode[]) nodes);

        setDirty();
        return this;
    }

    public PlayerAbilitiesData upgradeAbility(ServerPlayerEntity player, AbilityNode<?, ?> abilityNode) {
        AbilityTree abilityTree = getAbilities((PlayerEntity) player);

        abilityTree.upgradeAbility(player.getServer(), abilityNode);

        abilityTree.sync(player.server);
        setDirty();
        return this;
    }

    public PlayerAbilitiesData downgradeAbility(ServerPlayerEntity player, AbilityNode<?, ?> abilityNode) {
        AbilityTree abilityTree = getAbilities((PlayerEntity) player);

        abilityTree.downgradeAbility(player.getServer(), abilityNode);

        abilityTree.sync(player.server);
        setDirty();
        return this;
    }

    public PlayerAbilitiesData selectSpecialization(ServerPlayerEntity player, String ability, @Nullable String specialization) {
        AbilityTree abilityTree = getAbilities((PlayerEntity) player);

        abilityTree.selectSpecialization(ability, specialization);

        abilityTree.sync(player.server);
        setDirty();
        return this;
    }

    public PlayerAbilitiesData resetAbilityTree(ServerPlayerEntity player) {
        UUID uniqueID = player.getUUID();

        AbilityTree oldAbilityTree = this.playerMap.get(uniqueID);
        if (oldAbilityTree != null) {
            for (AbilityNode<?, ?> node : (Iterable<AbilityNode<?, ?>>) oldAbilityTree.getNodes()) {
                if (node.isLearned()) {
                    node.onRemoved((PlayerEntity) player);
                }
            }
        }

        AbilityTree abilityTree = new AbilityTree(uniqueID);
        this.playerMap.put(uniqueID, abilityTree);

        abilityTree.sync(player.server);
        setDirty();
        return this;
    }

    public static void setAbilityOnCooldown(ServerPlayerEntity player, String abilityName) {
        AbilityTree abilities = get(player.getLevel()).getAbilities((PlayerEntity) player);
        AbilityNode<?, ?> abilityNode = abilities.getNodeByName(abilityName);

        abilities.putOnCooldown(player.getServer(), abilityNode, ModConfigs.ABILITIES.getCooldown(abilityNode, player));
    }


    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (event.side.isServer() && event.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;

            get(sPlayer.getLevel()).getAbilities((PlayerEntity) sPlayer).tick(sPlayer);
        }
    }


    public void load(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", 8);
        ListNBT abilitiesList = nbt.getList("AbilityEntries", 10);

        if (playerList.size() != abilitiesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            getAbilities(playerUUID).deserializeNBT(abilitiesList.getCompound(i));
        }
    }


    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT abilitiesList = new ListNBT();

        this.playerMap.forEach((uuid, researchTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));

            abilitiesList.add(researchTree.serializeNBT());
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("AbilityEntries", (INBT) abilitiesList);

        return nbt;
    }

    public static PlayerAbilitiesData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static PlayerAbilitiesData get(MinecraftServer srv) {
        return (PlayerAbilitiesData) srv.overworld()
                .getDataStorage().computeIfAbsent(PlayerAbilitiesData::new, "the_vault_PlayerAbilities");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerAbilitiesData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */