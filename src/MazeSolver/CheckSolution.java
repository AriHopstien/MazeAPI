// שלב 10
package MazeSolver;
import java.util.*;

public class CheckSolution {
    public static int[][] solve(int[][] result) {
        int rows = result.length;
        int cols = result[0].length;

        int[][] dist = new int[rows][cols];
        for (int[] row : dist)
            Arrays.fill(row, Integer.MAX_VALUE);

            PriorityQueue<Node> pq = new PriorityQueue<>(
                    Comparator.comparingInt(Node::getTotalCost)
            );

            Node start = new Node(0, 0, 0, 0, null);
            dist[0][0] = 0;
            pq.add(start);

            while (!pq.isEmpty()) {
                Node current = pq.poll();

                int x = current.getX();
                int y = current.getY();

                if (result[x][y] == 5)
                    return buildPath(current);
                if (current.getTotalCost() > dist[x][y])
                    continue;

                int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                for (int[] dir : dirs) {
                    int newX = x + dir[0];
                    int newY = y + dir[1];

                    if (newX < 0 || newX >= rows || newY < 0 || newY >= cols)
                        continue;
                    if (result[newX][newY] == 0)
                        continue;

                    int newCost = current.getTotalCost() + result[newX][newY] + 1;
                    if (newCost < dist[newX][newY]) {
                        dist[newX][newY] = newCost;
                        pq.add(new Node(newX, newY, 0, newCost, current));
                    }
                }
            }

            return null;
    }
    private static  int[][] buildPath(Node end) {
        List<int[]> path = new ArrayList<>();
        Node current = end;

        while (current != null) {
            path.add(new int[]{current.getX(), current.getY()});
            current = current.getPrevious();
        }

        Collections.reverse(path);
        return path.toArray(new int[0][]);
    }

}