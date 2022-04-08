package iskallia.vault.dump;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import iskallia.vault.init.ModModels;

import java.util.Map;

public class GearModelDump
        extends JsonDump {
    public String fileName() {
        return "gear_models.json";
    }


    public JsonObject dumpToJSON() {
        JsonObject jsonObject = new JsonObject();
        JsonArray regularModels = new JsonArray();
        JsonArray specialHeadModels = new JsonArray();
        JsonArray specialChestModels = new JsonArray();
        JsonArray specialLegsModels = new JsonArray();
        JsonArray specialFeetModels = new JsonArray();

        putRegularModels(regularModels, ModModels.GearModel.REGISTRY, false);
        putRegularModels(regularModels, ModModels.GearModel.SCRAPPY_REGISTRY, true);

        putSpecialModels(specialHeadModels, ModModels.SpecialGearModel.HEAD_REGISTRY);
        putSpecialModels(specialChestModels, ModModels.SpecialGearModel.CHESTPLATE_REGISTRY);
        putSpecialModels(specialLegsModels, ModModels.SpecialGearModel.LEGGINGS_REGISTRY);
        putSpecialModels(specialFeetModels, ModModels.SpecialGearModel.BOOTS_REGISTRY);

        jsonObject.add("regularModels", (JsonElement) regularModels);
        jsonObject.add("specialHeadModels", (JsonElement) specialHeadModels);
        jsonObject.add("specialChestModels", (JsonElement) specialChestModels);
        jsonObject.add("specialLegsModels", (JsonElement) specialLegsModels);
        jsonObject.add("specialFeetModels", (JsonElement) specialFeetModels);
        return jsonObject;
    }

    private static void putRegularModels(JsonArray array, Map<Integer, ModModels.GearModel> registry, boolean isScrappy) {
        for (Map.Entry<Integer, ModModels.GearModel> entry : registry.entrySet()) {
            Integer modelIndex = entry.getKey();
            ModModels.GearModel model = entry.getValue();

            String modelId = model.getDisplayName().toLowerCase().replace(" ", "_");

            JsonObject modelJson = new JsonObject();
            modelJson.addProperty("modelId", modelId);
            modelJson.addProperty("modelIndex", modelIndex);
            modelJson.addProperty("name", model.getDisplayName());
            modelJson.addProperty("scrappy", Boolean.valueOf(isScrappy));

            array.add((JsonElement) modelJson);
        }
    }

    private static void putSpecialModels(JsonArray array, Map<Integer, ModModels.SpecialGearModel> registry) {
        for (Map.Entry<Integer, ModModels.SpecialGearModel> entry : registry.entrySet()) {
            Integer modelIndex = entry.getKey();
            ModModels.SpecialGearModel model = entry.getValue();

            String modelId = model.getDisplayName().toLowerCase().replace(" ", "_");

            JsonObject modelJson = new JsonObject();
            modelJson.addProperty("modelId", modelId);
            modelJson.addProperty("modelIndex", modelIndex);
            modelJson.addProperty("name", model.getDisplayName());

            array.add((JsonElement) modelJson);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\dump\GearModelDump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */