package eu.unipv.epsilon.enigma.loader.levels;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class EqcFileEntry implements ContainerEntry {

    ZipFile zip;
    ZipEntry zipEntry;

    public EqcFileEntry(ZipFile zip, String entryPath) {
        this.zip = zip;
        this.zipEntry = zip.getEntry(entryPath);
        if (zipEntry == null) {
            // This should already be checked by EqcFile for existence
            throw new RuntimeException("Entry \"" + entryPath + "\" not found in \"" + zip.getName() + "\".");
        }
    }

    @Override
    public long getSize() {
        return zipEntry.getSize();
    }

    @Override
    public InputStream getStream() throws IOException {
        return zip.getInputStream(zipEntry);
    }

}
