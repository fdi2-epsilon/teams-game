package eu.unipv.epsilon.enigma.quest.status;

/**
 * A specification interface defining method names that can be accessed from the quiz sandbox to get environment
 * data and store game completion status.
 *
 * Since all quizzes are actually rich HTML pages, the idea is to map an implementation of this interface to
 * a JavaScript object accessible from the page.
 *
 * Game status storage is implementation defined.
 */
public interface QuestViewInterface {

    /** Allows a quiz to mark itself as completed. */
    void setComplete();

    /** Allows a quiz to check if it is already solved or not. */
    boolean getComplete();

}
