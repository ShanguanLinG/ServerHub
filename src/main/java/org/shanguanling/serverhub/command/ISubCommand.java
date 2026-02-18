package org.shanguanling.serverhub.command;

import net.md_5.bungee.api.CommandSender;

public interface ISubCommand {

    String getName();

    String getPermission();

    String getUsage();

    int getMinArgs();

    int getMaxArgs();

    void execute(CommandSender sender, String[] args);
}