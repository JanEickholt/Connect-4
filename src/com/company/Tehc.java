package com.company;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Tehc {
    char[][] board;
    char[] player_score;

    char current_player = 'r';
    int number_of_games = 0;
    int game_count = 0;

    String red_player_name = "Rot";
    String yellow_player_name = "Gelb";

    JPanel content_pane;
    JButton[][] all_buttons;
    JButton[] score;
    JButton restart_button;
    Icon[][][] previous_games;
    Graphics2D[][] allGraphics;
    BufferedImage[][] allImages;

    JLabel last_winner;
    JLabel player_last_winner;
    JLabel turn;
    JLabel player_turn;

    public static String image_folder = new File(".").getAbsolutePath() + "\\src\\com\\company\\images\\";
    public static String sound_folder = new File(".").getAbsolutePath() + "\\src\\com\\company\\sounds\\";

    Color color_button = new Color(200, 200, 200);
    Color color_player1 = new Color(204, 43, 11);
    Color color_player2 = new Color(219, 196, 0);
    Color color_player2_turn = new Color(144, 134, 49);

    Timer animation;
    int animation_delay = 45;

    JLabel selection_title;
    JLabel selection_starting_turn;
    JButton selection_finished;
    JButton selection_starting_turn_red_button;
    JButton selection_starting_turn_yellow_button;
    JLabel selection_starting_turn_red_label;
    JLabel selection_starting_turn_yellow_label;
    JLabel selection_best_of_label;
    JTextField selection_best_of_text_field;
    JLabel selection_player_names;
    JLabel selection_player_names_red;
    JLabel selection_player_names_yellow;
    JTextField selection_player_names_red_text_field;
    JTextField selection_player_names_yellow_text_field;

    ImageIcon play_again = new ImageIcon(image_folder + "ButtonPlayAgain.png");
    ImageIcon chip_insert = new ImageIcon(image_folder + "ChipInsert.png");
    ImageIcon selected_circle = new ImageIcon(image_folder + "SelectedCircle.png");
    ImageIcon not_selected_circle = new ImageIcon(image_folder + "NotSelectedCircle.png");
    BufferedImage img_background = ImageIO.read(new File(image_folder + "ChipBackground.png"));
    BufferedImage img_empty = ImageIO.read(new File(image_folder + "ChipEmpty.png"));
    BufferedImage img_player1 = ImageIO.read(new File(image_folder + "ChipRed.png"));
    BufferedImage img_player2 = ImageIO.read(new File(image_folder + "ChipYellow.png"));
    BufferedImage img_vertical = ImageIO.read(new File(image_folder + "WinLineVertical.png"));
    BufferedImage img_horizontal = ImageIO.read(new File(image_folder + "WinLineHorizontal.png"));
    BufferedImage img_top_left_to_bottom_right = ImageIO.read(new File(image_folder + "WinLineTLBR.png"));
    BufferedImage img_bottom_left_to_top_right = ImageIO.read(new File(image_folder + "WinLineBLTR.png"));


    public Tehc() throws Exception {
        board = new char[8][8];
        initializeGraphics();
        initializeImages();

        // Erstellst das Fenster mit seinen Eigenschaften
        content_pane = new JPanel();
        content_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        content_pane.setLayout(null);


        // Zeigt den letzten Gewinner an
        last_winner = new JLabel("Letzter Gewinner:");
        last_winner.setFont(new Font("Roboto", Font.PLAIN, 15));
        last_winner.setBounds(25, 740, 500, 20);


        player_last_winner = new JLabel("");
        player_last_winner.setFont(new Font("Roboto", Font.PLAIN, 15));
        player_last_winner.setBounds(145, 740, 500, 20);


        // Zeigt den aktuellen Spieler an
        turn = new JLabel("Reihe:");
        turn.setFont(new Font("Roboto", Font.PLAIN, 15));
        turn.setBounds(575, 740, 500, 20);


        player_turn = new JLabel(longName(current_player));
        player_turn.setFont(new Font("Roboto", Font.PLAIN, 15));
        player_turn.setForeground(color_player1);
        player_turn.setBounds(620, 740, 500, 20);


        // Erstellt den Restart Button
        restart_button = new JButton();
        restart_button.setIcon(play_again);
        restart_button.setBackground(Color.white);
        restart_button.setBorder(null);
        restart_button.setFont(new Font("Roboto", Font.PLAIN, 15));
        restart_button.setBounds(275, 737, 200, 30);
        restart_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        restart_button.addActionListener(arg0 -> {
            try {
                restart();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            restart_button.setVisible(false);
        });
        restart_button.setVisible(false);

        // Erstellt die Buttons mit den jeweiligen Koordinaten und fügt sie zu allButtons hinzu
        all_buttons = new JButton[8][7];

        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                createButton(new JButton(), x_axis, y_axis);
            }
        }

        for (int x_axis = 0; x_axis < 7; x_axis++) {
            createClickableButton(new JButton(), x_axis, 0);
        }

        for (int x_axis = 0; x_axis < number_of_games; x_axis++){
            for (int y_axis = 0; y_axis < 8; y_axis++){
                for (int z_axis = 0; z_axis < 7; z_axis++){
                    final BufferedImage combinedImage = new BufferedImage(img_background.getWidth(), img_background.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = combinedImage.createGraphics();
                    g2d.drawImage(img_background, 0, 0, null);
                    g2d.drawImage(img_empty, 0, 0, null);
                    g2d.dispose();
                    previous_games[x_axis][y_axis][z_axis] = new ImageIcon(combinedImage);
                }
            }
        }

        alignLabel(selection_title = new JLabel("Einstellungen"), 30, 225, 25, 300, 50, true);
        alignLabel(selection_starting_turn = new JLabel("Beginner"), 20, 75, 100, 150, 50, true);
        alignLabel(selection_starting_turn_red_label = new JLabel("Rot"), 15, 100, 150, 50, 50, true);
        alignLabel(selection_starting_turn_yellow_label = new JLabel("Gelb"), 15, 150, 150, 50, 50, true);
        alignLabel(selection_best_of_label = new JLabel("Best of"), 20, 562, 100, 100, 50, false);
        alignLabel(selection_player_names = new JLabel("Name"), 20, 300, 290, 150, 50, true);
        alignLabel(selection_player_names_red = new JLabel("Rot"), 15, 245, 325, 50, 50, false);
        alignLabel(selection_player_names_yellow = new JLabel("Gelb"), 15, 245, 350, 50, 50, false);

        selection_player_names_red.setHorizontalAlignment(JLabel.RIGHT);
        selection_player_names_yellow.setHorizontalAlignment(JLabel.RIGHT);

        selection_player_names_red_text_field = new JTextField();
        selection_player_names_red_text_field.setBounds(303, 338, 150, 25);
        selection_player_names_red_text_field.setFont(new Font("Roboto", Font.PLAIN, 15));
        selection_player_names_red_text_field.setDocument(new JTextFieldLimit(12));
        content_pane.add(selection_player_names_red_text_field);


        selection_player_names_yellow_text_field = new JTextField();
        selection_player_names_yellow_text_field.setBounds(303, 363, 150, 25);
        selection_player_names_yellow_text_field.setFont(new Font("Roboto", Font.PLAIN, 15));
        selection_player_names_yellow_text_field.setDocument(new JTextFieldLimit(12));
        content_pane.add(selection_player_names_yellow_text_field);


        selection_best_of_text_field = new JTextField();
        selection_best_of_text_field.setFont(new Font("Roboto", Font.PLAIN, 20));
        selection_best_of_text_field.setBounds(570, 140, 50, 50);
        selection_best_of_text_field.setDocument(new JTextFieldLimit(3));
        selection_best_of_text_field.setText("3");
        selection_best_of_text_field.setHorizontalAlignment(JTextField.CENTER);
        content_pane.add(selection_best_of_text_field);

        selection_starting_turn_red_button = new JButton();
        selection_starting_turn_red_button.setIcon(selected_circle);
        selection_starting_turn_red_button.setBounds(113, 140, 25, 25);
        selection_starting_turn_red_button.setHorizontalAlignment(JButton.CENTER);
        selection_starting_turn_red_button.setBorder(null);
        selection_starting_turn_red_button.addActionListener(arg0 -> {
            current_player = 'r';
            selection_starting_turn_yellow_button.setIcon(not_selected_circle);
            selection_starting_turn_red_button.setIcon(selected_circle);
            player_turn.setText(longName(current_player));
            player_turn.setForeground(color_player1);
        });
        content_pane.add(selection_starting_turn_red_button);


        selection_starting_turn_yellow_button = new JButton();
        selection_starting_turn_yellow_button.setIcon(not_selected_circle);
        selection_starting_turn_yellow_button.setBounds(162, 140, 25, 25);
        selection_starting_turn_yellow_button.setBorder(null);
        selection_starting_turn_yellow_button.addActionListener(arg0 -> {
            current_player = 'y';
            selection_starting_turn_yellow_button.setIcon(selected_circle);
            selection_starting_turn_red_button.setIcon(not_selected_circle);
            player_turn.setText(longName(current_player));
            player_turn.setForeground(color_player2);
        });
        content_pane.add(selection_starting_turn_yellow_button);


        selection_finished = new JButton("Start");
        selection_finished.setFont(new Font("Roboto", Font.PLAIN, 20));
        selection_finished.setBounds(275, 737, 200, 30);
        selection_finished.setHorizontalAlignment(JButton.CENTER);
        selection_finished.addActionListener(arg0 -> {

            if (selection_validation()) {

                try {
                    initializeBoard();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                content_pane.removeAll();

                for (int x_axis = 0; x_axis < 7; x_axis++) {
                    for (int y_axis = 0; y_axis < 7; y_axis++) {
                        content_pane.add(all_buttons[x_axis][y_axis]);
                    }
                }

                previous_games = new Icon[number_of_games][8][7];
                createBestOfButtons(number_of_games);

                player_score = new char[number_of_games];
                initializeScore();

                for (int i = 0; i < number_of_games; i++) {
                    content_pane.add(score[i]);
                }

                // Blockiert die best of Buttons, wenn ein Spiel läuft
                for (int i = 0; i < number_of_games; i++) {
                    score[i].setEnabled(false);
                }

                content_pane.add(last_winner);
                content_pane.add(player_last_winner);
                content_pane.add(turn);
                content_pane.add(player_turn);
                content_pane.add(restart_button);

                content_pane.revalidate();
                content_pane.repaint();
            }

        });
        content_pane.add(selection_finished);
    }

    public boolean selection_validation() {
        try {
            Integer.parseInt(selection_best_of_text_field.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie eine gültige Zahl im Best of Feld ein!");
            return false;
        }

        if (Integer.parseInt(selection_best_of_text_field.getText()) < 1 || Integer.parseInt(selection_best_of_text_field.getText()) > 128) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie eine Zahl zwischen 1 und 128 im Best of Feld ein!");
            return false;
        }

        if (!selection_player_names_red_text_field.getText().equals("")){
            red_player_name = selection_player_names_red_text_field.getText();
        }

        if (!selection_player_names_yellow_text_field.getText().equals("")){
            yellow_player_name = selection_player_names_yellow_text_field.getText();
        }

        number_of_games = Integer.parseInt(selection_best_of_text_field.getText());

        player_turn.setText(longName(current_player));

        return true;
    }

    public void alignLabel(JLabel lbl, int font_size, int x_axis, int y_axis, int width, int height, boolean center) {
        lbl.setFont(new Font("Roboto", Font.PLAIN, font_size));
        lbl.setBounds(x_axis, y_axis, width, height);
        if (center) {lbl.setHorizontalAlignment(JLabel.CENTER);}
        content_pane.add(lbl);
    }

    public void initializeBoard() throws Exception{
        /*
        Initialisiert das Spielbrett
        und setzt dafür die Array-Elemente = '-'
         */
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                board[x_axis][y_axis] = '-';
            }
        }
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sound_folder + "start.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

    public void initializeScore() {
        /*
        Initialisiert die Punkte-Array-Elemente
        und setzt dafür die Array-Elemente = -
         */
        for (int index = 0; index < number_of_games; index++) {
            player_score[index] = '-';
        }
    }

    public void initializeGraphics() {
        /*
        Initialisiert die Graphics Array-Elemente
        und setzt dafür die Array-Elemente = null
         */
        allGraphics = new Graphics2D[8][7];
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                allGraphics[x_axis][y_axis] = null;
            }
        }
    }

    public void initializeImages() {
        /*
        Initialisiert die Images-Array-Elemente
        und setzt dafür die Array-Elemente = null
         */
        allImages = new BufferedImage[8][7];
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                allImages[x_axis][y_axis] = null;
            }
        }
    }

    public void createButton(JButton btn, int x, int y) {
        /*
        Erstellt einen Button mit einer x und y Koordinate
        und fügt ihn zu allButtons hinzu und zeigt diesen in dem
        Feld an
         */
        final BufferedImage combinedImage = new BufferedImage(
                img_background.getWidth(),
                img_background.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();
        g.drawImage(img_background, 0, 0, null);
        g.drawImage(img_empty, 0, 0, null);
        g.dispose();
        btn.setIcon(new ImageIcon(combinedImage));
        btn.setDisabledIcon(new ImageIcon(combinedImage));
        btn.setBounds(x * 100 + 20, y * 100, 100, 100);
        btn.setBorder(new EmptyBorder(0, 0, 0, 0));
        btn.setEnabled(false);
        all_buttons[x][y] = btn;
    }

    public void createClickableButton(JButton btn, int x_axis, int y_axis) {
        /*
        Kreiert einen Button oberhalb des Feldes,
        der durch einen Klick einen Chip in die jeweilige Spalte setzt
         */
        btn.setBackground(new Color(238, 238, 238));
        btn.setBorder(new EmptyBorder(0, 0, 0, 0));
        btn.setIcon(chip_insert);
        btn.setDisabledIcon(chip_insert);
        btn.setRolloverSelectedIcon(chip_insert);
        btn.setBounds(x_axis * 100 + 20, y_axis * 100, 100, 100);
        btn.addActionListener(arg0 -> fallingAnimation(x_axis));
        all_buttons[x_axis][y_axis] = btn;
    }

    public void changePlayer() {
        /*
        Ändert den Spieler
         */
        current_player = ((current_player == 'r') ? 'y' : 'r');
    }

    public boolean checkWin() {
        /*
        Überprüft, ob, ein Spieler gewonnen hat
        gibt true zurück, wenn ein Spieler gewonnen hat
        sonst false
        - durch jede Funktion gehen, damit alle Gewinnlinien markiert werden,
        sonst: return checkWinHorizontal() || checkWinVertical() || checkWinDiagonal();
         */
        boolean win = false;
        if (checkHorizontal()){
            win = true;
        }
        if (checkVertical()){
            win = true;
        }
        if (checkDiagonal()){
            win = true;
        }
        return win;
    }

    public boolean checkVertical() {
        /*
        Überprüft, ob ein Spieler Vertical gewonnen hat
        gibt true zurück, wenn ja, sonst false
         */
        boolean won = false;
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 8; y_axis++) {
                if (board[x_axis][y_axis] == current_player && board[x_axis][y_axis + 1] == current_player && board[x_axis][y_axis + 2] == current_player && board[x_axis][y_axis + 3] == current_player) {
                    colorVerticalWin(x_axis, y_axis);
                    colorVerticalWin(x_axis, y_axis + 1);
                    colorVerticalWin(x_axis, y_axis + 2);
                    colorVerticalWin(x_axis, y_axis + 3);
                    won = true;
                }
            }
        }
        return won;
    }

    public boolean checkHorizontal() {
        /*
        Überprüft, ob ein Spieler horizontal gewonnen hat
        gibt true zurück, wenn ja, sonst false
         */
        boolean won = false;
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 0; y_axis < 8; y_axis++) {
                if (board[x_axis][y_axis] == current_player && board[x_axis + 1][y_axis] == current_player && board[x_axis + 2][y_axis] == current_player && board[x_axis + 3][y_axis] == current_player) {
                    colorHorizontalWin(x_axis, y_axis);
                    colorHorizontalWin(x_axis + 1, y_axis);
                    colorHorizontalWin(x_axis + 2, y_axis);
                    colorHorizontalWin(x_axis + 3, y_axis);
                    won = true;
                }
            }
        }
        return won;
    }

    public boolean checkDiagonal() {
        /*
        Überprüft, ob ein Spieler diagonal gewonnen hat
        gibt true zurück, wenn ja, sonst false
         */
        boolean won = false;
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 0; y_axis < 8; y_axis++) {
                if (board[x_axis][y_axis] == current_player && board[x_axis + 1][y_axis + 1] == current_player && board[x_axis + 2][y_axis + 2] == current_player && board[x_axis + 3][y_axis + 3] == current_player) {
                    colorTLBRWin(x_axis, y_axis);
                    colorTLBRWin(x_axis + 1, y_axis + 1);
                    colorTLBRWin(x_axis + 2, y_axis + 2);
                    colorTLBRWin(x_axis + 3, y_axis + 3);
                    won = true;
                }
                if (board[x_axis][y_axis] == current_player && board[x_axis + 1][y_axis - 1] == current_player && board[x_axis + 2][y_axis - 2] == current_player && board[x_axis + 3][y_axis - 3] == current_player) {
                    colorBLTRWin(x_axis, y_axis);
                    colorBLTRWin(x_axis + 1, y_axis - 1);
                    colorBLTRWin(x_axis + 2, y_axis - 2);
                    colorBLTRWin(x_axis + 3, y_axis - 3);
                    won = true;
                }
            }
        }
        return won;

    }

    public void colorVerticalWin(int x_axis, int y_axis){
        colorWinLine(x_axis, y_axis, img_vertical);
    }

    public void colorHorizontalWin(int x_axis, int y_axis){
        colorWinLine(x_axis, y_axis, img_horizontal);
    }

    public void colorBLTRWin(int x_axis, int y_axis){
        colorWinLine(x_axis, y_axis, img_bottom_left_to_top_right);
    }

    public void colorTLBRWin(int x_axis, int y_axis){
        colorWinLine(x_axis, y_axis, img_top_left_to_bottom_right);
    }

    public void colorWinLine(int x_axis, int y_axis, BufferedImage img){
        /*
        Ändert erstellt eine Linie, wenn ein Spieler gewonnen hat
         */
        if (allGraphics[x_axis][y_axis] == null) {
            final BufferedImage combinedImage = new BufferedImage(img_background.getWidth(), img_background.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = combinedImage.createGraphics();
            g.drawImage(img_background, 0, 0, null);
            g.drawImage(current_player == 'r' ? img_player1: img_player2, 0, 0, null);
            allGraphics[x_axis][y_axis] = g;
            allImages[x_axis][y_axis] = combinedImage;
        }

        Graphics2D g = allGraphics[x_axis][y_axis];
        allGraphics[x_axis][y_axis] = g;
        g.drawImage(img, 0, 0, null);
        allGraphics[x_axis][y_axis] = g;
    }

    public void colorWin() {
        /*
        Zieht eine Linie an gewonnenen Positionen
         */
        for (int x = 0; x < 8; x++) {
            for (int y = 1; y < 7; y++) {
                if (allGraphics[x][y] != null) {
                    ImageIcon icon;
                    allGraphics[x][y].dispose();
                    icon = new ImageIcon(allImages[x][y]);
                    all_buttons[x][y].setIcon(icon);
                    all_buttons[x][y].setDisabledIcon(icon);
                }
            }
        }
    }

    public boolean checkIfColumnFull(int x) {
        /*
        Überprüft, ob, eine Spalte voll ist
        gibt true zurück, wenn sie voll ist
        sonst false
         */
        return board[x][1] != '-';
    }

    public void disableButtons() {
        /*
        Deaktiviert alle Buttons
         */
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 0; y_axis < 6; y_axis++) {
                all_buttons[x_axis][y_axis].setEnabled(false);
            }
        }
    }

    public String longName(char currentPlayer) {
        /*
        Gibt den langen Namen des Spielers zurück
         */
        if (currentPlayer == 'r') {
            return red_player_name;
        } else {
            return yellow_player_name;
        }
    }

    public void restart() throws Exception {
        /*
        Startet ein neues Spiel
         */
        initializeBoard();

        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                final BufferedImage combinedImage = new BufferedImage(img_background.getWidth(), img_background.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = combinedImage.createGraphics();
                g.drawImage(img_background, 0, 0, null);
                g.drawImage(img_empty, 0, 0, null);
                g.dispose();
                all_buttons[x_axis][y_axis].setIcon(new ImageIcon(combinedImage));
                all_buttons[x_axis][y_axis].setDisabledIcon(new ImageIcon(combinedImage));
            }
        }
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            all_buttons[x_axis][0].setEnabled(true);
        }
        for (int x_axis = 0; x_axis < 8; x_axis++) {
            for (int y_axis = 0; y_axis < 7; y_axis++) {
                allGraphics[x_axis][y_axis] = null;
                allImages[x_axis][y_axis] = null;
            }
        }
    }

    public boolean boardFull() {
        /*
        Überprüft, ob, das Spielfeld voll ist
        gibt true zurück, wenn das Spielfeld voll ist
        sonst false
         */
        for (int x_axis = 0; x_axis < 7; x_axis++) {
                if (board[x_axis][1] == '-') {
                    return false;
                }
        }
        return true;
    }

    public void updateScore(char p) {
        /*
        Aktualisiert die Punkte des Spielers
        und fügt dieser einem Array hinzu
         */
        if (p == 'r') {
            append(player_score, 'r');
        } else if (p == 'y') {
            append(player_score, 'y');
        } else if (p == 'd') {
            append(player_score, 'd');
        }
    }

    public void renderScore(int number_of_games) {
        /*
        Zeigt die Punkte des Spielers auf dem GUI an,
        nimmt diese werte aus dem Array
         */
        for (int index = 0; index < number_of_games; index++) {
            if (player_score[index] == 'r') {
                score[index].setBackground(color_player1);
            } else if (player_score[index] == 'y') {
                score[index].setBackground(color_player2);
            } else if (player_score[index] == 'd') {
                score[index].setBackground(Color.BLACK);
            } else {
                score[index].setBackground(color_button);
            }
        }
    }

    public void scoreFull() {
        /*
        Überprüft, ob der Score voll ist,
        wenn ja, dann wird das Spiel beendet
         */

        int redScore = 0;
        int yellowScore = 0;
        int uScore = 0;

        for (int i = 0; i < number_of_games; i++) {
            if (player_score[i] == 'r') {
                redScore++;
            } else if (player_score[i] == 'y') {
                yellowScore++;
            } else if (player_score[i] == 'd') {
                uScore++;
            }
        }
        if (redScore > (number_of_games / 2)) {
            enableScoreButtons();
            restart_button.setVisible(false);
            JOptionPane.showMessageDialog(null, "Partie beendet! \nErgebnis: Rot hat gewonnen!\n" + redScore + " : " + yellowScore);
        } else if (yellowScore > (number_of_games / 2)) {
            enableScoreButtons();
            restart_button.setVisible(false);
            JOptionPane.showMessageDialog(null, "Partie beendet! \nErgebnis: Gelb hat gewonnen!\n" + yellowScore + " : " + redScore);
        } else if (redScore + yellowScore + uScore == number_of_games) {
            enableScoreButtons();
            restart_button.setVisible(false);
            JOptionPane.showMessageDialog(null, "Partie beendet! \nErgebnis: Unentschieden!\n" + redScore + " : " + yellowScore);
        }
    }

    public void append(char[] my_array, char c) {
        /*
        Fügt einem Array einen Wert hinzu
         */
        for (int index = 0; index < my_array.length; index++) {
            if (my_array[index] == '-') {
                my_array[index] = c;
                return;
            }
        }
    }

    public void evaluate(int x_axis) throws Exception {
        /*
        Überprüft, ob ein Spieler gewonnen hat,
        wenn ja, dann wird das Spiel beendet.
        Guckt, ob die Reihe voll ist,
        wenn ja, dann wird die Reihe gesperrt.
        Überprüft, ob das Spielfeld voll ist,
        wenn ja, dann wird das Spiel beendet.
        Wechselt den Spieler.
         */
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sound_folder + "drop.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();

        if (checkIfColumnFull(x_axis)) {
            all_buttons[x_axis][0].setEnabled(false);
        }

        if (checkWin()) {
            AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(new File(sound_folder + "win.wav"));
            Clip clip2 = AudioSystem.getClip();
            clip2.open(audioInputStream2);
            clip2.start();
            colorWin();

            if (current_player == 'r') {
                player_last_winner.setForeground(color_player1);
            } else {
                player_last_winner.setForeground(color_player2_turn);
            }
            player_last_winner.setText(longName(current_player));
            roundEnded(current_player);
        }

        if (boardFull()) {
            player_last_winner.setText("Unentschieden");
            player_last_winner.setForeground(Color.BLACK);
            roundEnded('d');
        }

        changePlayer();
        if (current_player == 'r') {
            player_turn.setText(longName(current_player));
            player_turn.setForeground(color_player1);
        } else {
            player_turn.setText(longName(current_player));
            player_turn.setForeground(color_player2_turn);
        }
    }

    public void roundEnded(char player_who_won) {
        /*
        Wird ausgeführt, wenn das Spiel auf eine art
        beendet wurde, Beispiel: wenn das Spielfeld voll ist
         */
        restart_button.setVisible(true);
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 0; y_axis < 7; y_axis++) {
                previous_games[game_count][x_axis][y_axis] = all_buttons[x_axis][y_axis].getIcon();
            }
        }
        updateScore(player_who_won);
        disableButtons();
        renderScore(number_of_games);
        scoreFull();
        game_count++;
    }

    public void fallingAnimation(int position) {
        /*
        Animiert einen Chip, wenn diese gesetzt wird
         */
        ActionListener action = new ActionListener() {
            int y_axis = 1;
            final JButton[] was_enabled = getEnabledButtons();

            public void actionPerformed(ActionEvent e) {
                disableButtons();
                // Wenn der Button unterhalb noch frei ist, wird die Farbe geändert
                if (board[position][y_axis] == '-') {
                    final BufferedImage combinedImage = new BufferedImage(img_background.getWidth(), img_background.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = combinedImage.createGraphics();
                    g.drawImage(img_background, 0, 0, null);
                    g.drawImage(current_player == 'r' ? img_player1 :img_player2, 0, 0, null);
                    g.dispose();
                    all_buttons[position][y_axis].setIcon(new ImageIcon(combinedImage));
                    all_buttons[position][y_axis].setDisabledIcon(new ImageIcon(combinedImage));

                    if (y_axis >= 2) {
                        final BufferedImage combinedImage2 = new BufferedImage(img_background.getWidth(), img_background.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = combinedImage2.createGraphics();
                        g2.drawImage(img_background, 0, 0, null);
                        g2.drawImage(img_empty, 0, 0, null);
                        g2.dispose();
                        all_buttons[position][y_axis-1].setIcon(new ImageIcon(combinedImage2));
                        all_buttons[position][y_axis-1].setDisabledIcon(new ImageIcon(combinedImage2));
                    }
                    y_axis++;
                }
                if (board[position][y_axis] != '-') {
                    board[position][y_axis - 1] = current_player;

                    for (JButton btn : was_enabled) {
                        if (btn != null) {
                            btn.setEnabled(true);
                        }
                    }

                    try {
                        evaluate(position);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    animation.stop();
                }
            }
        };
        animation = new Timer(animation_delay, action);
        animation.setInitialDelay(0);
        animation.start();
    }

    public JButton[] getEnabledButtons() {
        /*
        Gibt alle Buttons zurück, die aktiviert sind
         */
        JButton[] enabledButtons = new JButton[7];
        int index = 0;
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            if (all_buttons[x_axis][0].isEnabled()) {
                enabledButtons[index] = all_buttons[x_axis][0];
                index++;
            }
        }
        return enabledButtons;
    }

    public void createBestOfButtons(int amount_of_buttons) {
        /*
        Erstellt die Anzahl, die den score darstellen
         */
        score = new JButton[amount_of_buttons];
        for (int created = 0; created < amount_of_buttons; created++) {
            createBestOfButton(amount_of_buttons, created);
        }
    }

    public void createBestOfButton(int amount_of_best_of_buttons, int index_of_best_of_button) {
        /*
        Erstellt einen Button für die Score-Anzeige
         */
        JButton btn = new JButton();
        btn.setBounds((700 / amount_of_best_of_buttons) * index_of_best_of_button + 20, 710, (700 / amount_of_best_of_buttons), 20);
        btn.setBackground(color_button);
        btn.addActionListener(arg0 -> renderGUI(index_of_best_of_button));
        btn.setEnabled(true);
        score[index_of_best_of_button] = btn;
    }

    public void renderGUI(int index_of_best_of_button) {
        /*
        Baut das GUI neu auf, mithilfe eines Arrays,
        dieses GUI kann nach einem Match angezeigt werden,
        indem auf den Score geklickt wird.
         */
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                final BufferedImage combinedImage = new BufferedImage(img_background.getWidth(), img_background.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = combinedImage.createGraphics();
                g.drawImage(img_background, 0, 0, null);
                all_buttons[x_axis][y_axis].setIcon(previous_games[index_of_best_of_button][x_axis][y_axis]);
                all_buttons[x_axis][y_axis].setDisabledIcon(previous_games[index_of_best_of_button][x_axis][y_axis]);
            }
        }
    }

    public void enableScoreButtons() {
        /*
        Aktiviert die Buttons, die den Score anzeigen
         */
        for (JButton btn : score) {
            btn.setEnabled(true);
        }
    }
}
