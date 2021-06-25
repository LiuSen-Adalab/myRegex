import common.Reader;
import common.StateType;
import dfa.DFAGraph;
import dfa.DFAState;
import nfa.NFAGraph;
import nfa.NFAState;

import java.util.*;

public class Regex {

    public NFAGraph nfaGraph;
    public DFAGraph dfaGraph;

    public static Regex compile(String regex) {
        return new Regex(regex);
    }

    private Regex(String regex) {
        nfaGraph = regexToNfaGraph(regex);
//        dfaGraph = new DFAGraph();
//        nfa2dfa();
    }

    /**
     * @param regex 待编译的正则字符串
     * @return regex 编译后生成的状态机图
     */
    private NFAGraph regexToNfaGraph(String regex) {
        Reader reader = new Reader(regex);
        NFAGraph graph = null;

        while (reader.hasNext()) {
            char ch = reader.next();
            String edge = String.valueOf(ch);

            if (ch == '(') {
                String subRegex = reader.getSubRegex();
                NFAGraph subGraph = regexToNfaGraph(subRegex);
                subGraph.checkRepeat(reader);
                if (graph == null) {
                    graph = subGraph;
                } else {
                    graph.addSeries(subGraph);
                }
            } else if (ch == '|') {
                String remain = reader.getRemain();
                NFAGraph parallel = regexToNfaGraph(remain);

                if (graph == null) {
                    System.out.println("正则表达式格式错误！".hashCode());
                    break;
                } else {
                    graph.addParallel(parallel);
                }
            } else if (!"\0".equals(edge)) {
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
     * @param text 待匹配字符串
     * @return text 中能匹配当前正则表达式的所有子串，贪婪匹配
     */
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

    /**
     * @param text 待匹配字符串
     * @return text 是否能完全匹配当前正则表达式
     */
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
        HashSet<NFAState> nfaStates = nfaGraph.start.getAllState();

        if (nfaStates.size() == 0) {
            nfaStates.add(nfaGraph.start);
        }
        dfaGraph.start = dfaGraph.getOrBuild(nfaStates);

        Queue<DFAState> dfaStates = new LinkedList<>();
        HashSet<DFAState> finishedStates = new HashSet<>();

        dfaStates.add(dfaGraph.start);

        while (!dfaStates.isEmpty()) {
            DFAState state = dfaStates.poll();
            if (finishedStates.contains(state)) {
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
