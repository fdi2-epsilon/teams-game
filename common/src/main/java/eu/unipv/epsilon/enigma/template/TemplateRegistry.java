package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.util.AnnotationFilter;
import eu.unipv.epsilon.enigma.template.util.FilteredIterator;

import java.util.HashMap;
import java.util.Iterator;

public class TemplateRegistry {

    private CandidateClassSource classSource;
    private HashMap<String, TemplateProcessor> localTemplates = new HashMap<>();
    private HashMap<String, TemplateProcessor> collectionTemplates = new HashMap<>();

    public TemplateRegistry(CandidateClassSource classSource) {
        this.classSource = classSource;

        // Register local templates
        registerAnnotatedClasses(classSource.getLocalCandidateClasses(), localTemplates);
    }

    public void registerCollectionTemplates(String collectionId) {
        collectionTemplates.clear();
        registerAnnotatedClasses(classSource.getCollectionCandidateClasses(collectionId), collectionTemplates);
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

    private void registerAnnotatedClasses(Iterator<Class<?>> classes, HashMap<String, TemplateProcessor> registry) {
        Iterator<Class<?>> templateClasses = new FilteredIterator<>(
                classes, new AnnotationFilter(Template.class));

        while (templateClasses.hasNext()) {
            Class<?> templateClass = templateClasses.next();
            Template meta = templateClass.getAnnotation(Template.class);
            registry.put(meta.id(), new TemplateProcessor(templateClass));
        }
    }

}
