package fr.vilia.twx.discovery;

import javax.xml.stream.XMLInputFactory;

public abstract class StaxDetector implements Detector {

    protected XMLInputFactory factory;

    protected StaxDetector() {
        factory = XMLInputFactory.newInstance();
    }
}
