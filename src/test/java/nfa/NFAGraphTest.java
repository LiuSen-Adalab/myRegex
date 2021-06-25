package nfa;

import common.Reader;
import common.StateType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class NFAGraphTest {

    @Test
    void addSeries() {
        NFAState state1 = new NFAState();
        NFAState state2 = new NFAState();
        NFAGraph graph1 = new NFAGraph(state1, state2);

        NFAState state3 = new NFAState();
        NFAState state4 = new NFAState();
        NFAGraph graph2 = new NFAGraph(state3, state4);

        graph1.addSeries(graph2);
        assertSame(graph1.start, state1);
        assertSame(graph1.end, state4);
    }

    @Test
    void addParallel() {
        NFAState state1 = new NFAState();
        NFAState state2 = new NFAState();
        state1.addNext(state2, "a");
        NFAGraph graph1 = new NFAGraph(state1, state2);

        NFAState state3 = new NFAState();
        NFAState state4 = new NFAState();
        state3.addNext(state4, "b");
        NFAGraph graph2 = new NFAGraph(state3, state4);

        graph1.addParallel(graph2);

        assertNotSame(state1, graph1.start);
        assertNotSame(state4, graph1.end);
        HashSet<NFAState> statesAfterStartNode = graph1.start.nextStates.get(StateType.EPSILON);
        assertEquals(2, statesAfterStartNode.size());
        assertTrue(statesAfterStartNode.stream().anyMatch(item -> item == state1));
        assertTrue(statesAfterStartNode.stream().anyMatch(item -> item == state3));
    }
}