package fr.vilia.twx.items;

import java.io.File;

public abstract class DiscoveredItem {

    protected final File file;

    protected DiscoveredItem(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
