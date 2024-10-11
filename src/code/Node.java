package code;

public class Node {

    public State state;
    public Node parent;
    public String operator; // format pour_from_to (pour_0_4 -> poured from bottle 0 to bottle 4)
    public int pathCost;
    public int depth;
    public int heuristicCost = -1;

    public Node(State state, Node parent, String operator, int pathCost, int depth) {
        this.state = state;
        this.parent = parent;
        this.operator = operator;
        this.pathCost = pathCost;
        this.depth = depth;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    @Override
    public String toString() {
        return  "\n     " +
                ConsoleColors.BLUE_BOLD + "State: " + ConsoleColors.RESET + this.state + "\n     " +
                ConsoleColors.BLUE_BOLD + " * Operator: " + ConsoleColors.RESET + this.operator + "\n     " +
                ConsoleColors.BLUE_BOLD + " * Path Cost: " + ConsoleColors.RESET + this.pathCost  + "\n     " +
                ConsoleColors.BLUE_BOLD + " * Heuristic Cost: " + ConsoleColors.RESET  + this.heuristicCost + "\n     " +
                ConsoleColors.BLUE_BOLD + " * Depth: " + ConsoleColors.RESET + this.depth  + "\n     " +
                ConsoleColors.BLUE_BOLD + " * Length: " + ConsoleColors.RESET + this.depth  + "\n     ";
    }

}
