package nfa;

import common.Reader;
import common.StateType;

public class NFAGraph {
    public NFAState start;
    public NFAState end;

    public NFAGraph(NFAState start, NFAState end){
        this.start = start;
        this.end = end;
    }

    public void addSeries(NFAGraph newGraph){
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

    public void checkRepeat(Reader reader){
        if (reader.peek() == '*'){
            reader.next();
            repeatNTimes();
        }else if(reader.peek() == '+'){
            reader.next();
            repeatOnePlus();
        }else if (reader.peek() == '?'){
            reader.next();
            repeatZeroOrOne();
        }
    }

    private void repeatZeroOrOne() {
        start.addNext(end, StateType.EPSILON);
    }

    private void repeatOnePlus() {
        end.addNext(start, StateType.EPSILON);

        NFAState newStart = new NFAState();
        NFAState newEnd = new NFAState();
        newStart.addNext(this.start, StateType.EPSILON);
        this.end.addNext(newEnd, StateType.EPSILON);

        start = newStart;
        end = newEnd;
    }

    private void repeatNTimes(){
        repeatOnePlus();
        repeatZeroOrOne();
    }


}
