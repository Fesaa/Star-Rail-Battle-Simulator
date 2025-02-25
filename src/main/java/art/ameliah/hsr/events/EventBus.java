package art.ameliah.hsr.events;

import art.ameliah.hsr.battleLogic.Battle;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class EventBus {

    private static final SortedSet<Method> EMPTY =
            new TreeSet<>(Comparator.comparingInt(EventBus::getPriority));

    private final Map<Object, Map<Class<Event>, SortedSet<Method>>> listeners = new HashMap<>();

    public <T extends Event> T fire(T event) {
        Objects.requireNonNull(event, "event cannot be null");
        Class<? extends Event> eventClass = event.getClass();
        synchronized (this) {

            for (var entry : new HashMap<>(listeners).entrySet()) {
                var methods = entry.getValue().getOrDefault(eventClass, EMPTY);
                for (var method : methods) {
                    try {
                        method.invoke(entry.getKey(), event);
                    } catch (Battle.ForceBattleEnd e) {
                        throw e;
                    } catch (InvocationTargetException e) {
                      if (e.getCause() instanceof Battle.ForceBattleEnd end) {
                          throw end;
                      }
                      throw new RuntimeException("An error occurred while handling the event", e);
                    } catch (Exception e) {
                        throw new RuntimeException("An error occurred while handling the event", e);
                    }
                }
            }
        }
        return event;
    }

    public void unregisterListener(Object listener) {
        Objects.requireNonNull(listener, "listener cannot be null");
        listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public void registerListener(Object listener) {
        Objects.requireNonNull(listener, "listener cannot be null");
        Method[] methods = listener.getClass().getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Subscribe.class)) {
                continue;
            }

            Class<?> eventClass = getEventClass(method);

            listeners.computeIfAbsent(listener, k -> new HashMap<>())
                    .computeIfAbsent((Class<Event>) eventClass,
                            k -> new TreeSet<>(Comparator.comparingInt(EventBus::getPriority)))
                    .add(method);
        }
    }

    private static @NotNull Class<?> getEventClass(Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length != 1) {
            throw new RuntimeException("Method " + method.getName() + " has @Subscribe annotation but does not have exactly one parameter");
        }

        Class<?> eventClass = parameters[0];
        if (!Event.class.isAssignableFrom(eventClass)) {
            throw new RuntimeException("Method " + method.getName() + " has @Subscribe annotation but parameter is not an Event");
        }
        return eventClass;
    }

    private static int getPriority(Method method){
        Subscribe methodSubscribe = method.getAnnotation(Subscribe.class);
        Subscribe classSubscribe = method.getDeclaringClass().getAnnotation(Subscribe.class);

        if(classSubscribe != null){
            return classSubscribe.priority().ordinal();
        } else {
            return methodSubscribe.priority().ordinal();
        }
    }

}
