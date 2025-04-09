package me.modify.portaportal.data.flatfile;

import me.modify.portaportal.PortaPortal;

public class ConfigFile extends FlatFile {
    public ConfigFile(PortaPortal plugin) {
        super(plugin, "config");
    }
}
