package iskallia.vault;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector
        implements IMixinConnector {
    public void connect() {
        Mixins.addConfigurations(new String[]{"assets/the_vault/the_vault.mixins.json"});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\MixinConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */