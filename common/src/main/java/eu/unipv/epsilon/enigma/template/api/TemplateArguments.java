package eu.unipv.epsilon.enigma.template.api;

import java.util.NoSuchElementException;

/** Arguments passed to a template processor to generate its view. */
public interface TemplateArguments {

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
     *
     * @param path the path of the argument to reach
     * @return the value of the selected element
     * @throws NoSuchElementException if the element can not be found
     */
    String query(String path);

    /**
     * Returns the argument value associated with the given key or the passed default value if it fails.
     * @param path the path of the argument to reach
     * @param defaultValue the default value to use if the element could not be found
     * @return the value of the selected element
     */
    String query(String path, String defaultValue);

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
    Object queryAll(String path);

}
