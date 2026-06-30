package com.vomlabs.lazytp.exception;

public class WarpNotFoundException extends RuntimeException {

    private final String warpName;

    public WarpNotFoundException(String warpName) {
        super("Warp not found: " + warpName);
        this.warpName = warpName;
    }

    public String getWarpName() {
        return warpName;
    }

}
