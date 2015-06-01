package eu.unipv.epsilon.enigma.template.api.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class NodeExtractor extends Extractor {

    private final String elemName;
    private final boolean getAll;
    private final Extractor parent;

    public NodeExtractor(String elemName, boolean getAll, Extractor parent) {
        this.elemName = elemName;
        this.getAll = getAll;
        this.parent = parent;
    }

    @Override
    public Element getNode()
    {
        Element parentNode = parent.getNode();
        Element thisElem = (Element) parentNode.getElementsByTagName(elemName).item(0);
        if (thisElem == null)
            throw new NoSuchElementException(
                    "Arguments: Node \"" + elemName + "\" not found in \"" + parentNode.getTagName() + '"');
        return thisElem;
    }

    @Override
    public List<Element> getNodes() {
        List<Element> parentNodes = parent.getNodes();
        List<Element> outNodes = new ArrayList<>();

        for (Element parentNode : parentNodes) {
            NodeList parentChildren = parentNode.getElementsByTagName(elemName);
            for (int i = 0; i < (getAll ? parentChildren.getLength() : 1); ++i)
                outNodes.add((Element) parentChildren.item(i));
        }
        return outNodes;
    }

    @Override
    public String toString() {
        return String.format("NodeExtractor(\"%s\", %b, %s)", elemName, getAll, parent);
    }

}
