package eu.unipv.epsilon.enigma;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    // Private constructor if this class contains only static methods and fields
    private Application() { }

    public static void main(String[] args) {
        LOG.info("App!");
    }

}
