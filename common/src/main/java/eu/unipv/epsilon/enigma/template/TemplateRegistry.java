package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.GameAssetsSystem;
import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.reflect.AssetsClassLoader;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.PackageScanner;
import eu.unipv.epsilon.enigma.template.util.AnnotationFilter;
import eu.unipv.epsilon.enigma.template.util.FilteredIterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TemplateRegistry {

    public static final String TEMPLATES_LOCATION_BUILTIN = "eu.unipv.epsilon.enigma.template.builtin";

    private HashMap<String, Class<?>> localTemplates = new HashMap<>();
    private HashMap<String, Class<?>> collectionTemplates = new HashMap<>();

    public TemplateRegistry() {
        registerLocalTemplates();
    }

    public void registerCollectionTemplates(GameAssetsSystem assetsSystem, String collectionId) {
        try {
            AssetsClassLoader classLoader = new AssetsClassLoader(assetsSystem, collectionId);
            List<Class<?>> classes = PackageScanner.getClassesInPackage("", classLoader, true);
            collectionTemplates.clear();
            registerAnnotatedClasses(classes, collectionTemplates);
        } catch (ClassNotFoundException e) {
            // No local templates or error
            e.printStackTrace();
        }
    }

    public Set<String> getLocalTemplateIDs() {
        return localTemplates.keySet();
    }

    private void registerLocalTemplates() {
        try {
            List<Class<?>> classes = PackageScanner.getClassesInPackage(TEMPLATES_LOCATION_BUILTIN);
            registerAnnotatedClasses(classes, localTemplates);
        } catch (ClassNotFoundException e) {
            // No local templates or error
            e.printStackTrace();
        }
    }

    private void registerAnnotatedClasses(Iterable<Class<?>> classes, HashMap<String, Class<?>> registry) {
        Iterator<Class<?>> templateClasses = new FilteredIterator<>(
                classes.iterator(), new AnnotationFilter(Template.class));

        while (templateClasses.hasNext()) {
            Class<?> templateClass = templateClasses.next();
            Template meta = templateClass.getAnnotation(Template.class);
            registry.put(meta.id(), templateClass);
        }
    }

}
