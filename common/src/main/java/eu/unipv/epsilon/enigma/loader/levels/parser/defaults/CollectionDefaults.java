package eu.unipv.epsilon.enigma.loader.levels.parser.defaults;

import java.util.NoSuchElementException;

import static eu.unipv.epsilon.enigma.loader.levels.parser.MetadataParser.*;

public class CollectionDefaults implements FieldProvider {

    ContentChecker context;

    public CollectionDefaults(ContentChecker context) {
        this.context = context;
    }

    @Override
    public String getPropertyValue(String property) {
        switch (property) {
            case KEY_QUESTCOLLECTION_TITLE:         return "Unnamed collection";
            case KEY_QUESTCOLLECTION_SUBTITLE:      return "";
            case KEY_QUESTCOLLECTION_DESCRIPTION:   return "";
            case KEY_QUESTCOLLECTION_PATH_ICON:
                if (context.containsEntry("pack.png"))
                    return "pack.png";
                if (context.containsEntry("pack.jpg"))
                    return "pack.jpg";
                return "";
            default:
                throw new NoSuchElementException("No default property value for \"" + property + '"');
        }
    }

}
