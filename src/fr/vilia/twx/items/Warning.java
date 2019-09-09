package fr.vilia.twx.items;

import java.io.File;

public class Warning extends DiscoveredItem {

    private final String message;

    protected Warning(File file, String message) {
        super(file);
        this.message = message;
    }
}
