package com.vomlabs.lazytp.hook;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final LazyTeleportPlugin plugin;

    public PlaceholderAPIHook(LazyTeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lazyteleport";
    }

    @Override
    public @NotNull String getAuthor() {
        return "VomLabs";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPlugin().getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        return switch (params) {
            case "warps_count" -> String.valueOf(plugin.getWarpManager() != null ? plugin.getWarpManager().getNames().size() : 0);
            case "homes_count" -> {
                if (offlinePlayer instanceof Player player && plugin.getHomeManager() != null) {
                    yield String.valueOf(plugin.getHomeManager().getNamesByOwner(player.getUniqueId()).size());
                }
                yield "0";
            }
            case "homes_limit" -> {
                if (offlinePlayer instanceof Player player) {
                    yield String.valueOf(plugin.getPluginConfig().getDefaultHomeLimit());
                }
                yield "0";
            }
            case "has_spawn" -> String.valueOf(plugin.getSpawnManager() != null && plugin.getSpawnManager().isSet());
            case "has_lobby" -> String.valueOf(plugin.getLobbyManager() != null && plugin.getLobbyManager().isSet());
            default -> null;
        };
    }

}
