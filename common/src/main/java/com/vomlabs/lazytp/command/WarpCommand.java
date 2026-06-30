package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.exception.WarpNotFoundException;
import com.vomlabs.lazytp.manager.TeleportManager;
import com.vomlabs.lazytp.manager.WarpManager;
import com.vomlabs.lazytp.model.Warp;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WarpCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final WarpManager warpManager;
    private final TeleportManager teleportManager;

    public WarpCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.warpManager = lazyTeleport.getWarpManager();
        this.teleportManager = lazyTeleport.getTeleportManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.WARP_USE)) {
            return true;
        }
        if (args.length == 0) {
            MessageUtils.sendMessage(sender, messages.getWarpUsage());
            return true;
        }

        String warpName = args[0];
        try {
            Warp warp = warpManager.get(warpName);
            MessageUtils.sendMessage(sender, messages.getWarpTeleporting(), "name", warp.getName());
            teleportManager.teleport(player, warp.toLocation(), () -> {});
        } catch (WarpNotFoundException e) {
            MessageUtils.sendMessage(sender, messages.getWarpNotFound(), "name", warpName);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission(Permissions.WARP_USE)) {
            return warpManager.getNames().stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }

}
