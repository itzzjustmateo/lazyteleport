package com.vomlabs.lazytp.config;

import com.vomlabs.lazytp.storage.YamlFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginConfig extends YamlFile {

    public PluginConfig(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    public int getTeleportDelay() {
        return config.getInt("teleport-delay", 3);
    }

    public int getTeleportCooldown() {
        return config.getInt("teleport-cooldown", 10);
    }

    public boolean isCancelOnMovement() {
        return config.getBoolean("cancel-on-movement", true);
    }

    public int getDefaultHomeLimit() {
        return config.getInt("default-home-limit", 5);
    }

    public String getSoundTeleportStart() {
        return config.getString("sounds.teleport-start", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getSoundTeleportCancel() {
        return config.getString("sounds.teleport-cancel", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundTeleportSuccess() {
        return config.getString("sounds.teleport-success", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getSoundWarpCreated() {
        return config.getString("sounds.warp-created", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getSoundWarpDeleted() {
        return config.getString("sounds.warp-deleted", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundWarpTeleport() {
        return config.getString("sounds.warp-teleport", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getSoundHomeCreated() {
        return config.getString("sounds.home-created", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getSoundHomeDeleted() {
        return config.getString("sounds.home-deleted", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundHomeTeleport() {
        return config.getString("sounds.home-teleport", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getSoundSpawnSet() {
        return config.getString("sounds.spawn-set", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getSoundSpawnDeleted() {
        return config.getString("sounds.spawn-deleted", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundSpawnTeleport() {
        return config.getString("sounds.spawn-teleport", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getSoundLobbySet() {
        return config.getString("sounds.lobby-set", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getSoundLobbyDeleted() {
        return config.getString("sounds.lobby-deleted", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundLobbyTeleport() {
        return config.getString("sounds.lobby-teleport", "ENTITY_ENDERMAN_TELEPORT");
    }

    public String getSoundError() {
        return config.getString("sounds.error", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getSoundHelpOpen() {
        return config.getString("sounds.help-open", "BLOCK_NOTE_BLOCK_PLING");
    }

    public String getParticleTeleport() {
        return config.getString("particles.teleport", "PORTAL");
    }

    public int getParticleCount() {
        return config.getInt("particles.teleport-count", 30);
    }

    public double getParticleSpeed() {
        return config.getDouble("particles.teleport-speed", 0.1);
    }

    public String getParticleWarpCreated() {
        return config.getString("particles.warp-created", "VILLAGER_HAPPY");
    }

    public String getParticleWarpDeleted() {
        return config.getString("particles.warp-deleted", "SMOKE");
    }

    public String getParticleHomeCreated() {
        return config.getString("particles.home-created", "VILLAGER_HAPPY");
    }

    public String getParticleHomeDeleted() {
        return config.getString("particles.home-deleted", "SMOKE");
    }

    public String getParticleSpawnSet() {
        return config.getString("particles.spawn-set", "VILLAGER_HAPPY");
    }

    public String getParticleSpawnDeleted() {
        return config.getString("particles.spawn-deleted", "SMOKE");
    }

    public String getParticleLobbySet() {
        return config.getString("particles.lobby-set", "VILLAGER_HAPPY");
    }

    public String getParticleLobbyDeleted() {
        return config.getString("particles.lobby-deleted", "SMOKE");
    }

    public int getGeneralParticleCount() {
        return config.getInt("particles.particle-count", 20);
    }

}
