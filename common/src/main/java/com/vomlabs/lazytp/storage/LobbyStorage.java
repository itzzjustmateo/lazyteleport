package com.vomlabs.lazytp.storage;

import com.vomlabs.lazytp.model.NamedLocation;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyStorage extends YamlFile {

    private NamedLocation cachedLobby;

    public LobbyStorage(JavaPlugin plugin) {
        super(plugin, "lobby.yml");
    }

    @Override
    public void load() {
        super.load();
        cachedLobby = null;
        if (config.contains("world")) {
            String world = config.getString("world");
            double x = config.getDouble("x");
            double y = config.getDouble("y");
            double z = config.getDouble("z");
            float yaw = (float) config.getDouble("yaw");
            float pitch = (float) config.getDouble("pitch");
            cachedLobby = new NamedLocation("lobby", world, x, y, z, yaw, pitch);
        }
    }

    public NamedLocation get() {
        return cachedLobby;
    }

    public boolean isSet() {
        return cachedLobby != null;
    }

    public void set(Location location) {
        cachedLobby = new NamedLocation("lobby", location);
        saveLocation(location);
    }

    public void remove() {
        cachedLobby = null;
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
