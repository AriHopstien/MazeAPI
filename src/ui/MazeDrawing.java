// שלב 9
package ui;

import MazeConstruction.OfflineMaze;
import MazeSolver.CheckSolution;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MazeDrawing extends JPanel {
    private JButton back;
    private JButton chack_solution;
    private JButton save_solution;
    private boolean state_chack_solution;
    private int[][] solution;
    private String[] setting;
    private BufferedImage image;
    private BufferedImage image_animation;
    private BufferedImage solution_image;
    private int[][] maze_image;
    private int width, height, square_size;
    public MazeDrawing(String[] setting, int[][] maze_image, int width, int height) {
        this.width = width;
        this.height = height;
        this.maze_image = maze_image;
        state_chack_solution = false;
        this.setting = setting;
        if (maze_image == null) {
            maze_image = OfflineMaze.generateMaze(width, height);
            this.maze_image = maze_image;
        }
        image = Grid.CrateImage(maze_image, width, height, setting[0], Boolean.parseBoolean(setting[2]), setting[3]);
        square_size = Grid.getSquare();

        solution = CheckSolution.solve(maze_image);
        this.setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(new Color(25, 25, 35));

        back = new JButton("חזרה");
        back.setFont(new Font("Segoe UI", Font.BOLD, 16));
        back.setBackground(new Color(231, 76, 60));
        back.setForeground(Color.WHITE);

        back.setFocusPainted(false);
        back.setBorderPainted(false);

        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.setBounds(1000, 400, 200, 100);
        chack_solution = new JButton("בדיקת פתרון");
        chack_solution.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chack_solution.setBackground(new Color(46, 204, 113));
        chack_solution.setForeground(Color.WHITE);

        chack_solution.setFocusPainted(false);
        chack_solution.setBorderPainted(false);

        chack_solution.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chack_solution.setBounds(1000, 120, 200, 100);

        save_solution = new JButton("שמור פתרון");
        save_solution.setFont(new Font("Segoe UI", Font.BOLD, 16));
        save_solution.setBackground(new Color(52, 152, 219));
        save_solution.setForeground(Color.WHITE);
        save_solution.setFocusPainted(false);
        save_solution.setBorderPainted(false);
        save_solution.setCursor(new Cursor(Cursor.HAND_CURSOR));
        save_solution.setBounds(1000, 260, 200, 100);
        save_solution.setEnabled(false);

        back.addActionListener(e -> {
            MainPanel mainPanel = (MainPanel) SwingUtilities.getWindowAncestor(back);
            mainPanel.setSettingsWindow();

        });
        chack_solution.addActionListener(e -> {
            if(solution == null){
                chack_solution.setText("אין פתרון");
                chack_solution.setBackground(new Color(220, 63, 63));
                this.revalidate();
                this.repaint();
                return;
            }
            Frame();
        });

        save_solution.addActionListener(e -> {
            if(solution_image != null) {
                boolean saved = Grid.SaveMazeAsImage(solution_image);
                if(saved) {
                    save_solution.setText("✓ נשמר!");
                    save_solution.setBackground(new Color(39, 174, 96));
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            save_solution.setText("שמור פתרון");
                            save_solution.setBackground(new Color(52, 152, 219));
                            SwingUtilities.invokeLater(() -> {
                                save_solution.revalidate();
                                save_solution.repaint();
                            });
                        } catch (InterruptedException ex) {}
                    }).start();
                }
            }
        });

        add(back);
        add(chack_solution);
        add(save_solution);

    }
    private void Frame() {
        new Thread(() -> {
            chack_solution.setEnabled(false);
            save_solution.setEnabled(false);
            state_chack_solution = true;
            image_animation = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image_animation.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            for (int i =0; i < solution.length; i++) {
                image_animation = Grid.Crateanimation(image_animation, solution[i], setting[1], Boolean.parseBoolean(setting[2]), setting[3]);
                SwingUtilities.invokeLater(() -> {
                    this.revalidate();
                    this.repaint();
                });
                try {
                    Thread.sleep(Integer.parseInt(setting[4]));
                }
                catch (InterruptedException e) {}

            }

            // יצירת תמונת הפתרון השלם לשמירה
            solution_image = Grid.CreateSolutionImage(image, solution, setting[1], Boolean.parseBoolean(setting[2]), setting[3]);
            image_animation = solution_image; // הצגת הפתרון השלם

            SwingUtilities.invokeLater(() -> {
                this.revalidate();
                this.repaint();
            });

            // המתן 1.5 שניות לפני האנימציה בחזרה
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {}

            // ============================================
            // אנימציה בחזרה - מהסוף (יעד) להתחלה
            // ============================================
            state_chack_solution = true;
            image_animation = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d_reverse = image_animation.createGraphics();
            g2d_reverse.drawImage(image, 0, 0, null);
            g2d_reverse.dispose();

            // עבור בסדר הפוך על הנתיב (מהסוף להתחלה)
            for (int i = solution.length - 1; i >= 0; i--) {
                image_animation = Grid.Crateanimation(image_animation, solution[i], setting[1], Boolean.parseBoolean(setting[2]), setting[3]);
                SwingUtilities.invokeLater(() -> {
                    this.revalidate();
                    this.repaint();
                });
                try {
                    Thread.sleep(Integer.parseInt(setting[4]));
                }
                catch (InterruptedException e) {}
            }

            // הצגת הפתרון השלם שוב (אחרי האנימציה בחזרה)
            image_animation = solution_image;
            state_chack_solution = false;

            chack_solution.setEnabled(true);
            save_solution.setEnabled(true);

            SwingUtilities.invokeLater(() -> {
                this.revalidate();
                this.repaint();
            });
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // רקע גרדיאנט
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(20, 20, 30),
                getWidth(), getHeight(),
                new Color(45, 45, 65)
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // כותרת
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString("Maze Viewer", 30, 45);

        // מסגרת למבוך
        g2d.setColor(new Color(70, 70, 90));
        g2d.fillRoundRect(20, 60, width*square_size+35, height*square_size+15, 20, 20);

        g2d.setColor(new Color(100, 170, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(20, 60, width*square_size+35, height*square_size+20, 20, 20);

        // ציור המבוך
        if (!state_chack_solution) {
            g2d.drawImage(image, 40, 70, this);
        } else {
            g2d.drawImage(image_animation, 40, 70, this);
        }
    }



}