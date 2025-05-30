package me.modify.portaportal.portal.block;

import org.bukkit.Location;
import java.util.UUID;

public record PortalBlockData(UUID playerId, Destination destinationType, Location destination) { }
