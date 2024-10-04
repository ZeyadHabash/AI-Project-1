package code;

public class WaterSortSearch extends GenericSearch {

    static Problem waterSearchProblem;

    /**
     * Solves problem using given strategy
     * Parse the initial state into an array of arrays of characters
     * @param initialState - A provided string that defines the parameters of the instance of the problem. It gives the initial context of each bottle.
     *                       It is a string provided in the following format:
     *                          numberOfBottles; // is the number of bottles in problem
     *                          bottleCapacity; // is the maximum number of layer each bottle can take
     *                          .
     *                          .
     *                          .
     *                          color_n_1, color_n_2,...,color_n_k
     * @param strategy - A string indicating the search strategy to be applied
     *                 * BF for breadth-first search
     *                 * DF for depth-first search
     *                 * ID for iterative deepening search
     *                 * UC for uniform cost search
     *                 * GRi for greedy search, with i in {1, 2} distinguishing the two heuristics.
     *                 * ASi for A* search with i in {1, 2} distinguishing the two heuristics.
     * @param visualize - A boolean parameter which, when set to true, results in your program's side-effecting displaying
     *                  the state information as it undergoes the different steps of the discovered solution (if one was discovered).
     * @return String representing the sequence of actions to perform (example: plan;pathCost;nodesExpanded)
     */
    public static String solve(String initialState, String strategy, boolean visualize) {
        // format of returned string

        // Step 1: parse the initial state
        WaterSearchState parsedInitialState = parseState(initialState);

        // Step 2: Define the water search problem
        waterSearchProblem = new Problem(parsedInitialState, new String[] { "pour" }) {
            @Override
            public State transitionFunction(State state, String operator) {
                // Copy the status of the initial state
                WaterSearchState nextState = WaterSearchState.copy((WaterSearchState) state);

                // Perform the pouring operation to get the next state
                pour(nextState, operator);

                return nextState;
            }

            @Override
            public boolean goalTest(State state) {
                return checkGoal((WaterSearchState) state);
            }

            @Override
            public int pathCost() {
                // TODO
                return 0;
            }

            public void setInitialState(State state) {
                initialState = state;
            }
        };

        // Step 3: Perform general search
        Node goalNode = generalSearch(waterSearchProblem, strategy);

        return goalNode != null ? constructSolution(goalNode) : "NOSOLUTION";
    }

    /**
     * Parse the initial state into an array of arrays of characters
     * @param initialState - the state that needs to be parsed
     * @return State object
     */
    private static WaterSearchState parseState(String initialState) {
        // State format
        // numberOf Bottles;
        // bottleCapacity;
        // color0,1, color0,2, ...color0,k;
        // color1,1, color1,2, ...color1,k;
        String[] parsedState = initialState.split(";");
        int numOfBottles = Integer.parseInt(parsedState[0]);
        int bottleCapacity = Integer.parseInt(parsedState[1]);

        // initialize initial state bottle capacity and number of bottles
        WaterSearchState state = new WaterSearchState(numOfBottles, bottleCapacity);

        for (int i = 2; i < parsedState.length; i++) {
            String[] arrayOfColors = parsedState[i].split(",");

            for (int j = arrayOfColors.length - 1; j >= 0; j--) {
                state.arrayOfTubes[i - 2][j] = arrayOfColors[j].charAt(0);

                if (state.arrayOfTubes[i - 2][j] != 'e') {
                    state.arrayOfTopPointers[i - 2] = j; // set the bottle top pointer to the heights layer that contains a color
                }
            }
        }

        return state;
    }

    /**
     * Parse the given state into a string
     * @param state - the state that needs to be parsed
     * @return string representing the tubes represented in the state variable
     */
    public static String stringfyState(WaterSearchState state) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < state.numOfBottles; i++) {
            for(int j = 0; j < state.bottleCapacity; j++) {
                if (j != state.bottleCapacity - 1) {
                    result.append(state.arrayOfTubes[i][j]).append(",");
                }
                else {
                    result.append(state.arrayOfTubes[i][j]);
                }
            }

            result.append(";");
        }

        return result.toString();
    }

    /**
     * Construct a solution by traversing back to the parent node till reaching root node (Path to goal)
     * @param goalNode the goal node reached
     * @return string representing the path of operations performed
     */
    public static String constructSolution(Node goalNode) {
        Node node = goalNode;
        StringBuilder operations = new StringBuilder();

        while(node.parent != null) {
            if (operations.isEmpty()) {
                operations.insert(0, node.operator);
            }
            else {
                operations.insert(0, node.operator + ",");
            }
            node = node.parent;
        }

        return operations.toString();
    }

    // pour_firstBottleIndex_secondBottleIndex
    public static void pour(WaterSearchState state, String operation) {

        String[] parsedOperation = operation.split("_"); // ["pour", firstBottleIndex, secondBottleIndex]
        int firstBottleIndex = Integer.parseInt(parsedOperation[1]);
        int secondBottleIndex = Integer.parseInt(parsedOperation[2]);

        int numOfLayersToPour = calculateCost(state, firstBottleIndex, secondBottleIndex);
        int firstBottleTopPointer = state.arrayOfTopPointers[firstBottleIndex];
        int secondBottleTopPointer = state.arrayOfTopPointers[secondBottleIndex];

        char colorOfTopLayer = state.arrayOfTubes[firstBottleIndex][firstBottleTopPointer];

        for (int i = 0; i < numOfLayersToPour; i++) {
            // if secondBottleTopPointer pointing to -1 meaning bottle is empty then we can pour into it.
            if (secondBottleTopPointer == -1) {
                secondBottleTopPointer = state.bottleCapacity - 1;
            } else {
                secondBottleTopPointer--;
            }

            // pour the color from the first bottle to the second
            state.arrayOfTubes[secondBottleIndex][secondBottleTopPointer] = colorOfTopLayer;

            // remove the top layer from the first bottle
            state.arrayOfTubes[firstBottleIndex][firstBottleTopPointer] = 'e';
            firstBottleTopPointer++;

            // update the top pointers after pouring
            state.arrayOfTopPointers[firstBottleIndex] = firstBottleTopPointer; // Update to the new top
            state.arrayOfTopPointers[secondBottleIndex] = secondBottleTopPointer; // Update to the new top
        }

        if (firstBottleTopPointer == state.bottleCapacity) {
            state.arrayOfTopPointers[firstBottleIndex] = -1; // Update to the new top
        }
    }

    public static boolean checkGoal(WaterSearchState state) {

        boolean flag = true; // a flag that check that current state is a goal

        // TODO check that tube reached its full capacity?
        for (int i = 0; i < state.numOfBottles; i++) {
            // if bottle is not empty to avoid index out of bound error
            if (state.arrayOfTopPointers[i] != -1) {
                char initialColor = state.arrayOfTubes[i][0];
                // traverse over a single tube
                for (int j = 0; j < state.bottleCapacity; j++) {
                    if (state.arrayOfTubes[i][j] != initialColor) {
                        flag = false;
                    }
                }
            }
            else { // check that whole tube is empty
                for (int j = 0; j < state.bottleCapacity; j++) {
                    if (state.arrayOfTubes[i][j] != 'e') {
                        flag = false;
                    }
                }
            }
        }

        return flag;
    }
}
