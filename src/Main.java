import MazeSolver.MazeArrey;

import static MazeSolver.CheckSolution.solve;

public static void main(String[] args) {
    int[][] maze = {
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1}
    };

    System.out.println("=== גריד מקורי ===");
    printGrid(maze, null);

    int[][] result = MazeArrey.solveMaze(maze);

    System.out.println("\n=== גריד אחרי MazeArrey (ערכי צמתים) ===");
    printGrid(result, null);

    int[][] path = solve(result);

    if (path == null) {
        System.out.println("אין פתרון");
        return;
    }

    System.out.println("\n=== נתיב שנמצא ===");
    Set<String> pathSet = new HashSet<>();
    for (int i = 0; i < path.length; i++) {
        int[] step = path[i];
        pathSet.add(step[0] + "," + step[1]);
        int cellValue = result[step[0]][step[1]];
        String type = switch (cellValue) {
            case 1 -> "פינה עם מוצא אחד";
            case 2 -> "מסדרון";
            case 3 -> "צומת T";
            case 4 -> "צומת +";
            case 5 -> "סיום";
            default -> "לא ידוע(" + cellValue + ")";
        };
        System.out.printf("צעד %2d: [%d,%d]  ערך=%d (%s)%n", i, step[0], step[1], cellValue, type);
    }

    System.out.println("\nסה\"כ צעדים: " + path.length);
    System.out.println("התחלה: [" + path[0][0] + "," + path[0][1] + "]");
    System.out.println("סיום:  [" + path[path.length-1][0] + "," + path[path.length-1][1] + "]");
    System.out.println("מגיע ל-(4,4)? " + (path[path.length-1][0] == 4 && path[path.length-1][1] == 4));

    System.out.println("\n=== גריד עם נתיב מסומן ב-* ===");
    printGrid(result, pathSet);
}

private static void printGrid(int[][] grid, Set<String> highlight) {
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
            if (highlight != null && highlight.contains(r + "," + c))
                System.out.print(" * ");
            else
                System.out.printf("%2d ", grid[r][c]);
        }
        System.out.println();
    }
}