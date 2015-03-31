package eu.unipv.epsilon.enigma.io.builderdefaults;

public class QuestDefaults implements DefaultFieldProvider<String> {

    int nameIndex;

    public QuestDefaults(int index) {
        nameIndex = index + 1;
    }

    @Override
    public String getPropertyDefaultValue(String property) {
        switch (property) {
            case "name":            return "Quest #" + nameIndex;
            case "description":     return "";
            case "main-document":   return String.format("quests/%02d/index.html", nameIndex);
            case "info-document":   return String.format("quests/%02d/story.html", nameIndex);
            case "icon":            return String.format("quests/%02d/icon.png", nameIndex);
        }
        return null;
    }

}
