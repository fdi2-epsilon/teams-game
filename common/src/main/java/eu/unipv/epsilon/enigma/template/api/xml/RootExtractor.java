package eu.unipv.epsilon.enigma.template.api.xml;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class RootExtractor extends Extractor {

    private final Element documentElement;

    public RootExtractor(Element documentElement) {
        this.documentElement = documentElement;
    }

    @Override
    public Element getNode() {
        return documentElement;
    }

    @Override
    public List<Element> getNodes() {
        return new ArrayList<Element>() {{
            add(documentElement);
        }};
    }

    @Override
    public String toString() {
        return String.format("RootExtractor(<%s>)", documentElement.getTagName());
    }

}
