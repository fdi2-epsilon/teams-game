package eu.unipv.epsilon.enigma.template.api.xml;

import org.w3c.dom.Element;

import java.util.NoSuchElementException;

public class AttributeExtractor extends Extractor {

    private final String attrName;
    private final Extractor parent;

    public AttributeExtractor(String attrName, Extractor parent) {
        this.attrName = attrName;
        this.parent = parent;
    }

    @Override
    public String getText() {
        Element parentNode = parent.getNode();
        String attribute = parentNode.getAttribute(attrName);
        if ("".equals(attribute))
            throw new NoSuchElementException(
                    "Arguments: Attribute \"" + attrName + "\" not found in \"" + parentNode.getTagName() + '"');
        return attribute;
    }

    @Override
    public Element getNode() {
        throw new UnsupportedOperationException("Tried to get node out from attribute");
    }

    @Override
    public String toString() {
        return String.format("AttributeExtractor(\"%s\", %s)", attrName, parent);
    }

}
