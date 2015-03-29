package eu.unipv.epsilon.enigma.io.builderdefaults;

public class RefQuestDefaults implements QuestDefaultFieldProvider {

    int nameIndex;

    public RefQuestDefaults(int index) {
        nameIndex = index + 1;
    }

    @Override
    public String genName() {
        return "Quest #" + nameIndex;
    }

    @Override
    public String genDescription() {
        return "";
    }

    @Override
    public String genMainDocumentPath() {
        return String.format("quests/%02d/index.html", nameIndex);
    }

    @Override
    public String genInfoDocumentPath() {
        return String.format("quests/%02d/story.html", nameIndex);
    }

    @Override
    public String genIconPath() {
        return String.format("quests/%02d/icon.png", nameIndex);
    }

}
