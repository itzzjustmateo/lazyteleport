package com.vomlabs.lazytp.folia;

import com.vomlabs.lazytp.scheduler.SchedulerAdapter;
import com.vomlabs.lazytp.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class FoliaSchedulerAdapter implements SchedulerAdapter {

    private final JavaPlugin plugin;

    public FoliaSchedulerAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task runGlobal(Runnable runnable) {
        return new FoliaTask(Bukkit.getGlobalRegionScheduler().run(plugin, scheduledTask -> runnable.run()));
    }

    @Override
    public Task runGlobalLater(Runnable runnable, long delayTicks) {
        return new FoliaTask(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), delayTicks));
    }

    @Override
    public Task runGlobalTimer(Runnable runnable, long delayTicks, long periodTicks) {
        return new FoliaTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), delayTicks, periodTicks));
    }

    @Override
    public Task runAtEntity(Entity entity, Runnable runnable) {
        return new FoliaTask(entity.getScheduler().run(plugin, scheduledTask -> runnable.run(), () -> {}));
    }

    @Override
    public Task runAtEntityLater(Entity entity, Runnable runnable, long delayTicks) {
        return new FoliaTask(entity.getScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), () -> {}, delayTicks));
    }

    @Override
    public Task runAtLocation(Location location, Runnable runnable) {
        return new FoliaTask(Bukkit.getRegionScheduler().run(plugin, location, scheduledTask -> runnable.run()));
    }

    @Override
    public Task runAtLocationLater(Location location, Runnable runnable, long delayTicks) {
        return new FoliaTask(Bukkit.getRegionScheduler().runDelayed(plugin, location, scheduledTask -> runnable.run(), delayTicks));
    }

}
