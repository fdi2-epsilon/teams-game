package eu.unipv.epsilon.enigma.template.util;

import java.util.Enumeration;
import java.util.Iterator;

public class IterableEnumeration<T> implements Iterable<T> {

    private final Enumeration<T> enumeration;

    public IterableEnumeration(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public T next() {
                return enumeration.nextElement();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    public static <T> Iterable<T> make(Enumeration<T> en) {
        return new IterableEnumeration<>(en);
    }

}
