package eu.unipv.epsilon.enigma.quest;

public class Quest {

    private String name;
    private String description;

    private String iconPath;
    private String mainDocumentPath;
    private String infoDocumentPath;

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

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getMainDocumentPath() {
        return mainDocumentPath;
    }

    public void setMainDocumentPath(String mainDocumentPath) {
        this.mainDocumentPath = mainDocumentPath;
    }

    public String getInfoDocumentPath() {
        return infoDocumentPath;
    }

    public void setInfoDocumentPath(String infoDocumentPath) {
        this.infoDocumentPath = infoDocumentPath;
    }

}
