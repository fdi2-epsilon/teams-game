package eu.unipv.epsilon.enigma.template.api.xml;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class Extractor {

    public String getText() {
        return getNode().getTextContent();
    }

    public List<String> getTexts() {
        List<String> ret = new ArrayList<>();
        // Java 8 has mappings, Scala even better, we have foreach!
        for (Element e : getNodes())
            ret.add(e.getTextContent());
        return ret;
    }

    public abstract Element getNode();

    public abstract List<Element> getNodes();

    public Object getResultObject() {
        return getTexts();
    }

}
