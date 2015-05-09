package eu.unipv.epsilon.enigma.template.reflect.classfinder;

import java.util.List;

public abstract class ScanAlgorithm {

    protected final ClassLoader classLoader;

    public ScanAlgorithm(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected static final String CLASS_FILE_EXT = ".class";

    protected static String stripClassExtension(String filePath) {
        return filePath.substring(0, filePath.length() - CLASS_FILE_EXT.length());
    }

    public abstract List<Class<?>> scan(String packageName) throws ClassNotFoundException;

}
