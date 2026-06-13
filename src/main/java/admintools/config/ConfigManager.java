package admintools.config;

import admintools.AdminTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "admintools.json");

    private static ModConfig config = new ModConfig();

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, ModConfig.class);
                if (config == null) {
                    config = new ModConfig();
                }
            } catch (IOException e) {
                AdminTools.LOGGER.error("Failed to load AdminTools config", e);
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            AdminTools.LOGGER.error("Failed to save AdminTools config", e);
        }
    }

    public static ModConfig getConfig() {
        return config;
    }
}
