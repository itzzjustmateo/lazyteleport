package com.vomlabs.lazytp.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface SchedulerAdapter {

    Task runGlobal(Runnable runnable);

    Task runGlobalLater(Runnable runnable, long delayTicks);

    Task runGlobalTimer(Runnable runnable, long delayTicks, long periodTicks);

    Task runAtEntity(Entity entity, Runnable runnable);

    Task runAtEntityLater(Entity entity, Runnable runnable, long delayTicks);

    Task runAtLocation(Location location, Runnable runnable);

    Task runAtLocationLater(Location location, Runnable runnable, long delayTicks);

}
