package eu.unipv.epsilon.enigma.ui;

/**
 * Classes implementing this interface hold views with content that may change while recycled by a
 * {@link android.widget.ListView ListView} or a {@link android.support.v7.widget.RecyclerView RecyclerView}.
 *
 * For example, an alert card may have static content defined in the layout but a book view card may change its content
 * to display a different book while recycled, so it should implement this interface.
 *
 * @param <T> The type of the data used to fill recycled views.
 */
public interface DynamicView<T> {

    void updateViewFromData(T dataElement);

}
