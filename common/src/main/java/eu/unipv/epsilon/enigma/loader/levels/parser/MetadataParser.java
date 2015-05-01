package eu.unipv.epsilon.enigma.loader.levels.parser;

import eu.unipv.epsilon.enigma.quest.QuestCollection;

public interface MetadataParser {

    String KEY_QUESTCOLLECTION_TITLE = "name";
    String KEY_QUESTCOLLECTION_SUBTITLE = "subtitle";
    String KEY_QUESTCOLLECTION_DESCRIPTION = "description";
    String KEY_QUESTCOLLECTION_PATH_ICON = "icon";
    String KEY_QUESTCOLLECTION_ELEMENTS = "quests";

    String KEY_QUEST_NAME = "name";
    String KEY_QUEST_DESCRIPTION = "description";
    String KEY_QUEST_PATH_NODE = "paths";
    String KEY_QUEST_PATH_MAINDOCUMENT = "main-document";
    String KEY_QUEST_PATH_INFODOCUMENT = "info-document";
    String KEY_QUEST_PATH_ICON = "icon";

    QuestCollection loadCollectionMetadata();

}
