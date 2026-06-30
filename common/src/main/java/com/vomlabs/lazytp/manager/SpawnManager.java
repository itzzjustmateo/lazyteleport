package com.vomlabs.lazytp.manager;

import com.vomlabs.lazytp.model.NamedLocation;
import com.vomlabs.lazytp.storage.SpawnStorage;
import org.bukkit.Location;

public class SpawnManager {

    private final SpawnStorage storage;

    public SpawnManager(SpawnStorage storage) {
        this.storage = storage;
    }

    public NamedLocation get() {
        return storage.get();
    }

    public boolean isSet() {
        return storage.isSet();
    }

    public void set(Location location) {
        storage.set(location);
    }

    public void delete() {
        storage.remove();
    }

}
