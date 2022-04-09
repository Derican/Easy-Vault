// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.dump;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import iskallia.vault.init.ModModels;

import java.util.Map;

public class GearModelDump extends JsonDump
{
    @Override
    public String fileName() {
        return "gear_models.json";
    }
    
    @Override
    public JsonObject dumpToJSON() {
        final JsonObject jsonObject = new JsonObject();
        final JsonArray regularModels = new JsonArray();
        final JsonArray specialHeadModels = new JsonArray();
        final JsonArray specialChestModels = new JsonArray();
        final JsonArray specialLegsModels = new JsonArray();
        final JsonArray specialFeetModels = new JsonArray();
        putRegularModels(regularModels, ModModels.GearModel.REGISTRY, false);
        putRegularModels(regularModels, ModModels.GearModel.SCRAPPY_REGISTRY, true);
        putSpecialModels(specialHeadModels, ModModels.SpecialGearModel.HEAD_REGISTRY);
        putSpecialModels(specialChestModels, ModModels.SpecialGearModel.CHESTPLATE_REGISTRY);
        putSpecialModels(specialLegsModels, ModModels.SpecialGearModel.LEGGINGS_REGISTRY);
        putSpecialModels(specialFeetModels, ModModels.SpecialGearModel.BOOTS_REGISTRY);
        jsonObject.add("regularModels", (JsonElement)regularModels);
        jsonObject.add("specialHeadModels", (JsonElement)specialHeadModels);
        jsonObject.add("specialChestModels", (JsonElement)specialChestModels);
        jsonObject.add("specialLegsModels", (JsonElement)specialLegsModels);
        jsonObject.add("specialFeetModels", (JsonElement)specialFeetModels);
        return jsonObject;
    }
    
    private static void putRegularModels(final JsonArray array, final Map<Integer, ModModels.GearModel> registry, final boolean isScrappy) {
        for (final Map.Entry<Integer, ModModels.GearModel> entry : registry.entrySet()) {
            final Integer modelIndex = entry.getKey();
            final ModModels.GearModel model = entry.getValue();
            final String modelId = model.getDisplayName().toLowerCase().replace(" ", "_");
            final JsonObject modelJson = new JsonObject();
            modelJson.addProperty("modelId", modelId);
            modelJson.addProperty("modelIndex", (Number)modelIndex);
            modelJson.addProperty("name", model.getDisplayName());
            modelJson.addProperty("scrappy", Boolean.valueOf(isScrappy));
            array.add((JsonElement)modelJson);
        }
    }
    
    private static void putSpecialModels(final JsonArray array, final Map<Integer, ModModels.SpecialGearModel> registry) {
        for (final Map.Entry<Integer, ModModels.SpecialGearModel> entry : registry.entrySet()) {
            final Integer modelIndex = entry.getKey();
            final ModModels.SpecialGearModel model = entry.getValue();
            final String modelId = model.getDisplayName().toLowerCase().replace(" ", "_");
            final JsonObject modelJson = new JsonObject();
            modelJson.addProperty("modelId", modelId);
            modelJson.addProperty("modelIndex", (Number)modelIndex);
            modelJson.addProperty("name", model.getDisplayName());
            array.add((JsonElement)modelJson);
        }
    }
}
