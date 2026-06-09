package MazeSolver;

public class MazeArrey {
    public static int[][] solveMaze(int[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;

        int[][] result = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                if (maze[row][col] == 0) {
                    result[row][col] = 0;
                    continue;
                }
                int count = 0;

                if (row > 0 && maze[row - 1][col] == 1) {
                    count++;
                }
                if (row < rows - 1 && maze[row + 1][col] == 1) {
                    count++;
                }
                if (col > 0 && maze[row][col - 1] == 1) {
                    count++;
                }
                if (col < cols - 1 && maze[row][col + 1] == 1) {
                    count++;
                }
                result[row][col] = count;
            }
        }
        result[rows - 1][cols - 1] = 5;
        return result;
    }
}