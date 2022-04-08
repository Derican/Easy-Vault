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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ClientPartyData {
    private static final List<VaultPartyData.Party> parties = new ArrayList<>();
    private static final Map<UUID, PartyMember> cachedPartyMembers = new HashMap<>();

    @Nullable
    public static VaultPartyData.Party getParty(UUID playerUUID) {
        for (VaultPartyData.Party party : parties) {
            if (party.hasMember(playerUUID)) {
                return party;
            }
        }
        return null;
    }

    @Nullable
    public static PartyMember getCachedMember(@Nullable UUID playerUUID) {
        if (playerUUID == null) return null;
        return cachedPartyMembers.get(playerUUID);
    }

    public static void receivePartyUpdate(ListNBT partyData) {
        parties.clear();

        for (int i = 0; i < partyData.size(); i++) {
            CompoundNBT data = partyData.getCompound(i);
            VaultPartyData.Party party = new VaultPartyData.Party();
            party.deserializeNBT(data);
            parties.add(party);
        }
    }

    public static void receivePartyMembers(ListNBT partyMembers) {
        for (int i = 0; i < partyMembers.size(); i++) {
            CompoundNBT nbt = partyMembers.getCompound(i);
            PartyMember partyMember = new PartyMember();
            partyMember.deserializeNBT(nbt);
            cachedPartyMembers.put(partyMember.playerUUID, partyMember);
        }
    }

    public static class PartyMember implements INBTSerializable<CompoundNBT> {
        public UUID playerUUID;
        public float healthPts;

        public enum Status {NORMAL, POISONED, WITHERED, DEAD;}


        public Status status = Status.NORMAL;


        public PartyMember(PlayerEntity player) {
            this.playerUUID = player.getUUID();
            this.healthPts = player.getHealth();

            if (this.healthPts <= 0.0F) {
                this.status = Status.DEAD;
            } else {
                for (EffectInstance potionEffect : player.getActiveEffects()) {
                    Effect potion = potionEffect.getEffect();
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
            CompoundNBT nbt = new CompoundNBT();
            nbt.putUUID("PlayerUUID", this.playerUUID);
            nbt.putFloat("HealthPts", this.healthPts);
            nbt.putInt("StatusIndex", this.status.ordinal());
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.playerUUID = nbt.getUUID("PlayerUUID");
            this.healthPts = nbt.getFloat("HealthPts");
            this.status = Status.values()[nbt.getInt("StatusIndex")];
        }

        public PartyMember() {
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\ClientPartyData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */