package org.shanguanling.serverhub.config;

import net.md_5.bungee.api.plugin.Plugin;

public class ConfigManager extends AbstractYamlManager {

    public ConfigManager(Plugin plugin) {
        super(plugin, "config.yml");
    }
}