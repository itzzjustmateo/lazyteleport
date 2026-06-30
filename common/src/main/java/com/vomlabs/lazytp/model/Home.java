package com.vomlabs.lazytp.model;

import org.bukkit.Location;

import java.util.Objects;
import java.util.UUID;

public class Home extends NamedLocation {

    private final UUID ownerId;

    public Home(String name, Location location, UUID ownerId) {
        super(name, location);
        this.ownerId = ownerId;
    }

    public Home(String name, String worldName, double x, double y, double z, float yaw, float pitch, UUID ownerId) {
        super(name, worldName, x, y, z, yaw, pitch);
        this.ownerId = ownerId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Home home = (Home) o;
        return ownerId.equals(home.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ownerId);
    }

}
