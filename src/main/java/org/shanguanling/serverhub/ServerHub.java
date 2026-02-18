package org.shanguanling.serverhub;

import net.md_5.bungee.api.plugin.Plugin;
import org.shanguanling.serverhub.command.PluginCommand;
import org.shanguanling.serverhub.config.ConfigManager;
import org.shanguanling.serverhub.config.MessageManager;

public final class ServerHub extends Plugin {

    ConfigManager configManager;
    MessageManager messageManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        messageManager = new MessageManager(this);
        getProxy().getPluginManager().registerCommand(this, new PluginCommand(this));
    }

    @Override
    public void onDisable() {
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}