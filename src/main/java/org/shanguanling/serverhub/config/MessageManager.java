package org.shanguanling.serverhub.config;

import net.md_5.bungee.api.plugin.Plugin;
import org.shanguanling.serverhub.enums.MessageKey;
import org.shanguanling.serverhub.util.CC;

public class MessageManager extends AbstractYamlManager {

    public MessageManager(Plugin plugin) {
        super(plugin, "message.yml");
    }

    @Override
    public String getYamlString(String path) {
        if (!getYaml().contains(path)) {
            return "<Missing message: " + path + ">";
        }
        String message = getYaml().getString(path);
        String prefix = getYaml().getString(MessageKey.PREFIX.getPath());
        prefix = prefix == null ? "<Prefix not exist> " : prefix;
        return CC.translate(prefix + message);
    }
}