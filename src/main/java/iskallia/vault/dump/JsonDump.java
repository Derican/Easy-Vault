package iskallia.vault.dump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class JsonDump {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public abstract String fileName();

    public abstract JsonObject dumpToJSON();

    public void dumpToFile(String parentDir) throws IOException {
        File configFile = new File(parentDir + File.separator + fileName());
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        }

        JsonObject jsonObject = dumpToJSON();
        FileWriter writer = new FileWriter(configFile);
        GSON.toJson((JsonElement) jsonObject, writer);
        writer.close();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\dump\JsonDump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */