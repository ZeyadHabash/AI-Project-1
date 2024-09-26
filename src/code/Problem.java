package code;

import java.util.ArrayList;

public abstract class Problem {
    public State initialState;
    public Operator[] operators;

    public abstract State transitionFunction(State state, Operator operator);

    public abstract boolean goalTest(State state);

    public abstract int pathCost();
}

