package eu.unipv.epsilon.enigma.template.api.xml;

import eu.unipv.epsilon.enigma.template.api.TemplateArguments;
import org.w3c.dom.Element;

import java.util.*;

public class XmlTemplateArguments extends TemplateArguments {

    public static final String ATTR_NODE_VALUE = "_value_";

    private final Element documentElement;
    private final Map<String, String> cache = new WeakHashMap<>();

    public XmlTemplateArguments(Element documentElement) {
        this.documentElement = documentElement;
    }

    public Element getDocumentElement() {
        return documentElement;
    }

    @Override
    public String query(String path) {
        if (cache.containsKey(path))
            return cache.get(path);

        String result = queryRaw(path).getText();
        cache.put(path, result);
        return result;
    }

    public Object queryAll(String path) {
        return queryRaw(path).getResultObject();
    }

    public Extractor queryRaw(String path) {
        if (path.length() > 0 && path.charAt(0) == '/') path = path.substring(1);

        List<String> tree = Arrays.asList(path.split("/"));
        Collections.reverse(tree);

        return getHeadExtractor(tree);
    }

    // Gets an extractor considering also attribute syntax
    private Extractor getHeadExtractor(List<String> tree) {
        String[] headAttr = tree.get(0).split(":", 2);

        if (headAttr.length == 1)
            return getExtractor(tree);

        // Drop attribute from head
        tree.set(0, headAttr[0]);

        boolean extractAll = "*".equals(headAttr[1]);

        return new AttributeExtractor(headAttr[1], extractAll, getExtractor(tree));
    }

    private Extractor getExtractor(List<String> tree) {
        if (tree.size() == 0 || "".equals(tree.get(0))) return new RootExtractor(documentElement);

        String currentNode = tree.get(0);
        boolean extractAll = false;
        if (currentNode.charAt(0) == '*') {
            currentNode = currentNode.substring(1);
            extractAll = true;
        }

        return new NodeExtractor(currentNode, extractAll, getExtractor(tree.subList(1, tree.size())));
    }

}
