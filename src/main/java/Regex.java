import common.Reader;
import common.StateType;
import dfa.DFAGraph;
import dfa.DFAState;
import nfa.NFAGraph;
import nfa.NFAState;

import java.util.*;
import java.util.function.BiConsumer;

public class Regex {

    public NFAGraph nfaGraph;
    public DFAGraph dfaGraph;


    static Regex compile(String regex) {
        return new Regex(regex);

    }

    private Regex(String regex) {
        nfaGraph = regex2NFAGraph(regex);
//        dfaGraph = new DFAGraph();
//        nfa2dfa();

    }

    private NFAGraph regex2NFAGraph(String regex) {
        Reader reader = new Reader(regex);
        NFAGraph graph = null;

        while (reader.hasNext()) {
            char ch = reader.next();
            String edge = String.valueOf(ch);

            if (ch == '(') {
                String subRegex = reader.getSubRegex();
                NFAGraph subGraph = regex2NFAGraph(subRegex);
                subGraph.checkRepeat(reader);
                if (graph == null) {
                    graph = subGraph;
                } else {
                    graph.addSeries(subGraph);
                }
                continue;
            } else if (ch == '|') {
                String remain = reader.getRemain();
                NFAGraph parallel = regex2NFAGraph(remain);
                graph.addParallel(parallel);
                continue;
            }

            if (!edge.equals("\0")) {

                NFAState start = new NFAState();
                NFAState end = new NFAState();
                start.addNext(end, edge);
                NFAGraph newGraph = new NFAGraph(start, end);
                newGraph.checkRepeat(reader);


                if (graph == null) {
                    graph = newGraph;
                } else {
                    graph.addSeries(newGraph);
                }
            }
        }

        return graph;
    }

    /**
     * print all nfa state
     */
    public void printNFAGraph() {
        Queue<NFAState> nodes = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        nodes.add(nfaGraph.start);
        StringBuffer buffer = new StringBuffer();

        while (!nodes.isEmpty()) {
            NFAState node = nodes.poll();


            if (visited.contains(node.id)) {
                continue;
            }
            node.nextStates.forEach(new BiConsumer<String, HashSet<NFAState>>() {
                @Override
                public void accept(String edge, HashSet<NFAState> stateNodes) {
                    for (NFAState stateNode : stateNodes) {
                        buffer.append(node.id).append(" - ").append(edge);
                        buffer.append(" -> ").append(stateNode.id).append("\n");
                        nodes.add(stateNode);
                    }
                }
            });
            visited.add(node.id);
        }

        System.out.println(buffer.toString());
    }

    public LinkedList<String> match(String text) {
        LinkedList<String> matched = new LinkedList<>();

        for (int start = 0; start < text.length(); start++) {
            int end = text.length();
            while (end != start) {
                String sub = text.substring(start, end);
                if (isMatch(sub)) {
                    matched.addLast(sub);
                    break;
                } else {
                    end -= 1;
                }
            }
        }

        return matched;

    }

    public boolean isMatch(String text) {
        return isMatch(nfaGraph.start, text, 0);
    }

    public boolean isMatch(NFAState curNode, String text, int position) {
        if (position == text.length()) {
            for (NFAState nextState : curNode.nextStates.getOrDefault(StateType.EPSILON, new HashSet<>())) {
                if (isMatch(nextState, text, position)) {
                    return true;
                }
            }
            return curNode == nfaGraph.end;
        }

        for (Map.Entry<String, HashSet<NFAState>> entry : curNode.nextStates.entrySet()) {
            String edge = entry.getKey();
            if (edge.equals(StateType.EPSILON)) {
                for (NFAState nextState : entry.getValue()) {
                    if (isMatch(nextState, text, position)) {
                        return true;
                    }
                }
            } else {
                if (edge.equals(String.valueOf(text.charAt(position)))) {
                    for (NFAState nextState : entry.getValue()) {
                        if (isMatch(nextState, text, position + 1)) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }


    private void nfa2dfa() {
        HashSet<String> allEdges = getAllNfaEdge(nfaGraph.start);
        HashSet<NFAState> nfaStates = nfaGraph.start.getAllEState();

        if (nfaStates.size() == 0) {
            nfaStates.add(nfaGraph.start);
        }
        dfaGraph.start = dfaGraph.getOrBuild(nfaStates);

        Queue<DFAState> dfaStates = new LinkedList<>();
        HashSet<DFAState> finishedStates = new HashSet<>();

        dfaStates.add(dfaGraph.start);

        while (!dfaStates.isEmpty()) {
            DFAState state = dfaStates.poll();
            if (finishedStates.contains(state)){
                continue;
            }
            for (String edge : allEdges) {
                DFAState newState = dfaGraph.moveTo(state, edge);
                dfaStates.add(newState);
            }
            finishedStates.add(state);
        }


    }

    private HashSet<String> getAllNfaEdge(NFAState curState) {
        HashSet<String> edges = new HashSet<>();
        Queue<NFAState> states = new LinkedList<>();
        Set<NFAState> visited = new HashSet<>();

        states.add(curState);
        while (!states.isEmpty()) {
            NFAState state = states.poll();
            if (visited.contains(state)) {
                continue;
            }
            for (Map.Entry<String, HashSet<NFAState>> entry : state.nextStates.entrySet()) {
                if (!entry.getKey().equals(StateType.EPSILON)) {
                    edges.add(entry.getKey());
                }
            }
            visited.add(state);
        }
        return edges;
    }

}
