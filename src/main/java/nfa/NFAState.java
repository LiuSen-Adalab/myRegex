package nfa;

import common.StateType;

import java.util.*;


public class NFAState {
    private static int NodeID = 1;
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

    public HashSet<NFAState> getAllState() {
        HashSet<NFAState> states = new HashSet<>();
        if (nextStates.containsKey(StateType.EPSILON)) {
            for (NFAState nfaState : nextStates.get(StateType.EPSILON)) {
                states.addAll(nfaState.getAllState());
            }
        }
        return states;
    }

    public boolean containNext(String edge) {
        return nextStates.containsKey(edge);
    }

    public boolean containEpsilon(){
        return nextStates.containsKey(StateType.EPSILON);
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

    public NFAState getNext(String substring) {
        return nextStates.get(substring).iterator().next();
    }

    public Set<NFAState> getEpsilon() {
        return nextStates.getOrDefault(StateType.EPSILON, (HashSet<NFAState>) Collections.<NFAState>emptySet());
    }
}
