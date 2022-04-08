package iskallia.vault.entity.eternal;

import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class EternalAttributes
        implements INBTSerializable<CompoundNBT> {
    public static final String HEALTH = "health";
    public static final String DAMAGE = "damage";
    public static final String MOVEMENT_SPEED = "movespeed";
    private final Map<Attribute, Float> attributes = new HashMap<>();


    private EternalAttributes(CompoundNBT tag) {
        deserializeNBT(tag);
    }

    public static EternalAttributes fromNBT(CompoundNBT tag) {
        return new EternalAttributes(tag);
    }

    void initializeAttributes() {
        ModConfigs.ETERNAL_ATTRIBUTES.createAttributes().forEach(this.attributes::put);
    }

    public Optional<Float> getAttributeValue(Attribute attribute) {
        return Optional.ofNullable(this.attributes.get(attribute));
    }

    public Map<Attribute, Float> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    private void setAttributeValue(Attribute attribute, float value) {
        this.attributes.put(attribute, Float.valueOf(value));
    }

    void addAttributeValue(Attribute attribute, float value) {
        float existing = ((Float) getAttributeValue(attribute).orElse(Float.valueOf(0.0F))).floatValue();
        setAttributeValue(attribute, existing + value);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        this.attributes.forEach((attribute, value) -> tag.putFloat(attribute.getRegistryName().toString(), value.floatValue()));


        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        this.attributes.clear();
        tag.getAllKeys().forEach(attributeKey -> {
            Attribute attr = (Attribute) ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeKey));
            if (attr != null)
                this.attributes.put(attr, Float.valueOf(tag.getFloat(attributeKey)));
        });
    }

    public EternalAttributes() {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\eternal\EternalAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */