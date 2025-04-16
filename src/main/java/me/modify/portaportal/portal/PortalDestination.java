package me.modify.portaportal.portal;

import com.earth2me.essentials.Essentials;
import lombok.Getter;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.util.PortaLogger;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PortalDestination {

    private Player player;
    private String portalDestination;

    public PortalDestination(Player player) {
        this.player = player;
        this.portalDestination = getDestinationType();
    }

    public void teleport() {
        player.teleport(getLocation());
        PortaLogger.info("Teleported to " + portalDestination);
    }

    private String getDestinationType() {
        PortaPortal plugin = PortaPortal.getInstance();
        FileConfiguration configFile = plugin.getConfigFile().getYaml();

        String destination = configFile.getString("portal.destination.type", "SPAWN");
        return destination.toUpperCase();
    }

    private Location getLocation() {
        switch (portalDestination) {
            case "HOME" -> {
                return getEssentialsHome();
            }
            case "BED" -> {
                return player.getBedLocation();
            }
            case "CUSTOM" -> {
                return readCustomDestinationConfig(); // use custom config location
            }

            // Default location is SPAWN
            default -> {
                return player.getWorld().getSpawnLocation();
            }
        }
    }

    private Location readCustomDestinationConfig() {
        PortaPortal plugin = PortaPortal.getInstance();
        FileConfiguration configFile = plugin.getConfigFile().getYaml();

        String defaultWorld = PortaPortal.getInstance().getServer().getWorlds().getFirst().getName();
        World world = plugin.getServer().getWorld(configFile.getString("portal.destination.custom.world", defaultWorld));

        double x = configFile.getDouble("portal.destination.custom.x", 0);
        double y = configFile.getDouble("portal.destination.custom.y", 0);
        double z = configFile.getDouble("portal.destination.custom.z", 0);

        float yaw = (float) configFile.getDouble("portal.destination.custom.yaw", 0);
        float pitch = (float) configFile.getDouble("portal.destination.custom.pitch", 0);
        return new Location(world, x, y, z, yaw, pitch);
    }

    private Location getEssentialsHome() {
        // Default location is SPAWN
        Location defaultDestination = player.getWorld().getSpawnLocation();

        PortaPortal plugin = PortaPortal.getInstance();
        if (!plugin.getEssentialsHook().isHooked()) {
            PortaLogger.error("Destination Teleport Fail - Essentials is not hooked " +
                    "[" + player.getUniqueId() + "]");
            return defaultDestination;
        }
        Essentials api = plugin.getEssentialsHook().getAPI();
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
