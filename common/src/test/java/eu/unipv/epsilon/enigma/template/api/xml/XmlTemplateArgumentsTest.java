package eu.unipv.epsilon.enigma.template.api.xml;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class XmlTemplateArgumentsTest {

    private static final String XML_DOC = "" +
            "<dio x1=\"ciao\">\n" +
            "    <mio ab=\"3z\">cane</mio>\n" +
            "    <zulu s=\"por\">\n" +
            "        <can>rabbia</can>\n" +
            "        <cat g=\"45\">suin</cat>\n" +
            "    </zulu>\n" +
            "</dio>\n";

    private final XmlTemplateArguments args;

    public XmlTemplateArgumentsTest() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(XML_DOC.getBytes()));
        args = new XmlTemplateArguments(doc.getDocumentElement());
    }


    @Test
    public void testTreeStructure() throws Exception {
        // Check parsing tree (may remove test to allow internal components to change
        assertEquals("AttributeExtractor(\"g\", NodeExtractor(\"cat\", NodeExtractor(\"zulu\", RootExtractor(<dio>))))", args.queryRaw("/zulu/cat:g").toString());
    }

    @Test
    public void testQueryStartEquality() {
        // Queries starting with '/' should give same result as the ones without it
        assertEquals(args.query(":x1"), args.query("/:x1"));
    }

    @Test
    public void testParsedValues() {
        // Check if the obtained values are correct
        assertEquals("ciao", args.query(":x1"));
        assertEquals("3z", args.query("mio:ab"));
        assertEquals("cane", args.query("mio"));
        assertEquals("por", args.query("zulu:s"));
        assertEquals("rabbia", args.query("zulu/can"));
        assertEquals("suin", args.query("zulu/cat"));
        assertEquals("45", args.query("zulu/cat:g"));
    }

}
