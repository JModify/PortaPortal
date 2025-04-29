package me.modify.portaportal.timer;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.timer.cooldown.CooldownTask;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskController <T extends PortaTask> {

    protected Map<UUID, T> tasks;
    public TaskController() {
        this.tasks = new HashMap<>();
    }

    public void cancelTask(int taskId) {
        PortaPortal.getInstance().getServer().getScheduler().cancelTask(taskId);
    }

    public void add(T task, long delay, long period) {
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(PortaPortal.getInstance(), task, delay, period);
        task.setTaskId(taskId);
        tasks.put(task.getPlayerId(), task);
    }

    public void remove(UUID playerId) {
        tasks.remove(playerId);
    }

    public void shutdown() {
        tasks.values().forEach(cooldownTask -> cancelTask(cooldownTask.getTaskId()));
        tasks.clear();
    }
}
