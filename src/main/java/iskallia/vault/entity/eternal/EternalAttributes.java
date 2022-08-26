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

public class EternalAttributes implements INBTSerializable<CompoundNBT> {
    public static final String HEALTH = "health";
    public static final String DAMAGE = "damage";
    public static final String MOVEMENT_SPEED = "movespeed";
    private final Map<Attribute, Float> attributes;

    public EternalAttributes() {
        this.attributes = new HashMap<Attribute, Float>();
    }

    private EternalAttributes(final CompoundNBT tag) {
        this.attributes = new HashMap<Attribute, Float>();
        this.deserializeNBT(tag);
    }

    public static EternalAttributes fromNBT(final CompoundNBT tag) {
        return new EternalAttributes(tag);
    }

    void initializeAttributes() {
        ModConfigs.ETERNAL_ATTRIBUTES.createAttributes().forEach(this.attributes::put);
    }

    public Optional<Float> getAttributeValue(final Attribute attribute) {
        return Optional.ofNullable(this.attributes.get(attribute));
    }

    public Map<Attribute, Float> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    private void setAttributeValue(final Attribute attribute, final float value) {
        this.attributes.put(attribute, value);
    }

    void addAttributeValue(final Attribute attribute, final float value) {
        final float existing = this.getAttributeValue(attribute).orElse(0.0f);
        this.setAttributeValue(attribute, existing + value);
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = new CompoundNBT();
        this.attributes.forEach((attribute, value) -> tag.putFloat(attribute.getRegistryName().toString(), value));
        return tag;
    }

    public void deserializeNBT(final CompoundNBT tag) {
        this.attributes.clear();
        tag.getAllKeys().forEach(attributeKey -> {
            final Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeKey));
            if (attr != null) {
                this.attributes.put(attr, tag.getFloat(attributeKey));
            }
        });
    }
}
