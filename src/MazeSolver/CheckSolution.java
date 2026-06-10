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

        Node start = new Node(0, 0, 0, null);
        dist[0][0] = 0;
        pq.add(start);

        int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            int x = current.getX();
            int y = current.getY();

            if (result[x][y] == 5)
                return buildPath(current);
            if (current.getTotalCost() > dist[x][y])
                continue;

            for (int[] dir : dirs) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                int wayCost = 0;
                int dx = dir[0];
                int dy = dir[1];

                // מעבר המסדרון
                while (newX >= 0 && newX < rows && newY >= 0 && newY < cols
                        && result[newX][newY] == 2) {
                    wayCost++;
                    newX += dir[0];
                    newY += dir[1];
                }

                if (newX < 0 || newX >= rows || newY < 0 || newY >= cols) continue;
                if (result[newX][newY] == 0) continue;

                int newTotalCost = current.getTotalCost() + wayCost + 1;

                if (newTotalCost < dist[newX][newY]) {
                    dist[newX][newY] = newTotalCost;
                    Way way = new Way(wayCost, dx, dy, current, null);
                    pq.add(new Node(newX, newY, newTotalCost, way));
                }
            }
        }

        return null;
    }

    private static int[][] buildPath(Node end) {
        List<int[]> path = new ArrayList<>();
        Node current = end;

        while (current != null) {
            Way way = current.getWayFromPrevious();

            if (way == null) {
                path.add(new int[]{current.getX(), current.getY()});
                break;
            }

            int x = current.getX();
            int y = current.getY();
            int dx = way.getDx();
            int dy = way.getDy();

            int steps = way.getCost() + 1;
            for (int i = 0; i < steps; i++) {
                path.add(new int[]{x, y});
                x -= dx;
                y -= dy;
            }

            current = way.getStart();
        }

        Collections.reverse(path);
        return path.toArray(new int[0][]);
    }
}