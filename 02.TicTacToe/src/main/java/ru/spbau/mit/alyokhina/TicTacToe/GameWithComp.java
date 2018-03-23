package ru.spbau.mit.alyokhina.TicTacToe;


import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Random;
import java.util.function.Consumer;

public class GameWithComp extends Game {
    /**
     * Constructor
     *
     * @param statistics for collection information
     */
    public GameWithComp(Pane pane, Statistics statistics) {
        super(pane, statistics);
    }


    /**
     * Menu.
     * Choose level or get statistics
     */
    @Override
    public void menu() {
        CreateElements.createButtonGetStatisticsForOnePlayers(pane, statistics, value -> menu());
        Button buttonEasyLevel = CreateElements.createButton(pane, 50, 200, 200, 70, "Легкий");
        Button buttonHardLevel = CreateElements.createButton(pane, 50, 200, 200, 170, "Сложный");
        buttonEasyLevel.setOnAction(value -> {
            pane.getChildren().clear();
            CreateElements.createButtonToMainActivity(pane);
            playGame(this::moveCompEasyLevel);
        });
        buttonHardLevel.setOnAction(value -> {
            pane.getChildren().clear();
            CreateElements.createButtonToMainActivity(pane);
            playGame(this::moveCompHardLevel);
        });
    }

    /**
     * Start game
     *
     * @param moveComp move computer
     */
    private void playGame(Consumer<Button[]> moveComp) {
        CreateElements.createButtonReplay(pane, statistics, this::goBack);
        Button[] table = CreateElements.createTableView(pane);
        if (numberGames % 2 == 0) {
            moveComp.accept(table);
        }
        for (Button button : table) {
            button.setOnAction(actionEvent -> {
                button.setText(getCompSymb(1));
                button.setDisable(true);
                moveComp.accept(table);
            });
        }


    }

    /** Move will be get with help random */
    private void moveCompEasyLevel(Button[] table) {
        Random random = new Random();
        if (check(toStringArray(table), false).equals("")) {
            while (true) {
                int x = random.nextInt(table.length);
                if (table[x].getText().equals("")) {
                    table[x].setText(getCompSymb(0));
                    table[x].setDisable(true);
                    break;
                }
            }
            check(toStringArray(table), false);
        }
    }

    /**
     * Analyze board.
     * It's guaranteed if there is a possibility of winning in one step, then this step will be taken
     */
    private void moveCompHardLevel(Button[] table) {
        int move = goodMove(toStringArray(table), getCompSymb(0), getCompSymb(1));
        if (move == -1) {
            moveCompEasyLevel(table);
        } else {
            table[move].setText(getCompSymb(0));
            table[move].setDisable(true);
            check(toStringArray(table), false);
        }
    }

    /**
     * Check, if there is a possibility of winning in one step, then return index of cell
     * And interfere enemy win in the next move
     *
     * @return number cell
     */
    public static int goodMove(String[] tableString, String symbolPlayer1, String symbolPlayer2) {

        for (int i = 0; i < tableString.length; i++) {
            if (tableString[i].equals("")) {
                tableString[i] = symbolPlayer1;
                String whoWin = Game.check(tableString);
                if (whoWin.equals(symbolPlayer1)) {
                    return i;
                }
                tableString[i] = "";
            }
        }
        for (int i = 0; i < tableString.length; i++) {
            if (tableString[i].equals("")) {
                tableString[i] = symbolPlayer2;
                String whoWin = check(tableString);
                if (whoWin.equals(symbolPlayer2)) {
                    return i;
                }
                tableString[i] = "";
            }
        }
        return -1;
    }
}
