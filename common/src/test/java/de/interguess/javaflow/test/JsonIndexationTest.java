package de.interguess.javaflow.test;

import de.interguess.javaflow.api.index.ExecutableIndex;
import de.interguess.javaflow.api.index.TriggerIndex;
import de.interguess.javaflow.api.index.WorkflowIndex;
import de.interguess.javaflow.api.index.executable.ProcedureIndex;
import de.interguess.javaflow.api.index.executable.RouterIndex;
import de.interguess.javaflow.common.indexer.JsonIndexer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

class JsonIndexationTest {

    private WorkflowIndex indexedWorkflow;

    @BeforeEach
    void setUp() throws IOException {
        JsonIndexer indexer = new JsonIndexer();
        InputStream workflowStream = getClass().getClassLoader().getResourceAsStream("workflow.json");

        assertNotNull(workflowStream, "Test workflow JSON file is missing");

        String workflowJson = new String(workflowStream.readAllBytes());
        indexedWorkflow = indexer.index(workflowJson);
    }

    @Test
    void testWorkflowMetadata() {
        assertEquals("free-money-command", indexedWorkflow.name(), "Workflow name should match");
    }

    @Test
    void testTriggerStructure() {
        List<TriggerIndex> triggers = indexedWorkflow.triggers();
        assertEquals(1, triggers.size(), "Workflow should have exactly one trigger");

        TriggerIndex commandTrigger = triggers.getFirst();
        assertEquals("57aec617", commandTrigger.id(), "Trigger ID should match");
        assertEquals("command", commandTrigger.type(), "Trigger type should be 'command'");
        assertEquals(3, commandTrigger.tasks().size(), "Trigger should have 3 tasks");
    }

    @Test
    void testGetPlayerMoneyTask() {
        ProcedureIndex getPlayerMoneyTask = (ProcedureIndex) indexedWorkflow.triggers().getFirst().tasks().getFirst();
        assertEquals("e1006be2", getPlayerMoneyTask.id(), "getPlayerMoney task ID should match");
        assertEquals("getPlayerMoney", getPlayerMoneyTask.type(), "Task type should be 'getPlayerMoney'");
        assertEquals("${ 57aec617.player }", getPlayerMoneyTask.input().getValues().get("player"), "Player input should reference trigger player");
    }

    @Test
    void testGreaterOrEqualTask() {
        ProcedureIndex greaterOrEqualTask = (ProcedureIndex) indexedWorkflow.triggers().getFirst().tasks().get(1);
        assertEquals("69bfd163", greaterOrEqualTask.id(), "greaterOrEqual task ID should match");
        assertEquals("greaterOrEqual", greaterOrEqualTask.type(), "Task type should be 'greaterOrEqual'");
        assertEquals("${ e1006be2.result }", greaterOrEqualTask.input().getValues().get("first"), "First input should reference previous task result");
        assertEquals(1000.0, greaterOrEqualTask.input().getValues().get("second"), "Second input should be 1000");
    }

    @Test
    void testRouterTask() {
        RouterIndex routerTask = (RouterIndex) indexedWorkflow.triggers().getFirst().tasks().get(2);
        assertEquals("a47faf75", routerTask.id(), "Router task ID should match");
        assertEquals("router", routerTask.type(), "Task type should be 'router'");
        assertEquals("${ 69bfd163.result }", routerTask.input(), "Router input should reference previous task result");

        Map<Object, List<ExecutableIndex>> routes = routerTask.routes();
        assertEquals(2, routes.size(), "Router should have two routes: true and false");
        assertTrue(routes.containsKey("true") && routes.containsKey("false"), "Router should have 'true' and 'false' routes");
    }

    @Test
    void testTrueRouteTask() {
        List<ExecutableIndex> trueRoute = ((RouterIndex) indexedWorkflow.triggers().getFirst().tasks().get(2)).routes().get("true");
        assertEquals(1, trueRoute.size(), "True route should have one task");

        ProcedureIndex sendMessageTask = (ProcedureIndex) trueRoute.getFirst();
        assertEquals("3c7878ea", sendMessageTask.id(), "True route task ID should match");
        assertEquals("sendPlayerMessage", sendMessageTask.type(), "True route task type should be 'sendPlayerMessage'");
        assertEquals("${ 57aec617.player }", sendMessageTask.input().getValues().get("player"), "Player input should reference trigger player");
        assertEquals("You already have more than 1000!", sendMessageTask.input().getValues().get("message"), "Message content should match");
    }

    @Test
    void testFalseRouteTasks() {
        List<ExecutableIndex> falseRoute = ((RouterIndex) indexedWorkflow.triggers().getFirst().tasks().get(2)).routes().get("false");
        assertEquals(3, falseRoute.size(), "False route should have three tasks");

        testAddNumbersTask((ProcedureIndex) falseRoute.getFirst());
        testSetPlayerMoneyTask((ProcedureIndex) falseRoute.get(1));
        testSendPlayerMessageTask((ProcedureIndex) falseRoute.get(2));
    }

    private void testAddNumbersTask(@NotNull ProcedureIndex addNumbersTask) {
        assertEquals("0a396db0", addNumbersTask.id(), "addNumbers task ID should match");
        assertEquals("addNumbers", addNumbersTask.type(), "Task type should be 'addNumbers'");
        assertEquals("${ e1006be2.result }", addNumbersTask.input().getValues().get("first"), "First input should reference getPlayerMoney result");
        assertEquals(1000.0, addNumbersTask.input().getValues().get("second"), "Second input should be 1000");
    }

    private void testSetPlayerMoneyTask(@NotNull ProcedureIndex setPlayerMoneyTask) {
        assertEquals("18338bb1", setPlayerMoneyTask.id(), "setPlayerMoney task ID should match");
        assertEquals("setPlayerMoney", setPlayerMoneyTask.type(), "Task type should be 'setPlayerMoney'");
        assertEquals("${ 57aec617.player }", setPlayerMoneyTask.input().getValues().get("player"), "Player input should reference trigger player");
        assertEquals("${ 0a396db0.result }", setPlayerMoneyTask.input().getValues().get("amount"), "Amount input should reference addNumbers result");
    }

    private void testSendPlayerMessageTask(@NotNull ProcedureIndex sendPlayerMessageTask) {
        assertEquals("b3cd9b82", sendPlayerMessageTask.id(), "sendPlayerMessage task ID should match");
        assertEquals("sendPlayerMessage", sendPlayerMessageTask.type(), "Task type should be 'sendPlayerMessage'");
        assertEquals("${ 57aec617.player }", sendPlayerMessageTask.input().getValues().get("player"), "Player input should reference trigger player");
        assertEquals("Du hast jetzt ${ 0a396db0.result }€!", sendPlayerMessageTask.input().getValues().get("message"), "Message content should match");
    }
}
