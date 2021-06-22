package dfa;

import nfa.NFAState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class DFAState {

    String allIds;
    HashSet<NFAState> allNFAState;
    HashMap<String, DFAState> nextStates;

    public DFAState(String allIds, HashSet<NFAState> allNFAState) {
        this.allIds = allIds;
        this.allNFAState = allNFAState;
    }

    public DFAState(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DFAState dfaState = (DFAState) o;
        return allIds.equals(dfaState.allIds);
    }

    @Override
    public int hashCode() {
        return allIds.hashCode();
    }
}
