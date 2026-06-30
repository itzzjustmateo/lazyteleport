package com.vomlabs.lazytp.paper;

import com.vomlabs.lazytp.scheduler.SchedulerAdapter;
import com.vomlabs.lazytp.scheduler.Task;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperSchedulerAdapter implements SchedulerAdapter {

    private final JavaPlugin plugin;

    public PaperSchedulerAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task runGlobal(Runnable runnable) {
        return new PaperTask(plugin.getServer().getScheduler().runTask(plugin, runnable));
    }

    @Override
    public Task runGlobalLater(Runnable runnable, long delayTicks) {
        return new PaperTask(plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delayTicks));
    }

    @Override
    public Task runGlobalTimer(Runnable runnable, long delayTicks, long periodTicks) {
        return new PaperTask(plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks));
    }

    @Override
    public Task runAtEntity(Entity entity, Runnable runnable) {
        return new PaperTask(plugin.getServer().getScheduler().runTask(plugin, runnable));
    }

    @Override
    public Task runAtEntityLater(Entity entity, Runnable runnable, long delayTicks) {
        return new PaperTask(plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delayTicks));
    }

    @Override
    public Task runAtLocation(Location location, Runnable runnable) {
        return new PaperTask(plugin.getServer().getScheduler().runTask(plugin, runnable));
    }

    @Override
    public Task runAtLocationLater(Location location, Runnable runnable, long delayTicks) {
        return new PaperTask(plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delayTicks));
    }

}
