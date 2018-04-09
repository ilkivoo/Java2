package ru.spbau.mit.alyokhina.TicTacToe;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

/** Create UI elements */
public class CreateElements {

    /**
     * Create button
     *
     * @param x    abscissa coordinate
     * @param y    ordinate coordinate
     * @param text this text will be on the button
     * @return new button
     */
    public static Button createButton(GridPane gridPane, double height, double width, int x, int y, String text) {
        Button button = new Button();
        gridPane.add(button, x, y);
        button.setText(text);
        button.setPrefHeight(height);
        button.setPrefWidth(width);
        button.setLayoutX(x);
        button.setLayoutY(y);
        return button;
    }


    /**
     * Create board for TicTacToe. Every cell is button
     *
     * @return array of buttons
     */
    public static Button[] createTableView(GridPane gridPane) {
        Button[] buttons = new Button[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = createButton(gridPane, 60, 60, 210 + 60 * (i % 3), 110 + 60 * (i / 3), "");
        }
        return buttons;
    }

    /**Create Menu for Main Activity */
    public static void createMainActivity(GridPane gridPane) {
        Button buttonPlayWithComp = CreateElements.createButton(gridPane, 50, 200, 0, 0, "Играть с компьютером");
        Button buttonPlayWithFriend = CreateElements.createButton(gridPane, 50, 200, 0, 1, "Играть с другом");
        Button buttonExit = CreateElements.createButton(gridPane, 50, 200, 0, 2, "Выход");
        buttonPlayWithComp.setOnAction(actionEvent -> {
            GameWithComp gameWithComp = new GameWithComp(gridPane, new Statistics());
            gameWithComp.menu();
        });

        buttonPlayWithFriend.setOnAction(actionEvent -> {
            GameWithFriend gameWithFriend = new GameWithFriend(gridPane, new Statistics());
            gameWithFriend.menu();
        });
        buttonExit.setOnAction(value -> Platform.exit());
    }

    /**
     * Create button for new game
     *
     * @param statistics is parameter for consumer
     * @param consumer   will be accepted after clicking on the button
     */
    public static void createButtonReplay(GridPane gridPane, Statistics statistics, Consumer<Statistics> consumer) {
        Button rePlay = CreateElements.createButton(gridPane, 50, 200, 400, 20, "Начать новую партию");
        rePlay.setOnAction(actionEvent -> {
            gridPane.getChildren().clear();
            consumer.accept(statistics);
        });
    }


    /** Create button to go to the main activity */
    public static void createButtonToMainActivity(GridPane gridPane) {
        Button buttonToMainActivity = CreateElements.createButton(gridPane, 50, 200, 400, 350, "В главное меню");
        buttonToMainActivity.setOnAction(actionEvent -> {
            gridPane.getChildren().clear();
            createMainActivity(gridPane);
        });
    }


    /**
     * Create button to go to statistics for hot seat. After clicking go to new activity
     *
     * @param go will be accepted after clicking button back
     */
    public static void createButtonGetStatisticsForOnePlayers(GridPane gridPane, Statistics statistics, Consumer<Statistics> go) {
        Button getStatistics = CreateElements.createButton(gridPane, 50, 200, 200, 270, "Статистика");
        getStatistics.setOnAction(actionEvent -> {
            gridPane.getChildren().clear();
            CreateElements.createButtonToMainActivity(gridPane);
            Button buttonBack = CreateElements.createButton(gridPane, 50, 200, 400, 20, "Назад");
            buttonBack.setOnAction(actionEvent1 -> {
                gridPane.getChildren().clear();
                go.accept(statistics);
            });
            TextArea textArea = new TextArea();
            textArea.setLayoutY(0);
            textArea.setLayoutX(0);
            textArea.setPrefHeight(600);
            textArea.setPrefWidth(200);
            gridPane.getChildren().add(textArea);
            textArea.setText("количество побед: " + statistics.getCountWins().toString()
                    + "\nколичество поражений: " + statistics.getCountLose().toString()
                    + "\nколичество ничьих: " + statistics.getCountDraw().toString());

        });
    }


    /**
     * Create button to go to statistics for two players. After clicking go to new activity
     *
     * @param go will be accepted after clicking button back
     */
    public static void createButtonGetStatisticsForTwoPlayers(GridPane gridPane, Statistics statistics, Consumer<Statistics> go) {
        Button getStatistics = CreateElements.createButton(gridPane, 50, 200, 200, 240, "Статистика");
        getStatistics.setOnAction(actionEvent -> {
            gridPane.getChildren().clear();
            CreateElements.createButtonToMainActivity(gridPane);
            Button button = CreateElements.createButton(gridPane, 50, 200, 400, 20, "Назад");
            button.setOnAction(actionEvent1 -> {
                gridPane.getChildren().clear();
                go.accept(statistics);
            });
            TextArea textAreaForFirst = new TextArea();
            //textAreaForFirst.setLayoutY(0);
            //textAreaForFirst.setLayoutX(0);
            textAreaForFirst.setPrefHeight(600);
            textAreaForFirst.setPrefWidth(200);
            gridPane.add(textAreaForFirst, 0, 0);
            textAreaForFirst.setText("Х \nколичество побед: " + statistics.getCountLose().toString()
                    + "\nколичество поражений: " + statistics.getCountWins().toString()
                    + "\nколичество ничьих: " + statistics.getCountDraw().toString());


            TextArea textAreaForSecond = new TextArea();
            textAreaForSecond.setLayoutY(0);
            textAreaForSecond.setLayoutX(200);
            textAreaForSecond.setPrefHeight(600);
            textAreaForSecond.setPrefWidth(200);
            gridPane.add(textAreaForSecond, 5, 0);
            textAreaForSecond.setText("О \nколичество побед: " + statistics.getCountWins().toString()
                    + "\nколичество поражений: " + statistics.getCountLose().toString()
                    + "\nколичество ничьих: " + statistics.getCountDraw().toString());
        });
    }

    /** Print result on screen and update statistics */
    public static void setResult(GridPane gridPane, Statistics statistics, String win) {
        gridPane.getChildren().clear();
        CreateElements.createButtonToMainActivity(gridPane);
        switch (win) {
            case "draw": {
                TextArea textArea = new TextArea();
                textArea.setLayoutY(150);
                textArea.setLayoutX(250);
                textArea.setPrefHeight(50);
                textArea.setPrefWidth(100);
                textArea.setText("draw");
                gridPane.getChildren().add(textArea);
                statistics.incDraw();
                break;
            }
            case "X": {
                TextArea textArea = new TextArea();
                textArea.setLayoutY(150);
                textArea.setLayoutX(250);
                textArea.setPrefHeight(50);
                textArea.setPrefWidth(100);
                textArea.setText("X wins");
                gridPane.getChildren().add(textArea);
                statistics.incLose();
                break;
            }
            default: {
                TextArea textArea = new TextArea();
                textArea.setLayoutY(150);
                textArea.setLayoutX(250);
                textArea.setPrefHeight(50);
                textArea.setPrefWidth(100);
                gridPane.getChildren().add(textArea);
                textArea.setText("O wins");
                statistics.incWins();
                break;
            }
        }

    }

}
