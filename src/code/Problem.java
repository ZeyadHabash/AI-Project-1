package code;

public abstract class Problem {
    public State initialState;
    public String[] operators;

    public Problem(State initialState, String[] operators) {
        this.initialState = initialState;
        this.operators = operators;
    }

    public abstract State transitionFunction(State state, String operator);

    public abstract boolean goalTest(State state);

    public abstract int pathCost();
}

