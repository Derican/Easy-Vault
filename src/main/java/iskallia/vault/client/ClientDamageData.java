package iskallia.vault.client;

import iskallia.vault.network.message.PlayerDamageMultiplierMessage;

public class ClientDamageData {
    private static float damageMultiplier = 1.0F;

    public static float getCurrentDamageMultiplier() {
        return damageMultiplier;
    }

    public static void receiveUpdate(PlayerDamageMultiplierMessage message) {
        damageMultiplier = message.getMultiplier();
    }

    public static void clearClientCache() {
        damageMultiplier = 1.0F;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\ClientDamageData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */