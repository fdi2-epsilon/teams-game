package eu.unipv.epsilon.enigma.io.builderdefaults;

import static eu.unipv.epsilon.enigma.io.QuestCollectionBuilder.KEY_QUESTCOLLECTION_NAME;
import static eu.unipv.epsilon.enigma.io.QuestCollectionBuilder.KEY_QUESTCOLLECTION_PATH_ICON;

public class QuestCollectionDefaults implements DefaultFieldProvider<String> {

    @Override
    public String getPropertyDefaultValue(String property) {
        switch (property) {
            case KEY_QUESTCOLLECTION_NAME:          return "Unnamed collection";
            case KEY_QUESTCOLLECTION_PATH_ICON:     return "pack.png";
        }
        return null;
    }

}