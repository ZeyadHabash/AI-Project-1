package code;

import java.util.ArrayList;
import java.util.List;

public class GenericSearch {

    /**
     * Calculate the cost of pouring from the first bottle into the second bottle.
     * @param root the current state
     * @return a queue
     */
    public static BaseQueue makeQueue(Node root) {
        BaseQueue queue = new BaseQueue();
        List<Node> nodes = new ArrayList<>();
        nodes.add(root);
        queue.enqueue("", nodes); // initially first node is inserted at the beginning of the queue
        return queue;
    }


    /**
     * Calculate the cost of pouring from the first bottle into the second bottle.
     * @param initialState the current state
     * @return a root node
     */
    public static Node makeNode(State initialState) {
        return new Node(
                initialState,
                null,
                "",
                0,
                0
        );
    }


    /**
     * Loops over different values of Depth to perform iterative deepening
     * @param problem general search problem
     */
    private static Node iterativeDeepeningSearch(Problem problem) {
        int depthLimit = 0;
        while (true) {
            BaseQueue queue = makeQueue(makeNode(problem.initialState));
            Node result = depthLimitedSearch(queue, problem, depthLimit);
            if (result != null)
                return result;
            depthLimit++;
        }
    }


    /**
     * Performs DFS with a limited depth (Helper method to iterativeDeepeningSearch)
     * @param queue for node exploration
     * @param problem general search problem
     * @param depthLimit of DFS
     * @return a goal node
     */
    private static Node depthLimitedSearch(BaseQueue queue, Problem problem, int depthLimit) {
        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
//            System.out.println("Current State: " + node.state + "    depth:" + node.depth + "     limit" + depthLimit);
            if (problem.goalTest(node.state))
                return node;
            if (node.depth < depthLimit ) {
                queue.enqueue("ID", problem.expand(node));
            }
        }
        return null;
    }


    /**
     * Calculate the cost of pouring from the first bottle into the second bottle.
     * @param problem general search problem
     * @param strategy a strategy to force the queue to follow [DF, BF, ID, UC, GR, AS]
     * @return a goal node
     */
    public static Node generalSearch(Problem problem, String strategy) {
        if (strategy.equals("ID"))
            return iterativeDeepeningSearch(problem);

        BaseQueue queue = makeQueue(makeNode(problem.initialState));

        while (true) {
            if (queue.isEmpty()) return null;

//            System.out.println("Queue: " + queue);
            Node node = queue.dequeue();


            if(problem.goalTest(node.state)) {
//                System.out.println(ConsoleColors.RED_BOLD + "Goal State: " + ConsoleColors.RESET + node.state);
                return node;
            }

//            System.out.println(ConsoleColors.RED_BOLD + "Current State: " + ConsoleColors.RESET + node.state);
            // TODO: enqueue next nodes in line
            switch (strategy) {
                case "DF": case "BF": case "UC":
                    queue.enqueue(strategy, problem.expand(node));
                case "GR1": case "AS1":
                    queue.enqueue(strategy, problem.expand(node), 1);
                case "GR2": case "AS2":
                    queue.enqueue(strategy, problem.expand(node), 2);
            }
        }
    }

//    /**
//     * Calculate the cost of pouring from the first bottle into the second bottle.
//     * @param node the current node
//     * @param problem general search problem
//     * @return a list of nodes resulted from expanding the given node
//     */
//    public static List<Node> expand(Node node, Problem problem) {
//
//        WaterSearchState parentState = (WaterSearchState) node.state;
//        List<Node> listOfPossibleNextNodes = new ArrayList<>();
//
//        for (int i = 0; i < parentState.numOfBottles; i++) {
//            for (int j = 0; j < parentState.numOfBottles; j++) {
//                enqueueNextPossibleNode(node, problem, listOfPossibleNextNodes, j, i);
//                enqueueNextPossibleNode(node, problem, listOfPossibleNextNodes, i, j);
//            }
//        }
//
//        return listOfPossibleNextNodes;
//    }

//    /**
//     * Create a new node from pouring from the ith tube into the jth tube and add it to the listOfPossibleNextNodes
//     * @param node the parent node
//     * @param problem general search problem
//     * @param listOfPossibleNextNodes list of possible next nodes to visit
//     * @param i the first bottle index
//     * @param j the second bottle index
//     */
//    private static void enqueueNextPossibleNode(Node node, Problem problem, List<Node> listOfPossibleNextNodes, int i, int j) {
//        // calculate cost of pouring from the ith tube into the jth tube.
//        int costOfPourFromSecondToFirst = calculateCost((WaterSearchState) node.state, j, i);
//        if (costOfPourFromSecondToFirst != -1) {
//            String operater = "pour_" + j + "_" + i;
//            // create the next possible state using the transition function
//            State possibleNextState = problem.transitionFunction(node.state, operater);
//
//            // add the next possible node to the list
//            Node childNode = new Node(
//                    possibleNextState,
//                    node,
//                    operater,
//                    node.pathCost + costOfPourFromSecondToFirst,
//                    node.depth + 1
//            );
//
//            listOfPossibleNextNodes.add(childNode);
//        }
//    }

//    /**
//     * Calculate the cost of pouring from the first bottle into the second bottle.
//     * @param state the current state
//     * @param firstBottleIndex index of the first bottle (zero-based)
//     * @param secondBottleIndex index of the second bottle (zero-based)
//     * @return integer which is the cost of pouring from firstBottleIndex to secondBottleIndex
//     */
//    public static int calculateCost(WaterSearchState state, int firstBottleIndex, int secondBottleIndex) {
//
//        int firstBottleTopPointer = state.arrayOfTopPointers[firstBottleIndex];
//        int secondBottleTopPointer = state.arrayOfTopPointers[secondBottleIndex];
//
//        if (canPour(state, firstBottleIndex, secondBottleIndex)) {
//
//            // if secondBottleTopPointer pointing to -1 meaning bottle is empty then we can pour into it.
//            int minimumAmountCanBePoured = secondBottleTopPointer == -1 ? state.bottleCapacity : secondBottleTopPointer;
//
//            // count of the same number of layers on the top of the tube
//            int numOfConsecutiveTopLayers = 0;
//
//            // get the color of the top layer in the first tube
//            char colorOfTopLayer = state.arrayOfTubes[firstBottleIndex][firstBottleTopPointer];
//
//            for (int i = firstBottleTopPointer; i < state.bottleCapacity; i++) {
//
//                if (state.arrayOfTubes[firstBottleIndex][i] == colorOfTopLayer) {
//                    numOfConsecutiveTopLayers++;
//                }
//                else {
//                    break;
//                }
//            }
//
//            minimumAmountCanBePoured = Math.min(minimumAmountCanBePoured, numOfConsecutiveTopLayers);
//
//            return minimumAmountCanBePoured;
//
//        } else {
//            return -1;
//        }
//    }

//    /**
//     * Check whether the first bottle can be poured into the second bottle.
//     * @param state the current state
//     * @param firstBottleIndex index of the first bottle (zero-based)
//     * @param secondBottleIndex index of the second bottle (zero-based)
//     * @return boolean that determines whether we can pour from firstBottleIndex to secondBottleIndex
//     */
//    public static boolean canPour(WaterSearchState state, int firstBottleIndex, int secondBottleIndex) {
//
//        char firstBottleTopColor = state.arrayOfTopPointers[firstBottleIndex] != -1 ? state.arrayOfTubes[firstBottleIndex][state.arrayOfTopPointers[firstBottleIndex]] : 'e';
//        char secondBottleTopColor = state.arrayOfTopPointers[secondBottleIndex] != -1 ? state.arrayOfTubes[secondBottleIndex][state.arrayOfTopPointers[secondBottleIndex]] : 'e';
//
//        // check whether the two different tubes contains the same top color & first bottle is not empty
//        return (secondBottleTopColor == 'e' || firstBottleTopColor == secondBottleTopColor)  && firstBottleTopColor != 'e' && firstBottleIndex != secondBottleIndex && state.arrayOfTopPointers[secondBottleIndex] != 0;
//    }

}
