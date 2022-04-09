// 
// Decompiled by Procyon v0.6.0
// 

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

public class MobAttributeInfluence extends VaultInfluence
{
    public static final ResourceLocation ID;
    private Attribute targetAttribute;
    private AttributeModifier modifier;
    
    MobAttributeInfluence() {
        super(MobAttributeInfluence.ID);
    }
    
    public MobAttributeInfluence(final Attribute targetAttribute, final AttributeModifier modifier) {
        this();
        this.targetAttribute = targetAttribute;
        this.modifier = modifier;
    }
    
    public void applyTo(final LivingEntity le) {
        final ModifiableAttributeInstance instance = le.getAttribute(this.targetAttribute);
        final AttributeModifier existing = instance.getModifier(this.modifier.getId());
        if (existing == null) {
            instance.addPermanentModifier(this.modifier);
        }
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        tag.putString("attribute", this.targetAttribute.getRegistryName().toString());
        tag.put("modifier", (INBT)this.modifier.save());
        return tag;
    }
    
    @Override
    public void deserializeNBT(final CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.targetAttribute = (Attribute)ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(tag.getString("attribute")));
        this.modifier = AttributeModifier.load(tag.getCompound("modifier"));
    }
    
    static {
        ID = Vault.id("mob_attribute");
    }
}
