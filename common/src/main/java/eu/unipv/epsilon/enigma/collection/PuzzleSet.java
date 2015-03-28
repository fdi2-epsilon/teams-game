package eu.unipv.epsilon.enigma.collection;

import eu.unipv.epsilon.enigma.puzzle.Puzzle;

import java.util.LinkedList;
import java.util.List;

public class PuzzleSet {

    String name;
    List<Puzzle> puzzles = new LinkedList<>();

    public String getName() {
        return name;
    }

    public static PuzzleSet fromFile(String path) {
        return null;
    }

}
