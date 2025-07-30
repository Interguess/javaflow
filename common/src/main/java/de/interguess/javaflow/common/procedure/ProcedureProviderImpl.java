package de.interguess.javaflow.common.procedure;

import com.google.inject.Singleton;
import de.interguess.javaflow.api.construct.procedure.CustomProcedure;
import de.interguess.javaflow.api.construct.procedure.Procedure;
import de.interguess.javaflow.api.construct.procedure.ProcedureProvider;
import de.interguess.javaflow.common.util.ClassSearcherUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Singleton
public class ProcedureProviderImpl extends ProcedureProvider {

    private final Object2ObjectOpenHashMap<String, Procedure> indexes;

    public ProcedureProviderImpl() {
        this.indexes = new Object2ObjectOpenHashMap<>();

        ProcedureProvider.setInstance(this);
    }

    @Override
    public void registerProcedures(@NotNull String packageName) {
        ClassSearcherUtil.listAllClasses(ClassLoader.getSystemClassLoader(), packageName).stream()
                .filter(clazz -> clazz.isAnnotationPresent(CustomProcedure.class))
                .forEach(clazz -> {
                    try {
                        final Constructor<?> constructor = clazz.getConstructor();

                        indexes.put(clazz.getAnnotation(CustomProcedure.class).id(), (Procedure) constructor.newInstance());
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
