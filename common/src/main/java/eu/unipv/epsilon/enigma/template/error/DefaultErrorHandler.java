package eu.unipv.epsilon.enigma.template.error;

import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

/**
 * The default error handler which simply returns a formatted error message for the HTML view.
 */
public class DefaultErrorHandler implements ErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultErrorHandler.class);

    /*           DO NOT put static resource URLS here!!! (I miss Scala lazy vals...)            */
    /* Because the URL system may not have been initialized when we create the template server. */

    @Override
    public InputStream handleTemplateException(Throwable exception) {
        // This exception was throws by the template processor
        return generateErrorPage("...template crashed; not my fault", exception);
    }

    @Override
    public InputStream handleTemplateNotFoundError(Throwable exception) {
        // The template server could not find a valid template processor
        return generateErrorPage("I should have tidied up my room...", exception);
    }

    @Override
    public InputStream handleArgumentsParseException(Throwable exception) {
        // Arguments document XML parsing exception
        return generateErrorPage("Too long; didn't read", exception);
    }

    @Override
    public InputStream handleServerError(Throwable exception) {
        // XML parsing, my bugs or reflection related exception
        return generateErrorPage("An internal error!? Impossible.", exception);
    }

    private InputStream generateErrorPage(String pageTitle, Throwable exception) {
        try {
            final URL pageUrl = ClasspathURLStreamHandler.createURL("assets/templates/error/error_page.html");
            final URL pageImageUrl = ClasspathURLStreamHandler.createURL("assets/templates/error/chainsaw.png");

            MappedValueInputStream page = new MappedValueInputStream(pageUrl.openStream());
            page.addMacro("PAGE_TITLE", pageTitle);
            page.addMacro("DESCRIPTION", '"' + exception.getLocalizedMessage() + '"');
            page.addMacro("CAUSE", exception.getClass().getName());
            page.addMacro("MESSAGE", printThrowableStackTrace(exception));
            page.addMacro("IMAGE_URL", pageImageUrl.toString());
            return page;
        } catch (IOException e) {
            LOG.error("Cannot generate styled template error message, returning bare text", e);
            // Return the original error's bare stack trace in case of styled document loading problems
            return new ByteArrayInputStream(printThrowableStackTrace(exception).getBytes());
        }
    }

    @SuppressWarnings("squid:S1148") // Sonar: Use a logger to log this exception (using printStackTrace)
    private String printThrowableStackTrace(Throwable e) {
        LOG.error("Handled exception", e);
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

}
