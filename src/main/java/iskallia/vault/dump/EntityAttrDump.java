package iskallia.vault.dump;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityAttrDump extends JsonDump {
    @Override
    public String fileName() {
        return "entity_attr.json";
    }

    @Override
    public JsonObject dumpToJSON() {
        final JsonObject root = new JsonObject();
        ForgeRegistries.ENTITIES.getValues().forEach(entity -> {
            try {
                this.baseAttrsFor((net.minecraft.entity.EntityType<LivingEntity>) entity, root);
            } catch (final Throwable t2) {
            }
            return;
        });
        return root;
    }

    private <T extends LivingEntity> void baseAttrsFor(final EntityType<T> entityType, final JsonObject root) {
        final JsonArray attributesJson = new JsonArray();
        final ResourceLocation entityId = entityType.getRegistryName();
        this.getAttributes(entityType).forEach((attr, attrInstance) -> {
            final JsonObject jsonEntry = new JsonObject();
            jsonEntry.addProperty("attributeId", attr.getRegistryName().toString());
            jsonEntry.addProperty("value", (Number) attrInstance.getValue());
            attributesJson.add((JsonElement) jsonEntry);
            return;
        });
        if (entityId == null) {
            throw new InternalError();
        }
        root.add(entityId.toString(), (JsonElement) attributesJson);
    }

    private <T extends LivingEntity> Map<Attribute, ModifiableAttributeInstance> getAttributes(final EntityType<T> entityType) {
        try {
            final AttributeModifierMap attributes = GlobalEntityTypeAttributes.getSupplier((EntityType) entityType);
            final Field attributeMapField = AttributeModifierMap.class.getDeclaredField("attributeMap");
            attributeMapField.setAccessible(true);
            return (Map) attributeMapField.get(attributes);
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            return new HashMap<Attribute, ModifiableAttributeInstance>();
        }
    }
}
