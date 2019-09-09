package fr.vilia.twx.items;

import java.io.File;

public class ThingWorxSources extends DiscoveredItem {
    private final String project;
    private final String version;

    public ThingWorxSources(File file, String project, String version) {
        super(file);
        this.project = project;
        this.version = version;
    }

    @Override
    public String toString() {
        return "ThingWorx sources: " + file;
    }
}
