package me.modify.portaportal.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.exceptions.NullWorldException;
import me.modify.portaportal.registry.PortalBlockRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.*;

public class PortalSchematic {

    private final String DEFAULT_SCHEMATIC = "portal.schem";
    private final String schematicName;

    public PortalSchematic() {
        this.schematicName = DEFAULT_SCHEMATIC;
    }

    public PortalSchematic(String schematicName) {
        this.schematicName = schematicName;
    }

    private File getFile() {
        File pluginDataFolder = PortaPortal.getInstance().getDataFolder();
        File schematicsFolder = new File(pluginDataFolder, "data");

        if (!schematicsFolder.exists()) {
            schematicsFolder.mkdirs();
        }

        File schematicFile = new File(schematicsFolder, schematicName);

        // If schematics file does not exist in data folder, copy the default one.
        if (!schematicFile.exists()) {
            try (InputStream inputStream = PortaPortal.getInstance().getResource("data/default-portal.schem");
                 FileOutputStream outputStream = new FileOutputStream(schematicFile)) {

                if (inputStream == null) {
                    PortaLogger.error("Could not find default-portal.schem in resources!");
                    return null;
                }

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                PortaLogger.info("Copied default-portal.schem to " + schematicFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return schematicFile;
    }

    private Clipboard loadSchematic(File file) {
        Clipboard clipboard;
        try {
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            if (format == null) {
                throw new IOException("Unknown schematic format: " + file.getName());
            }

            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                clipboard = reader.read();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return clipboard;
    }

    private void pasteByHolder(ClipboardHolder holder, Location location)
            throws WorldEditException, NullWorldException {

        if (location.getWorld() == null) return;

        World weWorld = BukkitAdapter.adapt(location.getWorld());
        EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld);
        Operations.complete(holder.createPaste(editSession)
                .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                .ignoreAirBlocks(true)
                .build());

        editSession.close();
    }

    public void pastePortalFacingPlayer(Player player, Location pasteLocation) {
        float playerYaw = player.getLocation().getYaw(); // Get player's yaw rotation
        int rotationDegrees = getRotationAngle(playerYaw);

        // Get schematics file using schematic name
        File schematicFile = getFile();

        // Get clipboard using schematic file
        Clipboard clipboard = loadSchematic(schematicFile);

        // Create abstract entity holding the clipboard
        ClipboardHolder holder = new ClipboardHolder(clipboard);

        // Rotate the holder's clipboard by degree facing player
        Transform transform = new AffineTransform().rotateY(rotationDegrees);
        holder.setTransform(transform);

        // Replace each block in the region by plugin schematic rules
        // AIR BLOCKS - become portal blocks
        // BARRIERS - become air blocks
        clipboard.getRegion().forEach(blockVector3 -> {
            BlockState blockState = clipboard.getBlock(blockVector3);

            // If block in schematic is not AIR or BARRIER do nothing.
            if (blockState.getBlockType() != BlockTypes.AIR
                    && blockState.getBlockType() != BlockTypes.BARRIER) return;

            try {
                // Replace barrier blocks with air
                if (blockState.getBlockType() == BlockTypes.BARRIER) {
                    clipboard.setBlock(blockVector3, BlockTypes.AIR.getDefaultState());
                    return;
                }

                // Replace air blocks with water (portal block)
                if (blockState.getBlockType() == BlockTypes.AIR) {
                    if (clipboard.setBlock(blockVector3, BlockTypes.WATER.getDefaultState())) {
                        registerPortalBlock(player, blockVector3, clipboard, pasteLocation, rotationDegrees);
                    }
                }
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        });

        // Paste the clipboard with respect to holder
        try {
            pasteByHolder(holder, pasteLocation);
            System.out.println("Pasting schematic at: " + pasteLocation);
        } catch (WorldEditException | NullWorldException e) {
            throw new RuntimeException(e);
        }
    }

    private Location rotateLocation(Location loc, Location axis, double angle) {
        // Convert angle from degrees to radians
        double radians = Math.toRadians(angle);

        // Rotation matrix for counterclockwise rotation around Y-axis
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        // Create vectors for rotation
        Vector v = loc.clone().subtract(axis).toVector();

        // Apply the rotation matrix
        double rotatedX = cos * v.getX() - sin * v.getZ();
        double rotatedZ = sin * v.getX() + cos * v.getZ();

        // Create the rotated vector and convert it back to a location
        Vector rotatedVector = new Vector(rotatedX, v.getY(), rotatedZ); // Y is not affected by rotation

        // Apply the offset to the axis to get the final rotated location
        return rotatedVector.add(axis.toVector()).toLocation(loc.getWorld());
    }

    private void registerPortalBlock(Player player, BlockVector3 blockVector3, Clipboard clipboard,
                                            Location pasteLocation, int rotationDegrees) {
        double x = pasteLocation.getX() + (blockVector3.x() - clipboard.getOrigin().x());
        double y = pasteLocation.getY() + (blockVector3.y() - clipboard.getOrigin().y());
        double z = pasteLocation.getZ() + (blockVector3.z() - clipboard.getOrigin().z());

        Location portalLocation = new Location(pasteLocation.getWorld(), x, y, z);
        Location axis = new Location(pasteLocation.getWorld(), pasteLocation.getBlockX(),
                pasteLocation.getBlockY(), pasteLocation.getBlockZ());
        Location rotatedLocation = rotateLocation(portalLocation, axis, rotationDegrees);

        PortalBlockRegistry.getInstance().addPortal(rotatedLocation, player.getUniqueId());
    }

    private int getRotationAngle(float yaw) {
        yaw = (yaw % 360 + 360) % 360;
        if (yaw >= 45 && yaw < 135) return 270; // West
        if (yaw >= 135 && yaw < 225) return 180; // North
        if (yaw >= 225 && yaw < 315) return 90; // East
        return 0; // South (default)
    }


}
