package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;


public class MobAttributeInfluence
        extends VaultInfluence {
    public static final ResourceLocation ID = Vault.id("mob_attribute");

    private Attribute targetAttribute;
    private AttributeModifier modifier;

    MobAttributeInfluence() {
        super(ID);
    }

    public MobAttributeInfluence(Attribute targetAttribute, AttributeModifier modifier) {
        this();
        this.targetAttribute = targetAttribute;
        this.modifier = modifier;
    }

    public void applyTo(LivingEntity le) {
        ModifiableAttributeInstance instance = le.getAttribute(this.targetAttribute);
        AttributeModifier existing = instance.getModifier(this.modifier.getId());
        if (existing == null) {
            instance.addPermanentModifier(this.modifier);
        }
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putString("attribute", this.targetAttribute.getRegistryName().toString());
        tag.put("modifier", (INBT) this.modifier.save());
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.targetAttribute = (Attribute) ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(tag.getString("attribute")));
        this.modifier = AttributeModifier.load(tag.getCompound("modifier"));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\influence\MobAttributeInfluence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */