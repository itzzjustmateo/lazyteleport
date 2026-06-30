package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.exception.HomeNotFoundException;
import com.vomlabs.lazytp.manager.HomeManager;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DelHomeCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final HomeManager homeManager;

    public DelHomeCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.homeManager = lazyTeleport.getHomeManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.HOME_DEL)) {
            return true;
        }
        if (args.length == 0) {
            MessageUtils.sendMessage(sender, messages.getHomeDelUsage());
            playError(player);
            return true;
        }

        String homeName = args[0];
        try {
            homeManager.delete(player.getUniqueId(), homeName);
            MessageUtils.sendMessage(sender, messages.getHomeDeleted(), "name", homeName);
            playSuccess(player, config.getSoundHomeDeleted(), config.getParticleHomeDeleted());
        } catch (HomeNotFoundException e) {
            MessageUtils.sendMessage(sender, messages.getHomeNotFound(), "name", homeName);
            playError(player);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender instanceof Player player && sender.hasPermission(Permissions.HOME_DEL)) {
            return homeManager.getNamesByOwner(player.getUniqueId()).stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }

}
