package iskallia.vault.world.data;

import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
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
public class PlayerSetsData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerSets";
    private final Map<UUID, SetTree> playerMap = new HashMap<>();

    public PlayerSetsData() {
        this("the_vault_PlayerSets");
    }

    public PlayerSetsData(String name) {
        super(name);
    }

    public SetTree getSets(PlayerEntity player) {
        return getSets(player.getUUID());
    }

    public SetTree getSets(UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, SetTree::new);
    }


    public PlayerSetsData add(ServerPlayerEntity player, SetNode<?>... nodes) {
        getSets((PlayerEntity) player).add(player.getServer(), (SetNode[]) nodes);

        setDirty();
        return this;
    }

    public PlayerSetsData remove(ServerPlayerEntity player, SetNode<?>... nodes) {
        getSets((PlayerEntity) player).remove(player.getServer(), (SetNode[]) nodes);
        setDirty();
        return this;
    }

    public PlayerSetsData resetSetTree(ServerPlayerEntity player) {
        UUID uniqueID = player.getUUID();

        SetTree oldTalentTree = this.playerMap.get(uniqueID);

        if (oldTalentTree != null) {
            for (SetNode<?> node : (Iterable<SetNode<?>>) oldTalentTree.getNodes()) {
                if (node.isActive()) {
                    node.getSet().onRemoved((PlayerEntity) player);
                }
            }
        }
        SetTree setTree = new SetTree(uniqueID);
        this.playerMap.put(uniqueID, setTree);

        setDirty();
        return this;
    }


    public PlayerSetsData tick(MinecraftServer server) {
        this.playerMap.values().forEach(setTree -> setTree.tick(server));
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
            get((ServerWorld) event.player.level).getSets(event.player);
        }
    }


    public void load(CompoundNBT nbt) {
        ListNBT playerList = nbt.getList("PlayerEntries", 8);
        ListNBT talentList = nbt.getList("SetEntries", 10);

        if (playerList.size() != talentList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }

        for (int i = 0; i < playerList.size(); i++) {
            UUID playerUUID = UUID.fromString(playerList.getString(i));
            getSets(playerUUID).deserializeNBT(talentList.getCompound(i));
        }
    }


    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT playerList = new ListNBT();
        ListNBT talentList = new ListNBT();

        this.playerMap.forEach((uuid, abilityTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));

            talentList.add(abilityTree.serializeNBT());
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("SetEntries", (INBT) talentList);

        return nbt;
    }

    public static PlayerSetsData get(ServerWorld world) {
        return (PlayerSetsData) world.getServer().overworld()
                .getDataStorage().computeIfAbsent(PlayerSetsData::new, "the_vault_PlayerSets");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerSetsData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */