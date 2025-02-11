package art.ameliah.hsr.registry;

import java.util.HashMap;
import java.util.Map;

public class AbstractRegistry<T> {

    protected final Map<Integer, Class<? extends T>> registry = new HashMap<>();

    protected void register(int id, Class<? extends T> clazz) {
        try {
            registry.put(id, clazz);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public T get(int id) throws Exception {
        if (registry.containsKey(id)) {
            return registry.get(id).getConstructor().newInstance();
        }
        throw new RuntimeException("Element with id" + id + " not found");
    }

}
