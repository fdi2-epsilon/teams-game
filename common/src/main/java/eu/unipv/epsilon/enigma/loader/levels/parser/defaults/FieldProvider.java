package eu.unipv.epsilon.enigma.loader.levels.parser.defaults;

import eu.unipv.epsilon.enigma.loader.levels.parser.MetadataParser;
import eu.unipv.epsilon.enigma.quest.QuestCollection;

/**
 * Implementers can be invoked by a {@link MetadataParser} to generate {@link QuestCollection} field values.
 *
 * Implement this to create default field providers called by the parser when it finds empty configuration
 * nodes as input. A working {@link IDefaultsFactory} implementation is needed to tell the parser to use your
 * class to handle empty fields.
 */
public interface FieldProvider {

    /** Creates a metadata property value given its name. */
    String getPropertyValue(String property);

}
