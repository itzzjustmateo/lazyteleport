package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.exception.InvalidNameException;
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

public class SetWarpCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final WarpManager warpManager;

    public SetWarpCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.warpManager = lazyTeleport.getWarpManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!config.isWarpsEnabled()) {
            MessageUtils.sendMessage(sender, "<red>Warps are disabled on this server!</red>");
            return true;
        }
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.WARP_SET)) {
            return true;
        }
        if (args.length == 0) {
            MessageUtils.sendMessage(sender, messages.getWarpNameRequired());
            playError(player);
            return true;
        }

        String name = args[0];
        try {
            warpManager.create(name, player.getLocation());
            MessageUtils.sendMessage(sender, messages.getWarpCreated(), "name", name);
            playSuccess(player, config.getSoundWarpCreated(), config.getParticleWarpCreated(), config.getWarpParticleCount());
        } catch (InvalidNameException e) {
            if (e.getMessage().contains("already exists")) {
                MessageUtils.sendMessage(sender, messages.getWarpAlreadyExists(), "name", name);
            } else {
                MessageUtils.sendMessage(sender, messages.getInvalidName());
            }
            playError(player);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

}
