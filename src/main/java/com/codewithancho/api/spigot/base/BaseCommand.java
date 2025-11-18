package com.codewithancho.api.spigot.base;

import org.bukkit.command.CommandSender;

public abstract class BaseCommand {
    public abstract void execute(CommandSender sender, String[] args);
}
