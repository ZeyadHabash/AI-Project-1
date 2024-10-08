package code;

import java.util.*;

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
                return isGoal((WaterSearchState) state);
            }

            @Override
            public int pathCost() {
                // TODO
                return 0;
            }

            @Override
            public List<Node> expand(Node node) {
                WaterSearchState parentState = (WaterSearchState) node.state;
                List<Node> listOfPossibleNextNodes = new ArrayList<>();
                int help = 78787;

                for (int i = 0; i < parentState.numOfBottles; i++) {
                    for (int j = 0; j < parentState.numOfBottles; j++) {
                        enqueueNextPossibleNode(node, this, listOfPossibleNextNodes, j, i);
                        enqueueNextPossibleNode(node, this, listOfPossibleNextNodes, i, j);
                    }
                }

                return listOfPossibleNextNodes;
            }
        };

        // Step 3: Perform general search
        Node goalNode = generalSearch(waterSearchProblem, strategy);

        return goalNode != null ? constructSolution(goalNode) : "NOSOLUTION";
    }


    /**
     * Create a new node from pouring from the ith tube into the jth tube and add it to the listOfPossibleNextNodes
     * @param node the parent node
     * @param problem instance of the class (problem dependent)
     * @param listOfPossibleNextNodes list of possible next nodes to visit
     * @param i the first bottle index
     * @param j the second bottle index
     */
    private static void enqueueNextPossibleNode(Node node, Problem problem, List<Node> listOfPossibleNextNodes, int i, int j) {
        // calculate cost of pouring from the ith tube into the jth tube.
        int costOfPourFromSecondToFirst = calculateCost((WaterSearchState) node.state, j, i);
        if (costOfPourFromSecondToFirst != -1) {
            String operater = "pour_" + j + "_" + i;
            // create the next possible state using the transition function
            State possibleNextState = problem.transitionFunction(node.state, operater);
            // add the next possible node to the list
            Node childNode = new Node(
                    possibleNextState,
                    node,
                    operater,
                    node.pathCost + costOfPourFromSecondToFirst,
                    node.depth + 1
            );
            listOfPossibleNextNodes.add(childNode);
        }
    }




    /**
     * Calculate the cost of pouring from the first bottle into the second bottle.
     * @param state the current state
     * @param firstBottleIndex index of the first bottle (zero-based)
     * @param secondBottleIndex index of the second bottle (zero-based)
     * @return integer which is the cost of pouring from firstBottleIndex to secondBottleIndex
     */
    public static int calculateCost(WaterSearchState state, int firstBottleIndex, int secondBottleIndex) {

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

    /**
     * Check whether the first bottle can be poured into the second bottle.
     * @param state the current state
     * @param firstBottleIndex index of the first bottle (zero-based)
     * @param secondBottleIndex index of the second bottle (zero-based)
     * @return boolean that determines whether we can pour from firstBottleIndex to secondBottleIndex
     */
    public static boolean canPour(WaterSearchState state, int firstBottleIndex, int secondBottleIndex) {

        char firstBottleTopColor = state.arrayOfTopPointers[firstBottleIndex] != -1 ? state.arrayOfTubes[firstBottleIndex][state.arrayOfTopPointers[firstBottleIndex]] : 'e';
        char secondBottleTopColor = state.arrayOfTopPointers[secondBottleIndex] != -1 ? state.arrayOfTubes[secondBottleIndex][state.arrayOfTopPointers[secondBottleIndex]] : 'e';

        // check whether the two different tubes contains the same top color & first bottle is not empty
        return (secondBottleTopColor == 'e' || firstBottleTopColor == secondBottleTopColor)  && firstBottleTopColor != 'e' && firstBottleIndex != secondBottleIndex && state.arrayOfTopPointers[secondBottleIndex] != 0;
    }


    /**
     * Calculate the cost of pouring from the first bottle into the second bottle.
     * @param node the current node
     * @return a list of nodes resulted from expanding the given node
     */

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

    /**
     * Perform the operation on the state
     * @param state the parent state to get the next state from it
     * @param operation a string that represent the operation performed (example: pour_firstBottleIndex_secondBottleIndex)
     */
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

    /**
     * Check whether a state is a goal state
     * @param state a candidate state
     * @return boolean whether a state is goal or not
     */
    public static boolean isGoal(WaterSearchState state) {

        boolean flag = true; // a flag that check that current state is a goal

        for (int i = 0; i < state.numOfBottles; i++) {
            // if bottle is not empty to avoid index out of bound error
            if (state.arrayOfTopPointers[i] != -1) {
                char initialColor = state.arrayOfTubes[i][state.arrayOfTopPointers[i]];
                // traverse over a single tube
                for (int j = state.arrayOfTopPointers[i]; j < state.bottleCapacity; j++) {
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


    /**
     * Calculate the sum of the number of layers remaining for each tube to pour
     * @param state the state for which to calculate its heuristic value
     * @return an integer represent the heuristic cost of the state
     */
    public static int calculateHeuristicCost1(WaterSearchState state) {

        // total number of layers remaining in each tube that need to be poured
        int heuristicCost = 0;

        // traverse over the list of tubes
        for (int i = 0; i < state.numOfBottles; i++) {
            char bottomLayerColor = state.arrayOfTubes[i][state.bottleCapacity - 1];
            boolean flag = false; // a flag that check that no different color other than bottom layer color is detected

            // if the tube is empty then skip
            if (state.arrayOfTopPointers[i] == -1) {
                continue;
            }

            for (int j = state.bottleCapacity - 1; j >= state.arrayOfTopPointers[i]; j--) {

                if (!flag && bottomLayerColor != state.arrayOfTubes[i][j] && state.arrayOfTubes[i][j] != 'e') {
                    flag = true;
                    heuristicCost++;
                }
                else if (flag && state.arrayOfTubes[i][j] != 'e') {
                    heuristicCost++;
                }

            }
        }

        // set the heuristic cost for the given node
        return heuristicCost;
    }

    /**
     * Calculate the number of swaps needed to reach a goal
     * @param state the state for which to calculate its heuristic value
     * @return an integer represent the heuristic cost of the state
     */
    public static int calculateHeuristicCost2(WaterSearchState state) {

        // an array that represents the label of each bottle in the problem and the number of occurrence of the color label in the bottle
        Pair<Character, Integer>[] arrayOfColoredLabels = new Pair[state.numOfBottles];

        // initialize the arrayOfColoredLabels
        for (int i = 0; i < state.numOfBottles; i++) {
            arrayOfColoredLabels[i] = new Pair<>('e', 0);
        }

        // a hashmap that keep track the list of bottles assigned to each color which is the key in the hashmap and the number of occurrence of the color in the bottle
        HashMap<Character, List<Pair<Integer, Integer>>> colorLabelToListOfIndexMap = new HashMap<>();

        // a list where its index represents the bottle index and containing a hashmap for every bottle to store the number of occurrence of each color in the bottle
        List<HashMap<Character, Integer>> listOfBottlesWithNumberOfOccurrenceOfEachColor = new ArrayList<>();

        // a hashmap that keep track the count of occurrence of each color in the problem
        HashMap<Character, Integer> colorToColorCountMap = new HashMap<>();

        // a hashset that keep tract of the distinct colors in the problem
        HashSet<Character> distinctColorsSet = new HashSet<>();

        int totalNumberOfDistinctColors = 0; // number of distinct colors in the problem
        int numberOfBottlesNeeded = 0; // total number of bottles needed

        /// Step 1: Set the color of each bottle based on the largest occurrence of a colored layer in the bottle

        // traverse over the list of bottles
        for (int i = 0; i < state.numOfBottles; i++) {

            // add a new hashmap to the list of bottles that count number of occurrence of each color in the bottle
            listOfBottlesWithNumberOfOccurrenceOfEachColor.add(i, new HashMap<>());

            // traverse over the number of layers per bottles
            for (int j = 0; j < state.bottleCapacity; j++) {

                // if the colorToColorCountMap has color of the current layer as a key then increment count
                if (colorToColorCountMap.containsKey(state.arrayOfTubes[i][j])) {
                    colorToColorCountMap.put(state.arrayOfTubes[i][j], colorToColorCountMap.get(state.arrayOfTubes[i][j]) + 1);
                } else {
                    // if the colorToColorCountMap does not have the color of the current layer as a key
                    // then set a new key value pair with the new color and number of occurrence to 1
                    if (state.arrayOfTubes[i][j] != 'e') {
                        colorToColorCountMap.put(state.arrayOfTubes[i][j], 1);
                        distinctColorsSet.add(state.arrayOfTubes[i][j]);

                        // increment total number of total distinct colors
                        totalNumberOfDistinctColors++;
                    }
                }

                // add the number of occurrence of each color in the bottle to its hashmap
                if (listOfBottlesWithNumberOfOccurrenceOfEachColor.get(i).containsKey(state.arrayOfTubes[i][j])) {
                    listOfBottlesWithNumberOfOccurrenceOfEachColor.get(i).put(state.arrayOfTubes[i][j], listOfBottlesWithNumberOfOccurrenceOfEachColor.get(i).get(state.arrayOfTubes[i][j]) + 1);
                }
                else {
                    listOfBottlesWithNumberOfOccurrenceOfEachColor.get(i).put(state.arrayOfTubes[i][j], 1);
                }
            }
        }

        /// Step 2: Calculate the number of bottles required for each color
        for (Map.Entry<Character, Integer> mapElement : colorToColorCountMap.entrySet()) {
            Integer numOfColorOccurrence = mapElement.getValue();
            numberOfBottlesNeeded += (int) Math.ceil((double)(numOfColorOccurrence/state.numOfBottles));
        }

        /// Step 3: Calculate the number of remaining bottles
        int totalNumberOfRemainingBottles = state.numOfBottles - numberOfBottlesNeeded;

        /// Step 4: Assign each bottle a colored label
        // traverse over the list of bottles
        for (int i = 0; i < state.numOfBottles; i++) {
            // if the bottle is empty then we assign it a label "$" which indicates none
            if (state.arrayOfTopPointers[i] == -1) {
                arrayOfColoredLabels[i].first = '$';
                arrayOfColoredLabels[i].second = 0;
            }
            else {
                // initialize the color of the bottom layer as the color with maximum occurrences at the beginning.
                char colorWithMaximumOccurrences = state.arrayOfTubes[i][state.bottleCapacity - 1];

                // initially the color with maximum occurrence has an occurrence of 1
                int countOfNumberOfOccurrences = 1;

                // initialize a hashmap of colors to their counts in the bottle
                HashMap<Character, Integer> colorToColorCountPerBottleMap = new HashMap<>();

                // search for the colored layer with the largest number of occurrence in the bottle
                // Or, if the bottles contains equal distribution of all colors in it. Then,
                // we would assign it a label with the same color as their bottom layer
                for (int j = 0; j < state.bottleCapacity; j++) {
                    // if colorToColorCountPerBottleMap contains the color then increment the value by 1
                    if (colorToColorCountPerBottleMap.containsKey(state.arrayOfTubes[i][j])) {
                        colorToColorCountPerBottleMap.put(state.arrayOfTubes[i][j], colorToColorCountPerBottleMap.get(state.arrayOfTubes[i][j]) + 1);
                    } else { // otherwise, set the count of the color to 1
                        colorToColorCountPerBottleMap.put(state.arrayOfTubes[i][j], 1);
                    }

                    // if the count of a color in colorToColorCountPerBottleMap is greater than the current maximum count of number of occurrence
                    // then update the colorWithMaximumOccurrences and colorWithMaximumOccurrences update to the new color
                    if (colorToColorCountPerBottleMap.get(state.arrayOfTubes[i][j]) > countOfNumberOfOccurrences) {
                        colorWithMaximumOccurrences = state.arrayOfTubes[i][j];
                        countOfNumberOfOccurrences = colorToColorCountPerBottleMap.get(state.arrayOfTubes[i][j]);
                    }
                }

                // if the number of occurrence of the bottom layer same as the number of occurrence of largest frequent color
                // we set the label of the bottle with color of the bottom layer as usually the bottom layers does not move much in pouring problem
                if (colorToColorCountPerBottleMap.get(state.arrayOfTubes[i][state.bottleCapacity - 1]) == countOfNumberOfOccurrences) {
                    arrayOfColoredLabels[i].first = state.arrayOfTubes[i][state.bottleCapacity - 1];
                    arrayOfColoredLabels[i].second = countOfNumberOfOccurrences;

                    // update the list of bottles mapped with the current color in colorWithMaximumOccurrences hashmap
                    if (colorLabelToListOfIndexMap.get(state.arrayOfTubes[i][state.bottleCapacity - 1]) == null) {
                        colorLabelToListOfIndexMap.put(state.arrayOfTubes[i][state.bottleCapacity - 1], new ArrayList<>());
                    }

                    colorLabelToListOfIndexMap.get(state.arrayOfTubes[i][state.bottleCapacity - 1]).add(new Pair<>(i, countOfNumberOfOccurrences));

                }
                else {
                    arrayOfColoredLabels[i].first = colorWithMaximumOccurrences;
                    arrayOfColoredLabels[i].second = countOfNumberOfOccurrences;

                    // update the list of bottles mapped with the current color in colorWithMaximumOccurrences hashmap
                    if (colorLabelToListOfIndexMap.get(colorWithMaximumOccurrences) == null) {
                        colorLabelToListOfIndexMap.put(colorWithMaximumOccurrences, new ArrayList<>());
                    }

                    colorLabelToListOfIndexMap.get(colorWithMaximumOccurrences).add(new Pair<>(i, countOfNumberOfOccurrences));
                }
            }
        }

        /// Step 5: check if there does not exist a color that need a bottle to be assigned to them

        // a hashmap that map each color the number of bottles it needs
        HashMap<Character, Integer> colorsToCountOfBottlesNeededMap = new HashMap<>();

        // a hashmap that map each color to the number of excess bottles it has
        HashMap<Character, Integer> colorsToCountOfExcessBottlesMap = new HashMap<>();

        // for every color count the number of remaining number of bottles needed to be assigned to them
        for (Map.Entry<Character, Integer> mapElement : colorToColorCountMap.entrySet()) {
            Character currentColor = mapElement.getKey();
            Integer numOfColorOccurrence = mapElement.getValue();

            numberOfBottlesNeeded += (int) Math.ceil((double)(numOfColorOccurrence/state.numOfBottles));
            System.out.println("Current Color: " + currentColor);
            int numberOfColorsAssignedToCurrentColor = colorLabelToListOfIndexMap.get(currentColor) != null ? colorLabelToListOfIndexMap.get(currentColor).size() : 0;
            int remainingNumberOfBottlesNeedForCurrentColor = numberOfBottlesNeeded - numberOfColorsAssignedToCurrentColor;

            if (remainingNumberOfBottlesNeedForCurrentColor > 0) {
                colorsToCountOfBottlesNeededMap.put(currentColor, remainingNumberOfBottlesNeedForCurrentColor);
            }
            else if (remainingNumberOfBottlesNeedForCurrentColor < 0) {
                colorsToCountOfExcessBottlesMap.put(currentColor, Math.abs(remainingNumberOfBottlesNeedForCurrentColor));
            }
        }

        // get the list of bottles that are excess
        HashSet<Integer> setOfExcessBottlesIndex = new HashSet<>();

        // traverse over the set of distinct colors
        for (Character color : distinctColorsSet) {

            // check whether the current color contains excess number of bottles than needed
            if (colorsToCountOfExcessBottlesMap.containsKey(color)) {

                // get the list of bottle indices that labelled with the current color and the number of occurrence of that color
                List<Pair<Integer, Integer>> listOfBottleIndexMapToCount = colorLabelToListOfIndexMap.get(color);

                // get the number of excess bottles for the color
                int numberOfExcessBottles = colorsToCountOfExcessBottlesMap.get(color);

                // sort the list ascending order based on the number of occurrence so that the excess contains the least number of occurrence of the color
                listOfBottleIndexMapToCount.sort((o1, o2) -> o1.second - o2.second);

                // add all excess bottles to setOfExcessBottlesIndex
                while (numberOfExcessBottles > 0) {

                    int bottleIndex = listOfBottleIndexMapToCount.removeFirst().first;
                    setOfExcessBottlesIndex.add(bottleIndex);

                    numberOfExcessBottles--;
                }

                // remove the color from the hashmap as it currently contains exactly the number of bottles it needs
                colorsToCountOfExcessBottlesMap.remove(color);
            }

        }

        // a variable representing the current bottle index that will be used later on
        int bottleIndex = 0;

        // a list that stores the indices of bottles that are excess or empty bottles and count of layers within bottle
        List<Pair<Integer, Integer>> listOfExcessBottleIndex = new LinkedList<>();

        // traverse over the list of bottles
        for (HashMap<Character, Integer> bottle : listOfBottlesWithNumberOfOccurrenceOfEachColor) {

            // if the bottles is empty then store it in the queue
            if (state.arrayOfTopPointers[bottleIndex] == -1) {
                listOfExcessBottleIndex.add(new Pair<>(bottleIndex++, 0));
                continue;
            }

            // if the current bottle is included in the set of excess bottle index.
            // Hence, we can change its color label.
            if (setOfExcessBottlesIndex.contains(bottleIndex)) {

                // store the values in the hashmap in a list
                List<Pair<Character, Integer>> listOfNumberOfOccurrenceOfEachColor = new ArrayList<>();
                for(Map.Entry<Character, Integer> mapElement : bottle.entrySet()) {
                    Character currentColor = mapElement.getKey();
                    Integer numOfColorOccurrence = mapElement.getValue();
                    listOfNumberOfOccurrenceOfEachColor.add(new Pair<>(currentColor, numOfColorOccurrence));
                }

                // sort the list descending order
                listOfNumberOfOccurrenceOfEachColor.sort((o1, o2) -> o2.second - o1.second);

                // a flag that represents whether the bottle is assigned to any bottle
                boolean flag = false;

                // for every color in the bottle
                for (Pair<Character, Integer> colorElement : listOfNumberOfOccurrenceOfEachColor) {
                    //  check if the color needs a bottle as its label
                    if (colorsToCountOfBottlesNeededMap.containsKey(colorElement.first)) {
                        // set the flag to true since the bottle is assigned a new label
                        flag = true;

                        // assign the label of the bottle to the color
                        arrayOfColoredLabels[bottleIndex].first = colorElement.first;
                        arrayOfColoredLabels[bottleIndex].second = colorElement.second;

                        // remove bottle index from setOfExcessBottlesIndex as it have an assigned label
                        setOfExcessBottlesIndex.remove(bottleIndex);

                        // reduce count of the needed bottle for the color in colorsToCountOfBottlesNeededMap
                        colorsToCountOfBottlesNeededMap.put(colorElement.first, colorsToCountOfBottlesNeededMap.get(colorElement.first) - 1);

                        if (colorsToCountOfBottlesNeededMap.get(colorElement.first) == 0) {
                            colorsToCountOfBottlesNeededMap.remove(colorElement.first);
                        }
                    }
                }

                // if the bottle is not assigned to any color
                if (!flag) {
                    listOfExcessBottleIndex.add(new Pair(bottleIndex, state.arrayOfTopPointers[bottleIndex] + 1));
                }
            }

            bottleIndex++;
        }

        // for the remaining bottles that are not assigned and empty bottles
        // we want to assign the empty bottles labels with colors that contains the minimum number of color occurrence
        // we need to calculate the remaining number of colored layers for colors that need bottles
        List<Pair<Character, Integer>> listOfColorsWithRemaining = new ArrayList<>();

        for (Map.Entry<Character, Integer> mapEntry : colorsToCountOfBottlesNeededMap.entrySet()) {

            Character currentColor = mapEntry.getKey();
            int numberOfBottlesAssignedToColor = colorLabelToListOfIndexMap.get(currentColor).size();
            int numberOfOccurrencesOfColorInProblem = colorToColorCountMap.get(currentColor);
            int numberOfRemainingColors = numberOfOccurrencesOfColorInProblem - numberOfBottlesAssignedToColor * state.bottleCapacity;

            if (numberOfRemainingColors > 0) {
                listOfColorsWithRemaining.add(new Pair<>(currentColor, numberOfRemainingColors));
            }
        }

        // sort the lists ascending order
        listOfColorsWithRemaining.sort((o1, o2) -> o1.second - o2.second);
        listOfExcessBottleIndex.sort((o1, o2) -> o1.second - o2.second);

        // for bottles that are empty or would be assigned labelled color different from the colors in them
        for (Pair<Character, Integer> colorElement : listOfColorsWithRemaining) {

            int numberOfBottlesNeededPerColor = colorsToCountOfBottlesNeededMap.get(colorElement.first);

            while (numberOfBottlesNeededPerColor > 0) {
                for (Pair<Integer, Integer> bottle : listOfExcessBottleIndex) {
                    if (setOfExcessBottlesIndex.contains(bottle.first)) {
                        arrayOfColoredLabels[bottle.first].first = colorElement.first;
                        arrayOfColoredLabels[bottle.first].second = 0;
                        setOfExcessBottlesIndex.remove(bottle.first);
                        numberOfBottlesNeededPerColor--;
                    }
                }
            }
        }

        /// Step 6: Swap layers
        // copy the array of tubes from the state
        char[][] copiedArrayOfTubes = new char[state.arrayOfTubes.length][];
        for (int i = 0; i < state.arrayOfTubes.length; i++) {
            copiedArrayOfTubes[i] = state.arrayOfTubes[i].clone();
        }
        // create an array that contains the count of the number colored layers in every bottle
        int[] arrayOfNumberOfColoredLayers = new int[state.numOfBottles];

        // a variable that keep track of the total number of swaps
        int totalNumberOfSwaps = 0;

        // get the count of the number of colored layers in every bottle
        for (int i = 0; i < state.arrayOfTopPointers.length; i++) {
            arrayOfNumberOfColoredLayers[i] = state.arrayOfTopPointers[i] + 1;
        }

        // traverse over the array ot bottles
        for (int i = 0; i < copiedArrayOfTubes.length; i++) {
            // get the color label of the current bottle
            char label = arrayOfColoredLabels[i].first;

            // traverse over the layers in the current bottle
            for (int j = 0; j < copiedArrayOfTubes[i].length; j++) {

                // a flag that checks whether the colored layer is swapped
                boolean isSwapped = false;

                // if the colored layer have the same color as the color of the bottle then do not do anything
                if (copiedArrayOfTubes[i][j] == label) {
                    continue;
                }
                else if (copiedArrayOfTubes[i][j] != 'e') { // if the colored layer is not empty and have a different color than the label of the bottle
                    /** Step 6.1:
                     *  we want to make as minimum number of swaps as possible so we will prioritize swaps
                     *  that would relocate two color layers in their target bottle
                     */
                    for (int k = 0; k < copiedArrayOfTubes.length; k++) {
                        if (i == k) continue; // do not swap two colored layers from the same bottle
                        else if (arrayOfColoredLabels[i].first == arrayOfColoredLabels[k].first) continue; // do not swap two colored layers from two bottles of the same label
                        else if (copiedArrayOfTubes[i][j] == arrayOfColoredLabels[k].first) { // swap the colored layer with a layer from a bottle with a label of the same color
                            for (int l = 0; l < copiedArrayOfTubes[k].length; l++) {
                                if (copiedArrayOfTubes[k][l] == arrayOfColoredLabels[k].first || copiedArrayOfTubes[k][l] == 'e') {
                                    continue;
                                }
                                else if (copiedArrayOfTubes[k][l] == arrayOfColoredLabels[i].first) {
                                    // swap the layer
                                    char tmp = copiedArrayOfTubes[k][l];
                                    copiedArrayOfTubes[k][l] = copiedArrayOfTubes[i][j];
                                    copiedArrayOfTubes[i][j] = tmp;
                                    isSwapped = true;
                                    totalNumberOfSwaps++;
                                    break;
                                }
                            }
                        }

                        // if the layers is swapped is stop
                        if (isSwapped) {
                            break;
                        }
                    }

                    // if the layer is swapped then we continue to the rest of layers
                    if (isSwapped) {
                        continue;
                    }

                    /** Step 6.2:
                     *  if the colored layer is not swapped then we will swap it
                     *  from any other colored layer that is not y from y labelled bottle that is not empty
                     */
                    for (int k = 0; k < copiedArrayOfTubes.length; k++) {
                        if (i == k) continue;
                        else if (arrayOfColoredLabels[i].first == arrayOfColoredLabels[k].first) continue;
                        else if (arrayOfNumberOfColoredLayers[k] != 0) {
                            for (int l = 0; l < copiedArrayOfTubes[k].length; l++) {
                                if (copiedArrayOfTubes[k][l] == arrayOfColoredLabels[k].first || copiedArrayOfTubes[k][l] == 'e') {
                                    continue;
                                }
                                else if (copiedArrayOfTubes[k][l] == arrayOfColoredLabels[i].first) {
                                    // swap the layers
                                    char tmp = copiedArrayOfTubes[k][l];
                                    copiedArrayOfTubes[k][l] = copiedArrayOfTubes[i][j];
                                    copiedArrayOfTubes[i][j] = tmp;
                                    isSwapped = true;
                                    totalNumberOfSwaps++;
                                    break;
                                }
                            }
                        }

                        // if the layers is swapped then stop
                        if (isSwapped) {
                            break;
                        }
                    }

                    // if the layer is swapped then we continue to the rest of layers
                    if (isSwapped) {
                        continue;
                    }

                    /** Step 6.3:
                     *  if the colored layer is not swapped then we will swap it
                     *  with an empty layer from one of the bottles that have it as it's label
                     */
                    for (int k = 0; k < copiedArrayOfTubes.length; k++) {
                        if (i == k) continue;
                        else if (arrayOfColoredLabels[i].first == arrayOfColoredLabels[k].first) continue;
                        else if (arrayOfColoredLabels[k].first == copiedArrayOfTubes[i][j]) {
                            for (int l = 0; l < copiedArrayOfTubes[k].length; l++) {
                                if (copiedArrayOfTubes[k][l] == arrayOfColoredLabels[k].first) {
                                    continue;
                                }
                                else if (copiedArrayOfTubes[k][l] == 'e') {
                                    // swap the layers
                                    char tmp = copiedArrayOfTubes[k][l];
                                    copiedArrayOfTubes[k][l] = copiedArrayOfTubes[i][j];
                                    copiedArrayOfTubes[i][j] = tmp;
                                    isSwapped = true;
                                    totalNumberOfSwaps++;
                                    arrayOfNumberOfColoredLayers[k]++;
                                    arrayOfNumberOfColoredLayers[i]--;
                                    break;
                                }
                            }
                        }

                        // if the layers is swapped then stop
                        if (isSwapped) {
                            break;
                        }
                    }

                    // if the layer is swapped then we continue to the rest of layers
                    if(isSwapped) {
                        continue;
                    }

                    /** Step 6.4:
                     *  if the colored layer is not swapped then we will swap it
                     *  with an empty layer from any of the bottles
                     */
                    for (int k = 0; k < copiedArrayOfTubes.length; k++) {
                        if (i == k) continue;
                        else if (arrayOfColoredLabels[i].first == arrayOfColoredLabels[k].first) continue;
                        else {

                            for (int l = 0; l < copiedArrayOfTubes[k].length; l++) {
                                if (copiedArrayOfTubes[k][l] == arrayOfColoredLabels[k].first) {
                                    continue;
                                }
                                else if (copiedArrayOfTubes[k][l] == 'e') {
                                    // swap the layers
                                    char tmp = copiedArrayOfTubes[k][l];
                                    copiedArrayOfTubes[k][l] = copiedArrayOfTubes[i][j];
                                    copiedArrayOfTubes[i][j] = tmp;
                                    isSwapped = true;
                                    totalNumberOfSwaps++;
                                    arrayOfNumberOfColoredLayers[k]++;
                                    arrayOfNumberOfColoredLayers[i]--;
                                    break;
                                }
                            }
                        }

                        // if the layers is swapped then stop
                        if (isSwapped) {
                            break;
                        }
                    }
                }
                else if (copiedArrayOfTubes[i][j] == 'e') { // swap an empty layer with any of the colored layers with same color as the label of the bottle

                    for (int k = 0; k < copiedArrayOfTubes.length; k++) {
                        if (i == k) continue; // do not swap two colored layers from the same bottle
                        else if (arrayOfColoredLabels[i].first == arrayOfColoredLabels[k].first) continue;
                        else {
                            for (int l = 0; l < copiedArrayOfTubes[k].length; l++) {
                                if (copiedArrayOfTubes[k][l] == arrayOfColoredLabels[k].first || copiedArrayOfTubes[k][l] == 'e') {
                                    continue;
                                }
                                else if (copiedArrayOfTubes[k][l] == arrayOfColoredLabels[i].first) {
                                    // swap the layer
                                    char tmp = copiedArrayOfTubes[k][l];
                                    copiedArrayOfTubes[k][l] = copiedArrayOfTubes[i][j];
                                    copiedArrayOfTubes[i][j] = tmp;
                                    isSwapped = true;
                                    totalNumberOfSwaps++;
                                    break;
                                }
                            }
                        }

                        // if the layers is swapped then stop
                        if (isSwapped) {
                            break;
                        }
                    }
                }
            }
        }

        return totalNumberOfSwaps;
    }
}
