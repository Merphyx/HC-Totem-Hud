package de.myronx.hctotemhud.config;

import net.fabricmc.loader.api.FabricLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().resolve("hctotemhud.json").toString());

    public boolean toggleMod = true;
    public int threshold = 5;
    public boolean warningToggle = true;
    public int warningThreshold = 1;
    public boolean soundToggle = true;
    public boolean pulsationToggle = true;
    public boolean vignetteToggle = true;

    private static final Config INSTANCE = new Config();

    public static Config getInstance() {
        return INSTANCE;
    }

    public void load() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                Config config = GSON.fromJson(reader, Config.class);
                this.toggleMod = config.toggleMod;
                this.threshold = config.threshold;
                this.warningToggle = config.warningToggle;
                this.warningThreshold = config.warningThreshold;
                this.soundToggle = config.soundToggle;
                this.pulsationToggle = config.pulsationToggle;
                this.vignetteToggle = config.vignetteToggle;
            } catch (IOException | JsonIOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
