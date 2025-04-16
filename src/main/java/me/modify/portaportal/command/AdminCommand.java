package me.modify.portaportal.command;

import me.modify.portaportal.portal.PortalItem;
import me.modify.portaportal.util.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand extends PortaPortalCommand {

    public AdminCommand() {
        super("portaportaladmin", "portaportal.admin", "ppa");
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        super.execute(commandSender, label, args);

        if (commandSender instanceof Player player) {
            if (args.length == 0) {
                Messenger.sendMessage(player, "Please enter an argument", Messenger.Type.GENERAL);
            }

            if (args[0].equalsIgnoreCase("revert")) {
                //PortalSchematic.revertPastes();
            } else if (args[0].equalsIgnoreCase("get")) {
                player.getInventory().addItem(new PortalItem());
            }

            Messenger.sendMessage(player, "Unknown command. /" + label + " ge", Messenger.Type.GENERAL);

            return false;
        }

        return false;
    }
}
