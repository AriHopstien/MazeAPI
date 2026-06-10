package MazeSolver;

public class Way {
    private int cost;
    private int dx;   // כיוון השורה: -1, 0, או 1
    private int dy;   // כיוון העמודה: -1, 0, או 1
    private Node start;
    private Node end;

    public Way(int cost, int dx, int dy, Node start, Node end) {
        this.cost = cost;
        this.dx = dx;
        this.dy = dy;
        this.start = start;
        this.end = end;
    }

    public Way(int wayCost, Node current, Node destination) {
    }

    public int getCost()   { return cost; }
    public int getDx()     { return dx; }
    public int getDy()     { return dy; }
    public Node getStart() { return start; }
    public Node getEnd()   { return end; }
}
