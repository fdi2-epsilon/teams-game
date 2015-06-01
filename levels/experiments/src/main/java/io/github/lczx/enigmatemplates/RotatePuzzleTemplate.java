package io.github.lczx.enigmatemplates;

import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.api.TemplateArguments;
import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

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

    // These are simple plugin classes, should I S.O.L.I.D. here too?
    // In any case, this is purely H.O.R.R.I.B.L.E. and if I have some time I will consider refactoring it.

    private static final Logger LOG = LoggerFactory.getLogger(RotatePuzzleTemplate.class);

    private static final String MY_ASSETS_PATH = "assets/templates/rotating-puzzle/";

    private static final URL STYLE_URL =
            ClasspathURLStreamHandler.createURL(MY_ASSETS_PATH + "style.css");
    private static final URL SCRIPT_URL =
            ClasspathURLStreamHandler.createURL(MY_ASSETS_PATH + "quizlogic.js");

    TemplateArguments args;

    @Template.EventHandler
    public void handler(DocumentGenerationEvent e) throws IOException {
        args = e.getArguments();

        if (!e.hasPathData())
            throw new UnsupportedOperationException("This template cannot work without path data.");

        // Get canvas image and size (size can be specified without image)
        String canvasImg = args.query("canvas:img", "");
        int canvasWidth = Integer.parseInt(args.query("canvas:width", "0"));
        int canvasHeight = Integer.parseInt(args.query("canvas:height", "0"));
        LOG.debug("Got image data: \"{}\" as {}x{}", canvasImg, canvasWidth, canvasHeight);

        MappedValueInputStream in = new MappedValueInputStream(
                getClass().getClassLoader().getResourceAsStream(MY_ASSETS_PATH + "index.html"));

        in.addMacro("STYLE_URL", STYLE_URL.toString());
        in.addMacro("SCRIPT_URL", SCRIPT_URL.toString());
        in.addMacro("BACKGROUND_IMAGE_PATH", args.query("background:img", ""));
        in.addMacro("QUIZ_TITLE", args.query("title"));
        in.addMacro("QUIZ_FOOTNOTE", args.query("footnote"));

        in.addMacro("FRAME_DATA", String.format(
                "{ background: '%s', width: %d, height: %d }", canvasImg, canvasWidth, canvasHeight));
        in.addMacro("ITEMS_DATA", getItemsData(canvasWidth, canvasHeight));

        e.setResponseStream(in);
        LOG.info("Response stream generated successfully");
    }

    public String getItemsData(int canvasWidth, int canvasHeight) {
        // Get number of item nodes
        List<Map<String, String>> canvasItems = getCanvasItems();

        ItemPlacer placer = makeItemPlacer(canvasWidth, canvasHeight, canvasItems.size());
        LOG.debug("Got item placer: " + placer);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < canvasItems.size(); ++i) {
            Dimensions defaultPosition = placer != null ? placer.getItemPosition(i) : new Dimensions(0, 0);
            sb.append(new PuzzleItem(canvasItems.get(i), defaultPosition).toJson());
        }
        return sb.toString();
    }

    // Returns null if no default placer
    public ItemPlacer makeItemPlacer(int canvasWidth, int canvasHeight, int numItems) {
        // Return null if we don't have the "auto-positioning" tag or something similar coming in the future
        if (args.query("auto-positioning", null) == null)
            return null;

        ItemPlacer placer;
        String rows = args.query("auto-positioning:rows", "");
        String cols = args.query("auto-positioning:cols", "");

        if (rows.isEmpty() || cols.isEmpty())
            placer = new ItemPlacer(canvasWidth, canvasHeight, numItems);
        else
            placer = new ItemPlacer(canvasWidth, canvasHeight, Integer.parseInt(rows), Integer.parseInt(cols));

        return placer
                .setPadding(Dimensions.fromXmlString(args.query("auto-positioning:padding", "0 0")))
                .setOffset(Dimensions.fromXmlString(args.query("auto-positioning:offset", "0 0")))
                .setItemSize(Dimensions.fromXmlString(args.query("auto-positioning:mean-item-size", "0 0")));
    }

    @SuppressWarnings("unchecked")
    List<Map<String, String>> getCanvasItems() {
        return (List<Map<String, String>>) args.queryAll("canvas/*item:*");
    }

}
