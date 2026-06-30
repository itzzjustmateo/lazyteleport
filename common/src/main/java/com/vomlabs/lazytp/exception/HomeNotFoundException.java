package com.vomlabs.lazytp.exception;

import java.util.UUID;

public class HomeNotFoundException extends RuntimeException {

    private final UUID playerId;
    private final String homeName;

    public HomeNotFoundException(UUID playerId, String homeName) {
        super("Home not found: " + homeName + " for player " + playerId);
        this.playerId = playerId;
        this.homeName = homeName;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getHomeName() {
        return homeName;
    }

}
