package com.vomlabs.lazytp;

import com.vomlabs.lazytp.config.MessageConfig;
import com.vomlabs.lazytp.config.PluginConfig;
import com.vomlabs.lazytp.listener.TeleportListener;
import com.vomlabs.lazytp.manager.HomeManager;
import com.vomlabs.lazytp.manager.LobbyManager;
import com.vomlabs.lazytp.manager.SpawnManager;
import com.vomlabs.lazytp.manager.TeleportManager;
import com.vomlabs.lazytp.manager.WarpManager;
import com.vomlabs.lazytp.scheduler.SchedulerAdapter;
import com.vomlabs.lazytp.storage.HomeStorage;
import com.vomlabs.lazytp.storage.LobbyStorage;
import com.vomlabs.lazytp.storage.SpawnStorage;
import com.vomlabs.lazytp.storage.WarpStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LazyTeleportPlugin {

    private final JavaPlugin plugin;
    private final SchedulerAdapter scheduler;

    private PluginConfig pluginConfig;
    private MessageConfig messageConfig;

    private WarpStorage warpStorage;
    private HomeStorage homeStorage;
    private SpawnStorage spawnStorage;
    private LobbyStorage lobbyStorage;

    private WarpManager warpManager;
    private HomeManager homeManager;
    private SpawnManager spawnManager;
    private LobbyManager lobbyManager;
    private TeleportManager teleportManager;

    public LazyTeleportPlugin(JavaPlugin plugin, SchedulerAdapter scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    public void enable() {
        loadConfigs();
        loadStorages();
        createManagers();
        registerListeners();
        registerCommands();
    }

    public void disable() {
        if (teleportManager != null) {
            teleportManager.cancelAll();
        }
    }

    public void reload() {
        pluginConfig.load();
        messageConfig.load();
        warpStorage.load();
        homeStorage.load();
        spawnStorage.load();
        lobbyStorage.load();
    }

    private void loadConfigs() {
        pluginConfig = new PluginConfig(plugin);
        pluginConfig.load();

        messageConfig = new MessageConfig(plugin);
        messageConfig.load();
    }

    private void loadStorages() {
        warpStorage = new WarpStorage(plugin);
        warpStorage.load();

        homeStorage = new HomeStorage(plugin);
        homeStorage.load();

        spawnStorage = new SpawnStorage(plugin);
        spawnStorage.load();

        lobbyStorage = new LobbyStorage(plugin);
        lobbyStorage.load();
    }

    private void createManagers() {
        teleportManager = new TeleportManager(plugin, pluginConfig, messageConfig, scheduler);
        warpManager = new WarpManager(warpStorage);
        homeManager = new HomeManager(homeStorage, pluginConfig);
        spawnManager = new SpawnManager(spawnStorage);
        lobbyManager = new LobbyManager(lobbyStorage);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new TeleportListener(teleportManager), plugin);
    }

    private void registerCommands() {
        CommandRegistrar registrar = new CommandRegistrar(plugin, this);
        registrar.registerAll();
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public SchedulerAdapter getScheduler() {
        return scheduler;
    }

}
