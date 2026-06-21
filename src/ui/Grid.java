// שלב 8
package ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Grid {
    private static int square;

    public static BufferedImage CrateImage(int[][] pixel, int width, int height, String wallCellColor,boolean drawGrid, String gridColor){
        Color color_wall = Color.decode(wallCellColor);
        Color color_grid = null;
        if (drawGrid) {
            color_grid = Color.decode(gridColor);
        }
        int max = Math.max(width,height);
        square = 600/max;

        width = width*square;
        height = height*square;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        for (int row = 0; row < pixel.length; row++) {
            for (int col = 0; col < pixel[row].length; col++) {
                if (pixel[row][col] == 1){
                    g2d.setColor(Color.WHITE);
                }
                else {
                    g2d.setColor(color_wall);
                }
                int x = col *square;
                int y = row *square;
                g2d.fillRect(x,y,square,square);
                if(drawGrid){
                    g2d.setColor(color_grid);
                    g2d.drawRect(x,y,square,square);
                }
            }
        }
        g2d.dispose();
        return image;
    }

    public static BufferedImage Crateanimation(BufferedImage image, int[] pixel,String pathcolor, boolean drawGrid, String gridColor){
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        Color color_path = Color.decode(pathcolor);
        Color color_grid = null;
        g2d.setColor(color_path);
        int y = pixel[0] * square;
        int x = pixel[1] * square;
        g2d.fillRect(x, y,square,square);
        if(drawGrid){
            color_grid = Color.decode(gridColor);
            g2d.setColor(color_grid);
            g2d.drawRect(x, y,square,square);
        }
        g2d.dispose();
        return image;
    }

    public static int getSquare() {
        return square;
    }

    /**
     * יוצרת תמונה של המבוך עם הפתרון מצוייר בשלמותו
     * @param image תמונת המבוך הבסיסית
     * @param solution מערך של קואורדינטות הפתרון
     * @param pathColor צבע הנתיב
     * @param drawGrid האם לצייר רשת
     * @param gridColor צבע הרשת
     * @return תמונה של המבוך עם הפתרון שלם
     */
    public static BufferedImage CreateSolutionImage(BufferedImage image, int[][] solution, String pathColor, boolean drawGrid, String gridColor) {
        if (solution == null) {
            return image;
        }

        BufferedImage solutionImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = solutionImage.createGraphics();

        // צייור התמונה המקורית
        g2d.drawImage(image, 0, 0, null);

        Color color_path = Color.decode(pathColor);
        Color color_grid = null;
        if (drawGrid) {
            color_grid = Color.decode(gridColor);
        }

        // צייור כל נקודות הפתרון
        for (int i = 0; i < solution.length; i++) {
            int y = solution[i][0] * square;
            int x = solution[i][1] * square;

            g2d.setColor(color_path);
            g2d.fillRect(x, y, square, square);

            if (drawGrid) {
                g2d.setColor(color_grid);
                g2d.drawRect(x, y, square, square);
            }
        }

        g2d.dispose();
        return solutionImage;
    }

    /**
     * שומרת תמונה כקובץ PNG בתיקיית Downloads
     * @param image התמונה לשמירה
     * @return true אם ההצלחה, false אם הייתה שגיאה
     */
    public static boolean SaveMazeAsImage(BufferedImage image) {
        try {
            // יצירת שם קובץ עם timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String timestamp = now.format(formatter);
            String filename = "maze_solution_" + timestamp + ".png";

            // נתיב לתיקיית Downloads
            String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
            File directory = new File(downloadsPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File outputFile = new File(directory, filename);

            // שמירת הקובץ
            ImageIO.write(image, "png", outputFile);

            System.out.println("✓ המבוך הפתור נשמר בהצלחה: " + outputFile.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("✗ שגיאה בשמירת המבוך: " + e.getMessage());
            return false;
        }
    }
}