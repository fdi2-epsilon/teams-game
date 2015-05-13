package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * A template processor class wrapper to handle all the reflection stuff.
 */
public class TemplateProcessor {

    private final Class<?> clazz;

    private Method generationEventHandler = null;

    public TemplateProcessor(Class<?> clazz) {
        this.clazz = clazz;
        findDocumentGenerationEventHandler();
    }

    private void findDocumentGenerationEventHandler() {
        // Matches the first 'EventHandler' annotated method with a 'DocumentGenerationEvent' parameter
        for (Method m : clazz.getMethods()) {
            if (m.isAnnotationPresent(Template.EventHandler.class) &&
                    Arrays.equals(m.getParameterTypes(), new Class<?>[] {DocumentGenerationEvent.class})) {
                generationEventHandler = m;
                System.out.println("found! " + m.getName());
                break;
            }
        }
    }

}
