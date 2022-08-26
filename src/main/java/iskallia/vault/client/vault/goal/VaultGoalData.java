package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;

import javax.annotation.Nullable;

public abstract class VaultGoalData {
    public static VaultGoalData CURRENT_DATA;
    public static VaultGoalData ADDITIONAL_DATA;

    public abstract void receive(final VaultGoalMessage p0);

    @Nullable
    public abstract BossBarOverlay getBossBarOverlay();

    public static void create(final VaultGoalMessage pkt) {
        switch (pkt.opcode) {
            case OBELISK_GOAL:
            case OBELISK_MESSAGE: {
                (VaultGoalData.CURRENT_DATA = new VaultObeliskData()).receive(pkt);
                break;
            }
            case SCAVENGER_GOAL: {
                (VaultGoalData.CURRENT_DATA = new VaultScavengerData()).receive(pkt);
                break;
            }
            case ARCHITECT_GOAL: {
                (VaultGoalData.CURRENT_DATA = new ArchitectGoalData()).receive(pkt);
                break;
            }
            case FINAL_ARCHITECT_GOAL: {
                (VaultGoalData.CURRENT_DATA = new FinalArchitectGoalData()).receive(pkt);
                break;
            }
            case ANCIENTS_GOAL: {
                (VaultGoalData.CURRENT_DATA = new AncientGoalData()).receive(pkt);
                break;
            }
            case RAID_GOAL: {
                (VaultGoalData.CURRENT_DATA = new ActiveRaidGoalData()).receive(pkt);
                break;
            }
            case CAKE_HUNT_GOAL: {
                (VaultGoalData.CURRENT_DATA = new CakeHuntData()).receive(pkt);
                break;
            }
            case CLEAR: {
                VaultGoalData.CURRENT_DATA = null;
                VaultGoalData.ADDITIONAL_DATA = null;
                break;
            }
        }
    }

    static {
        VaultGoalData.CURRENT_DATA = null;
        VaultGoalData.ADDITIONAL_DATA = null;
    }
}
