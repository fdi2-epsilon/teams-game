package eu.unipv.epsilon.enigma.template.error;

import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * The default error handler which simply returns a formatted error message for the HTML view.
 */
public class DefaultErrorHandler implements ErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultErrorHandler.class);

    /*           DO NOT put static resource URLS here!!! (I miss Scala lazy vals...)            */
    /* Because the URL system may not have been initialized when we create the template server. */

    @Override
    public InputStream handleArgumentsParseException(Throwable exception) {
        try {
            if (exception instanceof InvocationTargetException) {
                // This exception was throws by the template processor
                return generateErrorPage("The template processor crashed!", exception.getCause());
            } else {
                // Other XML parsing or reflection related exception
                return generateErrorPage("An internal error!? Impossible.", exception);
            }
        } catch (IOException e) {
            LOG.error("Cannot generate styled template error message, returning bare text", e);
            // Return the original error's bare stack trace in case of styled document loading problems
            return new ByteArrayInputStream(printThrowableStackTrace(exception).getBytes());
        }
    }

    private InputStream generateErrorPage(String pageTitle, Throwable exception) throws IOException {
        final URL PAGE_URL = ClasspathURLStreamHandler.createURL("assets/templates/error/error_page.html");
        final URL PAGE_IMAGE_URL = ClasspathURLStreamHandler.createURL("assets/templates/error/chainsaw.png");

        MappedValueInputStream page = new MappedValueInputStream(PAGE_URL.openStream());
        page.addMacro("PAGE_TITLE", pageTitle);
        page.addMacro("DESCRIPTION", '"' + exception.getLocalizedMessage() + '"');
        page.addMacro("CAUSE", exception.getClass().getName());
        page.addMacro("MESSAGE", printThrowableStackTrace(exception));
        page.addMacro("IMAGE_URL", PAGE_IMAGE_URL.toString());
        return page;
    }

    @SuppressWarnings("squid:S1148") // Sonar: Use a logger to log this exception
    private String printThrowableStackTrace(Throwable e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

}
