package org.shanguanling.serverhub.enums;

public enum MessageKey {

    PREFIX("prefix"),
    ONLY_PLAYER("only-player"),
    NO_PERMISSION("no-permission"),
    RELOAD_SUCCESS("reload-success"),
    IN_BLACKLIST("in-blacklist"),
    NO_LOBBY("no-lobby"),
    ALREADY_LOBBY("already-lobby"),
    SERVER_NOT_FOUND("server-not-found");

    private final String path;

    MessageKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
