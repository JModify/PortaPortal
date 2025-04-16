package me.modify.portaportal.portal;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PortalBlockRegistry {

    private static PortalBlockRegistry instance;

    // Key = portal block location
    // Value = player owning portal block
    private Map<Location, UUID> registry;

    private PortalBlockRegistry() {
        registry = new HashMap<>();
    }

    public static PortalBlockRegistry getInstance() {
        if (instance == null) {
            instance = new PortalBlockRegistry();
        }
        return instance;
    }

    public void addPortal(Location location, UUID uuid) {
        registry.put(location, uuid);
    }

    public void removePortal(UUID uuid) {
        for (Map.Entry<Location, UUID> entry : registry.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                registry.remove(entry.getKey());
            }
        }
    }

    public boolean isPortal(Location location) {
        for (Location portalLocation : registry.keySet()) {
            int portalX = portalLocation.getBlockX();
            int portalY = portalLocation.getBlockY();
            int portalZ = portalLocation.getBlockZ();

            if (portalX == location.getBlockX()
                    && portalY == location.getBlockY()
                    && portalZ == location.getBlockZ()) {
                return true;
            }
        }
        return false;
    }

    public void deleteAllPortals() {
        registry.clear();
    }


}
