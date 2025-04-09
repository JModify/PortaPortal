package me.modify.portaportal.command;

import me.modify.portaportal.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;

public abstract class PortaPortalCommand extends BukkitCommand {

    public PortaPortalCommand(String name, String node, String... aliases) {
        super(name);
        setPermission(node);
        setAliases(List.of(aliases));
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!label.equalsIgnoreCase(getName())) {
            return false;
        }

        if (getAliases().stream().noneMatch(alias -> alias.equals(label))) {
            return false;
        }

        if (!hasPermission(sender)) {
            Messenger.sendMessage(sender, "Insufficient permission.", Messenger.Type.ERROR);
            return false;
        }

        return true;
    }

    private boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(Objects.requireNonNull(getPermission()));
    }

    /**
     * Register command in the command map to avoid listing commands in the plugin.yml
     * @param command command to register
     */
    public static void registerCommand(BukkitCommand command) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(command.getName(), command);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
