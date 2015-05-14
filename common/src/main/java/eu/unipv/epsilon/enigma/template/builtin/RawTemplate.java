package eu.unipv.epsilon.enigma.template.builtin;

import eu.unipv.epsilon.enigma.template.api.DocumentGenerationEvent;
import eu.unipv.epsilon.enigma.template.api.Template;

import java.io.ByteArrayInputStream;

@Template(id = "raw")
public class RawTemplate {

    @Template.EventHandler
    public void blablabla(DocumentGenerationEvent e) {
        String inText = e.getArguments().getAttribute("text");

        DummyClass d = new DummyClass();

        String out = "<html>" + inText + d.f() + "</html>";
        e.setResponseStream(new ByteArrayInputStream(out.getBytes()));
    }

}
