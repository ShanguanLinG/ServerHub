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
import org.shanguanling.serverhub.util.CC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginCommand extends Command {

    private final ServerHub plugin;
    private final MessageManager messageManager;
    private final Map<String, ISubCommand> subCommands = new HashMap<>();

    public PluginCommand(ServerHub plugin) {
        super("hub", null, "lobby");
        this.plugin = plugin;
        this.messageManager = plugin.getMessageManager();
        addSubCommand(new ReloadCommand(plugin));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            sendPlayerToLobby(commandSender);
            return;
        }
        ISubCommand subcommand = subCommands.get(args[0].toLowerCase());
        if (subcommand == null) {
            commandSender.sendMessage(CC.translate("&cUnknown command."));
            return;
        }
        if (subcommand.getPermission() != null && !commandSender.hasPermission(subcommand.getPermission())) {
            commandSender.sendMessage(CC.translate(messageManager.getYamlStringWithPrefix(MessageKey.NO_PERMISSION.getPath())));
            return;
        }
        int length = args.length - 1;
        if (length < subcommand.getMinArgs() || (subcommand.getMaxArgs() != -1 && length > subcommand.getMaxArgs())) {
            commandSender.sendMessage(CC.translate("&cUsage: " + subcommand.getUsage()));
            return;
        }
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        subcommand.execute(commandSender, subArgs);
    }

    public void addSubCommand(ISubCommand subcommand) {
        subCommands.put(subcommand.getName().toLowerCase(), subcommand);
    }

    private void sendPlayerToLobby(CommandSender commandSender) {
        if (!(commandSender instanceof ProxiedPlayer player)) {
            commandSender.sendMessage(messageManager.getYamlStringWithPrefix(MessageKey.ONLY_PLAYER.getPath()));
            return;
        }
        String serverName = player.getServer().getInfo().getName();
        ConfigManager configManager = plugin.getConfigManager();
        if (configManager
                .getYamlStringList(ConfigKey.BLACKLIST_SERVERS.getPath())
                .stream()
                .anyMatch(s -> s.equals(serverName))) {
            player.sendMessage(messageManager.getYamlStringWithPrefix(MessageKey.IN_BLACKLIST.getPath()));
            return;
        }
        List<String> lobbyServers = configManager.getYamlStringList(ConfigKey.LOBBY_SERVERS.getPath());
        if (lobbyServers.isEmpty()) {
            player.sendMessage(messageManager.getYamlStringWithPrefix(MessageKey.NO_LOBBY.getPath()));
            return;
        }
        if (lobbyServers.contains(player.getServer().getInfo().getName())) {
            player.sendMessage(messageManager.getYamlStringWithPrefix(MessageKey.ALREADY_LOBBY.getPath()));
            return;
        }
        String randomLobbyServerName = configManager.getRandomYamlStringFromList(ConfigKey.LOBBY_SERVERS.getPath());
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(randomLobbyServerName);
        if (serverInfo == null) {
            player.sendMessage(messageManager.getYamlStringWithPrefix(MessageKey.SERVER_NOT_FOUND.getPath()));
            return;
        }
        player.connect(serverInfo);
    }
}