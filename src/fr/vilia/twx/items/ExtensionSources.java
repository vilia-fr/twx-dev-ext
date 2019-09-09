package fr.vilia.twx.items;

import java.io.File;

public class ExtensionSources extends DiscoveredItem {

    private final File buildScript;

    protected ExtensionSources(File file, File buildScript) {
        super(file);
        this.buildScript = buildScript;
    }
}
