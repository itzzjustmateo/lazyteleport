package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.exception.HomeNotFoundException;
import com.vomlabs.lazytp.manager.HomeManager;
import com.vomlabs.lazytp.manager.TeleportManager;
import com.vomlabs.lazytp.model.Home;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final HomeManager homeManager;
    private final TeleportManager teleportManager;

    public HomeCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.homeManager = lazyTeleport.getHomeManager();
        this.teleportManager = lazyTeleport.getTeleportManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.HOME_USE)) {
            return true;
        }

        String homeName = args.length == 0 ? "home" : args[0];

        try {
            Home home = homeManager.get(player.getUniqueId(), homeName);
            MessageUtils.sendMessage(sender, messages.getHomeTeleporting(), "name", home.getName());
            teleportManager.teleport(player, home.toLocation(), () -> {});
        } catch (HomeNotFoundException e) {
            MessageUtils.sendMessage(sender, messages.getHomeNotFound(), "name", homeName);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender instanceof Player player && sender.hasPermission(Permissions.HOME_USE)) {
            return homeManager.getNamesByOwner(player.getUniqueId()).stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }

}
