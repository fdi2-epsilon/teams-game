package eu.unipv.epsilon.enigma.template.api;

import java.util.NoSuchElementException;

/** Arguments passed to a template processor to generate its view. */
public abstract class TemplateArguments {

    /**
     * Returns the argument value associated with the given key.
     * <p>
     *     The user can reach inner elements in the tree by using slashes, (e.g. this is a valid query
     *     {@code "foo/bar/asdf"}).
     * </p>
     * <p>
     *     Different implementations can add further syntactic sugar: for example, the XML implementation allows
     *     usage of colons to reach head element attributes (e.g. {@code "foo/bar/baz:color"} or {@code ":root-attr"}).
     * </p>
     * @param path the path of the argument to reach
     * @return the value of the key
     * @throws NoSuchElementException if the element can not be found
     */
    public abstract String query(String path) throws NoSuchElementException;

    /**
     * Returns a memory structure containing the result of a multiple selection query.<br>
     * Here are some examples of queries:
     * <p>
     *   {@code "foo/*bar/baz"} extracts the value of the {@code baz} node in all {@code bar}s in {@code foo}<br>
     *   {@code "foo/*bar/baz:color} is the same, only that we now extract the value of the {@code color} attribute<br>
     *   {@code "foo/bar/*baz:*} for any {@code baz} in the first {@code bar} found, extract any attribute in a map<br>
     * </p>
     *
     * <b>Note</b>: This does not throw exceptions if elements are not found, it will return empty collections instead.
     *
     * @param path the elements selector path
     * @return a {@code List<String>} if for elements selection (nodes or attributes in XmlTemplateArguments) or a
     *         {@code List<Map<String, String>>} if all arguments are selected in multiple nodes.
     */
    public abstract Object queryAll(String path);

    public String query(String path, String defaultValue) {
        try {
            return query(path);
        } catch (NoSuchElementException e) {
            return defaultValue;
        }
    }

}
