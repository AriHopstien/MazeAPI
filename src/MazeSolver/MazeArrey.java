package MazeSolver;
import MazeConstraction.GetInstructions;

public class MazeArrey {
    public static int[][] solveMaze(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        int[][] result = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                if (grid[row][col] == 0) {
                    result[row][col] = 0;
                    continue;
                }
                int count = 0;

                if (row > 0 && grid[row - 1][col] == 1) {
                    count++;
                }
                if (row < rows - 1 && grid[row + 1][col] == 1) {
                    count++;
                }
                if (col > 0 && grid[row][col - 1] == 1) {
                    count++;
                }
                if (col < cols - 1 && grid[row][col + 1] == 1) {
                    count++;
                }
                result[row][col] = count;
            }
        }
        result[rows - 1][cols - 1] = 5;
        return result;
    }
}