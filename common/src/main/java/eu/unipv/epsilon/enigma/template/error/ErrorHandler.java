package eu.unipv.epsilon.enigma.template.error;

import java.io.InputStream;

/**
 * Used by a {@link eu.unipv.epsilon.enigma.template.TemplateServer TemplateServer} to handle errors.
 * Implementing classes can log the exception or return {@link InputStream}s with view data.
 */
public interface ErrorHandler {

    /**
     * Called if a template processor crashed during execution.
     *
     * @param exception the cause of the crash (the cause of InvocationTargetException)
     * @return an {@link InputStream} containing an error document for the view or {@code null}
     */
    InputStream handleTemplateException(Throwable exception);

    /**
     * Called when the requested template class was not found by the template server.
     *
     * @param exception the thrown exception
     * @return an {@link InputStream} containing an error document for the view or {@code null}
     */
    InputStream handleTemplateNotFoundError(Throwable exception);

    /**
     * Called in the case that an exception was thrown during template processor arguments parsing.
     *
     * @param exception the thrown exception
     * @return an {@link InputStream} containing an error document for the view or {@code null}
     */
    InputStream handleArgumentsParseException(Throwable exception);

    /**
     * Called in the case that an exception was throws during the template server normal execution.
     *
     * @param exception the throws exception
     * @return an {@link InputStream} containing an error document for the view or {@code null}
     */
    InputStream handleServerError(Throwable exception);

}
