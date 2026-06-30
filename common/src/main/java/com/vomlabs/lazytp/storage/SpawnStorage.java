package com.vomlabs.lazytp.storage;

import com.vomlabs.lazytp.model.NamedLocation;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnStorage extends YamlFile {

    private NamedLocation cachedSpawn;

    public SpawnStorage(JavaPlugin plugin) {
        super(plugin, "spawn.yml");
    }

    @Override
    public void load() {
        super.load();
        cachedSpawn = null;
        if (config.contains("world")) {
            String world = config.getString("world");
            double x = config.getDouble("x");
            double y = config.getDouble("y");
            double z = config.getDouble("z");
            float yaw = (float) config.getDouble("yaw");
            float pitch = (float) config.getDouble("pitch");
            cachedSpawn = new NamedLocation("spawn", world, x, y, z, yaw, pitch);
        }
    }

    public NamedLocation get() {
        return cachedSpawn;
    }

    public boolean isSet() {
        return cachedSpawn != null;
    }

    public void set(Location location) {
        cachedSpawn = new NamedLocation("spawn", location);
        saveLocation(location);
    }

    public void remove() {
        cachedSpawn = null;
        config.set("world", null);
        config.set("x", null);
        config.set("y", null);
        config.set("z", null);
        config.set("yaw", null);
        config.set("pitch", null);
        save();
    }

    private void saveLocation(Location location) {
        config.set("world", location.getWorld().getName());
        config.set("x", location.getX());
        config.set("y", location.getY());
        config.set("z", location.getZ());
        config.set("yaw", (double) location.getYaw());
        config.set("pitch", (double) location.getPitch());
        save();
    }

}
