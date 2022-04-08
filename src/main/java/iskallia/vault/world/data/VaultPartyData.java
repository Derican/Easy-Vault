package iskallia.vault.world.data;

import iskallia.vault.client.ClientPartyData;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.network.message.PartyMembersMessage;
import iskallia.vault.network.message.PartyStatusMessage;
import iskallia.vault.util.MiscUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;

@EventBusSubscriber
public class VaultPartyData
        extends WorldSavedData {
    private static final Random rand = new Random();

    protected static final String DATA_NAME = "the_vault_VaultParty";
    protected VListNBT<Party, CompoundNBT> activeParties = VListNBT.of(Party::new);

    public VaultPartyData() {
        this("the_vault_VaultParty");
    }

    public VaultPartyData(String name) {
        super(name);
    }


    public void load(CompoundNBT nbt) {
        this.activeParties.deserializeNBT(nbt.getList("ActiveParties", 10));
    }


    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("ActiveParties", (INBT) this.activeParties.serializeNBT());
        return nbt;
    }

    public static VaultPartyData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static VaultPartyData get(MinecraftServer server) {
        return (VaultPartyData) server.overworld().getDataStorage().computeIfAbsent(VaultPartyData::new, "the_vault_VaultParty");
    }

    public static void broadcastPartyData(ServerWorld world) {
        VaultPartyData data = get(world);
        ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new PartyStatusMessage(data.activeParties.serializeNBT()));
    }

    public Optional<Party> getParty(UUID playerId) {
        return this.activeParties.stream().filter(party -> party.hasMember(playerId)).findFirst();
    }

    public boolean createParty(UUID playerId) {
        if (getParty(playerId).isPresent()) {
            return false;
        }

        Party newParty = new Party();
        newParty.addMember(playerId);
        this.activeParties.add(newParty);
        return true;
    }

    public boolean disbandParty(UUID playerId) {
        Optional<Party> party = getParty(playerId);

        if (!party.isPresent()) {
            return false;
        }

        this.activeParties.remove(party.get());
        return true;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        MinecraftServer serverInstance = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

        if (event.phase != TickEvent.Phase.END)
            return;
        if (serverInstance.getTickCount() % 20 == 0) {
            VaultPartyData vaultPartyData = get(serverInstance);
            vaultPartyData.activeParties.forEach(party -> {
                ListNBT partyMembers = party.toClientMemberList();
                PartyMembersMessage pkt = new PartyMembersMessage(partyMembers);
                party.members.forEach(());
            });
        }
    }


    public static class Party
            implements INBTSerializable<CompoundNBT> {
        private UUID leader = null;
        private final VListNBT<UUID, StringNBT> members = VListNBT.ofUUID();
        private final VListNBT<UUID, StringNBT> invites = VListNBT.ofUUID();


        public List<UUID> getMembers() {
            return Collections.unmodifiableList((List) this.members);
        }

        @Nullable
        public UUID getLeader() {
            return this.leader;
        }

        public boolean addMember(UUID member) {
            if (this.members.isEmpty()) {
                this.leader = member;
            }
            return this.members.add(member);
        }

        public boolean invite(UUID member) {
            if (this.invites.contains(member)) {
                return false;
            }
            this.invites.add(member);
            return true;
        }

        public boolean remove(UUID member) {
            boolean removed = this.members.remove(member);
            if (removed && member.equals(this.leader)) {
                this.leader = (UUID) MiscUtils.getRandomEntry((Collection) this.members, VaultPartyData.rand);
            }
            return removed;
        }

        public boolean confirmInvite(UUID member) {
            if (this.invites.contains(member) && this.invites.remove(member)) {
                this.members.add(member);
                return true;
            }

            return false;
        }

        public boolean hasMember(UUID member) {
            return this.members.contains(member);
        }

        public ListNBT toClientMemberList() {
            MinecraftServer serverInstance = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
            ListNBT partyMembers = new ListNBT();
            for (UUID uuid : this.members) {
                ServerPlayerEntity player = serverInstance.getPlayerList().getPlayer(uuid);
                if (player == null) {
                    continue;
                }

                ClientPartyData.PartyMember partyMember = new ClientPartyData.PartyMember((PlayerEntity) player);
                partyMembers.add(partyMember.serializeNBT());
            }
            return partyMembers;
        }


        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            if (this.leader != null) {
                nbt.putUUID("leader", this.leader);
            }
            nbt.put("Members", (INBT) this.members.serializeNBT());
            nbt.put("Invites", (INBT) this.invites.serializeNBT());
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.leader = null;
            if (nbt.hasUUID("leader")) {
                this.leader = nbt.getUUID("leader");
            }
            this.members.deserializeNBT(nbt.getList("Members", 8));
            this.invites.deserializeNBT(nbt.getList("Invites", 8));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\VaultPartyData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */