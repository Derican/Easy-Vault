package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectInfluence extends VaultInfluence {
    public static final ResourceLocation ID = Vault.id("effect");

    private Effect effect;
    private int amplifier;

    EffectInfluence() {
        super(ID);
    }

    public EffectInfluence(Effect effect, int amplifier) {
        this();
        this.effect = effect;
        this.amplifier = amplifier;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public EffectTalent makeTalent() {
        return new EffectTalent(0, getEffect(), getAmplifier(), EffectTalent.Type.HIDDEN, EffectTalent.Operator.ADD);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putString("effect", this.effect.getRegistryName().toString());
        tag.putInt("amplifier", this.amplifier);
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.effect = (Effect) ForgeRegistries.POTIONS.getValue(new ResourceLocation(tag.getString("effect")));
        this.amplifier = tag.getInt("amplifier");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\influence\EffectInfluence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */