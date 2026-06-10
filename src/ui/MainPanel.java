package ui;

import javax.swing.*;

public class MainPanel extends JFrame {
    private SettingsWindow settingsWindow;
    private MazeDrawing mazeDrawing;
    public MainPanel() {
        super("Maze");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        this.settingsWindow = new SettingsWindow();
        add(settingsWindow);
    }
    public void showMazeDrawing(String[] setting, int[][] maze, int width, int height) {
        getContentPane().removeAll();
        mazeDrawing = new MazeDrawing(setting, maze, width, height);
        add( mazeDrawing );
        getContentPane().revalidate();
        getContentPane().repaint();
    }
    public void setSettingsWindow() {
        getContentPane().removeAll();
        add(settingsWindow);
        getContentPane().revalidate();
        getContentPane().repaint();

    }
}
