package iskallia.vault.world.data;

import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.PlayerTalent;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTalentsData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerTalents";
    private Map<UUID, TalentTree> playerMap;

    public PlayerTalentsData() {
        this("the_vault_PlayerTalents");
    }

    public PlayerTalentsData(final String name) {
        super(name);
        this.playerMap = new HashMap<UUID, TalentTree>();
    }

    public TalentTree getTalents(final PlayerEntity player) {
        return this.getTalents(player.getUUID());
    }

    public TalentTree getTalents(final UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, TalentTree::new);
    }

    public PlayerTalentsData add(final ServerPlayerEntity player, final TalentNode<?>... nodes) {
        this.getTalents((PlayerEntity) player).add(player.getServer(), nodes);
        this.setDirty();
        return this;
    }

    public PlayerTalentsData remove(final ServerPlayerEntity player, final TalentNode<?>... nodes) {
        this.getTalents((PlayerEntity) player).remove(player.getServer(), nodes);
        this.setDirty();
        return this;
    }

    public PlayerTalentsData upgradeTalent(final ServerPlayerEntity player, final TalentNode<?> talentNode) {
        final TalentTree talentTree = this.getTalents((PlayerEntity) player);
        talentTree.upgradeTalent(player.getServer(), talentNode);
        talentTree.sync(player.getServer());
        this.setDirty();
        return this;
    }

    public PlayerTalentsData downgradeTalent(final ServerPlayerEntity player, final TalentNode<?> talentNode) {
        final TalentTree talentTree = this.getTalents((PlayerEntity) player);
        talentTree.downgradeTalent(player.getServer(), talentNode);
        talentTree.sync(player.getServer());
        this.setDirty();
        return this;
    }

    public PlayerTalentsData resetTalentTree(final ServerPlayerEntity player) {
        final UUID uniqueID = player.getUUID();
        final TalentTree oldTalentTree = this.playerMap.get(uniqueID);
        if (oldTalentTree != null) {
            for (final TalentNode<?> node : oldTalentTree.getNodes()) {
                if (node.isLearned()) {
                    ((PlayerTalent) node.getTalent()).onRemoved((PlayerEntity) player);
                }
            }
        }
        final TalentTree talentTree = new TalentTree(uniqueID);
        this.playerMap.put(uniqueID, talentTree);
        talentTree.sync(player.getServer());
        this.setDirty();
        return this;
    }

    public PlayerTalentsData tick(final MinecraftServer server) {
        this.playerMap.values().forEach(abilityTree -> abilityTree.tick(server));
        return this;
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            get((ServerWorld) event.world).tick(((ServerWorld) event.world).getServer());
        }
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            get((ServerWorld) event.player.level).getTalents(event.player);
        }
    }

    public void load(final CompoundNBT nbt) {
        final ListNBT playerList = nbt.getList("PlayerEntries", 8);
        final ListNBT talentList = nbt.getList("TalentEntries", 10);
        if (playerList.size() != talentList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        for (int i = 0; i < playerList.size(); ++i) {
            final UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.getTalents(playerUUID).deserialize(talentList.getCompound(i), true);
        }
        this.setDirty();
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        final ListNBT playerList = new ListNBT();
        final ListNBT talentList = new ListNBT();
        this.playerMap.forEach((uuid, talentTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            talentList.add(talentTree.serializeNBT());
            return;
        });
        nbt.put("PlayerEntries", (INBT) playerList);
        nbt.put("TalentEntries", (INBT) talentList);
        return nbt;
    }

    public static PlayerTalentsData get(final ServerWorld world) {
        return get(world.getServer());
    }

    public static PlayerTalentsData get(final MinecraftServer srv) {
        return (PlayerTalentsData) srv.overworld().getDataStorage().computeIfAbsent((Supplier) PlayerTalentsData::new, "the_vault_PlayerTalents");
    }
}
