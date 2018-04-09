package ru.spbau.mit.alyokhina.TicTacToe;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


public class GameWithFriend extends Game {

    /**
     * Constructor
     *
     * @param statistics for collection information
     */
    public GameWithFriend(GridPane gridPane, Statistics statistics) {
        super(gridPane, statistics);
    }

    /** Menu. Two categories : play and get statistics */
    @Override
    public void menu() {
        Button buttonPlay = CreateElements.createButton(gridPane, 50, 200, 200, 110, "Играть");
        buttonPlay.setOnAction(actionEvent -> {
            gridPane.getChildren().clear();
            CreateElements.createButtonReplay(this.gridPane, statistics, this::goBack);
            playGame();
        });
        CreateElements.createButtonGetStatisticsForTwoPlayers(gridPane, statistics, value -> menu());
    }

    /** Start play */
    private void playGame() {
        Button[] table = CreateElements.createTableView(gridPane);
        changeMove(table, 0);
    }

    /** Change players move */
    private void changeMove(Button[] table, int x) {
        String c = getCompSymb(x);
        for (Button button : table) {
            button.setOnAction(actionEvent -> {
                button.setText(c);
                button.setDisable(true);
                check(toStringArray(table), false);
                changeMove(table, 1 - x);
            });
        }

    }


}
