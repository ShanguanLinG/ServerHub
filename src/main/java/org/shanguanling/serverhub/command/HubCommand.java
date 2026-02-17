package org.shanguanling.serverhub.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.shanguanling.serverhub.ServerHub;
import org.shanguanling.serverhub.config.ConfigManager;

import java.util.List;
import java.util.Random;

public class HubCommand extends Command {

    private final ServerHub plugin;
    private final Random random = new Random();

    public HubCommand(ServerHub plugin) {
        super("hub", null, "lobby");
        this.plugin = plugin;
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
            commandSender.sendMessage(plugin.getMessageManager().get("only-player"));
            return;
        }
        String serverName = player.getServer().getInfo().getName();
        ConfigManager configManager = plugin.getConfigManager();
        if (configManager.getBlackListServers().stream().anyMatch(s -> s.equals(serverName))) {
            player.sendMessage(plugin.getMessageManager().get("in-blacklist"));
            return;
        }
        List<String> lobbyServers = configManager.getLobbyServers();
        if (lobbyServers.isEmpty()) {
            player.sendMessage(plugin.getMessageManager().get("no-lobby"));
            return;
        }
        if (lobbyServers.contains(player.getServer().getInfo().getName())) {
            player.sendMessage(plugin.getMessageManager().get("already-lobby"));
            return;
        }
        String lobbyServerName = lobbyServers.get(random.nextInt(lobbyServers.size()));
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(lobbyServerName);
        if (serverInfo == null) {
            player.sendMessage(plugin.getMessageManager().get("server-not-found"));
            return;
        }
        player.connect(serverInfo);
    }

    private void reloadConfig(String arg, CommandSender commandSender) {
        if (!commandSender.hasPermission("serverhub.reload")) {
            commandSender.sendMessage(plugin.getMessageManager().get("no-permission"));
            return;
        }
        if ("reload".equalsIgnoreCase(arg)) {
            plugin.getConfigManager().reload();
            plugin.getMessageManager().reload();
            commandSender.sendMessage(plugin.getMessageManager().get("reload-success"));
        }
    }
}
