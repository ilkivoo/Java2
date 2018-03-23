package ru.spbau.mit.alyokhina.TicTacToe;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


public class GameWithFriend extends Game {

    /**
     * Constructor
     *
     * @param statistics for collection information
     */
    public GameWithFriend(Pane pane, Statistics statistics) {
        super(pane, statistics);
    }

    /** Menu. Two categories : play and get statistics */
    @Override
    public void menu() {
        Button buttonPlay = CreateElements.createButton(pane, 50, 200, 200, 110, "Играть");
        buttonPlay.setOnAction(actionEvent -> {
            pane.getChildren().clear();
            CreateElements.createButtonReplay(this.pane, statistics, this::goBack);
            playGame();
        });
        CreateElements.createButtonGetStatisticsForTwoPlayers(pane, statistics, value -> menu());
    }

    /** Start play */
    private void playGame() {
        Button[] table = CreateElements.createTableView(pane);
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
