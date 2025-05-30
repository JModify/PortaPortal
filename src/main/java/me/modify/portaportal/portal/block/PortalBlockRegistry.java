package me.modify.portaportal.portal.block;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.hook.essentials.EssentialsHandler;
import me.modify.portaportal.util.PortaLogger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PortalBlockRegistry {

    private static PortalBlockRegistry instance;

    // Key = portal block location
    // Value = player owning portal block
    private Map<Location, PortalBlockData> registry;

    private PortalBlockRegistry() {
        registry = new HashMap<>();
    }

    public static PortalBlockRegistry getInstance() {
        if (instance == null) {
            instance = new PortalBlockRegistry();
        }
        return instance;
    }

    public PortalBlockData get(Location location) {
        for (Location portalLocation : registry.keySet()) {
            int portalX = portalLocation.getBlockX();
            int portalY = portalLocation.getBlockY();
            int portalZ = portalLocation.getBlockZ();

            if (portalX == location.getBlockX()
                    && portalY == location.getBlockY()
                    && portalZ == location.getBlockZ()) {
                return registry.get(portalLocation);
            }
        }

        return null;
    }

    public void add(Location location, Player player) {
        FileConfiguration configFile = PortaPortal.getInstance().getConfigFile().getYaml();
        Destination destination = Destination.valueOf(configFile.getString("portal.destination.type",
                Destination.SPAWN.name()).toUpperCase());

        PortalBlockData blockData = new PortalBlockData(player.getUniqueId(), destination, getLocation(player, destination));

        registry.put(location, blockData);
    }

    public void remove(Location location) {
        registry.remove(location);
    }

    public void removeByPlayer(UUID playerId) {
        registry.entrySet().removeIf(entry -> entry.getValue().playerId().equals(playerId));
    }

    public boolean isCloseToPortal(Location location) {
        FileConfiguration fileConfiguration = PortaPortal.getInstance().getConfigFile().getYaml();
        int portalSpacing = fileConfiguration.getInt("portal.portal-spacing", 20);

        for (Location portalLocation : registry.keySet()) {
            if (portalLocation.getWorld() == null) {
                continue;
            }

            if (!portalLocation.getWorld().equals(location.getWorld())) {
                continue;
            }

            if (portalLocation.distance(location) <= portalSpacing) {
                return true;
            }
        }
        return false;
    }

    private Location getLocation(Player player, Destination destination) {
        Location defaultDestination = player.getWorld().getSpawnLocation();
        switch (destination) {
            case HOME -> {
                PortaPortal plugin = PortaPortal.getInstance();
                if (!plugin.getEssentialsHook().isHooked()) {
                    PortaLogger.error("Destination Failure - Essentials is not hooked " +
                            "[" + player.getUniqueId() + "]");
                    return defaultDestination;
                }

                return EssentialsHandler.getEssentialsHome(player);
            }
            case BED -> {
                // If bed respawn point is not set
                Location bedSpawnLocation = player.getBedSpawnLocation();
                if (bedSpawnLocation == null) {
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

//    public void teleport() {
//        player.teleport(getLocation());
//        Map<String, String> teleportPlaceholders = Map.of("%DESTINATION%", portalDestination.name());
//        Messenger.sendMessage(player, Messenger.Type.GENERAL, "portal-teleport", teleportPlaceholders);
//    }
}
