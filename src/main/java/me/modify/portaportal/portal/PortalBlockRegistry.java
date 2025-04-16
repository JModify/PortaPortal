package me.modify.portaportal.portal;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Iterator;
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
        Iterator<Map.Entry<Location, UUID>> iterator = registry.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Location, UUID> entry = iterator.next();

            if (entry.getValue().equals(uuid)) {
                iterator.remove();
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
