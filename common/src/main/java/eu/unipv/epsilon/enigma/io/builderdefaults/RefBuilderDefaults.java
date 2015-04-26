package eu.unipv.epsilon.enigma.io.builderdefaults;

import java.util.zip.ZipFile;

public class RefBuilderDefaults implements BuilderDefaultsFactory<String> {

    public DefaultFieldProvider getCollectionDefaults(ZipFile zipFile) {
        return new QuestCollectionDefaults(zipFile);
    }

    public DefaultFieldProvider getQuestDefaults(int index) {
        return new QuestDefaults(index);
    }

}
