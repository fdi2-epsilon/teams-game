package eu.unipv.epsilon.enigma.io;

import eu.unipv.epsilon.enigma.io.builderdefaults.BuilderDefaultsFactory;
import eu.unipv.epsilon.enigma.io.builderdefaults.DefaultFieldProvider;
import eu.unipv.epsilon.enigma.io.builderdefaults.RefBuilderDefaults;
import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DefaultQuestCollectionBuilder implements QuestCollectionBuilder {

    public static final String FILENAME_PACK_CONFIGURATION = "metadata.yaml";

    BuilderDefaultsFactory<String> builderDefaults;

    public DefaultQuestCollectionBuilder() {
        this(new RefBuilderDefaults());
    }

    public DefaultQuestCollectionBuilder(BuilderDefaultsFactory<String> builderDefaults) {
        this.builderDefaults = builderDefaults;
    }

    @Override
    public QuestCollection createCollectionFromFile(File file) throws IOException {
        try (ZipFile zip = new ZipFile(file)) {
            InputStream stream = zip.getInputStream(new ZipEntry(FILENAME_PACK_CONFIGURATION));
            // Zip file needs to be closed, however its derived streams not.
            return generateCollection((Map) new Yaml().load(stream));
        }
    }

    private QuestCollection generateCollection(Map meta) {
        QuestCollection qc = new QuestCollection();
        DefaultFieldProvider<String> defaults = builderDefaults.getCollectionDefaults();

        qc.setName(valueOrDefault(meta, KEY_QUESTCOLLECTION_NAME, defaults));
        qc.setDescription(valueOrDefault(meta, KEY_QUESTCOLLECTION_DESCRIPTION, defaults));
        qc.setIconPath(valueOrDefault(meta, KEY_QUESTCOLLECTION_PATH_ICON, defaults));

        List quests = (List) meta.get(KEY_QUESTCOLLECTION_ELEMENTS);
        if (quests != null)
            for (int i = 0; i < quests.size(); i++) qc.addQuest(generateQuest(i, (Map) quests.get(i)));

        return qc;
    }

    private Quest generateQuest(int index, Map meta) {
        Quest q = new Quest();
        DefaultFieldProvider<String> defaults = builderDefaults.getQuestDefaults(index);

        q.setName(valueOrDefault(meta, KEY_QUEST_NAME, defaults));
        q.setDescription(valueOrDefault(meta, KEY_QUEST_DESCRIPTION, defaults));

        Map paths = (Map) meta.get(KEY_QUEST_PATH_NODE);
        // Check if 'paths' node collection exists moved inside 'valueOrDefault'
        q.setMainDocumentPath(valueOrDefault(paths, KEY_QUEST_PATH_MAINDOCUMENT, defaults));
        q.setInfoDocumentPath(valueOrDefault(paths, KEY_QUEST_PATH_INFODOCUMENT, defaults));
        q.setIconPath(valueOrDefault(paths, KEY_QUEST_PATH_ICON, defaults));

        return q;
    }

    private <T> T valueOrDefault(Map collection, String key, DefaultFieldProvider<T> def) {
        if (collection != null) {
            // Because Scala collections have 'getOrElse' and Java's 'Optional' is the poor man's 'Option[T]'...
            @SuppressWarnings("unchecked")
            T val = (T) collection.get(key);
            // Java 8's ugly way: return Optional.ofNullable(val).orElseGet(def);
            return val != null ? val : def.getPropertyDefaultValue(key);
        }
        else return def.getPropertyDefaultValue(key);
    }

}
