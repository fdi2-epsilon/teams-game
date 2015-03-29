package eu.unipv.epsilon.enigma.io;

import eu.unipv.epsilon.enigma.io.builderdefaults.BuilderDefaultsFactory;
import eu.unipv.epsilon.enigma.io.builderdefaults.QCDefaultFieldProvider;
import eu.unipv.epsilon.enigma.io.builderdefaults.QuestDefaultFieldProvider;
import eu.unipv.epsilon.enigma.io.builderdefaults.RefBuilderDefaults;
import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DefaultQuestCollectionBuilder implements QuestCollectionBuilder {

    BuilderDefaultsFactory builderDefaults;

    public DefaultQuestCollectionBuilder() {
        this(new RefBuilderDefaults());
    }

    public DefaultQuestCollectionBuilder(BuilderDefaultsFactory builderDefaults) {
        this.builderDefaults = builderDefaults;
    }

    @Override
    public QuestCollection createFromFile(File file) throws IOException {
        try (ZipFile zip = new ZipFile(file)) {
            InputStream stream = zip.getInputStream(new ZipEntry("metadata.yaml"));
            // Zip file needs to be closed, however its derived streams not.
            return generateCollection((Map) new Yaml().load(stream));
        }
    }

    private QuestCollection generateCollection(Map meta) {
        QuestCollection qc = new QuestCollection();
        QCDefaultFieldProvider defaults = builderDefaults.getCollectionDefaults();

        qc.setName(valueOrDefault(meta, "name", defaults::genName));
        qc.setIconPath(valueOrDefault(meta, "icon", defaults::genIconPath));

        List quests = (List) meta.get("quests");
        if (quests != null)
            for (int i = 0; i < quests.size(); i++) qc.addQuest(generateQuest(i, (Map) quests.get(i)));

        return qc;
    }

    private Quest generateQuest(int index, Map meta) {
        Quest q = new Quest();
        QuestDefaultFieldProvider defaults = builderDefaults.getQuestDefaults(index);

        q.setName(Optional.ofNullable((String) meta.get("name")).orElseGet(defaults::genName));
        q.setDescription(Optional.ofNullable((String) meta.get("description")).orElseGet(defaults::genDescription));

        Map paths = (Map) meta.get("paths");
        if (paths != null) {
            q.setMainDocumentPath(valueOrDefault(paths, "main-document", defaults::genMainDocumentPath));
            q.setInfoDocumentPath(valueOrDefault(paths, "info-document", defaults::genInfoDocumentPath));
            q.setIconPath(valueOrDefault(paths, "icon", defaults::genIconPath));
        } else { // Here, the 'paths' node does not even exist.
            q.setMainDocumentPath(defaults.genMainDocumentPath());
            q.setInfoDocumentPath(defaults.genInfoDocumentPath());
            q.setIconPath(defaults.genIconPath());
        }

        return q;
    }

    private <T> T valueOrDefault(Map collection, String key, Supplier<T> def) {
        // Because Scala collections have 'getOrElse' and Java's 'Optional' is the poor man's 'Option[T]'...
        @SuppressWarnings("unchecked")
        T val = (T) collection.get(key);
        // Java 8's ugly way: return Optional.ofNullable(val).orElseGet(def);
        return val != null ? val : def.get();
    }

}
