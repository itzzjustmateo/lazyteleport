package com.vomlabs.lazytp.storage;

import com.vomlabs.lazytp.model.Home;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class HomeStorage extends YamlFile {

    private final ConcurrentMap<String, Home> cache = new ConcurrentHashMap<>();

    public HomeStorage(JavaPlugin plugin) {
        super(plugin, "homes.yml");
    }

    @Override
    public void load() {
        super.load();
        cache.clear();
        ConfigurationSection section = config.getConfigurationSection("homes");
        if (section == null) {
            return;
        }
        for (String uuidKey : section.getKeys(false)) {
            ConfigurationSection playerSection = section.getConfigurationSection(uuidKey);
            if (playerSection == null) {
                continue;
            }
            UUID ownerId = UUID.fromString(uuidKey);
            for (String homeName : playerSection.getKeys(false)) {
                String world = playerSection.getString(homeName + ".world");
                double x = playerSection.getDouble(homeName + ".x");
                double y = playerSection.getDouble(homeName + ".y");
                double z = playerSection.getDouble(homeName + ".z");
                float yaw = (float) playerSection.getDouble(homeName + ".yaw");
                float pitch = (float) playerSection.getDouble(homeName + ".pitch");
                Home home = new Home(homeName, world, x, y, z, yaw, pitch, ownerId);
                cache.put(cacheKey(ownerId, homeName), home);
            }
        }
    }

    public Collection<Home> getAll() {
        return cache.values();
    }

    public Collection<Home> getByOwner(UUID ownerId) {
        return cache.values().stream()
                .filter(h -> h.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public Home get(UUID ownerId, String name) {
        return cache.get(cacheKey(ownerId, name));
    }

    public boolean contains(UUID ownerId, String name) {
        return cache.containsKey(cacheKey(ownerId, name));
    }

    public int countByOwner(UUID ownerId) {
        return (int) cache.values().stream()
                .filter(h -> h.getOwnerId().equals(ownerId))
                .count();
    }

    public void add(Home home) {
        cache.put(cacheKey(home.getOwnerId(), home.getName()), home);
        saveHome(home);
    }

    public void remove(UUID ownerId, String name) {
        cache.remove(cacheKey(ownerId, name));
        config.set("homes." + ownerId + "." + name, null);
        save();
    }

    private String cacheKey(UUID ownerId, String name) {
        return ownerId.toString() + ":" + name.toLowerCase();
    }

    private void saveHome(Home home) {
        String path = "homes." + home.getOwnerId() + "." + home.getName();
        config.set(path + ".world", home.getWorldName());
        config.set(path + ".x", home.getX());
        config.set(path + ".y", home.getY());
        config.set(path + ".z", home.getZ());
        config.set(path + ".yaw", (double) home.getYaw());
        config.set(path + ".pitch", (double) home.getPitch());
        save();
    }

}
