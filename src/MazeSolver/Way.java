package MazeSolver;

public class Way {
    private int cost;
    private Node start;
    private Node end;

    public Way(int cost, Node start, Node end) {
        this.cost = cost;
        this.start = start;
        this.end = end;
    }
    public int getCost() {return cost;}
    public Node getStart() {return start;}
    public Node getEnd() {return end;}
}
