package ui;

import MazeConstruction.GetInstructions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JPanel {


    private JTextArea title;
    private JTextArea setting_wallCellColor;
    private JTextArea setting_pathColor;
    private JTextArea setting_drawGrid;
    private JTextArea setting_gridColor;
    private JTextArea setting_animationDelayMs;

    private JButton refresh_setting;
    private JButton get_maze;
    private JTextField width_maze;
    private JTextField height_maze;

    private String[] setting;
    private int width, height;

    public SettingsWindow() {


        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        this.setBackground(new Color(30, 30, 40));

        setting = GetInstructions.getApiSettings();

        title = new JTextArea("⚙ SETTINGS", 1, 20);
        setting_wallCellColor = new JTextArea(1, 30);
        setting_pathColor = new JTextArea(1, 30);
        setting_drawGrid = new JTextArea(1, 30);
        setting_gridColor = new JTextArea(1, 30);
        setting_animationDelayMs = new JTextArea(1, 30);

        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(255, 215, 0)); // זהב

        JTextArea[] allFields = {
                title, setting_wallCellColor, setting_pathColor,
                setting_drawGrid, setting_gridColor, setting_animationDelayMs
        };

        for (JTextArea area : allFields) {
            area.setEditable(false);
            area.setFocusable(false);
            area.setOpaque(false);
            area.setBorder(null);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);

            if (area != title) {
                area.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                area.setForeground(Color.WHITE);
            }
        }
        setRefresh_setting();

        // 2. אתחול כפתורים ושדות קלט לעריכה
        refresh_setting = new JButton("רענן הגדרות");
        get_maze = new JButton("צור מבוך");
        styleButton(refresh_setting);
        styleButton(get_maze);

        width_maze = new JTextField(3);
        height_maze = new JTextField(3);

        JTextField[] fields = {width_maze, height_maze};

        for (JTextField field : fields) {
            field.setFont(new Font("Segoe UI", Font.BOLD, 18));
            field.setPreferredSize(new Dimension(80, 35));

            field.setBackground(new Color(50, 50, 65));
            field.setForeground(Color.WHITE);

            field.setCaretColor(Color.WHITE);

            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }

        refresh_setting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setting = GetInstructions.getApiSettings();
                setRefresh_setting();
            }
        });

        get_maze.addActionListener(e -> {
            MainPanel mainPanel = (MainPanel) SwingUtilities.getWindowAncestor(get_maze);
            try {
                width = Integer.valueOf(width_maze.getText());
                height = Integer.valueOf(height_maze.getText());
                if (!(4<width && width<101 && height<101 && height>4)){
                    width = 30;
                    height = 30;
                }
            }
            catch (NumberFormatException ex) {
                width = 30;
                height = 30;
            }
            mainPanel.showMazeDrawing(setting, GetInstructions.convertImageToBinaryGrid(width, height), width, height);
        });



        // 4. מיקום הרכיבים ב-GridBagLayout

        // --- חוקים כלליים לעמודה הראשונה (הטקסטים בצד שמאל) ---
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weighty = 1.0;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 100, 10, 20);

        // הוספת הטקסטים לעמודה 0
        gbc.gridx = 0;
        gbc.gridy = 0; this.add(title, gbc);
        gbc.gridy = 1; this.add(setting_wallCellColor, gbc);
        gbc.gridy = 2; this.add(setting_pathColor, gbc);
        gbc.gridy = 3; this.add(setting_drawGrid, gbc);
        gbc.gridy = 4; this.add(setting_gridColor, gbc);
        gbc.gridy = 5; this.add(setting_animationDelayMs, gbc);

        // --- חוקים כלליים לעמודה השנייה (הרכיבים האינטראקטיביים) ---
        gbc.gridx = 1; // עוברים לעמודה הימנית יותר
        gbc.anchor = GridBagConstraints.WEST; // גם אותם נצמיד לשמאל העמודה שלהם כדי שיהיו קרובים לטקסט
        gbc.fill = GridBagConstraints.NONE; // כפתורים ושדות קלט לא צריכים להימתח לגובה
        gbc.weightx = 1.0; // נותן לעמודה השנייה "לדחוף" ימינה ולתפוס את שאר רוחב המסך

        // נמקם כל רכיב קלט בשורה המתאימה לו מול הטקסט שלו:
        gbc.gridy = 1; this.add(width_maze, gbc);              // מול wallCellColor (לדוגמה)
        gbc.gridy = 2; this.add(height_maze, gbc);             // מול pathColor
        gbc.gridy = 3; this.add(refresh_setting, gbc);          // מול drawGrid
        gbc.gridy = 4; this.add(get_maze, gbc);                 // מול gridColor
    }


    private void updateSettingsTexts(String wallColor, String pathColor, String drawGrid, String gridColor, String animDelay) {
        if(wallColor != null) setting_wallCellColor.setText(wallColor);
        if(pathColor != null) setting_pathColor.setText(pathColor);
        if(drawGrid != null) setting_drawGrid.setText(drawGrid);
        if(gridColor != null) setting_gridColor.setText(gridColor);
        if(animDelay != null) setting_animationDelayMs.setText(animDelay);
        this.revalidate();
        this.repaint();
    }
    private void setRefresh_setting(){
        String[] setting_arry = setting;
        updateSettingsTexts("Wall color:   "+setting_arry[0],"Path color:   "+setting_arry[1],"Is grid:   "+setting_arry[2],"Grid color:   "+setting_arry[3],"Delay:   "+setting_arry[4]);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));

        button.setBackground(new Color(70, 130, 255));
        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setPreferredSize(new Dimension(180, 40));
    }

}