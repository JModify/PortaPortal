package me.modify.portaportal.portal.timer;

import lombok.Getter;
import me.modify.portaportal.PortaPortal;
import org.bukkit.Bukkit;

import java.util.*;

public class PortalTaskManager {

    private static PortalTaskManager instance;

    private final Set<PortalErasureTask> erasureTasks;

    @Getter
    private final Map<UUID, PlayerPortalCooldownTask> cooldownTasks;

    private PortalTaskManager() {
        erasureTasks = new HashSet<>();
        cooldownTasks = new HashMap<>();
    }

    public static PortalTaskManager getInstance() {
        if (instance == null) {
            instance = new PortalTaskManager();
        }
        return instance;
    }

    public void add(PlayerPortalCooldownTask task) {
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(PortaPortal.getInstance(), task, 0L, 20L);
        task.setTaskId(taskId);
        cooldownTasks.put(task.getPlayerId(), task);
    }

    public void add(PortalErasureTask task) {
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(PortaPortal.getInstance(), task, 20L, 20L);
        task.setTaskId(id);
        erasureTasks.add(task);
    }

    public void removeCooldownTask(UUID playerId) {
        cooldownTasks.remove(playerId);

    }

    public void removeErasureTask(PortalErasureTask task) {
        erasureTasks.remove(task);
    }

    public void cancelTask(int taskId) {
        PortaPortal.getInstance().getServer().getScheduler().cancelTask(taskId);
    }

    public void shutdown() {
        erasureTasks.forEach(erasureTask -> {
            erasureTask.erase();
            cancelTask(erasureTask.getTaskId());
        });

        cooldownTasks.values().forEach(cooldownTask -> {
            cancelTask(cooldownTask.getTaskId());
        });

        erasureTasks.clear();
        cooldownTasks.clear();
    }


}
