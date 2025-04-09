package me.modify.portaportal.listener;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.item.HomePortal;
import me.modify.portaportal.util.PortalSchematic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.io.File;
import java.io.IOException;

public class ProjectileListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (entity.getType() != EntityType.SNOWBALL) return;

        Snowball snowball = (Snowball) entity;
        if (!snowball.getItem().isSimilar(new HomePortal())) return;

        Block block = event.getHitBlock();
        if (block == null) return;

        ProjectileSource shooter = event.getEntity().getShooter();
        if (!(shooter instanceof Player player)) return;

        PortalSchematic portalSchematic = new PortalSchematic();

        // Create paste location and offset Y by 1 block to be above ground.
        Location pasteLocation = block.getLocation();
        pasteLocation.setY(block.getY() + 1);

        portalSchematic.pastePortalFacingPlayer(player, pasteLocation);
    }

}
