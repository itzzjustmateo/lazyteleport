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

public class DelSpawnCommand extends BaseCommand implements CommandExecutor {

    private final SpawnManager spawnManager;

    public DelSpawnCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.spawnManager = lazyTeleport.getSpawnManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!hasPermission(sender, Permissions.SPAWN_DEL)) {
            return true;
        }

        spawnManager.delete();
        MessageUtils.sendMessage(sender, messages.getSpawnDeleted());

        if (sender instanceof Player player) {
            playSuccess(player, config.getSoundSpawnDeleted(), config.getParticleSpawnDeleted());
        }

        return true;
    }

}
