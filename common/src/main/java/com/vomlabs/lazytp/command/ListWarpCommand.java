package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.manager.WarpManager;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListWarpCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final WarpManager warpManager;

    public ListWarpCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.warpManager = lazyTeleport.getWarpManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!hasPermission(sender, Permissions.WARP_LIST)) {
            return true;
        }

        List<String> names = warpManager.getNames().stream().sorted().toList();

        if (names.isEmpty()) {
            MessageUtils.sendMessage(sender, messages.getWarpListEmpty());
            return true;
        }

        MessageUtils.sendMessage(sender, messages.getWarpListHeader());
        for (String name : names) {
            MessageUtils.sendMessage(sender, messages.getWarpListFormat(), "name", name);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

}
