package eu.unipv.epsilon.enigma.loader.levels.parser.defaults;

import eu.unipv.epsilon.enigma.loader.levels.parser.MetadataParser;
import eu.unipv.epsilon.enigma.quest.QuestCollection;

/**
 * Used by a {@link MetadataParser} to get an implementation of {@link FieldProvider} in capable to assign
 * default {@link QuestCollection} values to fields not specified in the source metadata document.
 */
public interface IDefaultsFactory {

    /** Creates a provider for default quest collection metadata values. */
    FieldProvider getCollectionDefaults();

    /** Creates a provider for default quest metadata values using its index inside the collection. */
    FieldProvider getQuestDefaults(int index);

}
