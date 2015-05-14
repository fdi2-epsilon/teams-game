package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * An holder for template processor classes; handling all of the reflection stuff.
 */
public class TemplateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateProcessor.class);

    private final Class<?> clazz;
    private Method generationEventHandler = null;

    public TemplateProcessor(Class<?> clazz) {
        this.clazz = clazz;
        findDocumentGenerationEventHandler();
    }

    /**
     * Creates a new instance of this template processor and triggers a document generation event.
     *
     * @param event a document generation event to process
     * @throws ReflectiveOperationException if there was a problem during method invocation
     */
    public void generateDocument(DocumentGenerationEvent event) throws ReflectiveOperationException {
        Object inst = clazz.newInstance();
        // Throws InvocationTargetException in case of errors
        generationEventHandler.invoke(inst, event);
    }

    private void findDocumentGenerationEventHandler() {
        // Matches the first 'EventHandler' annotated method with a 'DocumentGenerationEvent' parameter
        for (Method m : clazz.getMethods()) {
            if (m.isAnnotationPresent(Template.EventHandler.class) &&
                    Arrays.equals(m.getParameterTypes(), new Class<?>[] {DocumentGenerationEvent.class})) {
                generationEventHandler = m;
                LOGGER.info("Found \"{}\" document generation event handler in class \"{}\".",
                        m.getName(), clazz.getName());
                break;
            }
        }
    }

}
