package me.modify.portaportal.portal.destination;

import lombok.Setter;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.hook.essentials.EssentialsHandler;
import me.modify.portaportal.util.Messenger;
import me.modify.portaportal.util.PortaLogger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

public class PortalDestinationHandler {

    private Player player;

    @Setter
    private Destination portalDestination;

    public PortalDestinationHandler(Player player) {
        this.player = player;
        this.portalDestination = getDestinationType();
    }

    public void teleport() {
        player.teleport(getLocation());
        Map<String, String> teleportPlaceholders = Map.of("%DESTINATION%", portalDestination.name());
        Messenger.sendMessage(player, Messenger.Type.GENERAL, "portal-teleport", teleportPlaceholders);
    }

    private Destination getDestinationType() {
        return PortalDestinationRegistry.getInstance().getDestination(player.getUniqueId());
    }

    private Location getLocation() {
        Location defaultDestination = player.getWorld().getSpawnLocation();
        switch (portalDestination) {
            case HOME -> {
                PortaPortal plugin = PortaPortal.getInstance();
                if (!plugin.getEssentialsHook().isHooked()) {
                    PortaLogger.error("Destination Teleport Fail - Essentials is not hooked " +
                            "[" + player.getUniqueId() + "]");
                    return defaultDestination;
                }

                return EssentialsHandler.getEssentialsHome(player);
            }
            case BED -> {
                // If bed respawn point is not set
                Location bedSpawnLocation = player.getBedSpawnLocation();
                if (bedSpawnLocation == null) {
                    setPortalDestination(Destination.SPAWN);
                    return defaultDestination;
                }
                return bedSpawnLocation;
            }
            case CUSTOM -> {
                return readCustomDestinationConfig(); // use custom config location
            }

            // Default location is SPAWN
            default -> {
                return defaultDestination;
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
}
