package me.modify.portaportal.util;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.portal.PortalBlockRegistry;
import org.bukkit.Bukkit;

public class MinecraftVersion {

    private static String version = Bukkit.getVersion();

    public static String getVersion() {
        return version.split("\\(MC: ")[1].replace(")", "");
    }

    public static String getMajorVersion() {
        String[] parts = getVersion().split("\\.");
        return parts[0].concat(".").concat(parts[1]);
    }
}
