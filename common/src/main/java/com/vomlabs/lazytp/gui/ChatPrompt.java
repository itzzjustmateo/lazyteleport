package com.vomlabs.lazytp.gui;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatPrompt implements Listener {

    private final LazyTeleportPlugin plugin;
    private final Map<UUID, Callback> pending = new ConcurrentHashMap<>();

    public ChatPrompt(LazyTeleportPlugin plugin) {
        this.plugin = plugin;
    }

    public void await(Player player, String prompt, Callback callback) {
        pending.put(player.getUniqueId(), callback);
        player.sendMessage(prompt);
    }

    public void cancel(Player player) {
        pending.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Callback callback = pending.get(event.getPlayer().getUniqueId());
        if (callback == null) return;
        event.setCancelled(true);
        pending.remove(event.getPlayer().getUniqueId());
        callback.onInput(event.getPlayer(), event.getMessage().trim());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        pending.remove(event.getPlayer().getUniqueId());
    }

    @FunctionalInterface
    public interface Callback {
        void onInput(Player player, String input);
    }

}
