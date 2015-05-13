package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import org.junit.Test;

public class TemplateRegistryTest {

    @Test
    public void testTemplateRegistry() {
        new TemplateRegistry(new JvmCandidateClassSource(new GameAssetsSystem()));
    }

}