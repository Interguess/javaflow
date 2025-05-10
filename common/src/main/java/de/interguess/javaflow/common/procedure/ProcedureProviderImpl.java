package de.interguess.javaflow.common.procedure;

import com.google.inject.Singleton;
import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.construct.procedure.ProcedureProvider;
import de.interguess.javaflow.common.util.ClassSearcherUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ProcedureProviderImpl extends ProcedureProvider {

    private final Map<String, Procedure> indexes;

    public ProcedureProviderImpl() {
        this.indexes = new HashMap<>();

        ProcedureProvider.setInstance(this);
    }

    @Override
    public void registerProcedures(@NotNull String packageName) {
        ClassSearcherUtil.listAllClasses(ClassLoader.getSystemClassLoader(), packageName).stream()
                .filter(clazz -> clazz.isAnnotationPresent(CustomProcedure.class))
                .forEach(clazz -> {
                    try {
                        final Constructor<?> constructor = clazz.getConstructor();

                        final CustomProcedure customProcedure = clazz.getAnnotation(CustomProcedure.class);

                        if (customProcedure == null) {
                            throw new IllegalStateException("CustomProcedure annotation not found on class " + clazz.getName());
                        }

                        if (!Procedure.class.isAssignableFrom(clazz)) {
                            throw new IllegalStateException("Class " + clazz.getName() + " is not a subclass of Procedure");
                        }

                        indexes.put(customProcedure.id(), (Procedure) constructor.newInstance());
                    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public @Nullable Procedure getProcedureById(@NotNull String id) {
        return indexes.get(id);
    }
}
