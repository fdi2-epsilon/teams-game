package eu.unipv.epsilon.enigma.io.builderdefaults;

import static eu.unipv.epsilon.enigma.io.QuestCollectionBuilder.*;

public class QuestDefaults implements DefaultFieldProvider<String> {

    int nameIndex;

    public QuestDefaults(int index) {
        nameIndex = index + 1;
    }

    @Override
    public String getPropertyDefaultValue(String property) {
        switch (property) {
            case KEY_QUEST_NAME:                return "Quest #" + nameIndex;
            case KEY_QUEST_DESCRIPTION:         return "";
            case KEY_QUEST_PATH_MAINDOCUMENT:   return String.format("quests/%02d/index.html", nameIndex);
            case KEY_QUEST_PATH_INFODOCUMENT:   return String.format("quests/%02d/story.html", nameIndex);
            case KEY_QUEST_PATH_ICON:           return String.format("quests/%02d/icon.png", nameIndex);
        }
        return null;
    }

}
