// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.data;

import iskallia.vault.skill.set.PlayerSet;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSetsData extends WorldSavedData
{
    protected static final String DATA_NAME = "the_vault_PlayerSets";
    private final Map<UUID, SetTree> playerMap;
    
    public PlayerSetsData() {
        this("the_vault_PlayerSets");
    }
    
    public PlayerSetsData(final String name) {
        super(name);
        this.playerMap = new HashMap<UUID, SetTree>();
    }
    
    public SetTree getSets(final PlayerEntity player) {
        return this.getSets(player.getUUID());
    }
    
    public SetTree getSets(final UUID uuid) {
        return this.playerMap.computeIfAbsent(uuid, SetTree::new);
    }
    
    public PlayerSetsData add(final ServerPlayerEntity player, final SetNode<?>... nodes) {
        this.getSets((PlayerEntity)player).add(player.getServer(), nodes);
        this.setDirty();
        return this;
    }
    
    public PlayerSetsData remove(final ServerPlayerEntity player, final SetNode<?>... nodes) {
        this.getSets((PlayerEntity)player).remove(player.getServer(), nodes);
        this.setDirty();
        return this;
    }
    
    public PlayerSetsData resetSetTree(final ServerPlayerEntity player) {
        final UUID uniqueID = player.getUUID();
        final SetTree oldTalentTree = this.playerMap.get(uniqueID);
        if (oldTalentTree != null) {
            for (final SetNode<?> node : oldTalentTree.getNodes()) {
                if (node.isActive()) {
                    ((PlayerSet)node.getSet()).onRemoved((PlayerEntity)player);
                }
            }
        }
        final SetTree setTree = new SetTree(uniqueID);
        this.playerMap.put(uniqueID, setTree);
        this.setDirty();
        return this;
    }
    
    public PlayerSetsData tick(final MinecraftServer server) {
        this.playerMap.values().forEach(setTree -> setTree.tick(server));
        return this;
    }
    
    @SubscribeEvent
    public static void onTick(final TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            get((ServerWorld)event.world).tick(((ServerWorld)event.world).getServer());
        }
    }
    
    @SubscribeEvent
    public static void onTick(final TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            get((ServerWorld)event.player.level).getSets(event.player);
        }
    }
    
    public void load(final CompoundNBT nbt) {
        final ListNBT playerList = nbt.getList("PlayerEntries", 8);
        final ListNBT talentList = nbt.getList("SetEntries", 10);
        if (playerList.size() != talentList.size()) {
            throw new IllegalStateException("Map doesn't have the same amount of keys as values");
        }
        for (int i = 0; i < playerList.size(); ++i) {
            final UUID playerUUID = UUID.fromString(playerList.getString(i));
            this.getSets(playerUUID).deserializeNBT(talentList.getCompound(i));
        }
    }
    
    public CompoundNBT save(final CompoundNBT nbt) {
        final ListNBT playerList = new ListNBT();
        final ListNBT talentList = new ListNBT();
        this.playerMap.forEach((uuid, abilityTree) -> {
            playerList.add(StringNBT.valueOf(uuid.toString()));
            talentList.add(abilityTree.serializeNBT());
            return;
        });
        nbt.put("PlayerEntries", (INBT)playerList);
        nbt.put("SetEntries", (INBT)talentList);
        return nbt;
    }
    
    public static PlayerSetsData get(final ServerWorld world) {
        return (PlayerSetsData)world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier)PlayerSetsData::new, "the_vault_PlayerSets");
    }
}
