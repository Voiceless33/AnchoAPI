package com.codewithancho.api.spigot.builders;

import com.codewithancho.api.spigot.base.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.inventory.ItemRarity;

import java.util.List;

public class CommandBuilder {
    private final String name;
    private String description;
    private String permission;
    private String usage;
    private List<String> aliases;
    private CommandExecutor executor;

    public CommandBuilder(String name) {
        this.name = name;
    }

    public CommandBuilder description(String desc) {
        this.description = desc;
        return this;
    }

    public CommandBuilder permission(String perm) {
        this.permission = perm;
        return this;
    }

    public CommandBuilder usage(String usage) {
        this.usage = usage;
        return this;
    }

    public CommandBuilder aliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public CommandBuilder executor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public Command build() {
        return new Command(name, description, permission, usage, aliases, executor);
    }
}