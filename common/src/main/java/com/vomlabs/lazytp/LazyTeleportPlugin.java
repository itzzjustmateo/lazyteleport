package com.vomlabs.lazytp;

import com.vomlabs.lazytp.config.MessageConfig;
import com.vomlabs.lazytp.config.PluginConfig;
import com.vomlabs.lazytp.gui.ChatPrompt;
import com.vomlabs.lazytp.gui.GUIListener;
import com.vomlabs.lazytp.hook.PlaceholderAPIHook;
import com.vomlabs.lazytp.hook.VaultHook;
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
import com.vomlabs.lazytp.util.MessageUtils;
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

    private VaultHook vaultHook;
    private PlaceholderAPIHook papiHook;
    private ChatPrompt chatPrompt;

    public LazyTeleportPlugin(JavaPlugin plugin, SchedulerAdapter scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    public void enable() {
        loadConfigs();
        initHooks();
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
        if (pluginConfig.isWarpsEnabled()) {
            warpStorage.load();
        }
        if (pluginConfig.isHomesEnabled()) {
            homeStorage.load();
        }
        if (pluginConfig.isSpawnEnabled()) {
            spawnStorage.load();
        }
        if (pluginConfig.isLobbyEnabled()) {
            lobbyStorage.load();
        }
    }

    private void loadConfigs() {
        pluginConfig = new PluginConfig(plugin);
        pluginConfig.load();

        messageConfig = new MessageConfig(plugin);
        messageConfig.load();
    }

    private void loadStorages() {
        if (pluginConfig.isWarpsEnabled()) {
            warpStorage = new WarpStorage(plugin);
            warpStorage.load();
        }
        if (pluginConfig.isHomesEnabled()) {
            homeStorage = new HomeStorage(plugin);
            homeStorage.load();
        }
        if (pluginConfig.isSpawnEnabled()) {
            spawnStorage = new SpawnStorage(plugin);
            spawnStorage.load();
        }
        if (pluginConfig.isLobbyEnabled()) {
            lobbyStorage = new LobbyStorage(plugin);
            lobbyStorage.load();
        }
    }

    private void createManagers() {
        teleportManager = new TeleportManager(pluginConfig, messageConfig, scheduler);
        if (pluginConfig.isWarpsEnabled() && warpStorage != null) {
            warpManager = new WarpManager(warpStorage);
        }
        if (pluginConfig.isHomesEnabled() && homeStorage != null) {
            homeManager = new HomeManager(homeStorage, pluginConfig);
        }
        if (pluginConfig.isSpawnEnabled() && spawnStorage != null) {
            spawnManager = new SpawnManager(spawnStorage);
        }
        if (pluginConfig.isLobbyEnabled() && lobbyStorage != null) {
            lobbyManager = new LobbyManager(lobbyStorage);
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new TeleportListener(teleportManager), plugin);
        Bukkit.getPluginManager().registerEvents(new GUIListener(), plugin);
        chatPrompt = new ChatPrompt(this);
        Bukkit.getPluginManager().registerEvents(chatPrompt, plugin);
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

    private void initHooks() {
        vaultHook = new VaultHook();
        boolean vaultReady = vaultHook.setup();
        plugin.getLogger().info("Vault " + (vaultReady ? "hooked" : "not available"));

        boolean papiReady = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        MessageUtils.setPapiAvailable(papiReady);
        if (papiReady) {
            papiHook = new PlaceholderAPIHook(this);
            papiHook.register();
            plugin.getLogger().info("PlaceholderAPI expansion registered");
        }
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }

    public boolean hasEconomy() {
        return vaultHook != null && vaultHook.isEnabled();
    }

    public ChatPrompt getChatPrompt() {
        return chatPrompt;
    }

}
