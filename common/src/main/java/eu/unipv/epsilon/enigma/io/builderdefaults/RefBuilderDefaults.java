package eu.unipv.epsilon.enigma.io.builderdefaults;

public class RefBuilderDefaults implements BuilderDefaultsFactory {

    public QCDefaultFieldProvider getCollectionDefaults() {
        return new RefQCDefaults();
    }

    public QuestDefaultFieldProvider getQuestDefaults(int index) {
        return new RefQuestDefaults(index);
    }

}
