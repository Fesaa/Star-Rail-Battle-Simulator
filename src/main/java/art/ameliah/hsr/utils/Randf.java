package art.ameliah.hsr.utils;

import java.util.Collection;
import java.util.Random;

public class Randf {

    public static <E> E rand(Collection<E> collection, Random random) {
        return collection.stream().skip(random.nextInt(collection.size())).findFirst().orElse(null);
    }

}
