package org.shanguanling.serverhub.config;

import net.md_5.bungee.api.plugin.Plugin;
import org.shanguanling.serverhub.util.CC;

public class MessageManager extends AbstractYamlManager {

    public MessageManager(Plugin plugin) {
        super(plugin, "message.yml");
    }

    public String get(String path) {
        if (!getConfig().contains(path)) {
            return "Missing message: " + path;
        }
        String message = getConfig().getString(path);
        String prefix = getConfig().getString("prefix");
        return CC.translate(prefix + message);
    }
}
