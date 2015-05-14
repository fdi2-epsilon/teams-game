package eu.unipv.epsilon.enigma.template.api;

import org.w3c.dom.Element;

import java.io.InputStream;

public class DocumentGenerationEvent {

    private final Element argumentsElement;
    private InputStream responseStream = null;

    public DocumentGenerationEvent(Element argumentsElement) {
        this.argumentsElement = argumentsElement;
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

}
