import MazeSolver.MazeArrey;

import static MazeSolver.CheckSolution.solve;

public class Main {
    public static void main(String[] args) {
        int[][] maze = {
                {1, 1, 0, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1},
                {0, 0, 1, 0, 0},
                {1, 1, 1, 1, 1}
        };

        int[][] result = MazeArrey.solveMaze(maze);
        int[][] path = solve(result);

        if (path == null) {
            System.out.println("אין פתרון");
            return;
        }

        for (int[] step : path) {
            System.out.println("[" + step[0] + "," + step[1] + "]");
        }
    }
}