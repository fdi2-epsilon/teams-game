package eu.unipv.epsilon.enigma.loader.levels;

import java.io.IOException;
import java.io.InputStream;

public interface ContainerEntry {

    long getSize();

    InputStream getStream() throws IOException;

}
