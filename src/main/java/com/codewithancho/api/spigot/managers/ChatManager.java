package com.codewithancho.api.spigot.managers;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatManager {

    List<String> badWords = new ArrayList<>();

    JavaPlugin plugin;
    private Logger logger;

    public ChatManager(JavaPlugin plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();

        plugin.getConfig().getList("bad-words").forEach(badWord -> {
            badWords.add(badWord.toString());
        });
    }

    public boolean isBadWordPresent(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        for (String badWord : badWords) {
            if(!message.contains(badWord)) {
                return true;
            }
        }

        return false;
    }

    public static String colored(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
