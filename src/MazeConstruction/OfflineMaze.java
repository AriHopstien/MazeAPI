package MazeConstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OfflineMaze {

    private static final String[] DEFAULT_CONFIG = {
            "#222222",  // wallCellColor
            "#00AA00",  // pathColor
            "true",     // drawGrid
            "#CCCCCC",  // gridColor
            "80"        // animationDelayMs
    };

    public static String[] getConfig() {
        return DEFAULT_CONFIG.clone();
    }

    public static int[][] generateMaze(int width, int height) {
        // מתחילים עם כל המבוך סגור
        int[][] grid = new int[height][width];

        Random rand = new Random();

        // Prim's רנדומלי — מתחיל מתא אקראי
        int startRow = rand.nextInt(height);
        int startCol = rand.nextInt(width);
        grid[startRow][startCol] = 1;

        // רשימת הקירות הפוטנציאליים
        List<int[]> walls = new ArrayList<>();
        addWalls(walls, startRow, startCol, height, width);

        while (!walls.isEmpty()) {
            // בחר קיר אקראי
            int idx = rand.nextInt(walls.size());
            int[] wall = walls.get(idx);
            walls.remove(idx);

            int row = wall[0];
            int col = wall[1];
            int fromRow = wall[2];
            int fromCol = wall[3];

            // בדוק שהתא הנגדי עדיין סגור
            int oppositeRow = row + (row - fromRow);
            int oppositeCol = col + (col - fromCol);

            if (oppositeRow < 0 || oppositeRow >= height ||
                    oppositeCol < 0 || oppositeCol >= width)
                continue;

            if (grid[oppositeRow][oppositeCol] == 0) {
                // פתח את הקיר ואת התא הנגדי
                grid[row][col] = 1;
                grid[oppositeRow][oppositeCol] = 1;
                addWalls(walls, oppositeRow, oppositeCol, height, width);
            }
        }

        // וודא שהתחלה וסיום פתוחים
        grid[0][0] = 1;
        grid[height-1][width-1] = 1;

        return grid;
    }

    private static void addWalls(List<int[]> walls, int row, int col,
                                 int height, int width) {
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] dir : dirs) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < height &&
                    newCol >= 0 && newCol < width) {
                walls.add(new int[]{newRow, newCol, row, col});
            }
        }
    }
}