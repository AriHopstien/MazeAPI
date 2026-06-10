package MazeSolver;

import java.util.*;

public class CheckSolution {

    public static int[][] solve(int[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;

        if (maze[0][0] == 0 || maze[rows-1][cols-1] == 0)
            return null;

        int[][] dist = new int[rows][cols];
        for (int[] row : dist)
            Arrays.fill(row, Integer.MAX_VALUE);

        PriorityQueue<Node> pq = new PriorityQueue<>(
                Comparator.comparingInt(Node::getTotalCost)
        );

        Node start = new Node(0, 0, 0, null);
        dist[0][0] = 0;
        pq.add(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int x = current.getX();
            int y = current.getY();

            if (x == rows-1 && y == cols-1)
                return buildPath(current);

            if (current.getTotalCost() > dist[x][y])
                continue;

            int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] dir : dirs) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                int wayCost = 0;

                // הולך לאורך המסדרון וסופר תאי 2
                while (newX >= 0 && newX < rows && newY >= 0 && newY < cols
                        && maze[newX][newY] == 2) {
                    wayCost++;
                    newX += dir[0];
                    newY += dir[1];
                }

                if (newX < 0 || newX >= rows || newY < 0 || newY >= cols) continue;
                if (maze[newX][newY] == 0) continue;

                Node destination = new Node(newX, newY, 0, null);
                Way way = new Way(wayCost, dir[0], dir[1], current, destination);

                int newTotalCost = current.getTotalCost() + way.getCost() + 1;

                if (newTotalCost < dist[newX][newY]) {
                    dist[newX][newY] = newTotalCost;
                    pq.add(new Node(newX, newY, newTotalCost,
                            new Way(wayCost, dir[0], dir[1], current, destination)));
                }
            }
        }

        return null;
    }

    private static int[][] buildPath(Node end) {
        List<int[]> path = new ArrayList<>();
        Node current = end;

        while (current != null) {
            // מוסיף את הצומת עצמה
            path.add(new int[]{current.getX(), current.getY()});

            // מוסיף את תאי ה-2 שלפניה דרך ה-Way
            Way way = current.getWayFromPrevious();
            if (way != null) {
                Node prev = way.getStart();
                int cx = current.getX() - way.getDx();
                int cy = current.getY() - way.getDy();
                while (cx != prev.getX() || cy != prev.getY()) {
                    path.add(new int[]{cx, cy});
                    cx -= way.getDx();
                    cy -= way.getDy();
                }
            }

            current = (way != null) ? way.getStart() : null;
        }

        Collections.reverse(path);
        return path.toArray(new int[0][]);
    }
}