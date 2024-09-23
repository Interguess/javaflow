package de.interguess.javaflow.test;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import de.interguess.javaflow.common.util.ClassSearcherUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
class ClassSearchTest {

    @Mock
    private ClassLoader mockClassLoader;

    @Mock
    private ClassPath mockClassPath;

    @Mock
    private ClassPath.ClassInfo mockClassInfo1;

    @Mock
    private ClassPath.ClassInfo mockClassInfo2;

    @BeforeEach
    void mock() throws Exception {
        MockitoAnnotations.openMocks(this);

        doReturn(String.class).when(mockClassInfo1).load();
        doReturn(Integer.class).when(mockClassInfo2).load();

        when(mockClassPath.getTopLevelClassesRecursive(anyString()))
                .thenReturn(ImmutableSet.of(mockClassInfo1, mockClassInfo2));

    }

    @Test
    void testSuccess() throws Exception {
        // Test for successful class loading
        try (var mockedStatic = mockStatic(ClassPath.class)) {
            mockedStatic.when(() -> ClassPath.from(mockClassLoader)).thenReturn(mockClassPath);

            List<Class<?>> result = ClassSearcherUtil.listAllClasses(mockClassLoader, "com.example.package");

            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.contains(String.class));
            assertTrue(result.contains(Integer.class));

            // Verify interactions
            verify(mockClassInfo1, times(1)).load();
            verify(mockClassInfo2, times(1)).load();
        }
    }

    @Test
    void testException() throws Exception {
        // Simulate IOException in ClassPath
        try (var mockedStatic = mockStatic(ClassPath.class)) {
            mockedStatic.when(() -> ClassPath.from(mockClassLoader)).thenThrow(new IOException());

            List<Class<?>> result = ClassSearcherUtil.listAllClasses(mockClassLoader, "com.example.package");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

}
