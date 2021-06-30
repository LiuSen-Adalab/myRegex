package dfa;

import nfa.NFAState;

import java.util.HashMap;
import java.util.HashSet;

public class DFAState {

    String allIds;
    HashSet<NFAState> allNfaState;
    HashMap<String, DFAState> nextStates;

    public DFAState(String allIds, HashSet<NFAState> allNfaState) {
        this.allIds = allIds;
        this.allNfaState = allNfaState;
    }

    public DFAState(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DFAState dfaState = (DFAState) o;
        return allIds.equals(dfaState.allIds);
    }

    @Override
    public int hashCode() {
        return allIds.hashCode();
    }
}
