package iskallia.vault.world.data;

import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
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
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTalentsData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerTalents";
    private Map<UUID, TalentTree> playerMap = new HashMap<>();

    public PlayerTalentsData() {
        this("the_vault_PlayerTalents");
    }

    public PlayerTalentsData(String name) {
        super(name);
    }

    public TalentTree getTalents(PlayerEntity player) {
        return getTalents(player.getUUID());
    }

    public TalentTree getTalents(UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, TalentTree::new);
    }


    public PlayerTalentsData add(ServerPlayerEntity player, TalentNode<?>... nodes) {
        getTalents((PlayerEntity) player).add(player.getServer(), (TalentNode[]) nodes);

        setDirty();
        return this;
    }

    public PlayerTalentsData remove(ServerPlayerEntity player, TalentNode<?>... nodes) {
        getTalents((PlayerEntity) player).remove(player.getServer(), (TalentNode[]) nodes);

        setDirty();
        return this;
    }

    public PlayerTalentsData upgradeTalent(ServerPlayerEntity player, TalentNode<?> talentNode) {
        TalentTree talentTree = getTalents((PlayerEntity) player);

        talentTree.upgradeTalent(player.getServer(), talentNode);

        talentTree.sync(player.getServer());
        setDirty();
        return this;
    }

    public PlayerTalentsData downgradeTalent(ServerPlayerEntity player, TalentNode<?> talentNode) {
        TalentTree talentTree = getTalents((PlayerEntity) player);

        talentTree.downgradeTalent(player.getServer(), talentNode);

        talentTree.sync(player.getServer());
        setDirty();
        return this;
    }

    public PlayerTalentsData resetTalentTree(ServerPlayerEntity player) {
        UUID uniqueID = player.getUUID();

        TalentTree oldTalentTree = this.playerMap.get(uniqueID);
        if (oldTalentTree != null) {
            for (TalentNode<?> node : (Iterable<TalentNode<?>>) oldTalentTree.getNodes()) {
                if (node.isLearned()) {
                    node.getTalent().onRemoved((PlayerEntity) player);
                }
            }
        }
        TalentTree talentTree = new TalentTree(uniqueID);
        this.playerMap.put(uniqueID, talentTree);

        talentTree.sync(player.getServer());
        setDirty();
        return this;
    }


    public PlayerTalentsData tick(MinecraftServer server) {
        this.playerMap.values().forEach(abilityTree -> abilityTree.tick(server));
        return this;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            get((ServerWorld) event.world).tick(((ServerWorld) event.world).getServer());
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            get((ServerWorld) event.player.level).getTalents(event.player);
        }
    }


    public void load(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", 8);
        ListNBT talentList = nbt.getList("TalentEntries", 10);

        if (playerList.size() != talentList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            getTalents(playerUUID).deserialize(talentList.getCompound(i), true);
        }
        setDirty();
    }


    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT talentList = new ListNBT();

        this.playerMap.forEach((uuid, talentTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));

            talentList.add(talentTree.serializeNBT());
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("TalentEntries", (INBT) talentList);

        return nbt;
    }

    public static PlayerTalentsData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static PlayerTalentsData get(MinecraftServer srv) {
        return (PlayerTalentsData) srv.overworld().getDataStorage().computeIfAbsent(PlayerTalentsData::new, "the_vault_PlayerTalents");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerTalentsData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */