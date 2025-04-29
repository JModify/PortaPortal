package me.modify.portaportal.timer;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public abstract class PortaTask implements Runnable {

    @Setter
    protected int taskId;

    protected final UUID playerId;

    public PortaTask(UUID playerId) {
        this.playerId = playerId;
    }
}
