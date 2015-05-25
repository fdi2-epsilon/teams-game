package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import org.w3c.dom.Element;

import java.io.IOException;

/**
 * <i>A pass-through quiz template that allows you to create your very own quiz design.</i>
 *
 * <p>
 *     Create your quiz HTML page and use the
 *     {@link eu.unipv.epsilon.enigma.quest.status.QuestViewInterface QuestViewInterface} JavaScript API
 *     to communicate with Enigma and store your status.
 * </p>
 * <p>
 *     Assuming that your HTML file is named {@code my_quiz.html}, place it in the same directory of a
 *     {@code document.xml} with this inside:
 * </p>
 * <pre>{@code
 * <quiz template="raw">
 *     <document src="my_quiz.html" />
 * </quiz>
 * }</pre>
 */
@Template(id = "raw")
public class RawTemplate {

    // TODO: Allow optional usage of macros specified through XML input arguments
    //       (using template.util.MappedValueInputStream)

    @Template.EventHandler
    public void generate(DocumentGenerationEvent e) throws IOException {
        Element args = e.getArguments();

        if (!e.hasPathData())
            throw new UnsupportedOperationException("The \"raw\" template cannot work without path data.");

        Element documentElement = (Element) args.getElementsByTagName("document").item(0);
        String docPath = documentElement.getAttribute("src");

        e.setResponseStream(e.createRelativePath(docPath).openStream());
    }

}
