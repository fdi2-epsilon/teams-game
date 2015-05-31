package eu.unipv.epsilon.enigma.loader.levels;

import eu.unipv.epsilon.enigma.loader.levels.parser.EqcMetadataParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.ContentChecker;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class EqcFile extends CollectionContainer implements ContentChecker {

    private static final Logger LOG = LoggerFactory.getLogger(EqcFile.class);
    public static final String CONTAINER_FILE_EXTENSION = "eqc";

    String id;
    ZipFile zipFile;

    public EqcFile(String id, File file) throws IOException {
        this.id = id;
        this.zipFile = new ZipFile(file);
        LOG.info("Opened container \"{}\"", zipFile.getName());
    }

    @Override
    public QuestCollection loadCollectionMeta() throws IOException {
        EqcMetadataParser parser = new EqcMetadataParser(id, this);
        InputStream entryStream = getEntry(parser.getSelectedMetadataFilePath()).getStream();
        return parser.loadCollectionMetadata(entryStream);
    }

    @Override
    public boolean containsEntry(String entryPath) {
        return zipFile.getEntry(entryPath) != null;
    }

    @Override
    public ContainerEntry getEntry(String entryPath) {
        if (containsEntry(entryPath))
            return new EqcFileEntry(zipFile, entryPath);
        return null;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        LOG.info("Closing container (invalidated) \"{}\"", zipFile.getName());
        try {
            zipFile.close();
        } catch (IOException e) {
            LOG.error("Cannot close zip file!", e);
        }
    }

    public ZipFile getZipFile() {
        return zipFile;
    }

}
