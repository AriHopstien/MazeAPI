package MazeSolver;

public class Node {
    private int x;
    private int y;
    private int totalCost;
    private Way wayFromPrevious;  // ה-Way שהוביל לצומת הזה

    public Node(int x, int y, int totalCost, Way wayFromPrevious) {
        this.x = x;
        this.y = y;
        this.totalCost = totalCost;
        this.wayFromPrevious = wayFromPrevious;
    }

    public int getX()                  { return x; }
    public int getY()                  { return y; }
    public int getTotalCost()          { return totalCost; }
    public Way getWayFromPrevious()    { return wayFromPrevious; }
    public void setTotalCost(int c)    { this.totalCost = c; }
}