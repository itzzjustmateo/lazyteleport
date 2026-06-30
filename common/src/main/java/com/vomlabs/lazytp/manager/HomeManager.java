package com.vomlabs.lazytp.manager;

import com.vomlabs.lazytp.config.PluginConfig;
import com.vomlabs.lazytp.exception.HomeNotFoundException;
import com.vomlabs.lazytp.exception.InvalidNameException;
import com.vomlabs.lazytp.model.Home;
import com.vomlabs.lazytp.storage.HomeStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HomeManager {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1,32}$");

    private final HomeStorage storage;
    private final PluginConfig config;

    public HomeManager(HomeStorage storage, PluginConfig config) {
        this.storage = storage;
        this.config = config;
    }

    public Home get(UUID ownerId, String name) {
        Home home = storage.get(ownerId, name);
        if (home == null) {
            throw new HomeNotFoundException(ownerId, name);
        }
        return home;
    }

    public Collection<Home> getByOwner(UUID ownerId) {
        return storage.getByOwner(ownerId);
    }

    public Collection<String> getNamesByOwner(UUID ownerId) {
        return storage.getByOwner(ownerId).stream()
                .map(Home::getName)
                .collect(Collectors.toList());
    }

    public void create(Player player, String name, Location location) {
        validateName(name);
        if (storage.contains(player.getUniqueId(), name)) {
            throw new InvalidNameException(name, "Home already exists");
        }
        if (storage.countByOwner(player.getUniqueId()) >= getMaxHomes(player)) {
            throw new IllegalStateException("Home limit reached");
        }
        storage.add(new Home(name, location, player.getUniqueId()));
    }

    public void delete(UUID ownerId, String name) {
        if (!storage.contains(ownerId, name)) {
            throw new HomeNotFoundException(ownerId, name);
        }
        storage.remove(ownerId, name);
    }

    public boolean exists(UUID ownerId, String name) {
        return storage.contains(ownerId, name);
    }

    public int countByOwner(UUID ownerId) {
        return storage.countByOwner(ownerId);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidNameException(name, "Name cannot be empty");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidNameException(name, "Name must be 1-32 characters, using letters, numbers, underscores, or hyphens");
        }
    }

    private int getMaxHomes(Player player) {
        int max = config.getDefaultHomeLimit();
        for (int i = max + 100; i > 0; i--) {
            if (player.hasPermission("lazyteleport.homes." + i)) {
                return i;
            }
        }
        return max;
    }

}
