package me.modify.portaportal.portal;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.portal.destination.PortalDestination;
import me.modify.portaportal.portal.schem.PortalSchematic;
import me.modify.portaportal.util.Messenger;
import me.modify.portaportal.util.MinecraftVersion;
import me.modify.portaportal.util.PortaLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PortalListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (!(projectile.getShooter() instanceof Player player)) return;

        if (projectile instanceof Snowball snowball) {
            if (!PortalItem.isPortalitem(snowball.getItem())) return;
        }

        if (projectile instanceof Egg egg) {
            if (!PortalItem.isPortalitem(egg.getItem())) return;
        }

        if (projectile instanceof EnderPearl pearl) {
            if (!PortalItem.isPortalitem(pearl.getItem())) return;

            NamespacedKey key = new NamespacedKey(PortaPortal.getInstance(), "block-teleport");
            player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
        }

        Block block = event.getHitBlock();
        if (block == null) return;

        PortalSchematic portalSchematic = new PortalSchematic();

        // Create paste location and offset Y by 1 block to be above ground.
        Location pasteLocation = block.getLocation();
        pasteLocation.setY(block.getY() + 2);

        portalSchematic.pasteSchematicFacingPlayer(player, pasteLocation);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) return;

        Player player = event.getPlayer();

        NamespacedKey key = new NamespacedKey(PortaPortal.getInstance(), "block-teleport");
        PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();

        if (persistentDataContainer.has(key, PersistentDataType.INTEGER)) {
            event.setCancelled(true);
            persistentDataContainer.remove(key);
        }
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent event) {
        // Cancel portal blocks from flowing water / lava everywhere
        if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.LAVA) {
            if (PortalBlockRegistry.getInstance().isPortal(event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        // Detection for when a player steps into a portal
        Player player = event.getPlayer();
        if (player.getLocation().getBlock().getType() != Material.WATER) return;
        if (!PortalBlockRegistry.getInstance().isPortal(player.getLocation())) return;

        PortalDestination destination = new PortalDestination(player);
        destination.teleport();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;

        Player player = event.getPlayer();
        if (player.hasPermission("portaportal.use")) return;
        if (PortalItem.isPortalitem(item)) {
            event.setCancelled(true);
            Messenger.sendMessage(player, Messenger.Type.ERROR, "insufficient-perms");
        }
    }

}
