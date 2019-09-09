package fr.vilia.twx.discovery;

import fr.vilia.twx.CachedFile;
import fr.vilia.twx.items.DiscoveredItem;
import fr.vilia.twx.items.ThingWorxSources;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ThingWorxDetector extends StaxDetector {

    private final static String LABEL = "twxsrc";

    @Override
    public Set<String> label(CachedFile file) {
        if (file.isDirectory() && file.childrenHaveLabel(LABEL) || checkThingWorxFile(file)) {
            return Collections.singleton(LABEL);
        } else {
            return Collections.emptySet();
        }
    }

    private boolean checkThingWorxFile(CachedFile file) {
        if (file.getName().endsWith(".xml")) {
            try (Reader r = new FileReader(file.getFile())) {
                XMLStreamReader sr = factory.createXMLStreamReader(r);
                while (sr.hasNext()) {
                    sr.next();
                    if (sr.getEventType() == XMLStreamReader.START_ELEMENT) {
                        // We only look at the very first element
                        return  sr.getLocalName().equals("Entities")
                                && sr.getAttributeValue(null, "build") != null
                                && sr.getAttributeValue(null, "majorVersion") != null
                                && sr.getAttributeValue(null, "minorVersion") != null
                                && sr.getAttributeValue(null, "revision") != null
                                && sr.getAttributeValue(null, "schemaVersion") != null;
                    }
                }
            } catch (XMLStreamException | IOException e) {
                // e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Set<DiscoveredItem> discover(CachedFile file) {
        if (file.hasLabel(LABEL) && !file.hasLabel("empty")) {
            return Collections.singleton(
                    new ThingWorxSources(file.getFile(), "Project", "1.0")
            );
        }
        return Collections.EMPTY_SET;
    }

}
