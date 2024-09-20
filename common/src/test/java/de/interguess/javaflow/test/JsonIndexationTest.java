package de.interguess.javaflow.test;

import de.interguess.javaflow.api.index.WorkflowIndex;
import de.interguess.javaflow.common.indexer.JsonIndexer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

public class JsonIndexationTest {

    @Test
    void testIndexation() throws IOException {
        final JsonIndexer indexer = new JsonIndexer();

        InputStream testStream = this.getClass().getClassLoader().getResourceAsStream("workflow.json");
        assertNotNull(testStream, "Test workflow missing");

        String contents = new String(testStream.readAllBytes());
        WorkflowIndex indexed = indexer.index(contents);

        assertEquals(indexed.getTriggers().size(), 1, "Wrong amount of triggers");

        // TODO: Check contents of indexed workflow index
    }

}
