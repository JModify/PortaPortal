package me.modify.portaportal.command;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.portal.PortalItem;
import me.modify.portaportal.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.sound.sampled.Port;
import java.util.HashMap;
import java.util.Map;

public class AdminCommand extends PortaPortalCommand {

    public AdminCommand() {
        super("portaportaladmin", "portaportal.admin", "ppa");
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        super.execute(commandSender, label, args);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                PortaPortal plugin = PortaPortal.getInstance();
                plugin.getConfigFile().reload();
                plugin.getMessageFile().reload();

                Messenger.sendMessage(commandSender, Messenger.Type.GENERAL, "reload-success");
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
                }

                player.getInventory().addItem(new PortalItem());
                Map<String, String> giveItemPlaceholders = Map.of("%AMOUNT%", amountRaw, "%PLAYER%", playerNameRaw);
                Messenger.sendMessage(commandSender, Messenger.Type.GENERAL, "item-given", giveItemPlaceholders);
                return true;
            }
        }

        sendUsageMessage(commandSender, "/" + label + " <reload/give/help>");
        return false;

//        if (commandSender instanceof Player player) {
//            if (args.length == 0) {
//                Messenger.sendMessage(player, "Please enter an argument", Messenger.Type.GENERAL);
//            }
//
//            if (args[0].equalsIgnoreCase("get")) {
//                player.getInventory().addItem(new PortalItem());
//            }
//
//            Messenger.sendMessage(player, "Unknown command. /" + label + " ge", Messenger.Type.GENERAL);
//
//            return false;
//        }
    }

    private void sendUsageMessage(CommandSender sender, String usageMessage) {
        Map<String, String> usagePlaceholder = Map.of("%USAGE%", usageMessage);
        Messenger.sendMessage(sender, Messenger.Type.ERROR, "invalid-usage", usagePlaceholder);
    }

    private void printMenu(CommandSender commandSender) {

    }
}
