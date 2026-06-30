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

public class SetLobbyCommand extends BaseCommand implements CommandExecutor {

    private final LobbyManager lobbyManager;

    public SetLobbyCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.lobbyManager = lazyTeleport.getLobbyManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!config.isLobbyEnabled()) {
            MessageUtils.sendMessage(sender, "<red>Lobby is disabled on this server!</red>");
            return true;
        }
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.LOBBY_SET)) {
            return true;
        }

        lobbyManager.set(player.getLocation());
        MessageUtils.sendMessage(sender, messages.getLobbySet());
        playSuccess(player, config.getSoundLobbySet(), config.getParticleLobbySet(), config.getLobbyParticleCount());

        return true;
    }

}
