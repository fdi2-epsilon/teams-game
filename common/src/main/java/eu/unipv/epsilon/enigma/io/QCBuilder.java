package eu.unipv.epsilon.enigma.io;

import eu.unipv.epsilon.enigma.quest.QuestCollection;

import java.io.File;
import java.io.IOException;

public interface QCBuilder {

    QuestCollection createFromFile(File file) throws IOException;

}
