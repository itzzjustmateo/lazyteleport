package com.vomlabs.lazytp.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public abstract class YamlFile {

    protected final JavaPlugin plugin;
    protected final File file;
    protected FileConfiguration config;

    protected YamlFile(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
    }

    public void load() {
        if (!file.exists()) {
            saveDefault();
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save " + file.getName(), e);
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    protected void saveDefault() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (InputStream inputStream = plugin.getResource(file.getName())) {
            if (inputStream != null) {
                FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                if (!file.exists()) {
                    config = defaultConfig;
                    save();
                }
            } else {
                file.createNewFile();
                config = new YamlConfiguration();
                save();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save default " + file.getName(), e);
        }
    }

}
