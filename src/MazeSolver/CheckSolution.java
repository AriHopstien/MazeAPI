package MazeSolver;

import java.util.*;

public class CheckSolution {

    public static int[][] solve(int[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;

        // ✓ בדוק אם התחלה או סיום הם קירות
        if (maze[0][0] == 0 || maze[rows - 1][cols - 1] == 0) {
            return null; // אין פתרון
        }

        // ✓ BFS - פשוט ואמין למבוך
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] parent = new int[rows][cols];

        // אתחול parent ל-(-1, -1)
        for (int[] row : parent) {
            Arrays.fill(row, -1);
        }

        queue.add(new int[]{0, 0});
        visited[0][0] = true;
        parent[0][0] = -2; // סימן התחלה

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            // ✓ בדוק אם הגענו לסיום
            if (x == rows - 1 && y == cols - 1) {
                return buildPath(parent, rows, cols);
            }

            // ✓ חפש בכל הכיוונים
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                // ✓ בדוק תקינות וערך
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols
                        && !visited[newX][newY] && maze[newX][newY] == 1) {

                    visited[newX][newY] = true;
                    parent[newX][newY] = x * cols + y; // שמור את ההורה
                    queue.add(new int[]{newX, newY});
                }
            }
        }

        return null; // אין פתרון
    }

    private static int[][] buildPath(int[][] parent, int rows, int cols) {
        List<int[]> path = new ArrayList<>();

        int x = rows - 1;
        int y = cols - 1;

        // ✓ בנה את הנתיב מהסוף להתחלה
        while (parent[x][y] != -2) {
            path.add(new int[]{x, y});

            int prevIdx = parent[x][y];
            x = prevIdx / cols;
            y = prevIdx % cols;
        }

        path.add(new int[]{0, 0}); // הוסף את ההתחלה
        Collections.reverse(path); // הפוך לסדר נכון

        return path.toArray(new int[0][]);
    }
}