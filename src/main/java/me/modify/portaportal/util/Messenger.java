package me.modify.portaportal.util;

import me.modify.portaportal.PortaPortal;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

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
        sendMessage(sender, message, type);
    }

    public static void sendMessage(CommandSender sender, Type type, String identifier, Map<String, String> placeholders) {
        FileConfiguration messagesFile = PortaPortal.getInstance().getMessageFile().getYaml();
        String message = messagesFile.getString(type.getConfigSectionId() + "." + identifier);
        sendMessage(sender, message, type, placeholders);
    }

    public static void sendMessageList(CommandSender sender, Type type, String... messages) {
        for (String message : messages) {
            sendMessage(sender, message, type);
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
