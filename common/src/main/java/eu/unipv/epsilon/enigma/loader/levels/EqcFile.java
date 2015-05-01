package eu.unipv.epsilon.enigma.loader.levels;

import eu.unipv.epsilon.enigma.loader.levels.parser.MetadataParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.XmlMetaParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.YamlMetaParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.DefaultsFactory;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class EqcFile implements CollectionContainer {

    public static final Logger logger = LoggerFactory.getLogger(EqcFile.class);
    public static final String FILE_EXTENSION = "eqc";

    ZipFile zipFile;

    public EqcFile(File file) throws IOException {
        this.zipFile = new ZipFile(file);
        logger.debug("Opened container \"%s\"", zipFile.getName());
    }

    @Override
    public QuestCollection loadCollectionMeta() throws IOException {
        MetadataParser parser;

        if (containsEntry("metadata.yaml"))
            parser = new YamlMetaParser(getEntry("metadata.yaml").getStream(), new DefaultsFactory(this));
        else if (containsEntry("metadata.xml"))
            parser = new XmlMetaParser(getEntry("metadata.xml").getStream(), new DefaultsFactory(this));
        else
            throw new IOException("Collection metadata not found");

        return parser.loadCollectionMetadata();
    }

    @Override
    public boolean containsEntry(String entryPath) {
        return (zipFile.getEntry(entryPath) != null);
    }

    @Override
    public ContainerEntry getEntry(String entryPath) throws IOException {
        return new EqcFileEntry(zipFile, entryPath);
    }

    @Override
    protected void finalize() throws Throwable {
        logger.debug("Closing container \"%s\" (garbage collected)", zipFile.getName());
        super.finalize();
        zipFile.close();
    }

}
