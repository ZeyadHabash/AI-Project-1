package code;

import java.util.*;
import java.util.function.BiConsumer;

public class BaseQueue {

    ArrayList<Node> queue;
    HashSet<String> visitedStates;

    BaseQueue() {
        this.queue = new ArrayList<>();
        this.visitedStates = new HashSet<>();
    }

    /**
     * Enqueue a node based on the strategy
     *
     * @param nodes list of nodes to add to the queue
     * @param queuingFunction a function to decide how to enqueue the nodes
     **/
    public void enqueue(List<Node> nodes, BiConsumer<ArrayList<Node>, Node> queuingFunction) {
        if (queue.isEmpty() & nodes.size() == 1 && nodes.getFirst().parent == null) {
            Node root = nodes.removeFirst();
            queue.addFirst(root);
            visitedStates.add(root.state.toString());
        } else {
            for (Node node : nodes) {
                if (!visitedStates.contains(node.state.toString())) {
                    visitedStates.add(node.state.toString());
                    queuingFunction.accept(queue, node);
                    System.out.println(ConsoleColors.BLUE_BOLD + " * Child Node: " + ConsoleColors.RESET + node);
                }
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