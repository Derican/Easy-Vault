package iskallia.vault.client;

import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.*;

public class ClientPartyData {
    private static final List<VaultPartyData.Party> parties;
    private static final Map<UUID, PartyMember> cachedPartyMembers;

    @Nullable
    public static VaultPartyData.Party getParty(final UUID playerUUID) {
        for (final VaultPartyData.Party party : ClientPartyData.parties) {
            if (party.hasMember(playerUUID)) {
                return party;
            }
        }
        return null;
    }

    @Nullable
    public static PartyMember getCachedMember(@Nullable final UUID playerUUID) {
        if (playerUUID == null) {
            return null;
        }
        return ClientPartyData.cachedPartyMembers.get(playerUUID);
    }

    public static void receivePartyUpdate(final ListNBT partyData) {
        ClientPartyData.parties.clear();
        for (int i = 0; i < partyData.size(); ++i) {
            final CompoundNBT data = partyData.getCompound(i);
            final VaultPartyData.Party party = new VaultPartyData.Party();
            party.deserializeNBT(data);
            ClientPartyData.parties.add(party);
        }
    }

    public static void receivePartyMembers(final ListNBT partyMembers) {
        for (int i = 0; i < partyMembers.size(); ++i) {
            final CompoundNBT nbt = partyMembers.getCompound(i);
            final PartyMember partyMember = new PartyMember();
            partyMember.deserializeNBT(nbt);
            ClientPartyData.cachedPartyMembers.put(partyMember.playerUUID, partyMember);
        }
    }

    static {
        parties = new ArrayList<VaultPartyData.Party>();
        cachedPartyMembers = new HashMap<UUID, PartyMember>();
    }

    public static class PartyMember implements INBTSerializable<CompoundNBT> {
        public UUID playerUUID;
        public float healthPts;
        public Status status;

        public PartyMember() {
            this.status = Status.NORMAL;
        }

        public PartyMember(final PlayerEntity player) {
            this.status = Status.NORMAL;
            this.playerUUID = player.getUUID();
            this.healthPts = player.getHealth();
            if (this.healthPts <= 0.0f) {
                this.status = Status.DEAD;
            } else {
                for (final EffectInstance potionEffect : player.getActiveEffects()) {
                    final Effect potion = potionEffect.getEffect();
                    if (potion == Effects.POISON) {
                        this.status = Status.POISONED;
                        break;
                    }
                    if (potion == Effects.WITHER) {
                        this.status = Status.WITHERED;
                        break;
                    }
                }
            }
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putUUID("PlayerUUID", this.playerUUID);
            nbt.putFloat("HealthPts", this.healthPts);
            nbt.putInt("StatusIndex", this.status.ordinal());
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.playerUUID = nbt.getUUID("PlayerUUID");
            this.healthPts = nbt.getFloat("HealthPts");
            this.status = Status.values()[nbt.getInt("StatusIndex")];
        }

        public enum Status {
            NORMAL,
            POISONED,
            WITHERED,
            DEAD;
        }
    }
}
