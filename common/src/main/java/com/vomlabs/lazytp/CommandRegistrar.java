package com.vomlabs.lazytp;

import com.vomlabs.lazytp.command.DelHomeCommand;
import com.vomlabs.lazytp.command.DelLobbyCommand;
import com.vomlabs.lazytp.command.DelSpawnCommand;
import com.vomlabs.lazytp.command.DelWarpCommand;
import com.vomlabs.lazytp.command.HomeCommand;
import com.vomlabs.lazytp.command.LazytpCommand;
import com.vomlabs.lazytp.command.ListHomeCommand;
import com.vomlabs.lazytp.command.ListWarpCommand;
import com.vomlabs.lazytp.command.LobbyCommand;
import com.vomlabs.lazytp.command.SetHomeCommand;
import com.vomlabs.lazytp.command.SetLobbyCommand;
import com.vomlabs.lazytp.command.SetSpawnCommand;
import com.vomlabs.lazytp.command.SetWarpCommand;
import com.vomlabs.lazytp.command.SpawnCommand;
import com.vomlabs.lazytp.command.WarpCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegistrar {

    private final JavaPlugin plugin;
    private final LazyTeleportPlugin lazyTeleport;

    public CommandRegistrar(JavaPlugin plugin, LazyTeleportPlugin lazyTeleport) {
        this.plugin = plugin;
        this.lazyTeleport = lazyTeleport;
    }

    public void registerAll() {
        LazytpCommand lazytpCommand = new LazytpCommand(lazyTeleport);
        register("lazytp", lazytpCommand, true, true);
        register("warp", new WarpCommand(lazyTeleport), true, true);
        register("setwarp", new SetWarpCommand(lazyTeleport), false, true);
        register("delwarp", new DelWarpCommand(lazyTeleport), false, true);
        register("listwarp", new ListWarpCommand(lazyTeleport), false, true);
        register("home", new HomeCommand(lazyTeleport), true, true);
        register("sethome", new SetHomeCommand(lazyTeleport), false, true);
        register("delhome", new DelHomeCommand(lazyTeleport), false, true);
        register("listhome", new ListHomeCommand(lazyTeleport), false, true);
        register("spawn", new SpawnCommand(lazyTeleport), true, false);
        register("setspawn", new SetSpawnCommand(lazyTeleport), false, false);
        register("delspawn", new DelSpawnCommand(lazyTeleport), false, false);
        register("lobby", new LobbyCommand(lazyTeleport), true, false);
        register("setlobby", new SetLobbyCommand(lazyTeleport), false, false);
        register("dellobby", new DelLobbyCommand(lazyTeleport), false, false);
    }

    private void register(String name, Object executor, boolean tabComplete, boolean executorInstance) {
        PluginCommand command = plugin.getCommand(name);
        if (command == null) {
            plugin.getLogger().warning("Command /" + name + " is not defined in plugin.yml");
            return;
        }
        if (executor instanceof org.bukkit.command.CommandExecutor cmdExec) {
            command.setExecutor(cmdExec);
        }
        if (tabComplete && executor instanceof org.bukkit.command.TabCompleter completer) {
            command.setTabCompleter(completer);
        }
    }

}
