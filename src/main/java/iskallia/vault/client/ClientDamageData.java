package iskallia.vault.client;

import iskallia.vault.network.message.PlayerDamageMultiplierMessage;

public class ClientDamageData {
    private static float damageMultiplier;

    public static float getCurrentDamageMultiplier() {
        return ClientDamageData.damageMultiplier;
    }

    public static void receiveUpdate(final PlayerDamageMultiplierMessage message) {
        ClientDamageData.damageMultiplier = message.getMultiplier();
    }

    public static void clearClientCache() {
        ClientDamageData.damageMultiplier = 1.0f;
    }

    static {
        ClientDamageData.damageMultiplier = 1.0f;
    }
}
