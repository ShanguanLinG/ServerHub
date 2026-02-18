package org.shanguanling.serverhub.config;

import net.md_5.bungee.api.plugin.Plugin;
import org.shanguanling.serverhub.enums.MessageKey;
import org.shanguanling.serverhub.util.CC;

public class MessageManager extends AbstractYamlManager {

    public MessageManager(Plugin plugin) {
        super(plugin, "message.yml");
    }

    public String getPrefix() {
        return getYaml().getString(MessageKey.PREFIX.getPath());
    }

    @Override
    public String getYamlStringWithPrefix(String path) {
        if (!getYaml().contains(path)) {
            return "<Missing message: " + path + ">";
        }
        String message = getYaml().getString(path);
        String prefix = getPrefix() == null ? "<Prefix not exist> " : getPrefix();
        return CC.translate(prefix + message);
    }
}