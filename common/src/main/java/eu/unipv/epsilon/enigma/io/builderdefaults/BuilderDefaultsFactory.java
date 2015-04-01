package eu.unipv.epsilon.enigma.io.builderdefaults;

/**
 * Used by a {@link eu.unipv.epsilon.enigma.io.QuestCollectionBuilder} to get an implementation of
 * {@link DefaultFieldProvider} in order to assign default
 * {@link eu.unipv.epsilon.enigma.quest.QuestCollection} values to fields not specified in the source document.
 */
public interface BuilderDefaultsFactory<T> {

    /** Creates a provider for default quest collection metadata values. */
    DefaultFieldProvider<T> getCollectionDefaults();

    /** Creates a provider for default quest metadata values using the passed in index inside the collection. */
    DefaultFieldProvider<T> getQuestDefaults(int index);

}
