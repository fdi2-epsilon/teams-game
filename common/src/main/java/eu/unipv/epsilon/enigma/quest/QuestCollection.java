package eu.unipv.epsilon.enigma.quest;

import eu.unipv.epsilon.enigma.io.DefaultQuestCollectionBuilder;
import eu.unipv.epsilon.enigma.io.QuestCollectionBuilder;

import java.io.File;
import java.io.IOException;
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
