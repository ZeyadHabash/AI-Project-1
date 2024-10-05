package code;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class BaseQueue {

    ArrayList<Node> queue;
    HashSet<String> visitedStates;

    BaseQueue() {
        this.queue = new ArrayList<>();
        this.visitedStates = new HashSet<>();
    }

    /**
    * Enqueue a node based on the strategy
    * @param strategy a string indicating the search strategy to be applied
    * @param nodes list of nodes to add it to the queue
    **/
    public void enqueue(String strategy, List<Node> nodes) {
        // initially the root node is inserted at the beginning of the queue
        if (queue.isEmpty() & nodes.size() == 1 && nodes.getFirst().parent == null) {
            Node root = nodes.removeFirst();
            queue.addFirst(root);
            visitedStates.add(root.state.toString());
        }
        else {
            switch (strategy) {
                case "DF": case "ID":
                    // add the nodes to the front of the queue
                    for (Node node : nodes) {
                        // check that the state is not visited
                        if (!visitedStates.contains(node.state.toString())) {
                            visitedStates.add(node.state.toString());
                            queue.addFirst(node);
                        }
                    }

                    break;
                case "BF":
                    // add the nodes to the end of the queue
                    for (Node node : nodes) {
                        // check that the state is not visited
                        if (!visitedStates.contains(node.state.toString())) {
                            visitedStates.add(node.state.toString());
                            queue.addLast(node);
                        }
                    }

                    break;
                case "UC":
                    // add to the queue in any order

                    for (Node node : nodes) {
                        if (!visitedStates.contains(node.state.toString())) {
                            visitedStates.add(node.state.toString());
                            queue.addFirst(node);
                        }
                    }
                    this.sortCost();
                    break;
            }
        }
    }
    private void sortCost() {
        System.out.println("I am in sorting");
        List<Node> nodeList = new ArrayList<>();
        while (!queue.isEmpty())
            nodeList.add(queue.removeFirst());
        nodeList.sort(Comparator.comparingInt(node -> node.pathCost));
        for (Node node : nodeList)
            queue.addLast(node);
        System.out.println("queue"+ queue);
    }

    /**
    * Enqueue a node based on the strategy and heuristic for Greedy and A*
    * @param strategy
    * @param nodes
    * @param heuristic
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Node node : queue) {
            result.append(node.toString()).append(" ");
        }

        return result.toString().trim();
    }
}
