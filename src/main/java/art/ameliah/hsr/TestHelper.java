package art.ameliah.hsr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static String commitHash() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("git", "rev-parse", "HEAD");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            String commitHash;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                commitHash = reader.readLine();
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Git commit failed");
            }
            return commitHash;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Class<?>> getStaticClassesExtendingA(Class<?> outerClass, Class<?> A) {
        List<Class<?>> staticClasses = new ArrayList<>();

        Class<?>[] innerClasses = outerClass.getDeclaredClasses();

        for (Class<?> innerClass : innerClasses) {
            if (Modifier.isStatic(innerClass.getModifiers()) && A.isAssignableFrom(innerClass)) {
                staticClasses.add(innerClass);
            }
        }

        return staticClasses;
    }

    public static Object callMethodOnClasses(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);

            if (Modifier.isStatic(method.getModifiers())) {
                method.invoke(null);
            } else {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                return method.invoke(instance);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
