package eu.unipv.epsilon.enigma.template;

import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.reflect.classfinder.PackageScanner;
import eu.unipv.epsilon.enigma.template.util.AnnotationFilter;
import eu.unipv.epsilon.enigma.template.util.FilteredIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A registry to filter and hold template processors to be used by a {@link TemplateServer}.
 */
public class TemplateRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateRegistry.class);

    /** The package to search for built-in template processor classes */
    public static final String TEMPLATES_LOCATION_BUILTIN = "eu.unipv.epsilon.enigma.template.builtin";

    /** The package to search for templates in collection containers, "" means root package */
    public static final String TEMPLATES_LOCATION_COLLECTION_CONTAINER = "";

    private PackageScanner packageScanner;
    private AssetsClassLoaderFactory classLoaderFactory;

    private Map<String, TemplateProcessor> localTemplates = new HashMap<>();

    private String currentlyRegisteredCollectionID = "";
    private ClassLoader collectionClassLoader; // The class loader used to load templates in the current collection
    private Map<String, TemplateProcessor> collectionTemplates = new HashMap<>();

    public TemplateRegistry(PackageScanner packageScanner, AssetsClassLoaderFactory classLoaderFactory) {
        this.packageScanner = packageScanner;
        this.classLoaderFactory = classLoaderFactory;

        // Register local templates
        loadLocalClasses();
    }

    /**
     * Attempts to register any template processor defined inside the collection with the given ID
     * @param collectionId the id of the collection to search for template processors
     */
    public void registerCollectionTemplates(String collectionId) {
        if (!collectionId.equalsIgnoreCase(currentlyRegisteredCollectionID)) {
            collectionTemplates.clear();
            loadCollectionClasses(collectionId);
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
        if (localTemplates.containsKey(id))
            return localTemplates.get(id);
        else return collectionTemplates.get(id); // It is ok that we return null
    }

    /** Gets the class loader used to find the actual collection classes and resources. */
    public ClassLoader getCollectionClassLoader() {
        return collectionClassLoader;
    }

    // Finds and registers built-in template processors
    private void loadLocalClasses() {
        try {
            List<Class<?>> foundClasses = packageScanner.getClassesInPackage(TEMPLATES_LOCATION_BUILTIN);
            registerAnnotatedClasses(foundClasses.iterator(), localTemplates);
        } catch (ClassNotFoundException e) {
            // No need to return empty iterator, do not register nothing
            LOG.warn("No classes found or error, registering nothing", e);
        }
    }

    // Finds and registers template processors defined in a collection container
    private void loadCollectionClasses(String collectionId) {
        try {
            // The following line may throw an exception too, in that case catch it as usual skipping scan
            collectionClassLoader = classLoaderFactory.createAssetsClassLoader(collectionId);

            List<Class<?>> foundClasses =
                    packageScanner.getClassesInPackage(TEMPLATES_LOCATION_COLLECTION_CONTAINER, collectionClassLoader, true);
            registerAnnotatedClasses(foundClasses.iterator(), collectionTemplates);
        } catch (ClassNotFoundException e) {
            // Do not register nothing, old collection data is already cleared
            LOG.warn("No classes found or error, registering nothing", e);

            // But we need to set a class loader, we use the application one, used to load builtin template processors.
            // Otherwise, the ClasspathURLStreamHandler protocol fails to get internal resources.
            collectionClassLoader = getClass().getClassLoader();
        }
    }

    // Filters 'classes' and registers found template processors in the 'registry' map with their @Template.id as key
    private void registerAnnotatedClasses(Iterator<Class<?>> classes, Map<String, TemplateProcessor> registry) {
        Iterator<Class<?>> templateClasses = new FilteredIterator<>(
                classes, new AnnotationFilter(Template.class));

        while (templateClasses.hasNext()) {
            Class<?> templateClass = templateClasses.next();
            Template meta = templateClass.getAnnotation(Template.class);
            LOG.info("Registered template \"{}\" ({})", meta.id(), templateClass.getName());
            registry.put(meta.id(), new TemplateProcessor(templateClass));
        }
    }

}
