package me.modify.portaportal.data.flatfile;


import me.modify.portaportal.PortaPortal;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class FlatFile {

    private final String name;
    private FileConfiguration yaml;
    private File file;
    private final PortaPortal plugin;


    public FlatFile(PortaPortal plugin, String name) {
        this.name = name;
        this.plugin = plugin;
        startup();
    }

    public String getFileName() {
        return name.toLowerCase() + ".yml";
    }

    private void startup() {
        file = new File(plugin.getDataFolder(), getFileName());
        createIfNotExists();

        reload();
    }

    public void reload() {
        if (file == null)
            startup();

        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getYaml() {
        if (yaml == null)
            reload();

        return yaml;
    }

    public void save() {
        if (file == null || yaml == null)
            reload();

        try {
            yaml.save(file);

        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save file " + getFileName() + " to " + e.getMessage());
        }
    }

    private void createIfNotExists() {
        if (!file.exists())
            plugin.saveResource(getFileName(), false);
    }

    public static boolean deleteFile(String path, String fileName) {
        File file = new File(path + fileName);
        if (!file.exists()) {
            return false;
        }

        return file.delete();
    }



}