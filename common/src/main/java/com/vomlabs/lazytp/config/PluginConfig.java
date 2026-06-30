package com.vomlabs.lazytp.config;

import com.vomlabs.lazytp.storage.YamlFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginConfig extends YamlFile {

    public PluginConfig(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    public int getTeleportDelay() {
        return config.getInt("teleportation.delay", 3);
    }

    public int getTeleportCooldown() {
        return config.getInt("teleportation.cooldown", 10);
    }

    public boolean isCancelOnMovement() {
        return config.getBoolean("teleportation.cancel-on-movement", true);
    }

    public boolean isHomesEnabled() {
        return config.getBoolean("homes.enabled", true);
    }

    public boolean isWarpsEnabled() {
        return config.getBoolean("warps.enabled", true);
    }

    public boolean isSpawnEnabled() {
        return config.getBoolean("spawn.enabled", true);
    }

    public boolean isLobbyEnabled() {
        return config.getBoolean("lobby.enabled", true);
    }

    public int getDefaultHomeLimit() {
        return config.getInt("homes.default-limit", 5);
    }

    public String getSoundTeleportStart() {
        return config.getString("teleportation.effects.sound-start", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getSoundTeleportCancel() {
        return config.getString("teleportation.effects.sound-cancel", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundTeleportSuccess() {
        return config.getString("teleportation.effects.sound-success", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getParticleTeleport() {
        return config.getString("teleportation.effects.particle", "PORTAL");
    }

    public int getParticleCount() {
        return config.getInt("teleportation.effects.particle-count", 30);
    }

    public double getParticleSpeed() {
        return config.getDouble("teleportation.effects.particle-speed", 0.1);
    }

    public String getSoundWarpCreated() {
        return config.getString("warps.sounds.created", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getSoundWarpDeleted() {
        return config.getString("warps.sounds.deleted", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundWarpTeleport() {
        return config.getString("warps.sounds.teleport", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getParticleWarpCreated() {
        return config.getString("warps.particles.created", "VILLAGER_HAPPY");
    }

    public String getParticleWarpDeleted() {
        return config.getString("warps.particles.deleted", "SMOKE");
    }

    public int getWarpParticleCount() {
        return config.getInt("warps.particles.count", 20);
    }

    public String getSoundHomeCreated() {
        return config.getString("homes.sounds.created", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getSoundHomeDeleted() {
        return config.getString("homes.sounds.deleted", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundHomeTeleport() {
        return config.getString("homes.sounds.teleport", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getParticleHomeCreated() {
        return config.getString("homes.particles.created", "VILLAGER_HAPPY");
    }

    public String getParticleHomeDeleted() {
        return config.getString("homes.particles.deleted", "SMOKE");
    }

    public int getHomeParticleCount() {
        return config.getInt("homes.particles.count", 20);
    }

    public String getSoundSpawnSet() {
        return config.getString("spawn.sounds.set", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getSoundSpawnDeleted() {
        return config.getString("spawn.sounds.deleted", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundSpawnTeleport() {
        return config.getString("spawn.sounds.teleport", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getParticleSpawnSet() {
        return config.getString("spawn.particles.set", "VILLAGER_HAPPY");
    }

    public String getParticleSpawnDeleted() {
        return config.getString("spawn.particles.deleted", "SMOKE");
    }

    public int getSpawnParticleCount() {
        return config.getInt("spawn.particles.count", 20);
    }

    public String getSoundLobbySet() {
        return config.getString("lobby.sounds.set", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getSoundLobbyDeleted() {
        return config.getString("lobby.sounds.deleted", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundLobbyTeleport() {
        return config.getString("lobby.sounds.teleport", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getParticleLobbySet() {
        return config.getString("lobby.particles.set", "VILLAGER_HAPPY");
    }

    public String getParticleLobbyDeleted() {
        return config.getString("lobby.particles.deleted", "SMOKE");
    }

    public int getLobbyParticleCount() {
        return config.getInt("lobby.particles.count", 20);
    }

    public String getSoundError() {
        return config.getString("system.sounds.error", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundHelpOpen() {
        return config.getString("system.sounds.help-open", "BLOCK_NOTE_BLOCK_PLING");
    }

    public boolean isEconomyEnabled() {
        return config.getBoolean("economy.enabled", true);
    }

    public double getWarpCost() {
        return config.getDouble("economy.costs.warp", 10.0);
    }

    public double getHomeCost() {
        return config.getDouble("economy.costs.home", 5.0);
    }

    public double getSpawnCost() {
        return config.getDouble("economy.costs.spawn", 0.0);
    }

    public double getLobbyCost() {
        return config.getDouble("economy.costs.lobby", 0.0);
    }

}
