package eu.unipv.epsilon.enigma.loader.levels.pool;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;
import eu.unipv.epsilon.enigma.loader.levels.EqcFile;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

public class DirectoryPool extends CollectionsPool {

    File baseDirectory;

    public DirectoryPool(File baseDirectory) {
        if (!baseDirectory.isDirectory())
            throw new IllegalArgumentException(baseDirectory.getPath() + "is not a directory.");

        this.baseDirectory = baseDirectory;
    }

    @Override
    protected CollectionContainer createCollectionContainer(String id) throws IOException {
        return new EqcFile(id, getFileFromID(id));
    }

    @Override
    public boolean containsCollection(String id) {
        return getFileFromID(id).isFile();
    }

    @Override
    public TreeSet<String> getStoredCollectionIDs() {
        File[] files = baseDirectory.listFiles();
        if (files == null) return new TreeSet<>();

        TreeSet<String> names = new TreeSet<>();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith("." + EqcFile.CONTAINER_FILE_EXTENSION)) {
                int extIndex = file.getName().lastIndexOf('.');
                names.add(file.getName().substring(0, extIndex));
            }
        }
        return names;
    }

    private File getFileFromID(String id) {
        return new File(baseDirectory, id + '.' + EqcFile.CONTAINER_FILE_EXTENSION);
    }

}
