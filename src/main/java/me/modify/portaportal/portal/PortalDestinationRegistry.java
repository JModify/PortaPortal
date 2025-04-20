package me.modify.portaportal.portal;

import me.modify.portaportal.PortaPortal;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PortalDestinationRegistry {

    private static PortalDestinationRegistry instance;

    private final Map<UUID, Destination> registry;

    public PortalDestinationRegistry() {
        registry = new HashMap<>();
    }

    public static PortalDestinationRegistry getInstance() {
        if (instance == null) {
            instance = new PortalDestinationRegistry();
        }
        return instance;
    }

    private void add(UUID user) {
        FileConfiguration configFile = PortaPortal.getInstance().getConfigFile().getYaml();
        Destination destination = Destination.valueOf(configFile.getString("portal.destination.type",
                Destination.SPAWN.name()).toUpperCase());
        registry.put(user, destination);
    }

//    public void toggleDestination(UUID uuid) {
//        registry.computeIfPresent(uuid, (k, destination) -> destination.next());
//    }

    public Destination getDestination(UUID user) {
        if (!registry.containsKey(user)) {
            add(user);
        }

        return registry.get(user);
    }

    public void clear() {
        registry.clear();
    }
}
