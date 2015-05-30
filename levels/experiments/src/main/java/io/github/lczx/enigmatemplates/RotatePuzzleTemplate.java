package io.github.lczx.enigmatemplates;

import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.URL;

/**
 * <i>A template defining rotating items that should be placed correctly to solve the puzzle.</i>
 *
 * <p>
 *     Create a {@code document.xml} like this:
 * </p>
 * <pre>{@code
 * <quiz template="rotating-puzzle">
 *     <!-- Put your arguments in here -->
 * </quiz>
 * }</pre>
 *
 * <h4>Here is a list of possible arguments:</h4>

 * <p>
 *     <pre>{@code<title>Thiz iz mah quiz</title>}</pre>
 *     Defines the title of the quiz to be displayed on top of the canvas; default value is {@code ""}.
 * </p>
 * <p>
 *     <pre>{@code<footnote>Look. At. MEE!</footnote>}</pre>
 *     A small note to be displayed under the canvas, may contain a suggestion or a copyright notice.
 * </p>
 * <p>
 *     <pre>{@code<background img="bg.img" />}</pre>
 *     A background image to be displayed under the entire page, path is relative to {@code document.xml}.
 * </p>
 * <p>
 *     <pre>{@code<canvas img="back.png" width="w" height="h">
    <item img="item1.png" init-pos="0" pos="1" pivot="10 0" />
    <item img="item2.png" init-pos="3" pos="3" pivot="0 20" />
    <!-- ... -->
</canvas>}</pre>
 *     The canvas holding items with a path to its background image.
 *     Width and height are used to get the items container's size on screen, it is suggested use the image size.<br>
 *
 *     Inner items have a different image each (preferably a circular image with transparencies on the borders)
 *     {@code init-pos} (initial position) and {@code pos} (target position) range from {@code 0} to {@code 3}
 *     representing a total of 4 orientations, when all items are in the target position, the quiz is solved.<br>
 *
 *     {@code pivot} is optional and overrides any positioning algorithm defined (see later), it specifies the
 *     {@code "x y"} position of the element on the canvas relative to its top-left corner.
 * </p>
 * <p>
 *     <pre>{@code<auto-positioning rows="i" cols="j" padding="x y" offset="x y" mean-item-size="w h" />}</pre>
 *     Tells the system to use {@link ItemPlacer} to define default values for item {@code pivot}s.<br>
 *     {@code padding} is where items start from the canvas border and defines an inner grid size,
 *     smaller than the canvas.<br>
 *     {@code offset} translates generated value coordinates for all items.<br>
 *     {@code rows} and {@code cols} are optional and define the size of the grid of items.
 *     If they are not specified, a square with equal dimensions is assumed.<br>
 *     {@code mean-item-size} is the assumed size of an item used to calculate its top-left coordinate.
 * </p>
 */
@Template(id = "rotating-puzzle")
public class RotatePuzzleTemplate {

    /*
    // REMOVE AFTER TESTS!
    private static GameAssetsSystem as = new GameAssetsSystem(new DirectoryPool(new File("levels/experiments/build/eqcs")));

    public static void main(String[] args) throws Exception {
        as.createTemplateServer(new JvmCandidateClassSource(as));
        URL mdurl = as.getCollectionContainer("experiments").getCollectionMeta().get(0).getMainDocumentUrl();

        System.out.println(mdurl);
        dumpStream(mdurl.openStream());
    }

    // teST thinger
    private static void dumpStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null)
            System.out.println(line);
    }

*/

    private static final Logger LOG = LoggerFactory.getLogger(RotatePuzzleTemplate.class);

    // PLS PLACE LOGS ALL AROUND WITH CALCD AND ARGS VALUES


    // These are simple plugin classes, should I S.O.L.I.D. here too?
    // In any case, this is purely H.O.R.R.I.B.L.E. and if I have some time I will consider refactoring it.

    private static final String MY_ASSETS_PATH = "assets/templates/rotating-puzzle/";

    private static final URL STYLE_URL =
            ClasspathURLStreamHandler.createURL(MY_ASSETS_PATH + "style.css");
    private static final URL SCRIPT_URL =
            ClasspathURLStreamHandler.createURL(MY_ASSETS_PATH + "quizlogic.js");

    Element xmlArgs;

    @Template.EventHandler
    public void handler(DocumentGenerationEvent e) throws IOException {
        xmlArgs = e.getArgumentsRaw();

        if (!e.hasPathData())
            throw new UnsupportedOperationException("This template cannot work without path data.");

        // Get canvas image size
        String canvasImg = getBaseArgumentAttribute("canvas", "img", "");
        int canvasW = 0, canvasH = 0;
        if (!"".equals(canvasImg)) {
            canvasW = Integer.parseInt(getBaseArgumentAttribute("canvas", "width", "0"));
            canvasH = Integer.parseInt(getBaseArgumentAttribute("canvas", "height", "0"));
        }

        // Get number of item nodes
        NodeList itemNodes = getSingleElement("canvas").getElementsByTagName("item");
        int numItems = itemNodes.getLength();

        ItemPlacer placer = buildItemPlacer(canvasW, canvasH, numItems);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < itemNodes.getLength(); ++i) {
            // We assume that we have element nodes and the input is correctly formatted
            Element itemNode = (Element) itemNodes.item(i);

            Position defaultPosition = placer != null ? placer.getItemPosition(i) : new Position(0, 0);
            sb.append(new Item(itemNode, defaultPosition).toJson());
        }

        MappedValueInputStream in = new MappedValueInputStream(
                getClass().getClassLoader().getResourceAsStream(MY_ASSETS_PATH + "index.html"));

        in.addMacro("STYLE_URL", STYLE_URL.toString());
        in.addMacro("SCRIPT_URL", SCRIPT_URL.toString());

        in.addMacro("BACKGROUND_IMAGE_PATH", getBaseArgumentAttribute("background", "img", ""));

        in.addMacro("QUIZ_TITLE",  getBaseArgument("title", ""));
        in.addMacro("QUIZ_FOOTNOTE", getBaseArgument("footnote", ""));

        in.addMacro("FRAME_DATA", String.format(
                "{ background: '%s', width: %d, height: %d }", canvasImg, canvasW, canvasH));
        in.addMacro("ITEMS_DATA", sb.toString());

        e.setResponseStream(in);
        LOG.info("Response stream generated successfully");
    }

    // Returns null if no default placer
    public ItemPlacer buildItemPlacer(int canvasWidth, int canvasHeight, int numItems) {
        if (getSingleElement("auto-positioning") == null) {
            // No auto-positioning tag, no item placer
            return null;
        }

        ItemPlacer p;
        String rows = getBaseArgumentAttribute("auto-positioning", "rows", "");
        String cols = getBaseArgumentAttribute("auto-positioning", "cols", "");

        if (isInteger(rows) && isInteger(cols))
            p = new ItemPlacer(canvasWidth, canvasHeight, Integer.parseInt(rows), Integer.parseInt(cols));
        else
            p = new ItemPlacer(canvasWidth, canvasHeight, numItems);

        String paddingStr = getBaseArgumentAttribute("auto-positioning", "padding", "0 0");
        Position padding = Position.fromXmlString(paddingStr);
        p.setPadding((int) padding.x, (int) padding.y);

        String offsetStr = getBaseArgumentAttribute("auto-positioning", "offset", "0 0");
        Position offset = Position.fromXmlString(offsetStr);
        p.setOffset((int) offset.x, (int) offset.y);

        String meanSizeStr = getBaseArgumentAttribute("auto-positioning", "mean-item-size", "0 0");
        Position meanSize = Position.fromXmlString(meanSizeStr);
        p.setItemSize((int) meanSize.x, (int) meanSize.y);

        return p;
    }

    /* XML helper functions */

    // Gets the first element found with the given <key></key> or null if not found
    public Element getSingleElement(String key) {
        return (Element) xmlArgs.getElementsByTagName(key).item(0);
    }

    // Extracts <key>value</key and returns defaultValue on error
    public String getBaseArgument(String key, String defaultValue) {
        Element elem = getSingleElement(key);
        return elem != null ? elem.getTextContent() : defaultValue;
    }

    // Extracts <key attr="value" /> and returns defaultValue on error
    public String getBaseArgumentAttribute(String key, String attr, String defaultValue) {
        Element elem = getSingleElement(key);
        if (elem == null)
            return defaultValue;

        String attrValue = elem.getAttribute(attr);
        return "".equals(attrValue) ? defaultValue : attrValue;
    }

    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            // Ignore exception
            return false;
        }
    }

    public class Item {

        private Position defaultPos;
        private String imgPath;
        private String initPos;
        private String targetPos;
        private String pivot;

        public Item(Element arg, Position defaultPos) {
            imgPath = arg.getAttribute("img");
            // ok if ""

            initPos = arg.getAttribute("init-pos");
            if (!isInteger(initPos)) initPos = "0";

            targetPos = arg.getAttribute("pos");
            if (!isInteger(targetPos)) targetPos = "0";

            pivot = arg.getAttribute("pivot");

            this.defaultPos = defaultPos;
        }

        public String toJson() {
            Position x;
            if (!"".equals(pivot)) {
                // Check for parsing errors?
                x = Position.fromXmlString(pivot);
            } else {
                x = defaultPos;
            }

            return String.format("{img: '%s', pos: %s, c: %s, t: %s}, ",
                    imgPath, x, initPos, targetPos);
        }

    }

}
