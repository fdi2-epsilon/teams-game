package eu.unipv.epsilon.enigma.loader.levels.parser.defaults;

import java.util.NoSuchElementException;

import static eu.unipv.epsilon.enigma.loader.levels.parser.MetadataParser.*;

public class QuestDefaults implements FieldProvider {

    ContentChecker context;
    int nameIndex;

    public QuestDefaults(ContentChecker context, int index) {
        this.context = context;
        this.nameIndex = index + 1;
    }

    @Override
    public String getPropertyValue(String property) {
        switch (property) {
            case KEY_QUEST_NAME:                return "Quest #" + nameIndex;
            case KEY_QUEST_DESCRIPTION:         return "";
            case KEY_QUEST_PATH_MAINDOCUMENT:   return String.format("quests/%02d/", nameIndex); // Using redirection
            case KEY_QUEST_PATH_INFODOCUMENT:   return String.format("quests/%02d/story.html", nameIndex);
            case KEY_QUEST_PATH_ICON:           return String.format("quests/%02d/icon.png", nameIndex);
            default:
                throw new NoSuchElementException("No default property value for \"" + property + '"');
        }
    }

}
