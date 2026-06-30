package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.manager.SpawnManager;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand extends BaseCommand implements CommandExecutor {

    private final SpawnManager spawnManager;

    public SetSpawnCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.spawnManager = lazyTeleport.getSpawnManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!config.isSpawnEnabled()) {
            MessageUtils.sendMessage(sender, "<red>Spawn is disabled on this server!</red>");
            return true;
        }
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }
        if (!hasPermission(sender, Permissions.SPAWN_SET)) {
            return true;
        }

        spawnManager.set(player.getLocation());
        MessageUtils.sendMessage(sender, messages.getSpawnSet());
        playSuccess(player, config.getSoundSpawnSet(), config.getParticleSpawnSet(), config.getSpawnParticleCount());

        return true;
    }

}
