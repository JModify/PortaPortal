package me.modify.portaportal.portal.timer;

import me.modify.portaportal.PortaPortal;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class PortalTaskManager {

    private static PortalTaskManager instance;
    private final Map<Integer, PortalErasureTask> tasks;

    private PortalTaskManager() {
        tasks = new HashMap<>();
    }

    public static PortalTaskManager getInstance() {
        if (instance == null) {
            instance = new PortalTaskManager();
        }
        return instance;
    }

    public void add(PortalErasureTask task) {
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(PortaPortal.getInstance(), task, 0, 20L);
        task.setTaskId(id);
        tasks.put(id, task);
    }

    public void remove(int id) {
        PortaPortal.getInstance().getServer().getScheduler().cancelTask(id);
        tasks.remove(id);
    }


}
