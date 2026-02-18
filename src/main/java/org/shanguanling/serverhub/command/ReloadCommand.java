package org.shanguanling.serverhub.command;

import net.md_5.bungee.api.CommandSender;
import org.shanguanling.serverhub.ServerHub;
import org.shanguanling.serverhub.config.ConfigManager;
import org.shanguanling.serverhub.config.MessageManager;
import org.shanguanling.serverhub.enums.MessageKey;

public class ReloadCommand implements ISubCommand {

    private final ConfigManager configManager;
    private final MessageManager messageManager;

    public ReloadCommand(ServerHub plugin) {
        this.configManager = plugin.getConfigManager();
        this.messageManager = plugin.getMessageManager();
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "serverhub.reload";
    }

    @Override
    public String getUsage() {
        return "/hub reload";
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public int getMaxArgs() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        configManager.reload();
        messageManager.reload();
        sender.sendMessage(messageManager.getYamlStringWithPrefix(MessageKey.RELOAD_SUCCESS.getPath()));
    }
}