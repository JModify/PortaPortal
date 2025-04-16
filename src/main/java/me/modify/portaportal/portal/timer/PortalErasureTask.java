package me.modify.portaportal.portal.timer;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import lombok.Getter;
import lombok.Setter;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.portal.PortalBlockRegistry;
import me.modify.portaportal.util.PortaLogger;

import java.util.UUID;

public class PortalErasureTask implements Runnable {

    @Getter @Setter
    int taskId;

    private int time;
    private final int eraseTime;

    private final UUID playerId;

    private final ClipboardHolder holder;
    private final EditSession session;
    private final World world;

    public PortalErasureTask(UUID playerId, EditSession session, ClipboardHolder holder, World world) {
        this.playerId = playerId;
        this.session = session;
        this.holder = holder;
        this.world = world;

        this.time = 0;
        this.eraseTime = 5;
    }

    @Override
    public void run() {
        time += 1;
        PortaLogger.info("[PortalErasureTask] " + time + " seconds");
        if (time >= eraseTime) {
            WorldEdit worldEdit = PortaPortal.getInstance().getWorldEditHook().getAPI().getWorldEdit();

            EditSession undoSession = worldEdit.newEditSession(world);
            session.undo(undoSession);
            session.close();

            Operations.complete(holder.createPaste(undoSession).build());
            undoSession.close();

            PortalTaskManager.getInstance().remove(taskId);
            PortalBlockRegistry.getInstance().removePortal(playerId);
        }
    }
}
