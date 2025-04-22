package me.modify.portaportal.portal.timer;

import lombok.Getter;
import lombok.Setter;
import me.modify.portaportal.PortaPortal;

import java.util.UUID;

public class PlayerPortalCooldownTask implements Runnable{

    @Getter @Setter
    int taskId;

    private int time;
    private final int cooldown;

    @Getter
    private final UUID playerId;

    public PlayerPortalCooldownTask(UUID playerId) {
        this.playerId = playerId;
        this.time = 0;
        this.cooldown = PortaPortal.getInstance().getConfigFile().getYaml().getInt("portal.cooldown", 5);
    }

    @Override
    public void run() {
        time += 1;
        if (time >= cooldown) {
            PortalTaskManager.getInstance().removeCooldownTask(playerId);
            PortalTaskManager.getInstance().cancelTask(taskId);
        }
    }

    public int getTimeRemaining() {
        return cooldown - time;
    }
}
