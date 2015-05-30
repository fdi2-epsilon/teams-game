package eu.unipv.epsilon.enigma.template.api.xml;

import org.w3c.dom.Element;

public abstract class Extractor {

    public String getText() {
        return getNode().getTextContent();
    }

    public abstract Element getNode();

}
