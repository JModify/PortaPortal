package me.modify.portaportal.timer;

import lombok.Getter;
import me.modify.portaportal.timer.cooldown.CooldownController;
import me.modify.portaportal.timer.erasure.ErasureController;

@Getter
public class PortaTaskManager {

    private static PortaTaskManager instance;
    private final CooldownController cooldownController;
    private final ErasureController erasureController;

    private PortaTaskManager() {
        cooldownController = new CooldownController();
        erasureController = new ErasureController();
    }

    public static PortaTaskManager getInstance() {
        if (instance == null) {
            instance = new PortaTaskManager();
        }
        return instance;
    }

    public void shutdown() {
       cooldownController.shutdown();
       erasureController.shutdown();
    }
}
