// שלב 9
package ui;

import MazeSolver.CheckSolution;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MazeDrawing extends JPanel {
    private JButton back;
    private JButton chack_solution;
    private boolean state_chack_solution;
    private int[][] solution;
    private String[] setting;
    private BufferedImage image;
    private BufferedImage image_animation;
    public MazeDrawing(String[] setting, int[][] maze_image, int width, int height) {
        state_chack_solution = false;
        this.setting = setting;
        image = Grid.CrateImage(maze_image, width, height, setting[0], Boolean.parseBoolean(setting[2]), setting[3]);
        System.out.println(image.getWidth()+" "+image.getHeight());
        solution = CheckSolution.solve(maze_image);
        this.setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        back = new JButton("Back");
        back.setBounds(1000, 100, 100, 50);
        chack_solution = new JButton("Chack Solution");
        chack_solution.setBounds(1000, 500, 100, 50);

        back.addActionListener(e -> {
            MainPanel mainPanel = (MainPanel) SwingUtilities.getWindowAncestor(back);
            mainPanel.setSettingsWindow();

        });
        chack_solution.addActionListener(e -> {

        });
        add(back);
        add(chack_solution);


    }
    private void Frame() {
        new Thread(() -> {
            chack_solution.setEnabled(false);
            state_chack_solution = false;
           for (int[] sqoure : solution) {
               image_animation = Grid.CrateSolution(image, sqoure, setting[4], Boolean.parseBoolean(setting[2]), setting[3]);
               SwingUtilities.invokeLater(() -> {
                   this.revalidate();
                   this.repaint();
               });
               try {
                   Thread.sleep(Integer.parseInt(setting[4]));
               }
               catch (InterruptedException e) {}
           }
           chack_solution.setEnabled(true);
           state_chack_solution = true;
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 15, this);
        if (state_chack_solution) {
            g2d.drawImage(image_animation, 0, 0, this);
        }
    }



}
