package com.vomlabs.lazytp.paper;

import com.vomlabs.lazytp.scheduler.Task;
import org.bukkit.scheduler.BukkitTask;

public class PaperTask implements Task {

    private final BukkitTask task;

    public PaperTask(BukkitTask task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

}
