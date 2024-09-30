package code;

public class Node {

    public State state;
    public Node parent;
    public String operator; // format pour_from_to (pour_0_4 -> poured from bottle 0 to bottle 4)
    public int pathCost;
    public int depth;

    public Node(State state, Node parent, String operator, int pathCost, int depth) {
        this.state = state;
        this.parent = parent;
        this.operator = operator;
        this.pathCost = pathCost;
        this.depth = depth;
    }

}
