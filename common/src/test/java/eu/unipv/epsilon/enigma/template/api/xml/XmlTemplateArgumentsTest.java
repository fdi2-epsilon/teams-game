package eu.unipv.epsilon.enigma.template.api.xml;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class XmlTemplateArgumentsTest {

    private final XmlTemplateArguments args1;
    private final XmlTemplateArguments args2;

    public XmlTemplateArgumentsTest() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document doc1 = db.parse(getClass().getClassLoader().getResourceAsStream("xmltools/doc1.xml"));
        args1 = new XmlTemplateArguments(doc1.getDocumentElement());

        Document doc2 = db.parse(getClass().getClassLoader().getResourceAsStream("xmltools/doc2.xml"));
        args2 = new XmlTemplateArguments(doc2.getDocumentElement());
    }


    @Test
    public void testTreeStructure() throws Exception {
        // Check parsing tree (may remove test to allow internal components to change
        assertEquals("AttributeExtractor(\"g\", false, NodeExtractor(\"cat\", false, " +
                "NodeExtractor(\"zulu\", false, RootExtractor(<dio>))))", args1.queryRaw("/zulu/cat:g").toString());

        /*System.out.println(args2.queryRaw("/cylinder/track/sector:p"));
        System.out.println(args2.queryRaw("/cylinder/track/sector:*dsa"));
        System.out.println(args2.queryRaw("/cylinder/track/sector:*"));
        System.out.println(args2.queryRaw("/*cylinder/*track/sector"));
        System.out.println(args2.queryRaw("/cylinder/*track/sector"));
        System.out.println(args2.queryRaw("/cylinder/track/*sector"));*/
    }

    @Test
    public void testQueryStartEquality() {
        // Queries starting with '/' should give same result as the ones without it
        assertEquals(args1.query(":x1"), args1.query("/:x1"));
    }

    @Test
    public void testValues() {
        // Check if the obtained values are correct
        assertEquals("ciao", args1.query(":x1"));
        assertEquals("3z", args1.query("mio:ab"));
        assertEquals("cane", args1.query("mio"));
        assertEquals("por", args1.query("zulu:s"));
        assertEquals("rabbia", args1.query("zulu/can"));
        assertEquals("suin", args1.query("zulu/cat"));
        assertEquals("45", args1.query("zulu/cat:g"));
    }

    @Test
    public void testValuesAll() {
        // Check if the obtained values are correct

        // Get id of the first cylinder
        assertEquals(
                "[00]",
                args2.queryAll("cylinder:id").toString());

        // Get id of all the cylinders
        assertEquals(
                "[00, 01, 02, 03, 04, 05]",
                args2.queryAll("*cylinder:id").toString());

        // Get id of all tracks in the first cylinder
        assertEquals(
                "[00, 01, 02, 03]",
                args2.queryAll("cylinder/*track:id").toString());


        // Get id of all tracks in all cylinders
        assertEquals(
                "[00, 01, 02, 03, 00, 01, 02, 03, 00, 01, 02, 03, 00, 01, 02, 03, 00, 01, 02, 03, 00, 01, 02, 03]",
                args2.queryAll("*cylinder/*track:id").toString());

        // Get position attribute of the first sector of all tracks in the first cylinder
        assertEquals(
                "[000, 008, 016, 024]",
                args2.queryAll("cylinder/*track/sector:p").toString());

        // Get position of all sectors in the first track in the first cylinder
        assertEquals(
                "[000, 001, 002, 003, 004, 005, 006, 007]",
                args2.queryAll("cylinder/track/*sector:p").toString());

        // Get content of all sectors in the first track
        assertEquals(
                "[00 00 00 00 00 00 00 00, " +
                 "00 00 00 00 00 00 00 01, " +
                 "00 00 00 00 00 00 00 02, " +
                 "00 00 00 00 00 00 00 03, " +
                 "00 00 00 00 00 00 00 04, " +
                 "00 00 00 00 00 00 00 05, " +
                 "00 00 00 00 00 00 00 06, " +
                 "00 00 00 00 00 00 00 07]",
                args2.queryAll("cylinder/track/*sector").toString());

        // Get all attributes and node value of the first sector
        assertEquals(new ArrayList<Map<String, String>>() {{
                        add(createComparisonAttribsMap("0000FFFF", "000", "00 00 00 00 00 00 00 00"));
                }},
                args2.queryAll("cylinder/track/sector:*"));

        // Get all attributes and node value of first sector in first track of all cylinders
        assertEquals(new ArrayList<Map<String, String>>() {{
                        add(createComparisonAttribsMap("0000FFFF", "000", "00 00 00 00 00 00 00 00"));
                        add(createComparisonAttribsMap("00FFFF00", "032", "00 00 00 00 00 00 00 20"));
                        add(createComparisonAttribsMap("00FF00FF", "064", "00 00 00 00 00 00 00 40"));
                        add(createComparisonAttribsMap("FFFF0000", "096", "00 00 00 00 00 00 00 60"));
                        add(createComparisonAttribsMap("FF0000FF", "128", "00 00 00 00 00 00 00 80"));
                        add(createComparisonAttribsMap("0F0F0F00", "160", "00 00 00 00 00 00 00 A0"));
                }},
                args2.queryAll("*cylinder/track/sector:*"));
    }

    private Map<String, String> createComparisonAttribsMap(final String crc, final String p, final String value) {
        return new HashMap<String, String>() {{
            put("crc", crc);
            put("p", p);
            put(XmlTemplateArguments.ATTR_NODE_VALUE, value);
        }};
    }

}
