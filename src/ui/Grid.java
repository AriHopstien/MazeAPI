package ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Grid {
    private static int square;
    private static Color colorStart;
    private static Color colorEnd;
    private static int startX = -1;
    private static int startY = -1;
    private static int endX = -1;
    private static int endY = -1;

    public static BufferedImage CrateImage(int[][] pixel, int width, int height, String wallCellColor, boolean drawGrid, String gridColor) {
        Random rand = new Random();
        colorStart = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        colorEnd = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        startX = -1;
        startY = -1;
        endX = -1;
        endY = -1;

        Color color_wall = Color.decode(wallCellColor);
        Color color_grid = null;
        if (drawGrid) {
            color_grid = Color.decode(gridColor);
        }
        int max = Math.max(width, height);
        square = 600 / max;

        width = width * square;
        height = height * square;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        boolean foundStart = false;
        boolean foundEnd = false;

        for (int row = 0; row < pixel.length; row++) {
            for (int col = 0; col < pixel[row].length; col++) {
                if (pixel[row][col] == 3) {
                    foundStart = true;
                    startY = row;
                    startX = col;
                } else if (pixel[row][col] == 5) {
                    foundEnd = true;
                    endY = row;
                    endX = col;
                }
            }
        }

        if (!foundStart) {
            startY = 0;
            startX = 0;
        }
        if (!foundEnd) {
            endY = pixel.length - 1;
            endX = pixel[0].length - 1;
        }

        for (int row = 0; row < pixel.length; row++) {
            for (int col = 0; col < pixel[row].length; col++) {
                if (row == startY && col == startX) {
                    g2d.setColor(colorStart);
                } else if (row == endY && col == endX) {
                    g2d.setColor(colorEnd);
                } else if (pixel[row][col] == 1 || pixel[row][col] == 2) {
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(color_wall);
                }

                int x = col * square;
                int y = row * square;
                g2d.fillRect(x, y, square, square);

                if (drawGrid) {
                    g2d.setColor(color_grid);
                    g2d.drawRect(x, y, square, square);
                }
            }
        }
        g2d.dispose();
        return image;
    }

    public static BufferedImage Crateanimation(BufferedImage image, int[] pixel, String pathcolor, boolean drawGrid, String gridColor) {
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        Color color_path = Color.decode(pathcolor);
        Color color_grid = null;
        g2d.setColor(color_path);
        int y = pixel[0] * square;
        int x = pixel[1] * square;
        g2d.fillRect(x, y, square, square);

        if (drawGrid) {
            color_grid = Color.decode(gridColor);
            g2d.setColor(color_grid);
            g2d.drawRect(x, y, square, square);
        }

        if (startX != -1 && startY != -1) {
            g2d.setColor(colorStart);
            g2d.fillRect(startX * square, startY * square, square, square);
            if (drawGrid) {
                g2d.setColor(Color.decode(gridColor));
                g2d.drawRect(startX * square, startY * square, square, square);
            }
        }
        if (endX != -1 && endY != -1) {
            g2d.setColor(colorEnd);
            g2d.fillRect(endX * square, endY * square, square, square);
            if (drawGrid) {
                g2d.setColor(Color.decode(gridColor));
                g2d.drawRect(endX * square, endY * square, square, square);
            }
        }

        g2d.dispose();
        return image;
    }

    public static int getSquare() {
        return square;
    }

    public static BufferedImage CreateSolutionImage(BufferedImage image, int[][] solution, String pathColor, boolean drawGrid, String gridColor) {
        if (solution == null) {
            return image;
        }

        BufferedImage solutionImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = solutionImage.createGraphics();

        g2d.drawImage(image, 0, 0, null);

        Color color_path = Color.decode(pathColor);
        Color color_grid = null;
        if (drawGrid) {
            color_grid = Color.decode(gridColor);
        }

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

        if (startX != -1 && startY != -1) {
            g2d.setColor(colorStart);
            g2d.fillRect(startX * square, startY * square, square, square);
            if (drawGrid) {
                g2d.setColor(Color.decode(gridColor));
                g2d.drawRect(startX * square, startY * square, square, square);
            }
        }
        if (endX != -1 && endY != -1) {
            g2d.setColor(colorEnd);
            g2d.fillRect(endX * square, endY * square, square, square);
            if (drawGrid) {
                g2d.setColor(Color.decode(gridColor));
                g2d.drawRect(endX * square, endY * square, square, square);
            }
        }

        g2d.dispose();
        return solutionImage;
    }

    public static boolean SaveMazeAsImage(BufferedImage image) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String timestamp = now.format(formatter);
            String filename = "maze_solution_" + timestamp + ".png";

            String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
            File directory = new File(downloadsPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File outputFile = new File(directory, filename);
            ImageIO.write(image, "png", outputFile);

            System.out.println("✓ המבוך הפתור נשמר בהצלחה: " + outputFile.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("✗ שגיאה בשמירת המבוך: " + e.getMessage());
            return false;
        }
    }



}