package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.api.TemplateArguments;
import eu.unipv.epsilon.enigma.template.api.xml.XmlTemplateArguments;
import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.URL;

/**
 * <i>The standard multiple-answers quiz type.</i>
 *
 * <p>
 *     A question and a variable set of answers, multiple answers can be correct, the sky is the limit!
 * </p>
 * <p>
 *     Usage sample (put this in your {@code document.xml}):
 * </p>
 * <pre>{@code
 * <quiz template="list">
 *     <title>Waz mah name?</title>
 *     <answers>
 *         <item>Homie</item>
 *         <item correct="true">Never told ya</item>
 *         <item>Buddy</item>
 *     </answers>
 * </quiz>
 * }</pre>
 */
@Template(id = "list")
public class ListTemplate {

    private static final URL PAGE_URL =
            ClasspathURLStreamHandler.createURL("assets/templates/list/index.html");

    private static final String QUIZ_TITLE_DEFAULT = "...?";
    private static final String STYLE_BACKGROUND_DEFAULT = "linear-gradient(#eee, #ccc)";

    @Template.EventHandler
    public void generate(DocumentGenerationEvent e) throws IOException {
        TemplateArguments args = e.getArguments();

        // BY NOW, we need to cast back TemplateArguments to perform a raw extractor query to get an XML item
        // and get array of child nodes. This WILL change in the future, probably with a "queryAll" function.
        // It is OK that we don't catch the NoSuchElementsException if answers node is not defined.
        Element answersNode = ((XmlTemplateArguments) args).queryRaw("answers").getNode();
        NodeList answers = answersNode.getElementsByTagName("item");

        MappedValueInputStream page = new MappedValueInputStream(PAGE_URL.openStream());
        page.addMacro("QUIZ_TITLE", args.query("title", QUIZ_TITLE_DEFAULT));
        page.addMacro("QUIZ_ANSWERS", createAnswersHTML(answers));
        page.addMacro("STYLE_BACKGROUND", args.query("style:background", STYLE_BACKGROUND_DEFAULT));

        e.setResponseStream(page);
    }

    private String createAnswersHTML(NodeList nodes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element x = (Element) nodes.item(i);

            // True if it has a "correct" attribute and it is set to true or is empty
            boolean correct = x.hasAttribute("correct") &&
                    ("true".equalsIgnoreCase(x.getAttribute("correct")) || x.getAttribute("correct").isEmpty());
            sb.append(String.format("<li%s>%s</li>", correct ? " correct" : "", x.getTextContent()));
        }

        return sb.toString();
    }

}
