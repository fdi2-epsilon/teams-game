package eu.unipv.epsilon.enigma.loader.levels;

import eu.unipv.epsilon.enigma.loader.levels.exception.MetadataNotFoundException;
import eu.unipv.epsilon.enigma.loader.levels.parser.MetadataParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.XmlMetaParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.YamlMetaParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.DefaultsFactory;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class EqcFile extends CollectionContainer {

    private static final Logger logger = LoggerFactory.getLogger(EqcFile.class);
    public static final String CONTAINER_FILE_EXTENSION = "eqc";
    public static final String CONFIG_YAML_FILENAME = "metadata.yaml";
    public static final String CONFIG_XML_FILENAME = "metadata.xml";

    String id;
    ZipFile zipFile;

    public EqcFile(String id, File file) throws IOException {
        this.id = id;
        this.zipFile = new ZipFile(file);
        logger.info("Opened container \"{}\"", zipFile.getName());
    }

    @Override
    public QuestCollection loadCollectionMeta() throws IOException {
        String fileName;
        MetadataParser parser;

        if (containsEntry(CONFIG_YAML_FILENAME)) {
            fileName = CONFIG_YAML_FILENAME;
            parser = new YamlMetaParser(id, new DefaultsFactory(this));
        }
        else if (containsEntry(CONFIG_XML_FILENAME)) {
            fileName = CONFIG_XML_FILENAME;
            parser = new XmlMetaParser(id, new DefaultsFactory(this));
        }
        else
            throw new MetadataNotFoundException(id);

        InputStream entryStream = getEntry(fileName).getStream();
        return parser.loadCollectionMetadata(entryStream);
    }

    @Override
    public boolean containsEntry(String entryPath) {
        return (zipFile.getEntry(entryPath) != null);
    }

    @Override
    public ContainerEntry getEntry(String entryPath) {
        if (containsEntry(entryPath)) return new EqcFileEntry(zipFile, entryPath);
        return null;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        logger.info("Closing container (invalidated) \"{}\"", zipFile.getName());
        try {
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
