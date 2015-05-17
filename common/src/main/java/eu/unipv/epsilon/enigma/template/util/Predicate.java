package eu.unipv.epsilon.enigma.template.util;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 *
 * This is a backport from the Java 8 API of "java.util.function.Predicate" to Java 7
 *
 * @param <T> the type of the input to the predicate
 */
public interface Predicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test(T t);

}
