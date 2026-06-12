// שלב 8
package ui;

import java.awt.*;
import java.awt.image.BufferedImage;


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
}
