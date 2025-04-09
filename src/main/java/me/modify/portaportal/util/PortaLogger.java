package me.modify.portaportal.util;

import lombok.Getter;
import me.modify.portaportal.PortaPortal;

import java.util.logging.Logger;

public class PortaLogger {

    public static void info(String message) {
        PortaPortal.getInstance().getLogger().info(message);
    }

    public static void warning(String message) {
        PortaPortal.getInstance().getLogger().warning(message);
    }

    public static void error(String message) {
        PortaPortal.getInstance().getLogger().severe(message);
    }


}
