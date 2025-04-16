package me.modify.portaportal.hook.essentials;

import com.earth2me.essentials.Essentials;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.util.PortaLogger;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EssentialsHandler {

    public static Location getEssentialsHome(Player player) {
        // Default location is SPAWN
        Location defaultDestination = player.getWorld().getSpawnLocation();

        PortaPortal plugin = PortaPortal.getInstance();
        Essentials api = (Essentials) plugin.getEssentialsHook().getAPI();
        IUser user = api.getUser(player.getUniqueId());

        if (user == null) {
            PortaLogger.error("Destination Teleport Fail - Essentials user could not be found " +
                    "[" + player.getUniqueId() + "]");
            return defaultDestination;
        }

        try {
            return user.getHome(user.getHomes().getFirst());
        } catch (Exception e) {
            PortaLogger.warning("Destination Teleport Warning - No home for user found. " +
                    "[" + player.getUniqueId() + "]");
            return defaultDestination;
        }
    }

}
