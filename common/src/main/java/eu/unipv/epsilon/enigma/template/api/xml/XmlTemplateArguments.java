package eu.unipv.epsilon.enigma.template.api.xml;

import eu.unipv.epsilon.enigma.template.api.TemplateArguments;
import org.w3c.dom.Element;

import java.util.*;

public class XmlTemplateArguments extends TemplateArguments {

    /** The key in ":*" syntax corresponding to the node value */
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

    @Override
    public Object queryAll(String path) {
        return queryRaw(path).getResultObject();
    }

    /**
     * Obtains extractors to perform a query.
     * Useful if you want to call getNode() / getNodes() on the head extractor and perform raw XML operations.
     */
    public Extractor queryRaw(String path) {
        if (path.length() > 0 && path.charAt(0) == '/') path = path.substring(1);

        List<String> tree = Arrays.asList(path.split("/"));
        Collections.reverse(tree);

        return getHeadExtractor(tree);
    }

    // Recursively get extractors for the passed "tree", considering also attribute syntax for the head
    private Extractor getHeadExtractor(List<String> tree) {
        // Check if head is in attribute syntax (e.g. "foo:baz")
        String[] headAttr = tree.get(0).split(":", 2);

        // No, proceed using normal extractor chain
        if (headAttr.length == 1)
            return getExtractor(tree);

        // Drop attribute from head ("foo:baz" -> "foo")
        tree.set(0, headAttr[0]);

        // Check if we have "foo:*" syntax and we should get all attributes + value in a map
        boolean extractAll = "*".equals(headAttr[1]);

        // Return an attribute extractor on the normal node obtained by getExtractor
        return new AttributeExtractor(headAttr[1], extractAll, getExtractor(tree));
    }

    // Recursively get extractors for the passed "tree"
    private Extractor getExtractor(List<String> tree) {
        // If end of recursion, extract root node
        if (tree.size() == 0 || "".equals(tree.get(0))) return new RootExtractor(documentElement);


        String currentNode = tree.get(0);
        boolean extractAll = false;

        // Extract all "bla" nodes if got "*bla"
        if (currentNode.charAt(0) == '*') {
            currentNode = currentNode.substring(1);
            extractAll = true;
        }

        // Extract this node from recursive call given tree tail.
        return new NodeExtractor(currentNode, extractAll, getExtractor(tree.subList(1, tree.size())));
    }

}
