package eu.unipv.epsilon.enigma.quest;

import java.util.LinkedList;
import java.util.List;

public class QuestCollection {

    private String name;
    private String iconPath;
    private List<Quest> quests = new LinkedList<>();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
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

}
