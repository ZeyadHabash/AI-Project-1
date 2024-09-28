package code;

import java.util.*;

public class WaterSortSearch extends GenericSearch {

    // Solves problem using given strategy
    public static String solve(String initialState, String strategy, boolean visualize) {
        // format of returned string
        // plan;pathCost;nodesExpanded

        // Step 1: parse the initial state
        State state = parseState(initialState);

        // Step 2:
        switch (strategy) {
            case "Depth-first search":
                HashSet<String> visited = new HashSet<>();
                return dfs(state, 0, visited, "").first;
            default: return null;
        }
    }

    // Parses initial state into array of arrays of characters
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

    public static Pair<String, Boolean> dfs(State state, int depth, HashSet<String> visited, String path) {

        // check if the current state is goal
        if (checkGoal(state)) {
            return new Pair<String, Boolean>(path, true);
        }

        List<Triple<Integer, Integer, Integer>> results = getAllNextPossibleState(state);

        for (Triple<Integer, Integer, Integer> triple : results) {

            // create a new state from the current state
            State newState = State.copy(state);

            // pour from one tube to another to create a new state
            pour(newState, triple.first, triple.second);

            String stringfyCurrentState = stringfyState(state);
            String stringfyNewState = stringfyState(newState);
            System.out.println("Depth: " + (depth + 1));
            System.out.println("from: " + triple.first + " to: " + triple.second + " numOfLayers: " + triple.third);
            System.out.println(stringfyCurrentState + " -> " + stringfyNewState);
            System.out.println(Arrays.toString(state.arrayOfTopPointers) + " -> " + Arrays.toString(newState.arrayOfTopPointers));
            System.out.println();

            // skip the new state if we already visited it before to avoid running in a loop
            if (visited.contains(stringfyNewState)) {
                continue;
            } else {
                visited.add(stringfyNewState);
                Pair<String, Boolean> outcome = dfs(newState, depth + 1, visited, path + "pour_" + triple.first + "_" + triple.second + ", ");

                if (outcome.second) {
                    return outcome;
                }
            }
        }

        return new Pair<>("", false);
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
