package com.codewithancho.api.spigot.base;

import com.codewithancho.api.spigot.managers.CommandBuilder;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Command extends org.bukkit.command.Command {
    private final CommandExecutor executor;

    public Command(
            String name,
            String description,
            String permission,
            String usageMessage,
            List<String> aliases,
            CommandExecutor executor
    ) {
        super(name);

        this.executor = executor;

        if (description != null) setDescription(description);
        if (permission != null) setPermission(permission);
        if (aliases != null) setAliases(aliases);
        if (usageMessage != null) setUsage(usageMessage);
    }


    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return executor.onCommand(sender, this, label, args);
    }
}