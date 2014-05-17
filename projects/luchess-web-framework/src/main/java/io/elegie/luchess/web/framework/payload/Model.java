package io.elegie.luchess.web.framework.payload;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple map which holds all model data.
 */
@SuppressWarnings("javadoc")
public class Model {

    private Map<Object, Object> model = new HashMap<>();

    public void put(Object key, Object value) {
        model.put(key, value);
    }

    public Object get(Object key) {
        return model.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Model)) {
            return false;
        }
        return model.equals(((Model) o).model);
    }

    @Override
    public int hashCode() {
        return model.hashCode();
    }

    @Override
    public String toString() {
        return model.toString();
    }

}
