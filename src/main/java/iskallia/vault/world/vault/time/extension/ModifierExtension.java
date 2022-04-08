package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.world.vault.modifier.TimerModifier;
import net.minecraft.util.ResourceLocation;

public class ModifierExtension
        extends TimeExtension {
    public static final ResourceLocation ID = Vault.id("modifier");


    public ModifierExtension() {
    }


    public ModifierExtension(int addedTime) {
        super(ID, addedTime);
    }

    public ModifierExtension(TimerModifier modifier) {
        this(modifier.getTimerAddend());
    }

    public ModifierExtension(ResourceLocation id, TimerModifier modifier) {
        super(id, modifier.getTimerAddend());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\ModifierExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */