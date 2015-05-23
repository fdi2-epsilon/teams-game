package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.URL;

@Template(id = "list")
public class ListTemplate {

    private static final String PAGE_PATH = "assets:/templates/list/index.html";

    @Template.EventHandler
    public void generate(DocumentGenerationEvent e) throws IOException {
        Element args = e.getArguments();
        Element title = (Element) args.getElementsByTagName("title").item(0);

        NodeList answers = ((Element) args.getElementsByTagName("answers").item(0)).getElementsByTagName("item");

        MappedValueInputStream page = new MappedValueInputStream(new URL(PAGE_PATH).openStream());
        page.addMacro("QUIZ_TITLE", title.getTextContent());
        page.addMacro("QUIZ_ANSWERS", createAnswersHTML(answers));

        e.setResponseStream(page);
    }

    private String createAnswersHTML(NodeList nodes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < nodes.getLength(); ++i) {
            Element x = (Element) nodes.item(i);
            boolean correct = x.getAttribute("correct").equalsIgnoreCase("true");
            sb.append(String.format("<li%s>%s</li>", correct ? " correct" : "", x.getTextContent()));
        }

        return sb.toString();
    }

}