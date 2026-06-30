package com.vomlabs.lazytp.storage;

import com.vomlabs.lazytp.model.Warp;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WarpStorage extends YamlFile {

    private final ConcurrentMap<String, Warp> cache = new ConcurrentHashMap<>();

    public WarpStorage(JavaPlugin plugin) {
        super(plugin, "warps.yml");
    }

    @Override
    public void load() {
        super.load();
        cache.clear();
        ConfigurationSection section = config.getConfigurationSection("warps");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            String world = section.getString(key + ".world");
            double x = section.getDouble(key + ".x");
            double y = section.getDouble(key + ".y");
            double z = section.getDouble(key + ".z");
            float yaw = (float) section.getDouble(key + ".yaw");
            float pitch = (float) section.getDouble(key + ".pitch");
            Warp warp = new Warp(key, world, x, y, z, yaw, pitch);
            cache.put(key.toLowerCase(), warp);
        }
    }

    public Collection<Warp> getAll() {
        return cache.values();
    }

    public Warp get(String name) {
        return cache.get(name.toLowerCase());
    }

    public boolean contains(String name) {
        return cache.containsKey(name.toLowerCase());
    }

    public void add(Warp warp) {
        cache.put(warp.getName().toLowerCase(), warp);
        saveWarp(warp);
    }

    public void remove(String name) {
        cache.remove(name.toLowerCase());
        config.set("warps." + name, null);
        save();
    }

    private void saveWarp(Warp warp) {
        String path = "warps." + warp.getName();
        config.set(path + ".world", warp.getWorldName());
        config.set(path + ".x", warp.getX());
        config.set(path + ".y", warp.getY());
        config.set(path + ".z", warp.getZ());
        config.set(path + ".yaw", (double) warp.getYaw());
        config.set(path + ".pitch", (double) warp.getPitch());
        save();
    }

}
