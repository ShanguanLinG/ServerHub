package org.shanguanling.serverhub.config;

import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class ConfigManager extends AbstractYamlManager {

    public ConfigManager(Plugin plugin) {
        super(plugin, "config.yml");
    }

    public List<String> getLobbyServers() {
        if (getConfig().contains("lobby-servers")) {
            return getConfig().getStringList("lobby-servers");
        }
        return Collections.emptyList();
    }

    public List<String> getBlackListServers() {
        if (getConfig().contains("blacklist-servers")) {
            return getConfig().getStringList("blacklist-servers");
        }
        return Collections.emptyList();
    }
}
