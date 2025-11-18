package com.codewithancho.api.spigot.managers;

import com.codewithancho.api.core.utils.ReflectionUtils;
import com.codewithancho.api.spigot.annotations.*;
import com.codewithancho.api.spigot.base.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CommandManager {

    JavaPlugin plugin;
    private final CommandMap commandMap;

    public CommandManager(JavaPlugin plugin, CommandMap commandMap) {
        this.plugin = plugin;
        this.commandMap = commandMap;
    }

    public void loadCommands (String commandsPackage) throws ClassNotFoundException {
        ReflectionUtils.getClasses(commandsPackage).forEach(clazz -> {
            if (!CommandExecutor.class.isAssignableFrom(clazz)) return;
            if (!clazz.isAnnotationPresent(CommandName.class)) return;
            if (!clazz.isAnnotationPresent(CommandPermission.class)) return;

            try {
                CommandName commandName = clazz.getAnnotation(CommandName.class);
                CommandDescription commandDescription = clazz.getAnnotation(CommandDescription.class);
                CommandUsage commandUsage = clazz.getAnnotation(CommandUsage.class);
                CommandPermission commandPermission = clazz.getAnnotation(CommandPermission.class);
                CommandAliases commandAliases = clazz.getAnnotation(CommandAliases.class);

                CommandExecutor executor = (CommandExecutor) clazz.getDeclaredConstructor().newInstance();

                Command dynamic = new CommandBuilder(commandName.value())
                        .description(commandDescription != null ? commandDescription.value() : null)
                                .permission(commandPermission.value())
                                        .usage(commandUsage != null ? commandUsage.value() : null)
                                                .aliases(commandAliases != null ? List.of(commandAliases.value()) : null)
                                                        .executor(executor)
                                                                .build();

                commandMap.register(plugin.getName(), dynamic);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
