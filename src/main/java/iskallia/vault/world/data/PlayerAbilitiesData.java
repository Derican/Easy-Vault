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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerAbilitiesData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerAbilities";
    private final Map<UUID, AbilityTree> playerMap;

    public PlayerAbilitiesData() {
        super("the_vault_PlayerAbilities");
        this.playerMap = new HashMap<UUID, AbilityTree>();
    }

    public PlayerAbilitiesData(final String name) {
        super(name);
        this.playerMap = new HashMap<UUID, AbilityTree>();
    }

    public AbilityTree getAbilities(final PlayerEntity player) {
        return this.getAbilities(player.getUUID());
    }

    public AbilityTree getAbilities(final UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, AbilityTree::new);
    }

    public PlayerAbilitiesData add(final ServerPlayerEntity player, final AbilityNode<?, ?>... nodes) {
        this.getAbilities((PlayerEntity) player).add(player.getServer(), nodes);
        this.setDirty();
        return this;
    }

    public PlayerAbilitiesData remove(final ServerPlayerEntity player, final AbilityNode<?, ?>... nodes) {
        this.getAbilities((PlayerEntity) player).remove(player.getServer(), nodes);
        this.setDirty();
        return this;
    }

    public PlayerAbilitiesData upgradeAbility(final ServerPlayerEntity player, final AbilityNode<?, ?> abilityNode) {
        final AbilityTree abilityTree = this.getAbilities((PlayerEntity) player);
        abilityTree.upgradeAbility(player.getServer(), abilityNode);
        abilityTree.sync(player.server);
        this.setDirty();
        return this;
    }

    public PlayerAbilitiesData downgradeAbility(final ServerPlayerEntity player, final AbilityNode<?, ?> abilityNode) {
        final AbilityTree abilityTree = this.getAbilities((PlayerEntity) player);
        abilityTree.downgradeAbility(player.getServer(), abilityNode);
        abilityTree.sync(player.server);
        this.setDirty();
        return this;
    }

    public PlayerAbilitiesData selectSpecialization(final ServerPlayerEntity player, final String ability, @Nullable final String specialization) {
        final AbilityTree abilityTree = this.getAbilities((PlayerEntity) player);
        abilityTree.selectSpecialization(ability, specialization);
        abilityTree.sync(player.server);
        this.setDirty();
        return this;
    }

    public PlayerAbilitiesData resetAbilityTree(final ServerPlayerEntity player) {
        final UUID uniqueID = player.getUUID();
        final AbilityTree oldAbilityTree = this.playerMap.get(uniqueID);
        if (oldAbilityTree != null) {
            for (final AbilityNode<?, ?> node : oldAbilityTree.getNodes()) {
                if (node.isLearned()) {
                    node.onRemoved((PlayerEntity) player);
                }
            }
        }
        final AbilityTree abilityTree = new AbilityTree(uniqueID);
        this.playerMap.put(uniqueID, abilityTree);
        abilityTree.sync(player.server);
        this.setDirty();
        return this;
    }

    public static void setAbilityOnCooldown(final ServerPlayerEntity player, final String abilityName) {
        final AbilityTree abilities = get(player.getLevel()).getAbilities((PlayerEntity) player);
        final AbilityNode<?, ?> abilityNode = abilities.getNodeByName(abilityName);
        abilities.putOnCooldown(player.getServer(), abilityNode, ModConfigs.ABILITIES.getCooldown(abilityNode, player));
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        if (event.side.isServer() && event.player instanceof ServerPlayerEntity) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
            get(sPlayer.getLevel()).getAbilities((PlayerEntity) sPlayer).tick(sPlayer);
        }
    }

    public void load(final CompoundNBT nbt) {
        final ListNBT playerList = nbt.getList("PlayerEntries", 8);
        final ListNBT abilitiesList = nbt.getList("AbilityEntries", 10);
        if (playerList.size() != abilitiesList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        for (int i = 0; i < playerList.size(); ++i) {
            final UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.getAbilities(playerUUID).deserializeNBT(abilitiesList.getCompound(i));
        }
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        final ListNBT playerList = new ListNBT();
        final ListNBT abilitiesList = new ListNBT();
        this.playerMap.forEach((uuid, researchTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            abilitiesList.add(researchTree.serializeNBT());
            return;
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("AbilityEntries", (INBT) abilitiesList);
        return nbt;
    }

    public static PlayerAbilitiesData get(final ServerWorld world) {
        return get(world.getServer());
    }

    public static PlayerAbilitiesData get(final MinecraftServer srv) {
        return (PlayerAbilitiesData) srv.overworld().getDataStorage().computeIfAbsent((Supplier) PlayerAbilitiesData::new, "the_vault_PlayerAbilities");
    }
}
