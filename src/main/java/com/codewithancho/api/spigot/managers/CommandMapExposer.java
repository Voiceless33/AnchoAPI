package com.codewithancho.api.spigot.managers;

import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public class CommandMapExposer {
    public static CommandMap getCommandMap(JavaPlugin plugin) {
        try {
            Method m = plugin.getServer().getClass().getDeclaredMethod("getCommandMap");
            m.setAccessible(true);
            return (CommandMap) m.invoke(plugin.getServer());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
