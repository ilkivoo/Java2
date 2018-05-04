package ru.spbau.mit.alyokhina;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Random;


/**
 * Class for the Game Pair
 */
public class Game {
    /**
     * Board size
     */
    private Integer n = 0;
    /**
     * For placing UI elements
     */
    private GridPane gridPane;
    /**
     * Random for value on the table
     */
    final Random random = new Random();
    /**
     * Cells values
     */
    private int[] values;
    /**
     * Index opened cell on the table
     */
    private int openField = -1;
    /**
     * Button which corresponds open cell
     */
    private Button openButton;
    /**
     * Text on th open cell
     */
    private String openString;

    /**
     * Constructor
     */
    public Game(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    /**
     * Create board
     *
     * @param str size board
     * @return Cells values
     * @throws Exception if size is even
     */
    public int[] CreateBoard(String str) throws Exception {
        n = Integer.parseInt(str);
        if ((n % 2) != 0) {
            throw new Exception("Number is odd");
        }
        values = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            values[i] = -1;
        }
        return addValues();
    }

    /**
     * Create values on the board
     *
     * @return Cells values
     */
    private int[] addValues() {
        for (int i = 0; i < (n * n) / 2; i++) {
            int rand1 = random.nextInt(n * n);

            while (values[rand1] != -1) {
                rand1 = random.nextInt(n * n);
            }
            values[rand1] = i;

            int rand2 = random.nextInt(n * n);
            while (values[rand2] != -1) {
                rand2 = random.nextInt(n * n);
            }
            values[rand2] = i;
        }
        return values;
    }


    /** Check the presence of closed cells
     * @param buttons values on the table
     * @return true - if exist close cells, else - false
     */
    public boolean isEmptyButtons(String[] buttons) {
        for (String button : buttons) {
            if (button.equals("")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Move player
     * @param i index cell, which player wants open
     * @param button cell, which player wants open
     */
    private void move(int i, Button button) {
        if (openField == -1) {
            openField = i;
            openButton = button;
            openString = ((Integer) values[i]).toString();
            openButton.setDisable(true);
        } else {
            openButton.setText(openString);
            button.setText(((Integer) values[i]).toString());

            String str2 = ((Integer) values[i]).toString();

            if (openString.equals(str2)) {
                button.setDisable(true);
                openButton.setDisable(true);
                openButton = null;
                openField = -1;
                openString = "";
            } else {
                button.setDisable(true);
                PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                delay.setOnFinished(event1 -> {
                    button.setText("");
                    openButton.setText("");
                    button.setDisable(false);
                    openButton.setDisable(false);

                    openButton = null;
                    openField = -1;
                    openString = "";
                });
                delay.play();
            }
        }
    }

    /** Start Game */
    public void startGame() {
        gridPane.getChildren().clear();
        Button[] table = CreateElements.createField(gridPane, n);

        for (int i = 0; i < table.length; i++) {
            final int j = i;
            table[i].setOnAction(actionEvent -> {
                move(j, table[j]);

                String[] valuesOnTheButtons = new String[table.length];
                for (int k = 0; k < table.length; k++) {
                    valuesOnTheButtons[k] = table[k].getText();
                }
                if (!isEmptyButtons(valuesOnTheButtons)) {
                    CreateElements.createResultActivity(gridPane);
                }
            });
        }
    }
}
