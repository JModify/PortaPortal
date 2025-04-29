package me.modify.portaportal.timer.cooldown;

import lombok.Getter;
import lombok.Setter;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.timer.PortaTask;
import me.modify.portaportal.timer.PortaTaskManager;

import java.util.UUID;

public class CooldownTask extends PortaTask {

    private int time;
    private final int cooldown;

    public CooldownTask(UUID playerId) {
        super(playerId);

        this.time = 0;
        this.cooldown = PortaPortal.getInstance().getConfigFile().getYaml().getInt("portal.cooldown", 5);
    }

    @Override
    public void run() {
        time += 1;
        if (time >= cooldown) {
            PortaTaskManager manager = PortaTaskManager.getInstance();
            manager.getCooldownController().remove(playerId);
            manager.getCooldownController().cancelTask(taskId);
        }
    }

    public int getTimeRemaining() {
        return cooldown - time;
    }
}
