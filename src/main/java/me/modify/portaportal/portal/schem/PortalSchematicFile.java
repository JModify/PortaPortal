package me.modify.portaportal.portal.schem;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.util.MinecraftVersion;
import me.modify.portaportal.util.PortaLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents the file associated with the portal schematic (portal.schem).
 */
public class PortalSchematicFile {

    public static File getFile() {
        File pluginDataFolder = PortaPortal.getInstance().getDataFolder();
        File schematicsFolder = new File(pluginDataFolder, "data");

        if (!schematicsFolder.exists()) {
            schematicsFolder.mkdirs();
        }

        File schematicFile = new File(schematicsFolder, "portal.schem");

        // If schematics file does not exist in data folder, copy the default one.
        if (!schematicFile.exists()) {

            String defaultPortal = "default-portal-" + MinecraftVersion.getMajorVersion() + ".schem";

            try (InputStream inputStream = PortaPortal.getInstance().getResource("data/" + defaultPortal);
                 FileOutputStream outputStream = new FileOutputStream(schematicFile)) {

                if (inputStream == null) {
                    PortaLogger.error("Could not find " + defaultPortal + " in resources!");
                    return null;
                }

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                PortaLogger.info("Copied " + defaultPortal + " to " + schematicFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return schematicFile;
    }


}
