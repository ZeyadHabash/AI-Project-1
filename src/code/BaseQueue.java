package code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BaseQueue {

    ArrayList<Node> queue = new ArrayList<>();
    HashSet<String> visitedStates = new HashSet<>();

    /**
    * Enqueue a node based on the strategy
    * @param strategy -
    * @param nodes -
    **/
    public void enqueue(String strategy, List<Node> nodes) {
        switch (strategy) {
            case "DF": case "ID":
                // add the nodes to the front of the queue
                for (Node node : nodes) {
                    queue.addFirst(node);
                }
                break;
            case "BF":
                // add the nodes to the end of the queue
                for (Node node : nodes) {
                    queue.addLast(node);
                }
                break;
            case "UC":
                // add to the queue in any order
                for (Node node : nodes) {
                    queue.addLast(node);
                }

                // sort the queue
                sort();
                break;
        }
    }

    /**
    * Enqueue a node based on the strategy and heuristic for Greedy and A*
    * @param strategy -
    * @param nodes -
    * @param heuristic -
    **/
    public void enqueue(String strategy, List<Node> nodes, String heuristic) {
        switch (strategy) {
            case "GR":
                // add the nodes to the end of the queue
                for (Node node : nodes) {
                    queue.addLast(node);
                }
                // TODO - sort based on heuristic
                break;
            case "AS":
                // add to the queue in any order
                for (Node node : nodes) {
                    queue.addLast(node);
                }
                // TODO - sort based on heuristic
                break;
        }
    }

    /**
     * Dequeue a node
     **/
    public Node dequeue() {
        // return first element and remove it from arraylist
        return queue.removeFirst();
    }

    /**
    * Checks if queue is empty
    **/
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Sort Basic Queue
    **/
    private void sort() {
        // TODO
    }
}
