package code;

import java.util.*;
import java.util.function.Function;

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
     * @param strategy a string indicating the search strategy to be applied
     * @param nodes    list of nodes to add it to the queue
     **/
    public void enqueue(String strategy, List<Node> nodes) {
        // initially the root node is inserted at the beginning of the queue
        if (queue.isEmpty() & nodes.size() == 1 && nodes.getFirst().parent == null) {
            Node root = nodes.removeFirst();
            queue.addFirst(root);
            visitedStates.add(root.state.toString());
        } else {
            switch (strategy) {
                case "DF":
                case "ID":
                    // add the nodes to the front of the queue
                    for (Node node : nodes) {
                        // check that the state is not visited
                        if (!visitedStates.contains(node.state.toString())) {
                            visitedStates.add(node.state.toString());
                            queue.addFirst(node);
                            System.out.println(ConsoleColors.BLUE_BOLD + " * Child Node: " + ConsoleColors.RESET + node);
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
                            System.out.println(ConsoleColors.BLUE_BOLD + " * Child Node: " + ConsoleColors.RESET + node);
                        }
                    }

                    break;

                case "UC": case "GR": case "AS":
                    for (Node node : nodes) {
                        if (!visitedStates.contains(node.state.toString())) {
                            visitedStates.add(node.state.toString());
                            sortedInsert(strategy, node);
                            System.out.println(ConsoleColors.BLUE_BOLD + " * Child Node: " + ConsoleColors.RESET + node);
                        }
                    }
                    break;
            }
        }
    }





    /**
     * Insert a node in the correct position based on the strategy
     *
     * @param strategy
     * @param node
     */
    private void sortedInsert(String strategy, Node node) {
        // get the sort value based on the strategy
        // if greedy, sort based on heuristic cost
        // if A*, sort based on heuristic cost + path cost
        // if uniform cost, sort based on path cost

        // Loop over the list to find the correct location
        int insertIndex = 0;
        if (strategy.equals("GR")) {
            while (insertIndex < queue.size() && queue.get(insertIndex).heuristicCost <= node.heuristicCost) {
                insertIndex++;
            }
        } else if (strategy.equals("AS")) {
            while (insertIndex < queue.size() && (queue.get(insertIndex).heuristicCost + queue.get(insertIndex).pathCost) <= node.heuristicCost + node.pathCost) {
                insertIndex++;
            }
        } else {
            while (insertIndex < queue.size() && queue.get(insertIndex).pathCost <= node.pathCost) {
                insertIndex++;
            }
        }
        // Insert the node at this location
        queue.add(insertIndex, node);
    }


    /**
     * Dequeue a node
     **/
    public Node dequeue() {
        WaterSortSearch.nodesExpanded++;
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


//    /**
//     * Sort the queue based on the provided strategy
//     *
//     * @param strategy a string indicating the search strategy to be applied
//     **/
//    public void genericSort(String strategy, int heuristicNumber) {
//
//        switch (strategy) {
//            case "UC":
//                queue.sort(Comparator.comparingInt(node -> node.pathCost));
//            case "GR1":
//            case "GR2":
//                queue.sort(Comparator.comparingInt(node -> node.heuristicCost));
//            case "AR1":
//            case "AR2":
//                // TODO sort based on heuristic number
//                queue.sort(Comparator.comparingInt(node -> node.heuristicCost + node.pathCost));
//        }
//    }

//    /**
//     * Enqueue a node based on the strategy and heuristic for Greedy and A*
//     *
//     * @param strategy a string indicating the search strategy to be applied
//     * @param nodes    list of nodes to add it to the queue
//     **/
//    public void enqueue(String strategy, List<Node> nodes, int heuristicValue) {
//        for (Node node : nodes) {
//            if (!visitedStates.contains(node.state.toString())) {
//                visitedStates.add(node.state.toString());
//                node.setHeuristicCost(heuristicValue);
//                int insertIndex = 0;
//                sortedInsert(strategy, node);
//                System.out.println(ConsoleColors.BLUE_BOLD + " * Child Node: " + ConsoleColors.RESET + node);
//            }
//        }
//    }