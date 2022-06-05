package com.company;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Tehc {
    char[][] board;
    final char[] player_score;
    char current_player = 'r';
    int number_of_games = 0;
    int user_input;
    JPanel content_pane;
    JButton[][] all_buttons;
    JLabel last_winner;
    JLabel turn;
    JButton restart_button;
    JButton[] score;
    Icon[][][] previous_games;
    int game_count = 0;
    Graphics2D[][] allGraphics;
    BufferedImage[][] allImages;
    Color color_button = new Color(200, 200, 200);
    public static String folder = new File(".").getAbsolutePath() + "\\src\\com\\company\\images\\";
    ImageIcon playAgain = new ImageIcon(folder + "ButtonPlayAgain.png");
    Color color_player1 = new Color(204, 43, 11);
    Color color_player2 = new Color(219, 196, 0);
    Timer animation;
    int animation_delay = 45;

    BufferedImage img_background = ImageIO.read(new File(folder + "ChipBackground.png"));
    BufferedImage img_empty = ImageIO.read(new File(folder + "ChipEmpty.png"));
    BufferedImage img_player1 = ImageIO.read(new File(folder + "ChipRed.png"));
    BufferedImage img_player2 = ImageIO.read(new File(folder + "ChipYellow.png"));
    BufferedImage img_vertical = ImageIO.read(new File(folder + "WinLineVertical.png"));
    BufferedImage img_horizontal = ImageIO.read(new File(folder + "WinLineHorizontal.png"));
    BufferedImage img_top_left_to_bottom_right = ImageIO.read(new File(folder + "WinLineTLBR.png"));
    BufferedImage img_bottom_left_to_top_right = ImageIO.read(new File(folder + "WinLineBLTR.png"));

    public Tehc() throws Exception {
        int x = 8;
        int y = 8;
        board = new char[x][y];
        getNumberOfGames();
        player_score = new char[number_of_games];
        initializeScore();
        initializeBoard();
        initializeGraphics();
        initializeImages();

        // Erstellst das Fenster mit seinen Eigenschaften
        content_pane = new JPanel();
        content_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        content_pane.setLayout(null);
        createBestOfButtons(number_of_games);

        // Zeigt den letzten Gewinner an
        last_winner = new JLabel("Letzter Gewinner:");
        last_winner.setFont(new Font("Roboto", Font.PLAIN, 15));
        last_winner.setBounds(25, 780, 500, 20);
        content_pane.add(last_winner);

        // Zeigt den aktuellen Spieler an
        turn = new JLabel("Reihe: Rot");
        turn.setFont(new Font("Roboto", Font.PLAIN, 15));
        turn.setBounds(575, 780, 500, 20);
        content_pane.add(turn);

        // Erstellt den Restart Button
        restart_button = new JButton();
        restart_button.setIcon(playAgain);
        restart_button.setBackground(Color.white);
        restart_button.setBorder(null);
        restart_button.setFont(new Font("Roboto", Font.PLAIN, 15));
        restart_button.setBounds(275, 777, 200, 30);
        restart_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        restart_button.addActionListener(arg0 -> {
            restart();
            restart_button.setVisible(false);
        });
        restart_button.setVisible(false);
        content_pane.add(restart_button);

        // Blockiert die best of Buttons, wenn ein Spiel läuft
        for (int i = 0; i < number_of_games; i++) {
            score[i].setEnabled(false);
        }

        // Erstellt die Buttons mit den jeweiligen Koordinaten und fügt sie zu allButtons hinzu
        all_buttons = new JButton[8][7];
        previous_games = new Icon[number_of_games][8][7];

        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                createButton(new JButton(), x_axis, y_axis);
            }
        }

        for (int x_axis = 0; x_axis < 7; x_axis++) {
            createClickableButton(new JButton(), x_axis, 0);
        }
    }
    
    public void initializeBoard() {
        /*
        Initialisiert das Spielbrett
        und setzt dafür die Array-Elemente = '-'
         */
        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                board[x_axis][y_axis] = '-';
            }
        }
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
        btn.setBounds(x * 100 + 20, y * 100 + 40, 100, 100);
        btn.setBorder(new EmptyBorder(0, 0, 0, 0));
        btn.setEnabled(false);
        content_pane.add(btn);
        all_buttons[x][y] = btn;
    }

    public void createClickableButton(JButton btn, int x_axis, int y_axis) {
        /*
        Kreiert einen Button oberhalb des Feldes,
        der durch einen Klick einen Stein in die jeweilige Spalte setzt
         */
        btn.setBackground(color_button);
        btn.setBounds(x_axis * 100 + 20, y_axis * 100 + 20, 100, 100);
        btn.addActionListener(arg0 -> fallingAnimation(x_axis));
        content_pane.add(btn);
        all_buttons[x_axis][y_axis] = btn;
    }

    public void getNumberOfGames() {
        /*
        Fragt den Benutzer nach der Anzahl der Spiele,
        die er spielen möchte
         */
        System.out.println("Wie viele Spiele willst du spielen? / Best of:");
        Scanner scanner = new Scanner(System.in);
        try {
            user_input = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Bitte eine Zahl eingeben!");
            getNumberOfGames();
        }

        if (user_input <= 0) {
            System.out.println("Bitte eine Zahl größer als 0 eingeben");
            getNumberOfGames();
        }
        if (user_input > 128) {
            System.out.println("Bitte eine Zahl kleiner oder gleich 128 eingeben");
            getNumberOfGames();
        } else {
            number_of_games = user_input;
            scanner.close();
        }
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

    public void colorWin() {
        /*
        Ändert die Farbe des Buttons, wenn ein Spieler gewonnen hat
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

    public boolean checkIfColumnFull(int x) {
        /*
        Überprüft, ob, eine Spalte voll ist
        gibt true zurück, wenn die Spalte voll ist
        sonst false
         */
        for (int y_axis = 1; y_axis < 7; y_axis++) {
            if (board[x][y_axis] == '-') {
                return false;
            }
        }
        return true;
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
            return "Rot";
        } else {
            return "Gelb";
        }
    }

    public void restart() {
        /*
        Startet ein neues Spiel
         */
        initializeBoard();

        for (int x_axis = 0; x_axis < 7; x_axis++) {
            for (int y_axis = 1; y_axis < 7; y_axis++) {
                final BufferedImage combinedImage = new BufferedImage(
                        img_background.getWidth(),
                        img_background.getHeight(),
                        BufferedImage.TYPE_INT_ARGB);
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
            for (int y_axis = 0; y_axis < 7; y_axis++) {
                if (board[x_axis][y_axis] == '-') {
                    return false;
                }
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
        } else if (p == 'u') {
            append(player_score, 'u');
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
            } else if (player_score[index] == 'u') {
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
            } else if (player_score[i] == 'u') {
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

    public void evaluate(int x_axis) {
        /*
        Überprüft, ob ein Spieler gewonnen hat,
        wenn ja, dann wird das Spiel beendet.
        Guckt, ob die Reihe voll ist,
        wenn ja, dann wird die Reihe gesperrt.
        Überprüft, ob das Spielfeld voll ist,
        wenn ja, dann wird das Spiel beendet.
        Wechselt den Spieler.
         */
        if (checkIfColumnFull(x_axis)) {
            all_buttons[x_axis][0].setEnabled(false);
        }

        if (checkWin()) {
            colorWin();
            last_winner.setText("Letzter Gewinner: " + longName(current_player));
            roundEnded(current_player);
        }

        if (boardFull()) {
            last_winner.setText("Letzter Gewinner: Unentschieden");
            roundEnded('u');
        }

        changePlayer();
        turn.setText("Reihe: " + longName(current_player));

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
        Animiert eine Kugel, wenn diese gesetzt wird
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
        btn.setBounds((700 / amount_of_best_of_buttons) * index_of_best_of_button + 20, 750, (700 / amount_of_best_of_buttons), 20);
        btn.setBackground(color_button);
        btn.addActionListener(arg0 -> renderGUI(index_of_best_of_button));
        btn.setEnabled(true);
        content_pane.add(btn);
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
