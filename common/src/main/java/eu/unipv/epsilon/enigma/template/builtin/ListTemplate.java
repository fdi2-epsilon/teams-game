package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import eu.unipv.epsilon.enigma.template.api.TemplateArguments;
import eu.unipv.epsilon.enigma.template.api.xml.XmlTemplateArguments;
import eu.unipv.epsilon.enigma.template.util.MappedValueInputStream;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

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

        MappedValueInputStream page = new MappedValueInputStream(PAGE_URL.openStream());
        page.addMacro("QUIZ_TITLE", args.query("title", QUIZ_TITLE_DEFAULT));
        page.addMacro("QUIZ_ANSWERS", createAnswersHTML(args.queryAll("answers/*item:*")));
        page.addMacro("STYLE_BACKGROUND", args.query("style:background", STYLE_BACKGROUND_DEFAULT));

        e.setResponseStream(page);
    }

    @SuppressWarnings("unchecked")
    private String createAnswersHTML(Object queryResult) {
        List<Map<String, String>> answers = (List<Map<String, String>>) queryResult;
        StringBuilder sb = new StringBuilder();

        for (Map<String, String> answer : answers) {
            // True if it has a "correct" attribute and it is set to true or is empty
            boolean correct = answer.containsKey("correct") &&
                    ("true".equalsIgnoreCase(answer.get("correct")) || answer.get("correct").isEmpty());
            sb.append(String.format("<li%s>%s</li>",
                    correct ? " correct" : "", answer.get(XmlTemplateArguments.ATTR_NODE_VALUE)));
        }

        return sb.toString();
    }

}
