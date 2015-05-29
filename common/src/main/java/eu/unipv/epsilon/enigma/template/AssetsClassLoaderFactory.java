package eu.unipv.epsilon.enigma.template;

/**
 * A factory used by {@link TemplateRegistry} to get a class loader capable to load classes defined in
 * collection containers. The implementation of this kind of class loader may be platform dependent.
 */
public interface AssetsClassLoaderFactory {

    /**
     * Creates a new assets class loader capable of loading classes in the specified collection.
     *
     * @param collectionId the id of the collection container being searched for templates
     * @return a class loader that can be used to load classes defined in the collection defined by the passed id
     * @throws ClassNotFoundException if there was a problem in finding a proper class loader; implementers may throw
     *                                this exception to skip package scanning in template-free containers.
     */
    ClassLoader createAssetsClassLoader(String collectionId) throws ClassNotFoundException;

}
