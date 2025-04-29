package me.modify.portaportal.timer.cooldown;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.timer.TaskController;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownController extends TaskController<CooldownTask> {

    public int getCooldownTime(UUID playerId) {
        return tasks.get(playerId).getTimeRemaining();
    }

    public boolean isCooldown(UUID playerId) {
        return tasks.containsKey(playerId);
    }
}
