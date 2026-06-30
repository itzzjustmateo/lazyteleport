package com.vomlabs.lazytp.scheduler;

public interface Task {

    void cancel();

    boolean isCancelled();

}
