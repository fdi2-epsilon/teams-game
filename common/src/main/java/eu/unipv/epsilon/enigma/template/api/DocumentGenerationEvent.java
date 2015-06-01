package eu.unipv.epsilon.enigma.template.api;

import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.template.api.xml.XmlTemplateArguments;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.net.URL;

/**
 * An event passed by the template server to a registered template processor when it should generate a document
 * for the view. Template processors need to call {@link #setResponseStream(InputStream)} to give the actual response
 * content.
 */
public class DocumentGenerationEvent {

    private final XmlTemplateArguments templateArguments;
    private final URL argsDocumentURL;
    private InputStream responseStream = null;

    public DocumentGenerationEvent(Element argumentsElement) {
        this(argumentsElement, null);
    }

    public DocumentGenerationEvent(Element argumentsElement, URL argsDocumentURL) {
        this.templateArguments = new XmlTemplateArguments(argumentsElement);
        this.argsDocumentURL = argsDocumentURL;
    }

    /**
     * Gets the arguments passed to this template processor.
     *
     * @return an instance of {@link TemplateArguments} to be queried for arguments
     */
    public TemplateArguments getArguments() {
        return templateArguments;
    }

    /**
     * Gets the root node of the XML arguments document.
     *
     * @return the XML arguments root element
     * @deprecated Use {@link #getArguments()} instead
     */
    @Deprecated
    public Element getArgumentsRaw() {
        return templateArguments.getDocumentElement();
    }

    /**
     * Gets the output response stream from the template processor or {@code null} if not given.
     */
    public InputStream getResponseStream() {
        return responseStream;
    }

    /**
     * Called by a template processor to set its response stream to be processed by the view.
     */
    public void setResponseStream(InputStream responseStream) {
        this.responseStream = responseStream;
    }

    /**
     * Returns false if this template processor was invoked from a raw input stream.<br>
     * You should <b>ALWAYS</b> check for this to be {@code true} before using {@link #getCollectionID()},
     * {@link #getBaseDir()} and {@link #createRelativePath(String)}, otherwise these function behavior is undefined.
     */
    public boolean hasPathData() {
        return argsDocumentURL != null;
    }

    /**
     * Gets the collection container ID that invoked this template processor.
     */
    public String getCollectionID() {
        if (argsDocumentURL == null)
            return null;
        return argsDocumentURL.getHost();
    }

    /**
     * Gets the directory path containing the template document being processed,
     * usually the quiz root directory inside the collection container.
     *
     * @return the base directory path inside the container
     */
    public String getBaseDir() {
        if (argsDocumentURL == null)
            return null;

        String path = argsDocumentURL.getPath();
        // Remove initial '/' and keep only directory
        return path.substring(1, path.lastIndexOf('/') + 1);
    }

    /**
     * Creates an URL to a resource relative to the template document file path inside the collection container.
     */
    public URL createRelativePath(String path) {
        return LevelAssetsURLStreamHandler.createURL(getCollectionID(), getBaseDir() + path);
    }

}
