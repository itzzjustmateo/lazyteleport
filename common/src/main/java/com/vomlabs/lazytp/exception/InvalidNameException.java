package com.vomlabs.lazytp.exception;

public class InvalidNameException extends IllegalArgumentException {

    private final String name;
    private final String reason;

    public InvalidNameException(String name, String reason) {
        super("Invalid name '" + name + "': " + reason);
        this.name = name;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

}
