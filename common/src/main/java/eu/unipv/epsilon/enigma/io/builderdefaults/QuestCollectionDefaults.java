package eu.unipv.epsilon.enigma.io.builderdefaults;

import java.util.zip.ZipFile;

import static eu.unipv.epsilon.enigma.io.QuestCollectionBuilder.*;

public class QuestCollectionDefaults implements DefaultFieldProvider<String> {

    ZipFile zipFile;

    public QuestCollectionDefaults(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public String getPropertyDefaultValue(String property) {
        switch (property) {
            case KEY_QUESTCOLLECTION_NAME:          return "Unnamed collection";
            case KEY_QUESTCOLLECTION_DESCRIPTION:   return "";
            case KEY_QUESTCOLLECTION_PATH_ICON:
                if (zipFile.getEntry("pack.png") != null) return "pack.png";
                if (zipFile.getEntry("pack.jpg") != null) return "pack.jpg";
                return null;
        }
        return null;
    }

}
