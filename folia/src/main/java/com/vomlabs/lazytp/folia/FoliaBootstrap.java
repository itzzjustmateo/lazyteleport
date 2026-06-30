package com.vomlabs.lazytp.folia;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FoliaBootstrap extends JavaPlugin {

    private LazyTeleportPlugin lazyTeleport;

    @Override
    public void onEnable() {
        lazyTeleport = new LazyTeleportPlugin(this, new FoliaSchedulerAdapter(this));
        lazyTeleport.enable();
    }

    @Override
    public void onDisable() {
        if (lazyTeleport != null) {
            lazyTeleport.disable();
        }
    }

}
