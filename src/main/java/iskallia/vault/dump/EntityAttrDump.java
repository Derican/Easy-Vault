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


public class EntityAttrDump
        extends JsonDump {
    public String fileName() {
        return "entity_attr.json";
    }


    public JsonObject dumpToJSON() {
        JsonObject root = new JsonObject();

        ForgeRegistries.ENTITIES.getValues().forEach(entity -> {
            try {
                baseAttrsFor((EntityType<? extends LivingEntity>) entity, root);
            } catch (Throwable throwable) {
            }
        });


        return root;
    }

    private <T extends LivingEntity> void baseAttrsFor(EntityType<T> entityType, JsonObject root) {
        JsonArray attributesJson = new JsonArray();

        ResourceLocation entityId = entityType.getRegistryName();

        getAttributes(entityType).forEach((attr, attrInstance) -> {
            JsonObject jsonEntry = new JsonObject();

            jsonEntry.addProperty("attributeId", attr.getRegistryName().toString());
            jsonEntry.addProperty("value", Double.valueOf(attrInstance.getValue()));
            attributesJson.add((JsonElement) jsonEntry);
        });
        if (entityId == null) {
            throw new InternalError();
        }

        root.add(entityId.toString(), (JsonElement) attributesJson);
    }


    private <T extends LivingEntity> Map<Attribute, ModifiableAttributeInstance> getAttributes(EntityType<T> entityType) {
        try {
            AttributeModifierMap attributes = GlobalEntityTypeAttributes.getSupplier(entityType);
            Field attributeMapField = AttributeModifierMap.class.getDeclaredField("attributeMap");
            attributeMapField.setAccessible(true);
            return (Map<Attribute, ModifiableAttributeInstance>) attributeMapField.get(attributes);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return new HashMap<>();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\dump\EntityAttrDump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */