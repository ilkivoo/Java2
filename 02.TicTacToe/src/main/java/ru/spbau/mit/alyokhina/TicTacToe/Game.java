package ru.spbau.mit.alyokhina.TicTacToe;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/** Class for key actions of game */
public class Game {
    /**
     * Pane for current game
     */
    protected Pane pane;
    /** Number playing */
    protected int numberGames = 0;
    /** Statistics for current game */
    protected Statistics statistics;

    /** Constructor */
    public Game(Pane pane, Statistics statistics) {
        this.pane = pane;
        this.statistics = statistics;
        pane.getChildren().clear();
        CreateElements.createButtonToMainActivity(pane);
    }

    /** Translated text from buttons array in string array */
    protected String[] toStringArray(Button[] table) {
        String[] tableString = new String[table.length];
        for (int i = 0; i < table.length; i++) {
            tableString[i] = table[i].getText();
        }
        return tableString;
    }

    /**
     * Check the state of the game
     *
     * @param tableString state board, "" - if cell is empty
     * @return "X" - if X wins , "O" - if O wins, "" - if state is unknown, else - draw
     */
    public static String check(String[] tableString) {

        boolean existEmpty = false;
        for (int i = 0; i < tableString.length; i++) {
            if (tableString[i].equals("")) {
                existEmpty = true;
            }
        }

        for (int i = 0; i < 9; i += 3) {
            if (!tableString[i].equals("") && tableString[i].equals(tableString[i + 1]) && tableString[i + 1].equals(tableString[i + 2])) {
                return tableString[i];
            }
        }

        for (int i = 0; i < 3; i++) {
            if (!tableString[i].equals("") && tableString[i].equals(tableString[i + 3]) && tableString[i + 3].equals(tableString[i + 6])) {
                return tableString[i];
            }
        }

        if (!tableString[0].equals("") && tableString[0].equals(tableString[4]) && tableString[4].equals(tableString[8])) {
            return tableString[0];
        }

        if (!tableString[2].equals("") && tableString[2].equals(tableString[4]) && tableString[4].equals(tableString[6])) {

            return tableString[2];
        }
        if (existEmpty) {
            return "";
        } else {
            return "draw";
        }
    }

    /**
     * /**
     * Check the state of the game
     *
     * @param tableString         state board, "" - if cell is empty
     * @param isPreliminaryCheck, true - if real state, false - if we want check move
     * @return "X" - if X wins , "O" - if O wins, "" - if state is unknown, else - draw
     */
    protected String check(String[] tableString, boolean isPreliminaryCheck) {
        String result = check(tableString);
        if (!isPreliminaryCheck && !result.equals("")) {
            setResult(result);
        }
        return result;
    }

    /** Print result games */
    private void setResult(String win) {
        numberGames++;
        CreateElements.setResult(pane, statistics, win);
        CreateElements.createButtonReplay(pane, statistics, this::goBack);
    }

    /** For return in this activity */
    protected void goBack(Statistics statistics) {
        this.statistics = statistics;
        pane.getChildren().clear();
        CreateElements.createButtonToMainActivity(pane);
        menu();
    }

    /** Symbol to which the player move */

    public String getCompSymb(int player) {
        return (numberGames % 2 == player) ? "X" : "O";
    }


    /** Before games we should choose characteristics game */
    public void menu() {
        CreateElements.createButton(pane, 50, 200, 200, 70, "Легкий");
        CreateElements.createButton(pane, 50, 200, 200, 170, "Сложный");
    }

}
