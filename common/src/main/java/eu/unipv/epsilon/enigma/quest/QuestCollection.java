package eu.unipv.epsilon.enigma.quest;

import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;

public class QuestCollection implements Serializable {

    /** Used to reference this collection in saved data structures. */
    private String id;

    private String title;
    private String subtitle;
    private String description;
    private URL iconUrl;

    // LinkedList<> maintains this fully serializable
    private LinkedList<Quest> quests = new LinkedList<>(); //NOSONAR

    public int size() {
        return quests.size();
    }

    public boolean isEmpty() {
        return quests.isEmpty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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

}
