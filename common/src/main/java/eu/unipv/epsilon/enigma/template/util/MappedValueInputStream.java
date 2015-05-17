package eu.unipv.epsilon.enigma.template.util;

import java.io.InputStream;
import java.util.HashMap;

/**
 * A {@link SubstitutionInputStream} that provides values from an {@link HashMap}.
 */
public class MappedValueInputStream extends SubstitutionInputStream {

    private HashMap<String, String> values;

    public MappedValueInputStream(InputStream in) {
        this(in, new HashMap<String, String>());
    }

    public MappedValueInputStream(InputStream in, HashMap<String, String> values) {
        super(in);
        this.values = values;
    }

    public void addMacro(String key, String value) {
        values.put(key, value);
    }

    @Override
    protected String expandKey(String key) {
        if (values.containsKey(key)) return values.get(key);
        return String.format("${%s}", key);
    }

}
