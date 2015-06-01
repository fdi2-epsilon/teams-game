package eu.unipv.epsilon.enigma.loader.levels.parser;

import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.DefaultsFactory;
import eu.unipv.epsilon.enigma.loader.levels.parser.defaults.FieldProvider;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;

import static eu.unipv.epsilon.enigma.loader.levels.parser.MetadataFileTags.*;

public class XmlMetaParser implements MetadataParser {

    public static final String KEY_XML_ROOT_NODE = "collection";
    public static final String KEY_XML_QUESTCOLLECTION_META = "meta";
    public static final String KEY_XML_QUESTCOLLECTION_ELEMENTS = "content";
    public static final String KEY_XML_QUEST_NODE = "quest";

    private String collectionId;
    private DefaultsFactory defaultsFactory;

    public XmlMetaParser(String collectionId, DefaultsFactory defaultsFactory) {
        this.collectionId = collectionId;
        this.defaultsFactory = defaultsFactory;
    }

    @Override
    public QuestCollection loadCollectionMetadata(InputStream in) throws IOException {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return generateCollection(db.parse(in).getDocumentElement());
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Cannot parse XML metadata from \"" + collectionId + "\".", e);
        }
    }

    private QuestCollection generateCollection(Element document) throws InvalidPropertiesFormatException {
        QuestCollection qc = new QuestCollection();
        FieldProvider defaults = defaultsFactory.getCollectionDefaults();

        // Check if root node is <document>...
        if (!document.getNodeName().equalsIgnoreCase(KEY_XML_ROOT_NODE)) {
            throw new InvalidPropertiesFormatException(
                    "Document should start with \"<" + KEY_XML_ROOT_NODE + ">\" root node.");
        }

        // ...it should contain only one <meta> tag
        Element meta = getSingleContainerElement(document, KEY_XML_QUESTCOLLECTION_META);

        qc.setId(collectionId);
        qc.setTitle(valueOrDefault(meta, KEY_QUESTCOLLECTION_TITLE, defaults));
        qc.setSubtitle(valueOrDefault(meta, KEY_QUESTCOLLECTION_SUBTITLE, defaults));
        qc.setDescription(valueOrDefault(meta, KEY_QUESTCOLLECTION_DESCRIPTION, defaults));

        String iconPathStr = valueOrDefault(meta, KEY_QUESTCOLLECTION_PATH_ICON, defaults);
        qc.setIconUrl(LevelAssetsURLStreamHandler.createURL(collectionId, iconPathStr));

        // ...and only one <content> tag with <quest> children
        Element content = getSingleContainerElement(document, KEY_XML_QUESTCOLLECTION_ELEMENTS);
        if (content == null) {
            // No quests content
            return qc;
        }

        NodeList quests = content.getElementsByTagName(KEY_XML_QUEST_NODE);
        int questIndex = 0;
        for (int i = 0; i < quests.getLength(); i++) {
            if (quests.item(i).getNodeType() == Node.ELEMENT_NODE) {
                qc.addQuest(generateQuest(questIndex, (Element) quests.item(i)));
                ++questIndex;
            }
        }

        return qc;
    }

    private Quest generateQuest(int index, Element meta) throws InvalidPropertiesFormatException {
        Quest q = new Quest();
        FieldProvider defaults = defaultsFactory.getQuestDefaults(index);

        q.setName(valueOrDefault(meta, KEY_QUEST_NAME, defaults));
        q.setDescription(valueOrDefault(meta, KEY_QUEST_DESCRIPTION, defaults));

        Element paths = getSingleContainerElement(meta, KEY_QUEST_PATH_NODE);
        // Following statements: check if 'paths' node collection exists is inside 'valueOrDefault'


        String mainDocPathStr = valueOrDefault(paths, KEY_QUEST_PATH_MAINDOCUMENT, defaults);
        q.setMainDocumentUrl(LevelAssetsURLStreamHandler.createURL(collectionId, mainDocPathStr));

        String infoDocPathStr = valueOrDefault(paths, KEY_QUEST_PATH_INFODOCUMENT, defaults);
        q.setInfoDocumentUrl(LevelAssetsURLStreamHandler.createURL(collectionId, infoDocPathStr));

        String iconPathStr = valueOrDefault(paths, KEY_QUEST_PATH_ICON, defaults);
        q.setIconUrl(LevelAssetsURLStreamHandler.createURL(collectionId, iconPathStr));

        return q;
    }

    private Element getSingleContainerElement(Element parentNode, String key) throws InvalidPropertiesFormatException {
        NodeList elements = parentNode.getElementsByTagName(key);

        if (elements.getLength() > 1)
            throw new InvalidPropertiesFormatException("Collection should have only one \"<" + key + ">\" node.");

        // Node type check should be short-circuited if empty check fails, so it should not throw.
        if (elements.getLength() == 0 || elements.item(0).getNodeType() != Node.ELEMENT_NODE) {
            // No node or wrong format
            return null;
        }

        return (Element) elements.item(0);
    }

    private String valueOrDefault(Element parentNode, String key, FieldProvider def) throws InvalidPropertiesFormatException {
        // Handle parent undefined:
        if (parentNode == null)
            return def.getPropertyValue(key);

        NodeList nodes = parentNode.getElementsByTagName(key);

        // Handle multiple nodes
        if (nodes.getLength() > 1)
            throw new InvalidPropertiesFormatException(String.format(
                    "\"<%s>\" should contain only one \"<%s>\" element.", parentNode.getNodeName(), key));
        // No nodes
        if (nodes.getLength() == 0)
            return def.getPropertyValue(key);

        return nodes.item(0).getTextContent();
    }

}
