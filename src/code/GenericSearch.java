package code;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GenericSearch {

    // <editor-fold desc="General Functions">

    /**
     * Calculate the cost of pouring from the first bottle into the second bottle.
     *
     * @param root the current state
     * @return a queue
     */
    public static BaseQueue makeQueue(Node root) {
        BaseQueue queue = new BaseQueue();
        List<Node> nodes = new ArrayList<>();
        nodes.add(root);
        BiConsumer<ArrayList<Node>, Node> queuingFunction = ArrayList::add; // add to the end of the queue
        queue.enqueue(nodes, queuingFunction); // initially first node is inserted at the beginning of the queue
        return queue;
    }


    /**
     * Calculate the cost of pouring from the first bottle into the second bottle.
     *
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
     * Perform a general search algorithm
     *
     * @param problem         general search problem
     * @param queuingFunction a function to decide how to enqueue the nodes
     * @return a goal node
     */
    public static Node generalSearch(Problem problem, BiConsumer<ArrayList<Node>, Node> queuingFunction) {

        BaseQueue queue = makeQueue(makeNode(problem.initialState));

        while (true) {
            if (queue.isEmpty()) return null;

            Node node = queue.dequeue(); // get the first node in the queue

            // check if the goal state is reached
            if (problem.goalTest(node.state)) {
                System.out.println(ConsoleColors.GREEN_BOLD + "Goal State: " + ConsoleColors.RESET + node.state);
                return node; // return the goal node
            }

            // if the goal state is not reached, expand the node
            System.out.println(ConsoleColors.RED_BOLD + "Current State: " + ConsoleColors.RESET + node.state);

            queue.enqueue(problem.expand(node), queuingFunction); // add the children nodes to the queue
        }
    }

    // </editor-fold>

    // <editor-fold desc="Uninformed Search Algorithms">

    /**
     * Performs Depth First Search
     *
     * @param problem general search problem
     * @return a goal node
     */
    public static Node depthFirstSearch(Problem problem) {
        BiConsumer<ArrayList<Node>, Node> queuingFunction = ArrayList::addFirst; // add to the beginning of the queue
        return generalSearch(problem, queuingFunction);
    }


    /**
     * Performs Breadth First Search
     *
     * @param problem general search problem
     * @return a goal node
     */
    public static Node breadthFirstSearch(Problem problem) {
        BiConsumer<ArrayList<Node>, Node> queuingFunction = ArrayList::addLast; // add to the end of the queue
        return generalSearch(problem, queuingFunction);
    }


    /**
     * Performs Uniform Cost Search
     *
     * @param problem general search problem
     * @return a goal node
     */
    public static Node uniformCostSearch(Problem problem) {
        BiConsumer<ArrayList<Node>, Node> queuingFunction = (queue, node) -> {
            int index = 0;
            for (Node n : queue) {
                if (n.pathCost > node.pathCost) {
                    break;
                }
                index++;
            }
            queue.add(index, node); // add the node to the queue based on the path cost
        };
        return generalSearch(problem, queuingFunction);
    }

    /**
     * Loops over different values of Depth to perform iterative deepening
     *
     * @param problem general search problem
     */
    public static Node iterativeDeepeningSearch(Problem problem) {
        int depthLimit = 0;
        while (true) {
            Node result = depthLimitedSearch(problem, depthLimit);
            if (result != null)
                return result;
            depthLimit++;
        }
    }


    /**
     * Performs DFS with a limited depth (Helper method to iterativeDeepeningSearch)
     *
     * @param problem    general search problem
     * @param depthLimit maximum depth that DFS can reach
     * @return a goal node
     */
    public static Node depthLimitedSearch(Problem problem, int depthLimit) {
        System.out.println(ConsoleColors.PURPLE + "limit: " + ConsoleColors.RESET + depthLimit);
        BiConsumer<ArrayList<Node>, Node> queuingFunction = (queue, node) -> {
            if (node.depth <= depthLimit) { // check if the depth is within the limit
                queue.addFirst(node); // add to the beginning of the queue
            }
        };
        return generalSearch(problem, queuingFunction);
    }

    // </editor-fold>

    // <editor-fold desc="Informed Search Algorithms">

    /**
     * Performs Greedy Best First Search
     *
     * @param problem general search problem
     * @return a goal node
     */
    public static Node greedySearch(Problem problem) {
        BiConsumer<ArrayList<Node>, Node> queuingFunction = (queue, node) -> {
            node.setHeuristicCost(problem.heuristicCost(node.state)); // set the heuristic cost
            int index = 0;
            for (Node n : queue) {
                if (n.heuristicCost > node.heuristicCost) {
                    break;
                }
                index++;
            }
            queue.add(index, node); // add the node to the queue based on the heuristic cost
        };
        return generalSearch(problem, queuingFunction);
    }

    /**
     * Performs A* Search
     *
     * @param problem general search problem
     * @return a goal node
     */
    public static Node aStarSearch(Problem problem) {
        BiConsumer<ArrayList<Node>, Node> queuingFunction = (queue, node) -> {
            node.setHeuristicCost(problem.heuristicCost(node.state)); // set the heuristic cost
            int index = 0;
            for (Node n : queue) {
                if (n.pathCost + n.heuristicCost > node.pathCost + node.heuristicCost) {
                    break;
                }
                index++;
            }
            queue.add(index, node); // add the node to the queue based on the path cost + heuristic cost
        };
        return generalSearch(problem, queuingFunction);
    }
    // </editor-fold>


}
