package eu.unipv.epsilon.enigma.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A {@link java.net.URLConnection} which loads its content from a specified zip entry within a local file.
 *
 * @see <a href="http://www.java2s.com/Code/Java/Network-Protocol/ZipURLStreamHandler.htm">Original implementation</a>
 */
public class ZipURLConnection extends URLConnection {

    /** The zip file. */
    private final File file;

    /** The name of the zip entry within the zip file. */
    private final String zipEntryName;

    /** The {@link ZipFile} object for the zip file. Created when {@link #connect()} is called. */
    private ZipFile zipFile = null;

    /** The {@link ZipEntry} object for the entry in the zip file. Created on {@link #connect()}. */
    private ZipEntry zipEntry = null;

    /** Creates a new {@link ZipURLConnection} for the specified zip entry within the {@link File}. */
    public ZipURLConnection(URL u, File file, String entryName) {
        super(u);
        this.file = file;
        this.zipEntryName = entryName;
    }

    /** Attempts to open the zip entry. */
    @Override
    public void connect() throws IOException {
        if (!connected) {
            this.zipFile = new ZipFile(file);
            this.zipEntry = zipFile.getEntry(zipEntryName);
            if (zipEntry == null)
                throw new IOException("Entry " + zipEntryName + " not found in file " + file);
            this.connected = true;
        }
    }

    /** Returns the {@link InputStream} that reads from this connection. */
    @Override
    public InputStream getInputStream() throws IOException {
        connect();
        return zipFile.getInputStream(zipEntry);
    }

    /** Returns the length of the uncompressed zip entry. */
    @Override
    public long getContentLengthLong() {
        if (!connected) return -1;
        return zipEntry.getSize();
    }

}
