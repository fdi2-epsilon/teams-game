package eu.unipv.epsilon.enigma.quest;

import java.io.Serializable;
import java.net.URL;

public class Quest implements Serializable {

    private String name;
    private String description;

    private URL iconUrl;
    private URL mainDocumentUrl;
    private URL infoDocumentUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
    }

    public URL getMainDocumentUrl() {
        return mainDocumentUrl;
    }

    public void setMainDocumentUrl(URL mainDocumentUrl) {
        this.mainDocumentUrl = mainDocumentUrl;
    }

    public URL getInfoDocumentUrl() {
        return infoDocumentUrl;
    }

    public void setInfoDocumentUrl(URL infoDocumentUrl) {
        this.infoDocumentUrl = infoDocumentUrl;
    }

}
