package me.modify.portaportal.util;

import me.modify.portaportal.PortaPortal;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;

public class Messenger {

    public static void sendMessage(CommandSender sender, String message, Type type) {
        sender.sendMessage(ColorFormat.format(type.getPrefix() + message));
    }

    public static void sendMessage(CommandSender sender, String message, Type type, Map<String, String> placeholders) {
        for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            message = message.replace(placeholder.getKey(), placeholder.getValue());
        }
        sender.sendMessage(ColorFormat.format(type.getPrefix() + message));
    }

    public static void sendMessage(CommandSender sender, Type type, String identifier) {
        FileConfiguration messagesFile = PortaPortal.getInstance().getMessageFile().getYaml();
        String message = messagesFile.getString(type.getConfigSectionId() + "." + identifier);
        if (message == null || message.isEmpty()) {
            PortaLogger.error("Message for " + identifier + " is empty. Delete messages.yml and/or reload the plugin.");
            return;
        }

        sendMessage(sender, message, type);
    }

    public static void sendMessage(CommandSender sender, Type type, String identifier, Map<String, String> placeholders) {
        FileConfiguration messagesFile = PortaPortal.getInstance().getMessageFile().getYaml();
        String message = messagesFile.getString(type.getConfigSectionId() + "." + identifier);
        if (message == null || message.isEmpty()) {
            PortaLogger.error("Message for " + identifier + " is empty. Delete messages.yml and/or reload the plugin.");
            return;
        }

        sendMessage(sender, message, type, placeholders);
    }

    public static void sendMessageList(CommandSender sender, List<String> messages) {
        for (String message : messages) {
            sender.sendMessage(ColorFormat.format(message));
        }
    }

    public enum Type {
        GENERAL, ERROR;

        public String getPrefix() {
            FileConfiguration messagesFile = PortaPortal.getInstance().getMessageFile().getYaml();
            switch (this) {
                case GENERAL:
                    return messagesFile.getString("prefix.general", "&5&lPortaPortal &7➤ ");

                case ERROR:
                    return messagesFile.getString("prefix.error", "&4&lERROR &7➤ ");
            }
            return null;
        }

        public String getConfigSectionId() {
            return this.name().toLowerCase();
        }
    }

}
