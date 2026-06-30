package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
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

public class ListHomeCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final HomeManager homeManager;

    public ListHomeCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.homeManager = lazyTeleport.getHomeManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.HOME_LIST)) {
            return true;
        }

        List<String> names = homeManager.getNamesByOwner(player.getUniqueId()).stream().sorted().toList();

        if (names.isEmpty()) {
            MessageUtils.sendMessage(sender, messages.getHomeListEmpty());
            return true;
        }

        MessageUtils.sendMessage(sender, messages.getHomeListHeader());
        for (String name : names) {
            MessageUtils.sendMessage(sender, messages.getHomeListFormat(), "name", name);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

}
