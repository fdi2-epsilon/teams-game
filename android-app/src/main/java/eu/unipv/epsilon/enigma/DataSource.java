package eu.unipv.epsilon.enigma;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import eu.unipv.epsilon.enigma.io.url.EqcURLStreamHandler;
import eu.unipv.epsilon.enigma.quest.QuestCollection;
import eu.unipv.epsilon.enigma.ui.main.CardType;
import eu.unipv.epsilon.enigma.ui.main.CollectionsViewAdapter;
import eu.unipv.epsilon.enigma.ui.main.TempElement;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSource {

    Resources resources;

    List<QuestCollection> collections = new ArrayList<>();

    public DataSource(File filesDir, Resources resources) {
        this.resources = resources;

        final File collectionsDir = new File(filesDir, "collections");

        // Get all files inside directory with 'eqc' extension
        File[] collectionFiles = collectionsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return dir == collectionsDir && filename.endsWith(".eqc");
            }
        });

        // Initialize URL hander
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
            @Override
            public URLStreamHandler createURLStreamHandler(String protocol) {
                if (protocol.equalsIgnoreCase("eqc")) return new EqcURLStreamHandler(collectionsDir);
                return null;
            }
        });

        // Return if no content
        if (collectionFiles == null) return;

        for (File file : collectionFiles) {
            try {
                collections.add(QuestCollection.fromFile(file));
            } catch (IOException e) {
                // Do not add item if error in file
                e.printStackTrace();
            }
        }
        Collections.reverse(collections);
    }

    public void populateMainView(RecyclerView list) {
        // Specify an adapter
        list.setAdapter(new CollectionsViewAdapter(collections));
    }

}
