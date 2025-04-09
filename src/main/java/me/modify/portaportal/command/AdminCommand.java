package me.modify.portaportal.command;

import me.modify.portaportal.item.HomePortal;
import me.modify.portaportal.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
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

            player.getInventory().addItem(new HomePortal());
        }

        return false;
    }
}
