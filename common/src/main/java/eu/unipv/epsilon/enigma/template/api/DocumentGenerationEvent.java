package eu.unipv.epsilon.enigma.template.api;

import org.w3c.dom.Element;

import java.io.InputStream;
import java.net.URL;

public class DocumentGenerationEvent {

    private final Element argumentsElement;
    private final URL argsDocumentURL;
    private InputStream responseStream = null;

    public DocumentGenerationEvent(Element argumentsElement) {
        this.argumentsElement = argumentsElement;
        this.argsDocumentURL = null;
    }

    public DocumentGenerationEvent(Element argumentsElement, URL argsDocumentURL) {
        this.argumentsElement = argumentsElement;
        this.argsDocumentURL = argsDocumentURL;
    }

    public Element getArguments() {
        return argumentsElement;
    }

    public InputStream getResponseStream() {
        return responseStream;
    }

    public void setResponseStream(InputStream responseStream) {
        this.responseStream = responseStream;
    }

    // TODO: JavaDoc
    public boolean hasPathData() {
        return argsDocumentURL != null;
    }

    // TODO: JavaDoc
    public String getCollectionID() {
        if (argsDocumentURL == null) return null;
        return argsDocumentURL.getHost();
    }

    // TODO: JavaDoc
    public String getBaseDir() {
        if (argsDocumentURL == null) return null;

        String path = argsDocumentURL.getPath();
        // Remove initial '/' and keep only directory
        return path.substring(1, path.lastIndexOf('/') + 1);
    }

}
