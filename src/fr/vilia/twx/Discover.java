package fr.vilia.twx;

import fr.vilia.twx.discovery.Detector;
import fr.vilia.twx.discovery.EmptyDirectoryDetector;
import fr.vilia.twx.discovery.ThingWorxDetector;
import fr.vilia.twx.items.DiscoveredItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Discover {

    private final static Detector[] DISCOVERERS = new Detector[]{
            new ThingWorxDetector(),
            new EmptyDirectoryDetector()
    };

    public static CachedFile traverseRecursive(File parent, CachedFile cachedParent) {
        CachedFile cf = new CachedFile(
                parent,
                parent.getName(),
                parent.getAbsolutePath(),
                parent.isDirectory(),
                cachedParent,
                parent.isDirectory() ? new HashSet<>() : null
        );
        if (parent.isDirectory()) {
            for (File f : parent.listFiles()) {
                cf.addChild(traverseRecursive(f, cf));
            }
        }
        return cf;
    }

    public static CachedFile label() {
        CachedFile cf = traverseRecursive(new File("d:\\git"), null);
        System.out.println("Found " + cf.totalChildren + " files");
        cf.traverseBackward(f -> {
            for (Detector d: DISCOVERERS) {
                Set<String> labels = d.label(f);
                for (String label: labels) {
                    f.label(label);
                }
            }
            return true;
        });
        return cf;
    }

    private static List<DiscoveredItem> discover(CachedFile root) {
        List<DiscoveredItem> items = new ArrayList<>();
        for (Detector d: DISCOVERERS) {
            root.traverseForward(cf -> {
                Set<DiscoveredItem> di = d.discover(cf);
                items.addAll(di);
                return di.isEmpty();
            });
        }
        return items;
    }

    public static void main(String[] args) {
        CachedFile root = label();
        List<DiscoveredItem> items = discover(root);
        for (DiscoveredItem item: items) {
            System.out.println("Discovered " + item);
        }
    }

}
