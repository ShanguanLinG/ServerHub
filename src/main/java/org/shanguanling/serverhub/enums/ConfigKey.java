package org.shanguanling.serverhub.enums;

public enum ConfigKey {

    LOBBY_SERVERS("lobby-servers"),
    BLACKLIST_SERVERS("blacklist-servers");

    private final String path;

    ConfigKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}