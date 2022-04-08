package iskallia.vault.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Random;

public abstract class Config {
    protected static final Random rand = new Random();

    private static final Gson GSON = (new GsonBuilder())
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();
    protected String root = "config/the_vault/";
    protected String extension = ".json";

    public void generateConfig() {
        reset();

        try {
            writeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getConfigFile() {
        return new File(this.root + getName() + this.extension);
    }

    public abstract String getName();

    public <T extends Config> T readConfig() {
        try {
            return (T) GSON.fromJson(new FileReader(getConfigFile()), getClass());
        } catch (FileNotFoundException e) {
            generateConfig();


            return (T) this;
        }
    }


    public void writeConfig() throws IOException {
        File dir = new File(this.root);
        if (!dir.exists() && !dir.mkdirs())
            return;
        if (!getConfigFile().exists() && !getConfigFile().createNewFile())
            return;
        FileWriter writer = new FileWriter(getConfigFile());
        GSON.toJson(this, writer);
        writer.flush();
        writer.close();
    }

    protected abstract void reset();
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */