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
     * @param path the key of the argument to reach
     * @return the value of the key
     * @throws NoSuchElementException if the element can not be found
     */
    public abstract String query(String path) throws NoSuchElementException;

    public String query(String path, String defaultValue) {
        try {
            return query(path);
        } catch (NoSuchElementException e) {
            return defaultValue;
        }
    }

}
