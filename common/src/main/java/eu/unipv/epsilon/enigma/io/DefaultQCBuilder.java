package eu.unipv.epsilon.enigma.io;

import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DefaultQCBuilder implements QCBuilder {

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

        qc.setName(Optional.ofNullable((String) meta.get("name")).orElseGet(() -> "Unnamed collection"));
        qc.setIconPath(Optional.ofNullable((String) meta.get("icon")).orElseGet(() -> "pack.png"));

        List quests = (List) meta.get("quests");
        if (quests != null)
            for (int i = 0; i < quests.size(); i++) qc.addQuest(generateQuest((Map) quests.get(i)));

        return qc;
    }

    private Quest generateQuest(Map meta) {
        Quest q = new Quest();

        q.setName(Optional.ofNullable((String) meta.get("name")).orElseGet(() -> "Unnamed quest"));
        q.setDescription(Optional.ofNullable((String) meta.get("description")).orElseGet(() -> ""));

        Map paths = (Map) meta.get("paths");
        if (paths != null) {
            q.setMainDocumentPath(Optional.ofNullable((String) meta.get("main-document")).orElseGet(() -> "---"));
            q.setInfoDocumentPath(Optional.ofNullable((String) meta.get("info-document")).orElseGet(() -> "---"));
            q.setIconPath(Optional.ofNullable((String) meta.get("icon")).orElseGet(() -> "---"));
        } else {
            q.setMainDocumentPath("---");
            q.setInfoDocumentPath("---");
            q.setIconPath("---");
        }

        return q;
    }

}
