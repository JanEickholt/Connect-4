package com.company;
import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class Tehc {
    final char[][] board;
    final char[] playerScore;
    char currentPlayer = 'r';
    int number_of_games = 0;
    int userInput;
    JPanel contentPane;
    JButton[][] allButtons;
    JLabel lastWinner;
    JLabel turn;
    JButton restartButton;
    JButton[] score;
    Color color_button = new Color(200, 200, 200);
    public static String folder = new File(".").getAbsolutePath()+"\\src\\com\\company\\images\\chip\\";
    ImageIcon empty = new ImageIcon(folder + "empty.png");
    ImageIcon playAgain = new ImageIcon(folder + "playAgain.png");
    Color color_player1 = new Color(204, 43, 11);
    Color color_player2 = new Color(219, 196, 0);
    ImageIcon player1 = new ImageIcon(folder + "player1.png");
    ImageIcon player2 = new ImageIcon(folder + "player2.png");
    ImageIcon win = new ImageIcon(folder + "win.png");
    Timer stopwatch;
    int delay = 45;
    public Tehc() {
        board = new char[8][8];
        getNumberOfGames();
        playerScore = new char[number_of_games];
        initializeScore();
        initializeBoard();
        // erstellst das Fenster mit seinen Eigenschaften
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        createBestOfButton(number_of_games);

        // zeigt den letzten Gewinner an
        lastWinner = new JLabel("Letzter Gewinner:");
        lastWinner.setFont(new Font("Roboto", Font.PLAIN, 15));
        lastWinner.setBounds(25, 780, 500, 20);
        contentPane.add(lastWinner);

        // zeigt den aktuellen Spieler an
        turn = new JLabel("Turn: Rot");
        turn.setFont(new Font("Roboto", Font.PLAIN, 15));
        turn.setBounds(575, 780, 500, 20);
        contentPane.add(turn);

        // erstellt den Restart Button
        restartButton = new JButton();
        restartButton.setIcon(playAgain);
        restartButton.setBackground(Color.white);
        restartButton.setBorder(null);
        restartButton.setFont(new Font("Roboto", Font.PLAIN, 15));
        restartButton.setBounds(275, 777, 200, 30);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(arg0 -> {
            restart();
            restartButton.setVisible(false);
        });
        restartButton.setVisible(false);
        contentPane.add(restartButton);


        // erstellt die Buttons mit den jeweiligen Koordinaten und fügt sie zu allButtons hinzu
        allButtons = new JButton[8][7];

        for (int i = 0; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                createButton(new JButton(), i, j);
            }
        }

        for (int i = 0; i < 7; i++) {
            createClickableButton(new JButton(), i, 0);
        }
    }
    public void initializeBoard() {
        /*
        initialisiert das Spielbrett
        und setzt dafür die Array-Elemente = '-'
         */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = '-';
            }
        }
    }
    public void initializeScore() {
        /*
        initialisiert die Punkte-Array-Elemente
        und setzt dafür die Array-Elemente = -
         */
        for (int i = 0; i < number_of_games; i++) {
            playerScore[i] = '-';
        }
    }
    public void createBestOfButton(int x){
        score = new JButton[x];
        for(int i = 0; i < x; i++){
            JButton btn = new JButton();
            btn.setBounds((700/x) * i + 20, 750 , (700/x), 20);
            btn.setBackground(color_button);
            btn.setEnabled(false);
            contentPane.add(btn);
            score[i] = btn;
        }
    }
    public void createButton(JButton btn, int x, int y){
        /*
        erstellt einen Button mit einer x und y Koordinate
        und fügt ihn zu allButtons hinzu und zeigt diesen in dem
        Feld an
         */
        btn.setIcon(empty);
        btn.setDisabledIcon(empty);
        btn.setBounds(x * 100 + 20, y * 100 + 40, 100, 100);
        btn.setBorder(new EmptyBorder(0, 0, 0, 0));
        btn.setEnabled(false);
        contentPane.add(btn);
        allButtons[x][y] = btn;
    }
    public void createClickableButton(JButton btn, int x, int y){
        /*
        kreiert einen Button oberhalb des Feldes,
        der durch einen Klick einen Stein in die jeweilige Spalte setzt
         */
        btn.setBackground(color_button);
        btn.setBounds(x * 100 + 20, y * 100 + 20, 100, 100);
        btn.addActionListener(arg0 -> fallingAnimation(x));
        contentPane.add(btn);
        allButtons[x][y] = btn;
    }
    public void getNumberOfGames() {
        System.out.println("How many games do you want to play?");
        Scanner scanner = new Scanner(System.in);
        try {
            userInput = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Please enter a number");
            getNumberOfGames();
        }

        if(userInput <= 0) {
            System.out.println("Please enter a number greater than 0");
            getNumberOfGames();
        }
        if(userInput >= 128) {
            System.out.println("Please enter a number less or equal to 128");
            getNumberOfGames();
        }
        else {
            number_of_games = userInput;
            scanner.close();
        }
    }
    public void changePlayer() {
        /*
        Ändert den Spieler
         */
        if (currentPlayer == 'r') {
            currentPlayer = 'y';
        } else {
            currentPlayer = 'r';
        }
    }
    public boolean checkWin() {
        /*
        Überprüft, ob, ein Spieler gewonnen hat
        gibt true zurück, wenn ein Spieler gewonnen hat
        sonst false
         */
        return checkHorizontal() || checkVertical() || checkDiagonal();
    }
    public void colorWin(int x, int y) {
        /*
        Ändert die Farbe des Buttons, wenn ein Spieler gewonnen hat
         */
        allButtons[x][y].setIcon(win);
        allButtons[x][y].setDisabledIcon(win);
    }
    public boolean checkVertical() {
        /*
        Überprüft, ob ein Spieler Vertical gewonnen hat
        gibt true zurück, wenn ja, sonst false
         */
        boolean won = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == currentPlayer && board[i][j + 1] == currentPlayer && board[i][j + 2] == currentPlayer && board[i][j + 3] == currentPlayer) {
                    colorWin(i, j);
                    colorWin(i, j + 1);
                    colorWin(i, j + 2);
                    colorWin(i, j + 3);
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[j][i] == currentPlayer && board[j + 1][i] == currentPlayer && board[j + 2][i] == currentPlayer && board[j + 3][i] == currentPlayer) {
                    colorWin(j, i);
                    colorWin(j + 1, i);
                    colorWin(j + 2, i);
                    colorWin(j + 3, i);
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[j][i] == currentPlayer && board[j + 1][i + 1] == currentPlayer && board[j + 2][i + 2] == currentPlayer && board[j + 3][i + 3] == currentPlayer) {
                    colorWin(j, i);
                    colorWin(j + 1, i + 1);
                    colorWin(j + 2, i + 2);
                    colorWin(j + 3, i + 3);
                    won = true;
                }
                if (board[j][i] == currentPlayer && board[j + 1][i - 1] == currentPlayer && board[j + 2][i - 2] == currentPlayer && board[j + 3][i - 3] == currentPlayer) {
                    colorWin(j, i);
                    colorWin(j + 1, i - 1);
                    colorWin(j + 2, i - 2);
                    colorWin(j + 3, i - 3);
                    won = true;
                }
            }
        }
        return won;

    }
    public boolean checkIfColumnFull(int x) {
        /*
        Überprüft, ob, eine Spalte voll ist
        gibt true zurück, wenn die Spalte voll ist
        sonst false
         */
        for (int i = 1; i < 7; i++) {
            if (board[x][i] == '-') {
                return false;
            }
        }
        return true;
    }
    public void disableButtons() {
        /*
        Deaktiviert alle Buttons
         */
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                allButtons[i][j].setEnabled(false);
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
        for (int i = 0; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                allButtons[i][j].setIcon(empty);
                allButtons[i][j].setDisabledIcon(empty);
            }
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                board[i][j] = '-';
            }
        }
        for (int i = 0; i < 7; i++) {
            allButtons[i][0].setEnabled(true);
        }
    }
    public boolean boardFull() {
        /*
        Überprüft, ob, das Spielfeld voll ist
        gibt true zurück, wenn das Spielfeld voll ist
        sonst false
         */
        for (int i = 0; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                if (board[i][j] == '-') {
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
            append(playerScore, 'r');
        } else if (p == 'y') {
            append(playerScore, 'y');
        }
        else if (p == 'u') {
            append(playerScore, 'u');
        }
    }
    public void renderScore(int number_of_games) {
        /*
        Zeigt die Punkte des Spielers auf dem GUI an,
        nimmt diese werte aus dem Array
         */
        for (int i = 0; i < number_of_games; i++) {
            if (playerScore[i] == 'r') {
                score[i].setBackground(color_player1);
            } else if (playerScore[i] == 'y') {
                score[i].setBackground(color_player2);
            } else if (playerScore[i] == 'u') {
                score[i].setBackground(Color.BLACK);
            } else {
                score[i].setBackground(color_button);
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
            if (playerScore[i] == 'r') {
                redScore++;
            } else if (playerScore[i] == 'y') {
                yellowScore++;
            }
            else if (playerScore[i] == 'u') {
                uScore++;
            }
        }
        if (redScore > (number_of_games / 2)) {
            restartButton.setVisible(false);
            JOptionPane.showMessageDialog(null, "Partie beendet! \nErgebnis: Rot hat gewonnen!\n" + redScore + " : " + yellowScore);
        } else if (yellowScore > (number_of_games / 2)) {
            restartButton.setVisible(false);
            JOptionPane.showMessageDialog(null, "Partie beendet! \nErgebnis: Gelb hat gewonnen!\n" + yellowScore + " : " + redScore);
        }
        else if(redScore + yellowScore + uScore == number_of_games){
            restartButton.setVisible(false);
            JOptionPane.showMessageDialog(null, "Partie beendet! \nErgebnis: Unentschieden!\n" + redScore + " : " + yellowScore);
        }
    }
    public void append(char [] my_array, char x){
        /*
        Fügt einem Array einen Wert hinzu
         */
        for(int i = 0; i < my_array.length; i++){
            if(my_array[i] == '-'){
                my_array[i] = x;
                return;
            }
        }
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
                    allButtons[position][y_axis].setIcon(currentPlayer == 'r' ? player1 : player2);
                    allButtons[position][y_axis].setDisabledIcon(currentPlayer == 'r' ? player1 : player2);

                    if (y_axis >= 2) {
                        allButtons[position][y_axis - 1].setIcon(empty);
                        allButtons[position][y_axis - 1].setDisabledIcon(empty);
                    };
                    y_axis++;
                }
                if (board[position][y_axis] != '-') {
                    board[position][y_axis - 1] = currentPlayer;

                    for (JButton btn : was_enabled) {
                        if (btn != null) {
                            btn.setEnabled(true);
                        }
                    }

                    if (checkWin()) {
                        lastWinner.setText("Letzter Gewinner: " + longName(currentPlayer));
                        disableButtons();
                        restartButton.setVisible(true);
                        updateScore(currentPlayer);
                        renderScore(number_of_games);
                        scoreFull();
                    }

                    if (checkIfColumnFull(position)) {
                        allButtons[position][0].setEnabled(false);
                    }

                    changePlayer();
                    turn.setText("Turn: " + longName(currentPlayer));

                    if (boardFull()) {
                        lastWinner.setText("Letzter Gewinner: Unentschieden");
                        restartButton.setVisible(true);
                        updateScore('u');
                        renderScore(number_of_games);
                        scoreFull();

                    }

                    stopwatch.stop();
                }
            }
        };
        stopwatch = new Timer(delay, action);
        stopwatch.setInitialDelay(0);
        stopwatch.start();
    }
    public JButton[] getEnabledButtons(){
        /*
        Gibt alle Buttons zurück, die aktiviert sind
         */
        JButton[] enabledButtons = new JButton[7];
        int i = 0;
        for (int j = 0; j < 7; j++) {
            if (allButtons[j][0].isEnabled()) {
                enabledButtons[i] = allButtons[j][0];
                i++;
            }
        }
        return enabledButtons;
    }
}
