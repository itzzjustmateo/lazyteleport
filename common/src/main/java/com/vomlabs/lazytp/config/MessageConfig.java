package com.vomlabs.lazytp.config;

import com.vomlabs.lazytp.storage.YamlFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig extends YamlFile {

    public MessageConfig(JavaPlugin plugin) {
        super(plugin, "messages.yml");
    }

    public String getPrefix() {
        return config.getString("prefix", "<gradient:gold:yellow>LazyTeleport</gradient> <gray>»</gray>");
    }

    public String getWarpCreated() {
        return getPrefix() + " " + config.getString("warp.created", "<green>Warp <name> created!</green>");
    }

    public String getWarpDeleted() {
        return getPrefix() + " " + config.getString("warp.deleted", "<green>Warp <name> deleted!</green>");
    }

    public String getWarpNotFound() {
        return getPrefix() + " " + config.getString("warp.not-found", "<red>Warp <name> not found!</red>");
    }

    public String getWarpTeleporting() {
        return getPrefix() + " " + config.getString("warp.teleporting", "<yellow>Teleporting to warp <name>...</yellow>");
    }

    public String getWarpListHeader() {
        return config.getString("warp.list-header", "<gold>Warps:</gold>");
    }

    public String getWarpListFormat() {
        return config.getString("warp.list-format", "  <gray>-</gray> <white><name></white>");
    }

    public String getWarpListEmpty() {
        return getPrefix() + " " + config.getString("warp.no-warps", "<red>No warps have been set!</red>");
    }

    public String getWarpUsage() {
        return getPrefix() + " " + config.getString("warp.usage", "<red>Usage: /warp <name></red>");
    }

    public String getWarpAlreadyExists() {
        return getPrefix() + " " + config.getString("warp.already-exists", "<red>Warp <name> already exists!</red>");
    }

    public String getWarpNameRequired() {
        return getPrefix() + " " + config.getString("warp.name-required", "<red>Usage: /setwarp <name></red>");
    }

    public String getWarpDelUsage() {
        return getPrefix() + " " + config.getString("warp.del-usage", "<red>Usage: /delwarp <name></red>");
    }

    public String getHomeCreated() {
        return getPrefix() + " " + config.getString("home.created", "<green>Home <name> created!</green>");
    }

    public String getHomeDeleted() {
        return getPrefix() + " " + config.getString("home.deleted", "<green>Home <name> deleted!</green>");
    }

    public String getHomeNotFound() {
        return getPrefix() + " " + config.getString("home.not-found", "<red>Home <name> not found!</red>");
    }

    public String getHomeTeleporting() {
        return getPrefix() + " " + config.getString("home.teleporting", "<yellow>Teleporting to home <name>...</yellow>");
    }

    public String getHomeListHeader() {
        return config.getString("home.list-header", "<gold>Your Homes:</gold>");
    }

    public String getHomeListFormat() {
        return config.getString("home.list-format", "  <gray>-</gray> <white><name></white>");
    }

    public String getHomeListEmpty() {
        return getPrefix() + " " + config.getString("home.no-homes", "<red>You have no homes set!</red>");
    }

    public String getHomeUsage() {
        return getPrefix() + " " + config.getString("home.usage", "<red>Usage: /home [name]</red>");
    }

    public String getHomeSetUsage() {
        return getPrefix() + " " + config.getString("home.set-usage", "<red>Usage: /sethome [name]</red>");
    }

    public String getHomeDelUsage() {
        return getPrefix() + " " + config.getString("home.del-usage", "<red>Usage: /delhome <name></red>");
    }

    public String getHomeLimitReached() {
        return getPrefix() + " " + config.getString("home.limit-reached", "<red>You have reached the maximum number of homes!</red>");
    }

    public String getHomeAlreadyExists() {
        return getPrefix() + " " + config.getString("home.already-exists", "<red>Home <name> already exists!</red>");
    }

    public String getSpawnTeleporting() {
        return getPrefix() + " " + config.getString("spawn.teleporting", "<yellow>Teleporting to spawn...</yellow>");
    }

    public String getSpawnSet() {
        return getPrefix() + " " + config.getString("spawn.set", "<green>Spawn location set!</green>");
    }

    public String getSpawnDeleted() {
        return getPrefix() + " " + config.getString("spawn.deleted", "<green>Spawn location deleted!</green>");
    }

    public String getSpawnNotSet() {
        return getPrefix() + " " + config.getString("spawn.not-set", "<red>Spawn location has not been set!</red>");
    }

    public String getLobbyTeleporting() {
        return getPrefix() + " " + config.getString("lobby.teleporting", "<yellow>Teleporting to lobby...</yellow>");
    }

    public String getLobbySet() {
        return getPrefix() + " " + config.getString("lobby.set", "<green>Lobby location set!</green>");
    }

    public String getLobbyDeleted() {
        return getPrefix() + " " + config.getString("lobby.deleted", "<green>Lobby location deleted!</green>");
    }

    public String getLobbyNotSet() {
        return getPrefix() + " " + config.getString("lobby.not-set", "<red>Lobby location has not been set!</red>");
    }

    public String getTeleportCancelled() {
        return getPrefix() + " " + config.getString("teleport.cancelled", "<red>Teleport cancelled!</red>");
    }

    public String getTeleportCooldown() {
        return getPrefix() + " " + config.getString("teleport.cooldown", "<red>Please wait <seconds> seconds before teleporting again.</red>");
    }

    public String getTeleportCountdown() {
        return config.getString("teleport.countdown", "<yellow>Teleporting in <seconds> seconds... Do not move!</yellow>");
    }

    public String getTeleportSuccess() {
        return getPrefix() + " " + config.getString("teleport.success", "<green>Teleported successfully!</green>");
    }

    public String getTeleportFailed() {
        return getPrefix() + " " + config.getString("teleport.failed", "<red>Teleport failed!</red>");
    }

    public String getWorldNotFound() {
        return getPrefix() + " " + config.getString("general.world-not-found", "<red>World not found!</red>");
    }

    public String getPlayerOnly() {
        return getPrefix() + " " + config.getString("general.player-only", "<red>This command can only be used by players!</red>");
    }

    public String getNoPermission() {
        return getPrefix() + " " + config.getString("general.no-permission", "<red>You do not have permission to use this command!</red>");
    }

    public String getInvalidName() {
        return getPrefix() + " " + config.getString("general.invalid-name", "<red>Invalid name! Use only letters, numbers, and underscores.</red>");
    }

}
