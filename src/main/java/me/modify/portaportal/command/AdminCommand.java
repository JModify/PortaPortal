package me.modify.portaportal.command;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.portal.PortalDestinationRegistry;
import me.modify.portaportal.portal.PortalItem;
import me.modify.portaportal.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminCommand extends PortaPortalCommand {

    public AdminCommand() {
        super("portaportaladmin", "portaportal.admin", "ppa", "portaladmin", "ppadmin");
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        super.execute(commandSender, label, args);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                PortaPortal plugin = PortaPortal.getInstance();
                plugin.getConfigFile().reload();
                plugin.getMessageFile().reload();
                PortalDestinationRegistry.getInstance().clear();

                Messenger.sendMessage(commandSender, Messenger.Type.GENERAL, "reload-success");
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                printHelpMenu(commandSender);
                return true;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                String playerNameRaw = args[1];
                String amountRaw = args[2];

                int amount = -1;
                try {
                    amount = Integer.parseInt(amountRaw);
                } catch(NumberFormatException ignored) {}

                Player player = Bukkit.getPlayer(playerNameRaw);
                if (player == null) {
                    Messenger.sendMessage(commandSender, Messenger.Type.ERROR, "player-not-found");
                    return true;
                }

                if (amount == -1) {
                    Messenger.sendMessage(commandSender, Messenger.Type.ERROR, "invalid-quantity");
                    return true;
                }

                player.getInventory().addItem(new PortalItem());
                Map<String, String> giveItemPlaceholders = Map.of("%AMOUNT%", amountRaw, "%PLAYER%", playerNameRaw);
                Messenger.sendMessage(commandSender, Messenger.Type.GENERAL, "item-given", giveItemPlaceholders);
                return true;
            }
        }

        sendUsageMessage(commandSender, "/" + label + " <reload/give/help>");
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();
        if (!hasPermission(sender)) return completions;

        if (args.length == 1) {
            List<String> subCommands = List.of("reload", "give", "help");
            for (String sub : subCommands) {
                if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            completions.addAll(List.of("1", "16", "32", "64"));
        }

        return completions;
    }

    private void sendUsageMessage(CommandSender sender, String usageMessage) {
        Map<String, String> usagePlaceholder = Map.of("%USAGE%", usageMessage);
        Messenger.sendMessage(sender, Messenger.Type.ERROR, "invalid-usage", usagePlaceholder);
    }

    private void printHelpMenu(CommandSender commandSender) {
        List<String> helpMenu = new ArrayList<>();
        helpMenu.add(" ");
        helpMenu.add("&7&m-----&r &5&lPortal Admin Help &r&7&m-----");
        helpMenu.add("&5/ppa reload &f- &dReload plugin configurations.");
        helpMenu.add("&5/ppa help &f- &dDisplay this help menu in chat.");
        helpMenu.add("&5/ppa give <player> <amount> &f- &dGive portal item to player.");
        helpMenu.add("&7&m------------------------------");
        helpMenu.add(" ");
        Messenger.sendMessageList(commandSender, helpMenu);
    }
}
