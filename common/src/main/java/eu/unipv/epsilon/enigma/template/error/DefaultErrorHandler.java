package eu.unipv.epsilon.enigma.template.error;

import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * The default error handler which simply returns a formatted error message for the HTML view.
 */
public class DefaultErrorHandler implements ErrorHandler {

    private static final String PAGE_PATH = "assets:/templates/error/error_page.html";
    private static final String PAGE_IMAGE_PATH = "assets:/templates/error/chainsaw.png";

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
            e.printStackTrace();
            // Return the original error's bare stack trace in case of styled document loading problems
            return new ByteArrayInputStream(printThrowableStackTrace(exception).getBytes());
        }
    }

    private InputStream generateErrorPage(String pageTitle, Throwable exception) throws IOException {
        MappedValueInputStream page = new MappedValueInputStream(new URL(PAGE_PATH).openStream());
        page.addMacro("PAGE_TITLE", pageTitle);
        page.addMacro("DESCRIPTION", '"' + exception.getLocalizedMessage() + '"');
        page.addMacro("CAUSE", exception.getClass().getName());
        page.addMacro("MESSAGE", printThrowableStackTrace(exception));
        page.addMacro("IMAGE_URL", PAGE_IMAGE_PATH);
        return page;
    }

    private String printThrowableStackTrace(Throwable e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

}
