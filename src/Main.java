import MazeSolver.MazeArrey;
import ui.MainPanel;

import javax.swing.*;

import static MazeSolver.CheckSolution.solve;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainPanel();
        });
    }
}