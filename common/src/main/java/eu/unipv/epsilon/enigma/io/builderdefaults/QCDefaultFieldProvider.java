package eu.unipv.epsilon.enigma.io.builderdefaults;

/**
 * Classes implementing this interface can be used to generate default values for
 * {@link eu.unipv.epsilon.enigma.quest.QuestCollection}s.
 *
 * The implementation should be passed to a {@link eu.unipv.epsilon.enigma.io.QuestCollectionBuilder}
 * through a {@link BuilderDefaultsFactory}, that will use it to generate default values in the case
 * that the source metadata does not declare them explicitly.
 */
public interface QCDefaultFieldProvider {

    /** Creates a default name for a quest collection. */
    String genName();

    /** Creates a default path for a quest icon. */
    String genIconPath();

}