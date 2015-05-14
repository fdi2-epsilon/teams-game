package eu.unipv.epsilon.enigma.template.error;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The default error handler which simply returns a formatted error message for the HTML view.
 */
public class DefaultErrorHandler implements ErrorHandler {

    @Override
    public InputStream handleArgumentsParseException(Throwable e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return new ByteArrayInputStream(errors.toString().getBytes());
    }

}
