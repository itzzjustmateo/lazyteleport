package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.exception.InvalidNameException;
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

public class SetHomeCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private final HomeManager homeManager;

    public SetHomeCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.homeManager = lazyTeleport.getHomeManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!config.isHomesEnabled()) {
            MessageUtils.sendMessage(sender, "<red>Homes are disabled on this server!</red>");
            return true;
        }
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.HOME_SET)) {
            return true;
        }

        String homeName = args.length == 0 ? "home" : args[0];

        try {
            homeManager.create(player, homeName, player.getLocation());
            MessageUtils.sendMessage(sender, messages.getHomeCreated(), "name", homeName);
            playSuccess(player, config.getSoundHomeCreated(), config.getParticleHomeCreated(), config.getHomeParticleCount());
        } catch (InvalidNameException e) {
            if (e.getMessage().contains("already exists")) {
                MessageUtils.sendMessage(sender, messages.getHomeAlreadyExists(), "name", homeName);
            } else {
                MessageUtils.sendMessage(sender, messages.getInvalidName());
            }
            playError(player);
        } catch (IllegalStateException e) {
            MessageUtils.sendMessage(sender, messages.getHomeLimitReached());
            playError(player);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

}
