package com.vomlabs.lazytp.model;

import org.bukkit.Location;

public class Warp extends NamedLocation {

    public Warp(String name, Location location) {
        super(name, location);
    }

    public Warp(String name, String worldName, double x, double y, double z, float yaw, float pitch) {
        super(name, worldName, x, y, z, yaw, pitch);
    }

}
