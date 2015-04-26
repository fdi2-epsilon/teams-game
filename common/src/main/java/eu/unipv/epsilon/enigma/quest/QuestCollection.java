package eu.unipv.epsilon.enigma.quest;

import eu.unipv.epsilon.enigma.io.DefaultQuestCollectionBuilder;
import eu.unipv.epsilon.enigma.io.QuestCollectionBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class QuestCollection implements Serializable {

    /**
     * Used to reference this collection in saved data structures.
     * Usually initialized to the collection source filename, without extension.
     */
    private String id;

    private String name;
    private String description;
    private URL iconUrl;
    private List<Quest> quests = new LinkedList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
    }

    public URL getIconUrl() {
        return iconUrl;
    }

    public void addQuest(Quest quest) {
        this.quests.add(quest);
    }

    public Quest get(int index) {
        return quests.get(index);
    }

    public int size() {
        return quests.size();
    }

    /* STATIC BUILDER UTILITIES */

    private static QuestCollectionBuilder builder = new DefaultQuestCollectionBuilder();

    public static void setBuilder(QuestCollectionBuilder builder) {
        QuestCollection.builder = builder;
    }

    public static QuestCollection fromFile(String path) throws IOException {
        return QuestCollection.fromFile(new File(path));
    }

    public static QuestCollection fromFile(File file) throws IOException {
        return builder.createCollectionFromFile(file);
    }

}
