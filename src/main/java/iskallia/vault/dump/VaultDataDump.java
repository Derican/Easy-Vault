package iskallia.vault.dump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.forgespi.language.IModInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class VaultDataDump {
    private static final Gson GSON = (new GsonBuilder())
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    public static void onStart(FMLServerStartedEvent event) {
        dumpModData();
    }

    public static void dumpModData() {
        File dir = new File("data/the_vault/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dataFile = new File(dir, "data.json");
        if (dataFile.exists()) {
            dataFile.delete();
        }
        try {
            dataFile.createNewFile();

            FileWriter writer = new FileWriter(dataFile);
            GSON.toJson(getData(), writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();


        String version = ModList.get().getModContainerById("the_vault").map(ModContainer::getModInfo).map(IModInfo::getVersion).map(Objects::toString).orElse("");
        data.put("version", version);
        return data;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\dump\VaultDataDump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */