package de.interguess.javaflow.common.util;


import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

@UtilityClass
public class ClassSearcherUtil {

    @NotNull
    @SuppressWarnings("UnstableApiUsage")
    public static List<Class<?>> listAllClasses(@NotNull ClassLoader classLoader, @NotNull String packageName) {
        try {
            ClassPath classPath = ClassPath.from(classLoader);
            ImmutableList<ClassPath.ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(packageName).asList();

            return classInfos.stream()
                    .map(ClassPath.ClassInfo::load)
                    .collect(ImmutableList.toImmutableList());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to list all classes", exception);
        }
    }
}
