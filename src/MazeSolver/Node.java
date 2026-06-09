package MazeSolver;

public class Node {
    private int x;
    private int y;
    private int totalCost;
    private Node previous;

    public Node(int x, int y, int cost, int totalCost, Node previous) {
        this.x = x;
        this.y = y;
        this.totalCost = totalCost;
        this.previous = previous;
    }
    public int getX() {return x;}
    public int getY() {return y;}
    public int getTotalCost() {return totalCost;}
    public Node getPrevious() {return previous;}

    public void setTotalCost(int totalCost) {this.totalCost = totalCost;}
    public void setPrevious(Node previous) {this.previous = previous;}
}