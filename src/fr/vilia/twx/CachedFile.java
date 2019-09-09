package fr.vilia.twx;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class CachedFile {

    private final File file;
    private final String name;
    private final String absolute;
    private final boolean directory;
    private final CachedFile parent;
    private final Set<CachedFile> children;
    private final Set<String> labels = new TreeSet<>();

    public int totalChildren = 1;

    public CachedFile(File file, String name, String absolute, boolean directory, CachedFile parent, Set<CachedFile> children) {
        this.file = file;
        this.name = name;
        this.absolute = absolute;
        this.directory = directory;
        this.parent = parent;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public String getAbsolute() {
        return absolute;
    }

    public boolean isDirectory() {
        return directory;
    }

    public CachedFile getParent() {
        return parent;
    }

    public File getFile() {
        return file;
    }

    public Set<CachedFile> getChildren() {
        return children;
    }

    public void addChild(CachedFile child) {
        children.add(child);
        totalChildren += child.totalChildren;
    }

    public boolean isEmptyDirectory() {
        return isDirectory() && children != null && children.isEmpty();
    }

    public boolean hasLabel(String label) {
        return labels.contains(label);
    }

    public boolean childrenHaveLabel(String label) {
        for(CachedFile c: getChildren()) {
            if (!c.hasLabel(label)) {
                return false;
            }
        }
        return true;
    }

    public void label(String label) {
        labels.add(label);
    }

    public interface Traverse {
        boolean process(CachedFile file);
    }

    public void traverseBackward(Traverse callback) {
        if (children != null) {
            for (CachedFile child: children) {
                child.traverseBackward(callback);
            }
        }
        callback.process(this);
    }

    public void traverseForward(Traverse callback) {
        if (callback.process(this)) {
            if (children != null) {
                for (CachedFile child : children) {
                    child.traverseForward(callback);
                }
            }
        }
    }

    @Override
    public String toString() {
        return absolute + ": " + labels;
    }
}
