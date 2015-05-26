package eu.unipv.epsilon.enigma.data;

import android.content.Context;
import android.os.Environment;
import eu.unipv.epsilon.enigma.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Provides the location of application assets and configuration folders given its context.
 */
public class StorageLocator {

    private static final Logger LOG = LoggerFactory.getLogger(StorageLocator.class);

    public static final String INTERNAL_COLLECTIONS_DIR = "collections";
    public static final String EXTERNAL_COLLECTIONS_DIR = "Collections";

    private Context context;

    public StorageLocator(Context context) {
        this.context = context;
    }

    /**
     * Retrieves the local data source to load any built-in level collections.
     *
     * @return the directory containing game collections shipped with the application
     */
    public File getInternalCollectionsLocation() {
        File dir = new File(context.getFilesDir(), INTERNAL_COLLECTIONS_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LOG.error("There was a problem while creating the internal collections directory!");
                return null;
            } else
                LOG.info("A new empty internal collections directory was created successfully");
        }
        return dir;
    }

    /**
     * Retrieves the external data source to load any user provided level collections.
     *
     * @return a file pointing to the external levels storage directory or {@code null} if not accessible
     */
    public File getExternalCollectionsLocation() {
        if (!isExternalStorageAccessible())
            return null;

        // External storage is readable, get path to collections directory
        File extDocsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File extCollectionsDir = new File(extDocsDir, String.format("%s/%s",
                context.getString(R.string.app_name), EXTERNAL_COLLECTIONS_DIR));

        // Create the directory tree if it does not exist
        if (!extCollectionsDir.exists()) {
            // Should we check if it is not a directory?
            if (!extCollectionsDir.mkdirs()) {
                LOG.error("There was a problem while creating the external collections directory!");
                return null;
            } else
                LOG.info("A new empty external collections directory was created successfully");
        }

        return extCollectionsDir;
    }

    /**
     * Checks if external storage is available at least in read-only mode.
     *
     * @return {@code true} if external storage is available
     */
    public boolean isExternalStorageAccessible() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}
