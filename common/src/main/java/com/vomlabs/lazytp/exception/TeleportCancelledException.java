package com.vomlabs.lazytp.exception;

import java.util.UUID;

public class TeleportCancelledException extends RuntimeException {

    private final UUID playerId;
    private final String reason;

    public TeleportCancelledException(UUID playerId, String reason) {
        super("Teleport cancelled for " + playerId + ": " + reason);
        this.playerId = playerId;
        this.reason = reason;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getReason() {
        return reason;
    }

}
