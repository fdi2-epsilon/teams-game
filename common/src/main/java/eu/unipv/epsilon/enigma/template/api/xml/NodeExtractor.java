package eu.unipv.epsilon.enigma.template.api.xml;

import org.w3c.dom.Element;

import java.util.NoSuchElementException;

public class NodeExtractor extends Extractor {

    private final String elemName;
    private final Extractor parent;

    public NodeExtractor(String elemName, Extractor parent) {
        this.elemName = elemName;
        this.parent = parent;
    }

    @Override
    public Element getNode() {
        Element parentNode = parent.getNode();
        Element thisElem = (Element) parentNode.getElementsByTagName(elemName).item(0);
        if (thisElem == null)
            throw new NoSuchElementException(
                    "Arguments: Node \"" + elemName + "\" not found in \"" + parentNode.getTagName() + '"');
        return thisElem;
    }

    @Override
    public String toString() {
        return String.format("NodeExtractor(\"%s\", %s)", elemName, parent);
    }

}
