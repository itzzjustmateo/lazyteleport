package com.vomlabs.lazytp.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import com.vomlabs.lazytp.scheduler.Task;

public class FoliaTask implements Task {

    private final ScheduledTask task;

    public FoliaTask(ScheduledTask task) {
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
