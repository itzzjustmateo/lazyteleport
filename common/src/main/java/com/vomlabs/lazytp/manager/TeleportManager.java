package com.vomlabs.lazytp.manager;

import com.vomlabs.lazytp.config.MessageConfig;
import com.vomlabs.lazytp.config.PluginConfig;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.scheduler.SchedulerAdapter;
import com.vomlabs.lazytp.scheduler.Task;
import com.vomlabs.lazytp.util.MessageUtils;
import com.vomlabs.lazytp.util.ParticleUtils;
import com.vomlabs.lazytp.util.SoundUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportManager {

    private final PluginConfig config;
    private final MessageConfig messages;
    private final SchedulerAdapter scheduler;
    private final Map<UUID, TeleportRequest> pendingTeleports = new ConcurrentHashMap<>();
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    public TeleportManager(PluginConfig config, MessageConfig messages, SchedulerAdapter scheduler) {
        this.config = config;
        this.messages = messages;
        this.scheduler = scheduler;
    }

    public void teleport(Player player, Location target, Runnable onSuccess) {
        UUID playerId = player.getUniqueId();

        if (isOnCooldown(player)) {
            long remaining = (cooldowns.get(playerId) + config.getTeleportCooldown() * 1000L - System.currentTimeMillis()) / 1000;
            MessageUtils.sendMessage(player, messages.getTeleportCooldown(), "seconds", String.valueOf(remaining + 1));
            return;
        }

        if (config.getTeleportDelay() <= 0 || player.hasPermission(Permissions.BYPASS_DELAY)) {
            executeTeleport(player, target, onSuccess);
            return;
        }

        Location startLocation = player.getLocation().clone();
        int delaySeconds = config.getTeleportDelay();
        TeleportRequest request = new TeleportRequest(playerId, startLocation, delaySeconds);
        pendingTeleports.put(playerId, request);

        SoundUtils.playSound(player, config.getSoundTeleportStart());

        Task[] taskRef = new Task[1];
        Task task = scheduler.runGlobalTimer(new Runnable() {
            int remaining = delaySeconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancelTeleport(player, "player offline");
                    return;
                }

                if (!pendingTeleports.containsKey(playerId)) {
                    return;
                }

                if (config.isCancelOnMovement() && hasMoved(player, startLocation)) {
                    cancelTeleport(player, "movement");
                    return;
                }

                if (remaining > 0) {
                    MessageUtils.sendMessage(player, messages.getTeleportCountdown(), "seconds", String.valueOf(remaining));
                }

                remaining--;

                if (remaining < 0) {
                    taskRef[0].cancel();
                    pendingTeleports.remove(playerId);
                    executeTeleport(player, target, onSuccess);
                }
            }
        }, 0L, 20L);
        taskRef[0] = task;

        request.setTask(task);
    }

    public void cancelTeleport(Player player) {
        cancelTeleport(player, "cancelled");
    }

    private void cancelTeleport(Player player, String reason) {
        UUID playerId = player.getUniqueId();
        TeleportRequest request = pendingTeleports.remove(playerId);
        if (request != null) {
            request.getTask().cancel();
            SoundUtils.playSound(player, config.getSoundTeleportCancel());
            MessageUtils.sendMessage(player, messages.getTeleportCancelled());
        }
    }

    public boolean isPending(Player player) {
        return pendingTeleports.containsKey(player.getUniqueId());
    }

    private void executeTeleport(Player player, Location target, Runnable onSuccess) {
        setCooldown(player);
        Location location = player.getLocation();

        scheduler.runAtEntity(player, () -> {
            player.teleportAsync(target).thenAccept(result -> {
                if (result) {
                    ParticleUtils.spawnParticle(location, config.getParticleTeleport(), config.getParticleCount(), config.getParticleSpeed());
                    SoundUtils.playSound(location, config.getSoundTeleportSuccess());
                    MessageUtils.sendMessage(player, messages.getTeleportSuccess());
                    onSuccess.run();
                } else {
                    MessageUtils.sendMessage(player, messages.getTeleportFailed());
                }
            });
        });
    }

    private boolean hasMoved(Player player, Location from) {
        Location current = player.getLocation();
        return from.getBlockX() != current.getBlockX()
                || from.getBlockY() != current.getBlockY()
                || from.getBlockZ() != current.getBlockZ();
    }

    private boolean isOnCooldown(Player player) {
        if (player.hasPermission(Permissions.BYPASS_COOLDOWN)) {
            return false;
        }
        Long cooldownUntil = cooldowns.get(player.getUniqueId());
        if (cooldownUntil == null) {
            return false;
        }
        return System.currentTimeMillis() < cooldownUntil;
    }

    private void setCooldown(Player player) {
        if (player.hasPermission(Permissions.BYPASS_COOLDOWN)) {
            return;
        }
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + config.getTeleportCooldown() * 1000L);
    }

    public void cancelAll() {
        for (TeleportRequest request : pendingTeleports.values()) {
            request.getTask().cancel();
        }
        pendingTeleports.clear();
        cooldowns.clear();
    }

    private static class TeleportRequest {

        private final UUID playerId;
        private final Location startLocation;
        private final int delaySeconds;
        private Task task;

        TeleportRequest(UUID playerId, Location startLocation, int delaySeconds) {
            this.playerId = playerId;
            this.startLocation = startLocation;
            this.delaySeconds = delaySeconds;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

    }

}
