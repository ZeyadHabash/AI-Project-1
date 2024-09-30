package code;

import java.util.*;

public class WaterSortSearch extends GenericSearch {

    // Solves problem using given strategy
    /**
     * Parse the initial state into an array of arrays of characters
     * @param initialState - A provided string that defines the parameters of the instance of the problem. It gives the initial context of each bottle.
     *                       It is a string provided in the following format:
     *                          numberOfBottles; // is the number of bottles in problem
     *                          bottleCapacity; // is the maximum number of layer each bottle can take
     *                          .
     *                          .
     *                          .
     *                          color_n_1, color_n_2,...,color_n_k
     * @param strategy -
     * @param visualize -
     * @return String representing the sequence of actions to perform
     */
    public static String solve(String initialState, String strategy, boolean visualize) {
        // format of returned string
        // plan;pathCost;nodesExpanded

        // Step 1: parse the initial state
        State state = parseState(initialState);

        // Step 2:
        switch (strategy) {
            case "Depth-first search":
                Node dfsGoalNode = dfs(state);

                if (dfsGoalNode == null) {
                    return "NOSOLUTION";
                }

                // Print the final state of the tubes
                System.out.println(stringfyState(dfsGoalNode.state));

                // Return constructed solution
                return constructSolution(dfsGoalNode);

            case "Breadth-first search":
                // Perform breadth-first search
                Node bfsGoalNode = bfs(state);

                if (bfsGoalNode == null) { // no goal node is found
                    return "NOSOLUTION";
                }

                // Print the final state of the tubes
                System.out.println(stringfyState(bfsGoalNode.state));

                // Return constructed solution
                return constructSolution(bfsGoalNode);

            default: return null;
        }
    }

    /**
     * Parse the initial state into an array of arrays of characters
     * @param initialState - the state that needs to be parsed
     * @return State object
     */
    private static State parseState(String initialState) {
        // State format
        // numberOf Bottles;
        // bottleCapacity;
        // color0,1, color0,2, ...color0,k;
        // color1,1, color1,2, ...color1,k;
        String[] parsedState = initialState.split(";");
        int numOfBottles = Integer.parseInt(parsedState[0]);
        int bottleCapacity = Integer.parseInt(parsedState[1]);

        // initialize initial state bottle capacity and number of bottles
        State state = new State(numOfBottles, bottleCapacity);

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
    public static String stringfyState(State state) {

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
     * Construct a solution by traversing back to the parent node till reaching root node
     * @param goalNode - the goal node reached
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

    /**
     * Breadth first search
     * @param initialState - the initial state of the tubes
     * @return the goal node
     */
    public static Node bfs(State initialState) {

        Queue<Node> queue = new LinkedList<>();

        // Create root node
        Node root = new Node(initialState, null, "", 0, 0);

        // Add root node to the queue
        queue.add(root);

        // Create a HashSet that checks whether a state has been visited previously
        HashSet<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {

            // Get the current state
            Node curentNode = queue.poll();

            // Check that we reached a goal state
            if (checkGoal(curentNode.state)) {
                return curentNode;
            }

            // Get all possible next states from the current state
            List<Triple<Integer, Integer, Integer>> results = getAllNextPossibleState(curentNode.state);

            for (Triple<Integer, Integer, Integer> triple : results) {

                // Create a new child state
                State newChildState = State.copy(curentNode.state);

                // Pour from one tube to another to create the new child state
                pour(newChildState, triple.first, triple.second);

                // Create the new child node
                Node newChildNode = new Node(newChildState, curentNode, "pour_" + triple.first + "_" + triple.second, triple.third, curentNode.depth + 1);

                // Stringfy state of the new child node created to determine whether it has been visited previously
                String newChildStateString = stringfyState(newChildState);

                // Check whether the new child node has been visited previously
                if (!visited.contains(newChildStateString)) { // if child node is not visited previously
                    // Add the newly created child node into the queue
                    queue.add(newChildNode);
                    visited.add(newChildStateString);
                }
            }
        }

        // No goal reached
        return null;
    }

    /**
     * Depth first search
     * @param initialState - the initial state of the tubes
     * @return the goal node
     */
    public static Node dfs(State initialState) {

        Stack<Node> stack = new Stack<>();

        // Create root node
        Node root = new Node(initialState, null, "", 0, 0);

        // Add root node to the queue
        stack.add(root);

        // Create a HashSet that checks whether a state has been visited previously
        HashSet<String> visited = new HashSet<>();

        while (!stack.isEmpty()) {

            // Get the current state
            Node curentNode = stack.pop();

            // Check that we reached a goal state
            if (checkGoal(curentNode.state)) {
                return curentNode;
            }

            // Get all possible next states from the current state
            List<Triple<Integer, Integer, Integer>> results = getAllNextPossibleState(curentNode.state);

            for (Triple<Integer, Integer, Integer> triple : results) {

                // Create a new child state
                State newChildState = State.copy(curentNode.state);

                // Pour from one tube to another to create the new child state
                pour(newChildState, triple.first, triple.second);

                // Create the new child node
                Node newChildNode = new Node(newChildState, curentNode, "pour_" + triple.first + "_" + triple.second, triple.third, curentNode.depth + 1);

                // Stringfy state of the new child node created to determine whether it has been visited previously
                String newChildStateString = stringfyState(newChildState);

                // Check whether the new child node has been visited previously
                if (!visited.contains(newChildStateString)) { // if child node is not visited previously
                    // Add the newly created child node into the queue
                    stack.add(newChildNode);
                    visited.add(newChildStateString);
                }
            }
        }

        // No goal reached
        return null;
    }

    public static void pour(State state, int firstBottleIndex, int secondBottleIndex) {
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

    public static boolean checkGoal(State state) {

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

    public static List<Triple<Integer, Integer, Integer>> getAllNextPossibleState(State state) {

        List<Triple<Integer, Integer, Integer>> listOfPossibleStates = new ArrayList<Triple<Integer, Integer, Integer>>();

        for (int i = 0; i < state.numOfBottles; i++) {

            for (int j = 0; j < state.numOfBottles; j++) {
                // calculate cost of pouring from the first tube into the second tube.
                int costOfPourFromFirstToSecond = calculateCost(state, i, j);
                if (costOfPourFromFirstToSecond != -1) {
                    listOfPossibleStates.add(new Triple<>(i, j, costOfPourFromFirstToSecond));
                }

                // calculate cost of pouring from the second tube into the first tube.
                int costOfPourFromSecondToFirst = calculateCost(state, i, j);
                if (costOfPourFromSecondToFirst != -1) {
                    listOfPossibleStates.add(new Triple<>(i, j, costOfPourFromSecondToFirst));
                }
            }
        }

        return listOfPossibleStates;
    }

    // check whether the first bottle can be poured into the second bottle.
    public static boolean canPour(State state, int firstBottleIndex, int secondBottleIndex) {

        char firstBottleTopColor = state.arrayOfTopPointers[firstBottleIndex] != -1 ? state.arrayOfTubes[firstBottleIndex][state.arrayOfTopPointers[firstBottleIndex]] : 'e';
        char secondBottleTopColor = state.arrayOfTopPointers[secondBottleIndex] != -1 ? state.arrayOfTubes[secondBottleIndex][state.arrayOfTopPointers[secondBottleIndex]] : 'e';

        // check whether the two different tubes contains the same top color & first bottle is not empty
        return (secondBottleTopColor == 'e' || firstBottleTopColor == secondBottleTopColor)  && firstBottleTopColor != 'e' && firstBottleIndex != secondBottleIndex && state.arrayOfTopPointers[secondBottleIndex] != 0;
    }

    // calculate cost of pouring from the first bottle into the second bottle.
    public static int calculateCost(State state, int firstBottleIndex, int secondBottleIndex) {

        int firstBottleTopPointer = state.arrayOfTopPointers[firstBottleIndex];
        int secondBottleTopPointer = state.arrayOfTopPointers[secondBottleIndex];

        if (canPour(state, firstBottleIndex, secondBottleIndex)) {

            // if secondBottleTopPointer pointing to -1 meaning bottle is empty then we can pour into it.
            int minimumAmountCanBePoured = secondBottleTopPointer == -1 ? state.bottleCapacity : secondBottleTopPointer;

            // count of the same number of layers on the top of the tube
            int numOfConsecutiveTopLayers = 0;

            // get the color of the top layer in the first tube
            char colorOfTopLayer = state.arrayOfTubes[firstBottleIndex][firstBottleTopPointer];

            for (int i = firstBottleTopPointer; i < state.bottleCapacity; i++) {

                if (state.arrayOfTubes[firstBottleIndex][i] == colorOfTopLayer) {
                    numOfConsecutiveTopLayers++;
                }
                else {
                    break;
                }
            }

            minimumAmountCanBePoured = Math.min(minimumAmountCanBePoured, numOfConsecutiveTopLayers);

            return minimumAmountCanBePoured;

        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println(WaterSortSearch.solve("5;4;b,y,r,b;b,y,r,r;y,r,b,y;e,e,e,e;e,e,e,e;", "Depth-first search", false));
    }
}
