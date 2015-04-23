package eu.unipv.epsilon.enigma.io;

import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.File;
import java.io.IOException;

public interface QuestCollectionBuilder {

    /* NOTE: Interface fields are always public, static and final. */

    String KEY_QUESTCOLLECTION_NAME = "name";
    String KEY_QUESTCOLLECTION_DESCRIPTION = "description";
    String KEY_QUESTCOLLECTION_PATH_ICON = "icon";
    String KEY_QUESTCOLLECTION_ELEMENTS = "quests";

    String KEY_QUEST_NAME = "name";
    String KEY_QUEST_DESCRIPTION = "description";
    String KEY_QUEST_PATH_NODE = "paths";
    String KEY_QUEST_PATH_MAINDOCUMENT = "main-document";
    String KEY_QUEST_PATH_INFODOCUMENT = "info-document";
    String KEY_QUEST_PATH_ICON = "icon";

    /** Populates a new Quest Collection from the metadata inside the given file. */
    QuestCollection createCollectionFromFile(File file) throws IOException;

}
