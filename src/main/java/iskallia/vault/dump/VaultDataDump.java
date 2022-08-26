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
    private static final Gson GSON;

    public static void onStart(final FMLServerStartedEvent event) {
        dumpModData();
    }

    public static void dumpModData() {
        final File dir = new File("data/the_vault/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final File dataFile = new File(dir, "data.json");
        if (dataFile.exists()) {
            dataFile.delete();
        }
        try {
            dataFile.createNewFile();
            final FileWriter writer = new FileWriter(dataFile);
            VaultDataDump.GSON.toJson(getData(), writer);
            writer.flush();
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getData() {
        final Map<String, String> data = new HashMap<String, String>();
        final String version = ModList.get().getModContainerById("the_vault").map(ModContainer::getModInfo).map(IModInfo::getVersion).map(Objects::toString).orElse("");
        data.put("version", version);
        return data;
    }

    static {
        GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    }
}
