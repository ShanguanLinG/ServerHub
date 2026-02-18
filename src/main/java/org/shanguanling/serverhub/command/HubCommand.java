package org.shanguanling.serverhub.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.shanguanling.serverhub.ServerHub;
import org.shanguanling.serverhub.config.ConfigManager;
import org.shanguanling.serverhub.config.MessageManager;
import org.shanguanling.serverhub.enums.ConfigKey;
import org.shanguanling.serverhub.enums.MessageKey;

import java.util.List;

public class HubCommand extends Command {

    private final ServerHub plugin;
    private final MessageManager messageManager;

    public HubCommand(ServerHub plugin) {
        super("hub", null, "lobby");
        this.plugin = plugin;
        this.messageManager = plugin.getMessageManager();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        switch (args.length) {
            case 0:
                sendPlayerToLobby(commandSender);
                break;
            case 1:
                reloadConfig(args[0], commandSender);
                break;
            default:
                break;
        }
    }

    private void sendPlayerToLobby(CommandSender commandSender) {
        if (!(commandSender instanceof ProxiedPlayer player)) {
            commandSender.sendMessage(messageManager.getYamlString(MessageKey.ONLY_PLAYER.getPath()));
            return;
        }
        String serverName = player.getServer().getInfo().getName();
        ConfigManager configManager = plugin.getConfigManager();
        if (configManager
                .getYamlStringList(ConfigKey.BLACKLIST_SERVERS.getPath())
                .stream()
                .anyMatch(s -> s.equals(serverName))) {
            player.sendMessage(messageManager.getYamlString(MessageKey.IN_BLACKLIST.getPath()));
            return;
        }
        List<String> lobbyServers = configManager.getYamlStringList(ConfigKey.LOBBY_SERVERS.getPath());
        if (lobbyServers.isEmpty()) {
            player.sendMessage(messageManager.getYamlString(MessageKey.NO_LOBBY.getPath()));
            return;
        }
        if (lobbyServers.contains(player.getServer().getInfo().getName())) {
            player.sendMessage(messageManager.getYamlString(MessageKey.ALREADY_LOBBY.getPath()));
            return;
        }
        String randomLobbyServerName = configManager.getRandomYamlStringFromList(ConfigKey.LOBBY_SERVERS.getPath());
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(randomLobbyServerName);
        if (serverInfo == null) {
            player.sendMessage(messageManager.getYamlString(MessageKey.SERVER_NOT_FOUND.getPath()));
            return;
        }
        player.connect(serverInfo);
    }

    private void reloadConfig(String arg, CommandSender commandSender) {
        if (!commandSender.hasPermission("serverhub.reload")) {
            commandSender.sendMessage(messageManager.getYamlString(MessageKey.NO_PERMISSION.getPath()));
            return;
        }
        if ("reload".equalsIgnoreCase(arg)) {
            plugin.getConfigManager().reload();
            plugin.getMessageManager().reload();
            commandSender.sendMessage(messageManager.getYamlString(MessageKey.RELOAD_SUCCESS.getPath()));
        }
    }
}