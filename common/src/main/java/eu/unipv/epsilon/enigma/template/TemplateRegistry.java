package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.util.AnnotationFilter;
import eu.unipv.epsilon.enigma.template.util.FilteredIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TemplateRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateRegistry.class);

    private CandidateClassSource classSource;
    private Map<String, TemplateProcessor> localTemplates = new HashMap<>();
    private String currentlyRegisteredCollectionID = "";
    private Map<String, TemplateProcessor> collectionTemplates = new HashMap<>();

    public TemplateRegistry(CandidateClassSource classSource) {
        this.classSource = classSource;

        // Register local templates
        registerAnnotatedClasses(classSource.getLocalCandidateClasses(), localTemplates);
    }

    public void registerCollectionTemplates(String collectionId) {
        if (!collectionId.equalsIgnoreCase(currentlyRegisteredCollectionID)) {
            collectionTemplates.clear();
            registerAnnotatedClasses(classSource.getCollectionCandidateClasses(collectionId), collectionTemplates);
            currentlyRegisteredCollectionID = collectionId;
        }
    }

    /**
     * Gets a {@link TemplateProcessor} registered with the passed in ID.
     * If the class is not found in the system registry, a search in the collection registry is performed.
     * @param id the ID if the desired {@link TemplateProcessor}
     * @return the corresponding {@link TemplateProcessor} or {@code null} if not found
     */
    public TemplateProcessor getTemplateById(String id) {
        if (localTemplates.containsKey(id)) return localTemplates.get(id);
        else return collectionTemplates.get(id); // It is ok that we return null
    }

    private void registerAnnotatedClasses(Iterator<Class<?>> classes, Map<String, TemplateProcessor> registry) {
        Iterator<Class<?>> templateClasses = new FilteredIterator<>(
                classes, new AnnotationFilter(Template.class));

        while (templateClasses.hasNext()) {
            Class<?> templateClass = templateClasses.next();
            Template meta = templateClass.getAnnotation(Template.class);
            LOGGER.info("Registered template \"{}\" ({})", meta.id(), templateClass.getName());
            registry.put(meta.id(), new TemplateProcessor(templateClass));
        }
    }

}
