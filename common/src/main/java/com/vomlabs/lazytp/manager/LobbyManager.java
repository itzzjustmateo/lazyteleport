package com.vomlabs.lazytp.manager;

import com.vomlabs.lazytp.model.NamedLocation;
import com.vomlabs.lazytp.storage.LobbyStorage;
import org.bukkit.Location;

public class LobbyManager {

    private final LobbyStorage storage;

    public LobbyManager(LobbyStorage storage) {
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
