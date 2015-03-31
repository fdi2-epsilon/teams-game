package eu.unipv.epsilon.enigma.io.builderdefaults;

public class QuestCollectionDefaults implements DefaultFieldProvider<String> {

    @Override
    public String getPropertyDefaultValue(String property) {
        switch (property) {
            case "name":    return "Unnamed collection";
            case "icon":    return "pack.png";
        }
        return null;
    }

}
