package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.exception.WarpNotFoundException;
import com.vomlabs.lazytp.manager.WarpManager;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DelWarpCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final WarpManager warpManager;

    public DelWarpCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.warpManager = lazyTeleport.getWarpManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!config.isWarpsEnabled()) {
            MessageUtils.sendMessage(sender, "<red>Warps are disabled on this server!</red>");
            return true;
        }
        if (!hasPermission(sender, Permissions.WARP_DEL)) {
            return true;
        }
        if (args.length == 0) {
            MessageUtils.sendMessage(sender, messages.getWarpDelUsage());
            if (sender instanceof Player player) {
                playError(player);
            }
            return true;
        }

        String name = args[0];
        try {
            warpManager.delete(name);
            MessageUtils.sendMessage(sender, messages.getWarpDeleted(), "name", name);
            if (sender instanceof Player player) {
                playSuccess(player, config.getSoundWarpDeleted(), config.getParticleWarpDeleted(), config.getWarpParticleCount());
            }
        } catch (WarpNotFoundException e) {
            MessageUtils.sendMessage(sender, messages.getWarpNotFound(), "name", name);
            if (sender instanceof Player player) {
                playError(player);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission(Permissions.WARP_DEL) && config.isWarpsEnabled() && warpManager != null) {
            return warpManager.getNames().stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }

}
