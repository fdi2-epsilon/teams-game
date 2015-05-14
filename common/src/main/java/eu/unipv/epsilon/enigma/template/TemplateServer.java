package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.error.DefaultErrorHandler;
import eu.unipv.epsilon.enigma.template.error.ErrorHandler;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

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

    TemplateRegistry getRegistry() {
        return templateRegistry;
        // So we can load another collection templates
    }

    /**
     * Dynamically creates a document described by the passed XML arguments stream.
     * Depending on the used {@link ErrorHandler}, the returned document may be an styled error message.
     *
     * @param argsDocumentStream an {@link InputStream} to an XML arguments document ({@code document.xml} in EQC)
     * @return the processed view data stream, usually an HTML page
     */
    InputStream getDynamicContentStream(InputStream argsDocumentStream) {
        try {
            // Parse and validate the document
            Element document = getArgumentsDocument(argsDocumentStream);

            // Obtain a template processor and trigger a new document generation event with args
            TemplateProcessor proc = getTemplateProc(document);
            DocumentGenerationEvent event = new DocumentGenerationEvent(document);
            proc.generateDocument(event);

            // Return the generated output
            InputStream output = event.getResponseStream();
            return output != null ? output : EMPTY_STREAM;

        } catch (Exception e) {
            // Catch all and handle with ErrorHandler
            InputStream errorDocumentStream = errorHandler.handleArgumentsParseException(e);
            if (errorDocumentStream == null) return EMPTY_STREAM;
            return errorDocumentStream;
        }
    }

    private Element getArgumentsDocument(InputStream argsDocumentStream) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Element document = db.parse(argsDocumentStream).getDocumentElement();

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
        return templateRegistry.getTemplateById(templateID.isEmpty() ? DEFAULT_TEMPLATE_ID : templateID);
    }

}
