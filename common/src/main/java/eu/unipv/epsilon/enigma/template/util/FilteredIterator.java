package eu.unipv.epsilon.enigma.template.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteredIterator<T> implements Iterator<T> {

    private Iterator<? extends T> iterator;
    private Predicate<T> filter;
    private T nextElement = null;

    public FilteredIterator(Iterator<? extends T> iterator, Predicate<T> filter) {
        this.iterator = iterator;
        this.filter = filter;

        nextMatch();
    }

    @Override
    public boolean hasNext() {
        return nextElement != null;
    }

    @Override
    public T next() {
        if (nextElement == null)
            throw new NoSuchElementException();

        return nextMatch();
    }

    private T nextMatch() {
        T oldMatch = nextElement;

        while (iterator.hasNext()) {
            T o = iterator.next();

            if (filter.test(o)) {
                nextElement = o;
                return oldMatch;
            }
        }

        nextElement = null;
        return oldMatch;
    }

    @Override
    public void remove() {
        iterator.remove();
    }

}
