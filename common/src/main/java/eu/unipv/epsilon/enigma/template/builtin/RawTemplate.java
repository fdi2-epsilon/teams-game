package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;
import org.w3c.dom.Element;

import java.io.IOException;

@Template(id = "raw")
public class RawTemplate {

    @Template.EventHandler
    public void generate(DocumentGenerationEvent e) throws IOException {
        Element args = e.getArguments();

        Element documentElement = (Element) args.getElementsByTagName("document").item(0);
        String docPath = documentElement.getAttribute("src");

        e.setResponseStream(e.createRelativePath(docPath).openStream());
    }

}
