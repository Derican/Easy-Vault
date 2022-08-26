package iskallia.vault.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Random;

public abstract class Config implements IFeatureConfig {
    protected static final Random rand;
    private static final Gson GSON;
    protected String root;
    protected String extension;

    public Config() {
        this.root = "config/the_vault/";
        this.extension = ".json";
    }

    public void generateConfig() {
        this.reset();
        try {
            this.writeConfig();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private File getConfigFile() {
        return new File(this.root + this.getName() + this.extension);
    }

    public abstract String getName();

    public <T extends Config> T readConfig() {
        try {
            return Config.GSON.fromJson(new FileReader(this.getConfigFile()), (Type) this.getClass());
        } catch (final FileNotFoundException e) {
            this.generateConfig();
            return (T) this;
        }
    }

    protected abstract void reset();

    public void writeConfig() throws IOException {
        final File dir = new File(this.root);
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }
        if (!this.getConfigFile().exists() && !this.getConfigFile().createNewFile()) {
            return;
        }
        final FileWriter writer = new FileWriter(this.getConfigFile());
        Config.GSON.toJson(this, writer);
        writer.flush();
        writer.close();
    }

    static {
        rand = new Random();
        GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    }
}
