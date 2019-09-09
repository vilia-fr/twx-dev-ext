package fr.vilia.twx.discovery;

import fr.vilia.twx.CachedFile;
import fr.vilia.twx.items.DiscoveredItem;

import java.util.Collections;
import java.util.Set;

public class EmptyDirectoryDetector implements Detector {

    private final static String LABEL = "empty";

    @Override
    public Set<String> label(CachedFile file) {
        if (file.isDirectory() && file.childrenHaveLabel(LABEL)) {
            return Collections.singleton(LABEL);
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<DiscoveredItem> discover(CachedFile file) {
        return Collections.EMPTY_SET;
    }

}
