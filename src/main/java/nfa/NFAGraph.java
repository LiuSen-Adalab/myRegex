package nfa;

import common.Reader;
import common.StateType;

public class NFAGraph {
    public NFAState start;
    public NFAState end;
    public NFAState cur;

    private static final char ASTERISK = '*';
    private static final char PLUS = '+';
    private static final char QUESTION_MARK = '?';

    public NFAGraph(NFAState start, NFAState end) {
        this.start = start;
        this.end = end;
        cur = start;
    }

    public void addSeries(NFAGraph newGraph) {
        this.end.addNext(newGraph.start, StateType.EPSILON);
        this.end = newGraph.end;
    }

    public void addParallel(NFAGraph parallel) {
        NFAState newStart = new NFAState();
        NFAState newEnd = new NFAState();

        newStart.addNext(this.start, StateType.EPSILON);
        newStart.addNext(parallel.start, StateType.EPSILON);

        this.end.addNext(newEnd, StateType.EPSILON);
        parallel.end.addNext(newEnd, StateType.EPSILON);

        this.start = newStart;
        this.end = newEnd;
    }

    public void checkRepeat(Reader reader) {
        if (reader.peek() == ASTERISK) {
            reader.next();
            repeatZeroOrMoreThanZero();
        } else if (reader.peek() == PLUS) {
            reader.next();
            repeatMoreThanZero();
        } else if (reader.peek() == QUESTION_MARK) {
            reader.next();
            repeatZeroOrOne();
        }
    }

    private void repeatZeroOrOne() {
        start.addNext(end, StateType.EPSILON);
    }

    private void repeatMoreThanZero() {
        end.addNext(start, StateType.EPSILON);

        NFAState newStart = new NFAState();
        NFAState newEnd = new NFAState();
        newStart.addNext(this.start, StateType.EPSILON);
        this.end.addNext(newEnd, StateType.EPSILON);

        start = newStart;
        end = newEnd;
    }

    private void repeatZeroOrMoreThanZero() {
        repeatMoreThanZero();
        repeatZeroOrOne();
    }
}
