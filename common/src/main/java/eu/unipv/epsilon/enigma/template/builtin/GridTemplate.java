package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.URL;

/**
 * @deprecated in favor of {@code io.github.lczx.enigmatemplates.RotatePuzzleTemplate}
 *             defined in project {@code ":levels:experiments"} as a collection defined template.
 */
@Template(id = "grid")
@Deprecated
public class GridTemplate {

    private static final String PAGE_PATH = "cp:/assets/templates/list/indexGrid.html";

    @Template.EventHandler
    public void generate(DocumentGenerationEvent e) throws IOException {
        Element args = e.getArgumentsRaw();
        Element gridTitle = (Element) args.getElementsByTagName("title").item(0);

        NodeList elements = ((Element) args.getElementsByTagName("grid").item(0)).getElementsByTagName("element");

        MappedValueInputStream page = new MappedValueInputStream(new URL(PAGE_PATH).openStream());
        page.addMacro("QUIZ_TITLE", gridTitle.getTextContent());
        page.addMacro("QUIZ_DATA", createQuizData(elements));

        e.setResponseStream(page);
    }

    private String createQuizData(NodeList elements) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            String imgPath = element.getAttribute("img");
            String pos = element.getAttribute("pos");

            String initPos = element.getAttribute("init_pos");
            if (initPos.isEmpty())
                initPos = "0";

            sb.append(String.format("{ img: '%s', start: %s, target: %s }, ", imgPath, pos, initPos));
        }
        return sb.toString();
    }

}
