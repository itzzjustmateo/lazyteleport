package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.manager.SpawnManager;
import com.vomlabs.lazytp.manager.TeleportManager;
import com.vomlabs.lazytp.model.NamedLocation;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpawnCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final SpawnManager spawnManager;
    private final TeleportManager teleportManager;

    public SpawnCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.spawnManager = lazyTeleport.getSpawnManager();
        this.teleportManager = lazyTeleport.getTeleportManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.SPAWN_USE)) {
            return true;
        }
        if (!spawnManager.isSet()) {
            MessageUtils.sendMessage(sender, messages.getSpawnNotSet());
            return true;
        }

        NamedLocation spawn = spawnManager.get();
        Location target = spawn.toLocation();
        if (target == null) {
            MessageUtils.sendMessage(sender, messages.getWorldNotFound());
            return true;
        }

        MessageUtils.sendMessage(sender, messages.getSpawnTeleporting());
        teleportManager.teleport(player, target, () -> {});

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

}
