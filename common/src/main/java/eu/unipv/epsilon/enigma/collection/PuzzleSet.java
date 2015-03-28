package eu.unipv.epsilon.enigma.collection;

import eu.unipv.epsilon.enigma.quest.Quest;

import java.util.LinkedList;
import java.util.List;

public class PuzzleSet {

    String name;
    List<Quest> quests = new LinkedList<>();

    public String getName() {
        return name;
    }

    public static PuzzleSet fromFile(String path) {
        return null;
    }

}
