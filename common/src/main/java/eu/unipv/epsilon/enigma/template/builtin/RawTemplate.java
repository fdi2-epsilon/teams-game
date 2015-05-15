package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;

import java.io.ByteArrayInputStream;

@Template(id = "raw")
public class RawTemplate {

    @Template.EventHandler
    public void blablabla(DocumentGenerationEvent e) {
        String inText = e.getArguments().getAttribute("text");

        if (e.hasPathData()) {
            System.out.println(String.format(
                    "Path data:\n\tID:  %s\n\tDir: %s", e.getCollectionID(), e.getBaseDir()));
        } else {
            System.out.println("Probably loaded from pure stream");
        }

        DummyClass d = new DummyClass();

        String out = "<html>" + inText + d.f() + "</html>";
        e.setResponseStream(new ByteArrayInputStream(out.getBytes()));
    }

}
