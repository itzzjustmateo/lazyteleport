package com.vomlabs.lazytp.paper;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperBootstrap extends JavaPlugin {

    private LazyTeleportPlugin lazyTeleport;

    @Override
    public void onEnable() {
        lazyTeleport = new LazyTeleportPlugin(this, new PaperSchedulerAdapter(this));
        lazyTeleport.enable();
    }

    @Override
    public void onDisable() {
        if (lazyTeleport != null) {
            lazyTeleport.disable();
        }
    }

}
