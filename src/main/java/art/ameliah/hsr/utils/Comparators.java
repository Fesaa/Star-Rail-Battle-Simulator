package art.ameliah.hsr.utils;

import art.ameliah.hsr.battleLogic.AbstractEntity;

import java.util.Map;

public class Comparators {

    public static int CompareSpd(Map.Entry<AbstractEntity, Float> e1, Map.Entry<AbstractEntity, Float> e2) {
        if (!e1.getValue().equals(e2.getValue())) {
            return Float.compare(e1.getValue(), e2.getValue());
        }

        if (e1.getKey().speedPriority != e2.getKey().speedPriority) {
            return Integer.compare(e1.getKey().speedPriority, e2.getKey().speedPriority);
        }

        // Yes, this is probably not how it happens ingame. But I really need something consistent
        return e1.getKey().getName().compareTo(e2.getKey().getName());
    }

}
