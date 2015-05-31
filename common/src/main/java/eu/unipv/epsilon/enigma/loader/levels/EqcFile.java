package eu.unipv.epsilon.enigma.loader.levels;

import eu.unipv.epsilon.enigma.loader.levels.parser.MetadataNotFoundException;
import eu.unipv.epsilon.enigma.loader.levels.parser.MetadataParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.XmlMetaParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.YamlMetaParser;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.ContentChecker;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.DefaultsFactory;
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
    public static final String CONFIG_YAML_FILENAME = "metadata.yaml";
    public static final String CONFIG_XML_FILENAME = "metadata.xml";

    String id;
    ZipFile zipFile;

    public EqcFile(String id, File file) throws IOException {
        this.id = id;
        this.zipFile = new ZipFile(file);
        LOG.info("Opened container \"{}\"", zipFile.getName());
    }

    @Override
    public QuestCollection loadCollectionMeta() throws IOException {
        String fileName;
        MetadataParser parser;

        // TODO: Consider removing dependency on parsers and instead pass this CollectionContainer to the parser.
        //       A "generic" parser can choose the right implementation depending on XML or YAML metadata.
        //       We can still get a method like this in a facade depending on that generic parser.

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
