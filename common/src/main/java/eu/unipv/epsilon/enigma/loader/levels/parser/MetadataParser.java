package eu.unipv.epsilon.enigma.loader.levels.parser;

import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.IOException;
import java.io.InputStream;

/**
 * A {@link QuestCollection} metadata parser.
 *
 * @see YamlMetaParser
 * @see XmlMetaParser
 */
public interface MetadataParser {

    /**
     * Populates a new {@link QuestCollection} from the metadata inside the given {@link InputStream}.
     * Stream data format is implementation-specific.
     */
    QuestCollection loadCollectionMetadata(InputStream in) throws IOException;

}
