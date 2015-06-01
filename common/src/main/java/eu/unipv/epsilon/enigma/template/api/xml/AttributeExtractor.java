package eu.unipv.epsilon.enigma.template.api.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.*;

public class AttributeExtractor extends Extractor {

    private final String attrName;
    private final boolean getAll;
    private final Extractor parent;

    public AttributeExtractor(String attrName, boolean getAll, Extractor parent) {
        this.attrName = attrName;
        this.getAll = getAll;
        this.parent = parent;
    }

    // Gets text for a single attribute in a single node
    @Override
    public String getText() {
        Element parentNode = parent.getNode();
        String attribute = parentNode.getAttribute(attrName);
        if ("".equals(attribute))
            throw new NoSuchElementException(
                    "Arguments: Attribute \"" + attrName + "\" not found in \"" + parentNode.getTagName() + '"');
        return attribute;
    }

    // Gets text for a single attribute in all nodes
    @Override
    public List<String> getTexts() {
        List<Element> parentNodes = parent.getNodes();
        List<String> attributes = new ArrayList<>();

        for (Element parentNode : parentNodes)
            attributes.add(parentNode.getAttribute(attrName));
        return attributes;
    }

    // Returns a List<String> if a single attribute is selected in all nodes
    // Returns a List<Map<String, String>> if all attributes are requested
    @Override
    public Object getResultObject() {
        if (!getAll) return getTexts();
        List<Element> parentNodes = parent.getNodes();

        List<Map<String, String>> attributeMaps = new ArrayList<>();

        for (Element parentNode : parentNodes) {
            Map<String, String> attrsMap = new HashMap<>();
            // Also add the node value to attributes list so if the user needs it, no more queries are needed
            attrsMap.put(XmlTemplateArguments.ATTR_NODE_VALUE, parentNode.getTextContent());
            NamedNodeMap attrs = parentNode.getAttributes();
            for (int i = 0; i < attrs.getLength(); ++i) {
                Node n = attrs.item(i);
                attrsMap.put(n.getNodeName(), n.getNodeValue());
            }
            attributeMaps.add(attrsMap);
        }
        return attributeMaps;
    }

    @Override
    public Element getNode() {
        throw new UnsupportedOperationException("Tried to get node out from attribute");
    }

    @Override
    public List<Element> getNodes() {
        throw new UnsupportedOperationException("Tried to get node out from attribute");
    }

    @Override
    public String toString() {
        return String.format("AttributeExtractor(\"%s\", %b, %s)", attrName, getAll, parent);
    }

}
