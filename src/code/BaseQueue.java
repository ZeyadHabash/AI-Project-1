package code;

import java.util.*;
import java.util.function.BiConsumer;

public class BaseQueue {

    ArrayList<Node> queue;
    public HashSet<String> visitedStates;

    BaseQueue() {
        this.queue = new ArrayList<>();
        this.visitedStates = new HashSet<>();
    }

    /**
     * Enqueue a node based on the strategy
     *
     * @param nodes           list of nodes to add to the queue
     * @param queuingFunction a function to decide how to enqueue the nodes
     **/
    public void enqueue(List<Node> nodes, BiConsumer<ArrayList<Node>, Node> queuingFunction) {
        for (Node node : nodes) {
            if (!visitedStates.contains(node.state.toString())) {
                int initialSize = queue.size();
                queuingFunction.accept(queue, node); // Apply queuing function to node
                if (queue.size() > initialSize) {
                    visitedStates.add(node.state.toString()); // only add to visited states if the node is actually added to the queue
                    if (WaterSortSearch.visualize)
                        System.out.println(ConsoleColors.GREEN_BOLD + "ENQUEUED");
                } else {
                    if (WaterSortSearch.visualize)
                        System.out.println(ConsoleColors.RED_BOLD + "NOT ENQUEUED");
                }
                if (WaterSortSearch.visualize)
                    System.out.println(ConsoleColors.BLUE_BOLD + " * Child Node: " + ConsoleColors.RESET + node);
            }
        }
    }

    /**
     * Dequeue a node
     **/
    public Node dequeue() {
        return queue.remove(0);
    }

    /**
     * Checks if queue is empty
     **/
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Node node : queue) {
            result.append(node.toString()).append(" ");
        }

        return result.toString().trim();
    }
}