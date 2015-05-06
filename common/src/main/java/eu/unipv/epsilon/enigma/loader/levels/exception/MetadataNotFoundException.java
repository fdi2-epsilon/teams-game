package eu.unipv.epsilon.enigma.loader.levels.exception;

import java.io.IOException;

public class MetadataNotFoundException extends IOException {

    public MetadataNotFoundException(String id) {
        super("Collection metadata not found in collection \"" + id + "\".");
    }

}
