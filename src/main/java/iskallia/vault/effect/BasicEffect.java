package iskallia.vault.effect;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;

public class BasicEffect extends Effect {
    public BasicEffect(EffectType typeIn, int liquidColorIn, ResourceLocation id) {
        super(typeIn, liquidColorIn);

        setRegistryName(id);
    }


    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\effect\BasicEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */