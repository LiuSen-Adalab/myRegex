package dfa;

import nfa.NFAState;

import java.util.HashMap;
import java.util.HashSet;

public class DFAGraph {
    HashMap<String, DFAState> allDfaStates = new HashMap<>();
    public DFAState start;

    public DFAGraph() {

    }

    public DFAState getOrBuild(HashSet<NFAState> states) {
        StringBuilder builder = new StringBuilder();

        int[] ids = states.stream()
                .mapToInt(NFAState::getId)
                .sorted()
                .toArray();

        for (int id : ids) {
            builder.append("#");
            builder.append(id);
        }
        if (!allDfaStates.containsKey(builder.toString())) {
            DFAState newState = new DFAState(builder.toString(), states);
            allDfaStates.put(builder.toString(), newState);
        }

        return allDfaStates.get(builder.toString());
    }

    public DFAState moveTo(DFAState state, String edge){
        HashSet<NFAState> nfaStates = new HashSet<>();

        for (NFAState nfaState : state.allNfaState) {
            if (nfaState.nextStates.containsKey(edge)){
                for (NFAState nextStates : nfaState.nextStates.get(edge)) {
                    nfaStates.add(nextStates);
                    nfaStates.addAll(nextStates.getAllState());
                }
            }
        }
        DFAState dfaState = getOrBuild(nfaStates);
        state.nextStates.put(dfaState.allIds, dfaState);
        return dfaState;
    }
}
