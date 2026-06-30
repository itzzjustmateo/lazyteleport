package com.vomlabs.lazytp.manager;

import com.vomlabs.lazytp.exception.InvalidNameException;
import com.vomlabs.lazytp.exception.WarpNotFoundException;
import com.vomlabs.lazytp.model.Warp;
import com.vomlabs.lazytp.storage.WarpStorage;
import org.bukkit.Location;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WarpManager {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1,32}$");

    private final WarpStorage storage;

    public WarpManager(WarpStorage storage) {
        this.storage = storage;
    }

    public Warp get(String name) {
        Warp warp = storage.get(name);
        if (warp == null) {
            throw new WarpNotFoundException(name);
        }
        return warp;
    }

    public Collection<Warp> getAll() {
        return storage.getAll();
    }

    public Collection<String> getNames() {
        return storage.getAll().stream()
                .map(Warp::getName)
                .collect(Collectors.toList());
    }

    public void create(String name, Location location) {
        validateName(name);
        if (storage.contains(name)) {
            throw new InvalidNameException(name, "Warp already exists");
        }
        storage.add(new Warp(name, location));
    }

    public void delete(String name) {
        if (!storage.contains(name)) {
            throw new WarpNotFoundException(name);
        }
        storage.remove(name);
    }

    public boolean exists(String name) {
        return storage.contains(name);
    }

    public int count() {
        return storage.getAll().size();
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidNameException(name, "Name cannot be empty");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidNameException(name, "Name must be 1-32 characters, using letters, numbers, underscores, or hyphens");
        }
    }

}
