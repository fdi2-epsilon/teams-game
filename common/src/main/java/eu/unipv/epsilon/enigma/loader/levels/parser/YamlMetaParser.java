package eu.unipv.epsilon.enigma.loader.levels.parser;

import com.esotericsoftware.yamlbeans.YamlReader;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.DefaultsFactory;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.FieldProvider;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class YamlMetaParser implements MetadataParser {

    private String collectionId;
    private DefaultsFactory defaultsFactory;
    
    public YamlMetaParser(String collectionId, DefaultsFactory defaultsFactory) {
        this.collectionId = collectionId;
        this.defaultsFactory = defaultsFactory;
    }

    @Override
    public QuestCollection loadCollectionMetadata(InputStream in) throws IOException {
        YamlReader yamlReader = new YamlReader(new InputStreamReader(in));
        return generateCollection((Map) yamlReader.read());
    }

    @SuppressWarnings("unchecked")
    private QuestCollection generateCollection(Map meta) {
        QuestCollection qc = new QuestCollection();
        FieldProvider defaults = defaultsFactory.getCollectionDefaults();

        qc.setId(collectionId);
        qc.setTitle(valueOrDefault(meta, KEY_QUESTCOLLECTION_TITLE, defaults));
        qc.setSubtitle(valueOrDefault(meta, KEY_QUESTCOLLECTION_SUBTITLE, defaults));
        qc.setDescription(valueOrDefault(meta, KEY_QUESTCOLLECTION_DESCRIPTION, defaults));

        String iconPathStr = valueOrDefault(meta, KEY_QUESTCOLLECTION_PATH_ICON, defaults);
        qc.setIconUrl(LevelAssetsURLStreamHandler.createURL(collectionId, iconPathStr));

        List quests = (List) meta.get(KEY_QUESTCOLLECTION_ELEMENTS);
        if (quests != null)
            for (int i = 0; i < quests.size(); i++)
                qc.addQuest(generateQuest(i, (Map) quests.get(i)));

        return qc;
    }

    @SuppressWarnings("unchecked")
    private Quest generateQuest(int index, Map meta) {
        Quest q = new Quest();
        FieldProvider defaults = defaultsFactory.getQuestDefaults(index);

        q.setName(valueOrDefault(meta, KEY_QUEST_NAME, defaults));
        q.setDescription(valueOrDefault(meta, KEY_QUEST_DESCRIPTION, defaults));

        Map paths = (Map) meta.get(KEY_QUEST_PATH_NODE);
        // Following statements: check if 'paths' node collection exists moved inside 'valueOrDefault'

        String mainDocPathStr = valueOrDefault(paths, KEY_QUEST_PATH_MAINDOCUMENT, defaults);
        q.setMainDocumentUrl(LevelAssetsURLStreamHandler.createURL(collectionId, mainDocPathStr));

        String infoDocPathStr = valueOrDefault(paths, KEY_QUEST_PATH_INFODOCUMENT, defaults);
        q.setInfoDocumentUrl(LevelAssetsURLStreamHandler.createURL(collectionId, infoDocPathStr));

        String iconPathStr = valueOrDefault(paths, KEY_QUEST_PATH_ICON, defaults);
        q.setIconUrl(LevelAssetsURLStreamHandler.createURL(collectionId, iconPathStr));

        return q;
    }

    private String valueOrDefault(Map<String, String> collection, String key, FieldProvider def) {
        if (collection != null) {
            // Because Scala collections have 'getOrElse' and Java's 'Optional is the poor man's 'Option[T]'...
            @SuppressWarnings("unchecked")
            String val = collection.get(key);
            return val != null ? val : def.getPropertyValue(key);
        }
        else return def.getPropertyValue(key);
    }

}
