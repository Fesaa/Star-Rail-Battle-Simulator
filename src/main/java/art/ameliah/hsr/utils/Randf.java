package art.ameliah.hsr.utils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Randf {

    @Nullable
    public static <E> E rand(Collection<E> collection, Random random) {
        if (collection.isEmpty()) {
            return null;
        }

        return collection.stream().skip(random.nextInt(collection.size())).findFirst().orElse(null);
    }

    /**
     * @param collection collection to pick from
     * @param count      amount of randomly picked elements that are needed
     * @param random     Random generator
     * @param <E>        element type
     * @return Collection<E>, an ArrayList is used
     */
    public static <E> Collection<E> rand(Collection<E> collection, int count, Random random) {
        Collection<E> res = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            res.add(rand(collection, random));
        }
        return res;
    }

}
