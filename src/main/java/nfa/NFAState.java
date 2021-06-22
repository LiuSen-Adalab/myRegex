package nfa;

import common.StateType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class NFAState {
    private static int NodeID;
    public int id;
    public HashMap<String, HashSet<NFAState>> nextStates;

    public NFAState() {
        id = NodeID++;
        nextStates = new LinkedHashMap<>();
    }

    public void addNext(NFAState node, String edge) {
        HashSet<NFAState> nodes = nextStates.get(edge);
        if (nodes == null) {
            nodes = new HashSet<>();
        }
        nodes.add(node);
        nextStates.put(edge, nodes);
    }

    public int getId() {
        return id;
    }

    public HashSet<NFAState> getAllEState() {
        HashSet<NFAState> states = new HashSet<>();
        if (nextStates.containsKey(StateType.EPSILON)) {
            for (NFAState nfaState : nextStates.get(StateType.EPSILON)) {
                states.addAll(nfaState.getAllEState());
            }
        }
        return states;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NFAState)) {
            return false;
        }
        NFAState nfaState = (NFAState) o;
        return id == nfaState.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
