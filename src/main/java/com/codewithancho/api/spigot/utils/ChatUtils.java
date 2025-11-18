package com.codewithancho.api.spigot.utils;

import org.bukkit.ChatColor;

public class ChatUtils {
    public static String colored(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
