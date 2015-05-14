package eu.unipv.epsilon.enigma.template.error;

import java.io.InputStream;

/**
 * Used by a {@link eu.unipv.epsilon.enigma.template.TemplateServer TemplateServer} to handle errors.
 * Implementing classes can log the exception or return {@link InputStream}s with view data.
 */
public interface ErrorHandler {

    /**
     * Called in the case that an exception was thrown during Template Processor arguments parsing.
     *
     * @param e the thrown exception
     * @return an {@link InputStream} containing an error document for the view or {@code null}
     */
    InputStream handleArgumentsParseException(Throwable e);

}
