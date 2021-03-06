package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.error.DefaultErrorHandler;
import eu.unipv.epsilon.enigma.template.error.ErrorHandler;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.NoSuchElementException;

/**
 * Entry point for template system queries: parses argument documents (i.e. {@code document.xml})
 * and invokes the correct {@link TemplateProcessor} returning streams to dynamic data.
 */
public class TemplateServer {

    public static final String ARGS_ROOT_NODE_NAME = "quiz";
    public static final String ARGS_ROOT_ATTRIBUTE_TEMPLATE_ID = "template";
    public static final String DEFAULT_TEMPLATE_ID = "raw";
    public static final InputStream EMPTY_STREAM = new ByteArrayInputStream(new byte[0]);

    private TemplateRegistry templateRegistry;
    private ErrorHandler errorHandler;

    public TemplateServer(TemplateRegistry registry) {
        this(registry, new DefaultErrorHandler());
    }

    public TemplateServer(TemplateRegistry registry, ErrorHandler errorHandler) {
        this.templateRegistry = registry;
        this.errorHandler = errorHandler;
    }

    public TemplateRegistry getRegistry() {
        return templateRegistry;
        // So we can load another collection's templates
    }

    /**
     * Dynamically creates a document described by the passed XML arguments stream.
     * Depending on the used {@link ErrorHandler}, the returned document may be an styled error message.
     *
     * @param argsDocumentStream an {@link InputStream} to an XML arguments document ({@code document.xml} in EQC)
     * @param docURL an optional document URL that can be used by the template processor for relative resource access,
     *               the template processor can check if this information is available
     * @return the processed view response, containing a stream (usually an HTML page)
     */
    public DynamicContentResponse loadDynamicContent(InputStream argsDocumentStream, URL docURL) {
        // If a collection URL is passed, register that collection's templates. If we have a generic URL, probably
        // the collection with the given host is not found and we have nothing registered, so it is ok.
        if (docURL != null)
            templateRegistry.registerCollectionTemplates(docURL.getHost());

        try {
            // Parse and validate the document
            Element document = getArgumentsDocument(argsDocumentStream);

            // Obtain a template processor and trigger a new document generation event with args
            TemplateProcessor proc = getTemplateProc(document);
            DocumentGenerationEvent event = new DocumentGenerationEvent(document, docURL);
            proc.generateDocument(event);

            // Return the generated output
            InputStream output = event.getResponseStream();

            return new DynamicContentResponse(proc, output != null ? output : EMPTY_STREAM);

        } catch (InvocationTargetException e) {
            // The template processor crashed, show THE CAUSE (e is always InvocationTargetException)
            return new DynamicContentResponse(null, errorHandler.handleTemplateException(e.getCause()));
        } catch (NoSuchElementException e) {
            // Template processor not found
            return new DynamicContentResponse(null, errorHandler.handleTemplateNotFoundError(e));
        } catch (ParserConfigurationException | SAXException e) {
            // Error while parsing arguments document
            return new DynamicContentResponse(null, errorHandler.handleArgumentsParseException(e));
        } catch (Exception e) {
            // Catch IOExceptions and such and handle as internal error
            return new DynamicContentResponse(null, errorHandler.handleServerError(e));
        }
    }

    private Element getArgumentsDocument(InputStream argsDocumentStream)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Element document = db.parse(argsDocumentStream).getDocumentElement();
        argsDocumentStream.close();

        // Check if the XML root is ok
        if (!document.getNodeName().equalsIgnoreCase(ARGS_ROOT_NODE_NAME)) {
            // This will be handled by the ErrorHandler
            throw new IllegalArgumentException(String.format("Invalid \"document.xml\"," +
                    " root should be \"<%s>\", found \"<%s>\".", ARGS_ROOT_NODE_NAME, document.getNodeName()));
        }
        return document;
    }

    private TemplateProcessor getTemplateProc(Element argsDocument) {
        // Get the template ID (or use default)
        String templateID = argsDocument.getAttribute(ARGS_ROOT_ATTRIBUTE_TEMPLATE_ID);
        TemplateProcessor templateProcessor =
                templateRegistry.getTemplateById(templateID.isEmpty() ? DEFAULT_TEMPLATE_ID : templateID);
        if (templateProcessor == null)
            throw new NoSuchElementException("Cannot find a template with ID \"" + templateID + "\".");
        return templateProcessor;
    }

    public static class DynamicContentResponse {

        private final TemplateProcessor selectedProcessor;
        private final InputStream responseStream;

        public DynamicContentResponse(TemplateProcessor selectedProcessor, InputStream responseStream) {
            this.selectedProcessor = selectedProcessor;
            this.responseStream = responseStream != null ? responseStream : EMPTY_STREAM;
        }

        public TemplateProcessor getSelectedProcessor() {
            return selectedProcessor;
        }

        public InputStream getResponseStream() {
            return responseStream;
        }

    }

}
