package com.codewithancho.api.spigot.managers;

import com.codewithancho.api.spigot.annotations.Command;
import com.codewithancho.api.spigot.base.BaseCommand;
import com.codewithancho.api.spigot.utils.ChatUtils;
import com.codewithancho.api.core.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

public class CommandManager {

    private final JavaPlugin plugin;
    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static String MISSING_PERMISSION = ChatUtils.colored("&cYou do not have permission to use this command.");

    /**
     *
     * @param packageName Point to the package where all the annotated commands are stored.
     */
    public void registerAnnotatedCommands(String packageName) {

        try {
            Set<Class<?>> classes = ReflectionUtils.getClasses(packageName);
            for (Class<?> clazz : classes) {
                if(!clazz.isAnnotation()) continue;

                Command cmdData = clazz.getAnnotation(Command.class);

                if(!BaseCommand.class.isAssignableFrom(clazz)) {
                    Bukkit.getLogger().warning("[Core] " + clazz.getName() + " does not extend BaseCommand!");
                    continue;
                }

                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);

                BaseCommand cmdInstance = (BaseCommand) constructor.newInstance();

                registerCommand(cmdData, cmdInstance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param data Pass the data from the loaded Annotated command
     * @param executor What to execute when the Command runs
     */
    private void registerCommand (Command data, BaseCommand executor) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            BukkitCommand command = new BukkitCommand(data.name()) {
                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {

                    if(data.playerOnly() && !(sender instanceof Player)){
                        sender.sendMessage("Only players can execute this command!");
                        return true;
                    }

                    if(!data.permission().isEmpty() && !sender.hasPermission(data.permission())){
                        sender.sendMessage(MISSING_PERMISSION);
                        return true;
                    }

                    executor.execute(sender, args);
                    return false;
                }
            };

            command.setAliases(Arrays.asList(data.aliases()));
            command.setPermission(data.permission());
            commandMap.register(plugin.getName(), command);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}