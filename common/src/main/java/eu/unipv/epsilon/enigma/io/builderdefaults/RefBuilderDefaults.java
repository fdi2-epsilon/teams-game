package eu.unipv.epsilon.enigma.io.builderdefaults;

public class RefBuilderDefaults implements BuilderDefaultsFactory<String> {

    public DefaultFieldProvider getCollectionDefaults() {
        return new QuestCollectionDefaults();
    }

    public DefaultFieldProvider getQuestDefaults(int index) {
        return new QuestDefaults(index);
    }

}
