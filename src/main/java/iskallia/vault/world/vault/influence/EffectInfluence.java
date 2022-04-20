package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectInfluence extends VaultInfluence {
    public static final ResourceLocation ID;
    private Effect effect;
    private int amplifier;

    EffectInfluence() {
        super(EffectInfluence.ID);
    }

    public EffectInfluence(final Effect effect, final int amplifier) {
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
        return new EffectTalent(0, this.getEffect(), this.getAmplifier(), EffectTalent.Type.HIDDEN, EffectTalent.Operator.ADD);
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        tag.putString("effect", this.effect.getRegistryName().toString());
        tag.putInt("amplifier", this.amplifier);
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.effect = (Effect) ForgeRegistries.POTIONS.getValue(new ResourceLocation(tag.getString("effect")));
        this.amplifier = tag.getInt("amplifier");
    }

    static {
        ID = Vault.id("effect");
    }
}
