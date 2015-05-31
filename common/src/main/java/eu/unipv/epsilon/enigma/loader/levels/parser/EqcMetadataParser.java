package eu.unipv.epsilon.enigma.loader.levels.parser;

import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.ContentChecker;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.DefaultsFactory;
import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.IOException;
import java.io.InputStream;

/**
 * Metadata parser for EQC files.
 * This will select the right parser implementation depending on the metadata format found in the collection container.
 */
public class EqcMetadataParser implements MetadataParser {

    public static final String CONFIG_YAML_FILENAME = "metadata.yaml";
    public static final String CONFIG_XML_FILENAME = "metadata.xml";

    private final String selectedMetadataFilePath;
    private final MetadataParser parserImpl;

    public EqcMetadataParser(String collectionId, ContentChecker contentChecker) throws MetadataNotFoundException {
        if (contentChecker.containsEntry(CONFIG_YAML_FILENAME)) {
            selectedMetadataFilePath = CONFIG_YAML_FILENAME;
            parserImpl = new YamlMetaParser(collectionId, new DefaultsFactory(contentChecker));
        } else if (contentChecker.containsEntry(CONFIG_XML_FILENAME)) {
            selectedMetadataFilePath = CONFIG_XML_FILENAME;
            parserImpl = new XmlMetaParser(collectionId, new DefaultsFactory(contentChecker));
        } else
            throw new MetadataNotFoundException(collectionId);
    }

    public String getSelectedMetadataFilePath() {
        return selectedMetadataFilePath;
    }

    @Override
    public QuestCollection loadCollectionMetadata(InputStream in) throws IOException {
        return parserImpl.loadCollectionMetadata(in);
    }

}
