package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.util.AnnotationFilter;
import eu.unipv.epsilon.enigma.template.util.FilteredIterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class TemplateRegistry {

    private CandidateClassSource classSource;
    private HashMap<String, Class<?>> localTemplates = new HashMap<>();
    private HashMap<String, Class<?>> collectionTemplates = new HashMap<>();

    public TemplateRegistry(CandidateClassSource classSource) {
        this.classSource = classSource;

        // Register local templates
        registerAnnotatedClasses(classSource.getLocalCandidateClasses(), localTemplates);
    }

    public void registerCollectionTemplates(String collectionId) {
        collectionTemplates.clear();
        registerAnnotatedClasses(classSource.getCollectionCandidateClasses(collectionId), collectionTemplates);
    }

    public Set<String> getLocalTemplateIDs() {
        return localTemplates.keySet();
    }

    private void registerAnnotatedClasses(Iterator<Class<?>> classes, HashMap<String, Class<?>> registry) {
        Iterator<Class<?>> templateClasses = new FilteredIterator<>(
                classes, new AnnotationFilter(Template.class));

        while (templateClasses.hasNext()) {
            Class<?> templateClass = templateClasses.next();
            Template meta = templateClass.getAnnotation(Template.class);
            registry.put(meta.id(), templateClass);
        }
    }

}
