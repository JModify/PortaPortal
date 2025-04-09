package me.modify.portaportal.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messenger {

    private static String GENERAL_PREFIX = "&7[&r&a&lPortaPortal&r&7] ";
    private static String ERROR_PREFIX = "&c&lERROR ";

    public static void sendMessage(CommandSender sender, String message, Type type) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', type.getPrefix() + message));
    }

    public static void sendMessageList(CommandSender sender, Type type, String... messages) {
        for (String message : messages) {
            sendMessage(sender, message, type);
        }
    }

    public enum Type {

        GENERAL, ERROR;

        private String GENERAL_PREFIX = "&7[&r&a&lPortaPortal&r&7] ";
        private String ERROR_PREFIX = "&c&lERROR ";

        public String getPrefix() {
            return switch (this) {
                case GENERAL -> GENERAL_PREFIX;
                case ERROR -> ERROR_PREFIX;
            };
        }

    }

}
