package fr.vilia.twx.discovery;

import fr.vilia.twx.CachedFile;
import fr.vilia.twx.items.DiscoveredItem;

import java.util.Set;

public interface Detector {
    /**
     * Traverses the cached file tree from bottom to top to label the leaves.
     *
     * @return Should never return null.
     */
    Set<String> label(CachedFile file);

    /**
     * Traverses the labeled cached file tree from top to bottom, until it discovers something, stopping the traversal.
     *
     * @return Should never return null.
     */
    Set<DiscoveredItem> discover(CachedFile file);
}
