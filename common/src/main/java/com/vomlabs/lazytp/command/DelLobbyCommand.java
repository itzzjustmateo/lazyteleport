package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.manager.LobbyManager;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelLobbyCommand extends BaseCommand implements CommandExecutor {

    private final LobbyManager lobbyManager;

    public DelLobbyCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.lobbyManager = lazyTeleport.getLobbyManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!hasPermission(sender, Permissions.LOBBY_DEL)) {
            return true;
        }

        lobbyManager.delete();
        MessageUtils.sendMessage(sender, messages.getLobbyDeleted());

        if (sender instanceof Player player) {
            playSuccess(player, config.getSoundLobbyDeleted(), config.getParticleLobbyDeleted());
        }

        return true;
    }

}
