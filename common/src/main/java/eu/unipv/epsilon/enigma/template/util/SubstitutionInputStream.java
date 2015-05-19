package eu.unipv.epsilon.enigma.template.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * A {@link FilterInputStream} that expands macros in the form {@code ${key}} from an InputStream
 * with values provided by the {@link #expandKey(String)} method.
 */
public abstract class SubstitutionInputStream extends FilterInputStream {

    // LinkedList<> allows removal without arguments
    private final LinkedList<Character> buffer = new LinkedList<>(); //NOSONAR

    public SubstitutionInputStream(InputStream in) {
        super(in);
    }

    /**
     * Provides a value for key expansion from the stream.
     *
     * @param key The extracted key (e.g. {@code "x"} in {@code "${x}"}
     * @return The equivalent text of the macro
     */
    protected abstract String expandKey(String key);

    @Override
    public int read() throws IOException {
        // If we have something in the local buffer, return it
        if (!buffer.isEmpty())
            return buffer.remove();

        // Read a new char and return it, if it is not a macro start
        int c = super.read();
        if (c != '$')
            return c;

        // It is potentially a macro start, check if the next is '{'
        int c2 = super.read();
        if (c2 == '{') {
            // We are inside a macro, get the value in between braces
            StringBuilder keyBuf = new StringBuilder();
            while ((c2 = super.read()) != '}')
                keyBuf.append((char) c2);

            // Get the expanded value and put it in the buffer
            for (char vc : expandKey(keyBuf.toString()).toCharArray())
                buffer.add(vc);

            // Return the first char in the buffer
            return buffer.remove();

        } else {
            // False alarm, return the first char and put the second in the buffer
            buffer.add((char) c2);
            return c;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        // Reimplement a solution similar to InputStream default
        for (int i = 0; i < len; ++i) {
            int c;
            try {
                if ((c = read()) == -1) {
                    // Reached end of stream, return bytes read
                    return i == 0 ? -1 : i;
                }
            } catch (IOException e) {
                if (i != 0)
                    return i;
                throw e;
            }
            b[off + i] = (byte) c;
        }
        return len;
    }

}
