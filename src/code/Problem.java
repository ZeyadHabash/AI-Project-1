package code;

import java.util.List;

public abstract class Problem {
    public State initialState;
    public String[] operators;
    public int treeDepth = 0;

    public Problem(State initialState, String[] operators) {
        this.initialState = initialState;
        this.operators = operators;
    }

    public abstract State transitionFunction(State state, String operator);

    public abstract boolean goalTest(State state);

    public abstract int pathCost(State state, String operation);

    public abstract List<Node> expand(Node node);

    public abstract int heuristicCost(State state);
}

