package com.vomlabs.lazytp.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class ParticleUtils {

    private ParticleUtils() {
    }

    public static void spawnParticle(Player player, String particleName, int count, double speed) {
        if (particleName == null || particleName.isEmpty() || particleName.equalsIgnoreCase("none")) {
            return;
        }
        try {
            Particle particle = Particle.valueOf(particleName);
            Location location = player.getLocation();
            player.spawnParticle(particle, location, count, 0.5, 0.5, 0.5, speed);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public static void spawnParticle(Location location, String particleName, int count, double speed) {
        if (particleName == null || particleName.isEmpty() || particleName.equalsIgnoreCase("none")) {
            return;
        }
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        try {
            Particle particle = Particle.valueOf(particleName);
            world.spawnParticle(particle, location, count, 0.5, 0.5, 0.5, speed);
        } catch (IllegalArgumentException ignored) {
        }
    }

}
