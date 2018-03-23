package ru.spbau.mit.alyokhina.TicTacToe;


import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
    public static Button createButton(Pane pane, double height, double width, double x, double y, String text) {
        Button button = new Button();
        pane.getChildren().add(button);
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
    public static Button[] createTableView(Pane pane) {
        Button[] buttons = new Button[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = createButton(pane, 60, 60, 210 + 60 * (i % 3), 110 + 60 * (i / 3), "");
        }
        return buttons;
    }

    /**Create Menu for Main Activity */
    public static void createMainActivity(Pane pane) {
        Button buttonPlayWithComp = CreateElements.createButton(pane, 50, 200, 200, 70, "Играть с компьютером");
        Button buttonPlayWithFriend = CreateElements.createButton(pane, 50, 200, 200, 170, "Играть с другом");
        Button buttonExit = CreateElements.createButton(pane, 50, 200, 200, 270, "Выход");
        buttonPlayWithComp.setOnAction(actionEvent -> {
            GameWithComp gameWithComp = new GameWithComp(pane, new Statistics());
            gameWithComp.menu();
        });

        buttonPlayWithFriend.setOnAction(actionEvent -> {
            GameWithFriend gameWithFriend = new GameWithFriend(pane, new Statistics());
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
    public static void createButtonReplay(Pane pane, Statistics statistics, Consumer<Statistics> consumer) {
        Button rePlay = CreateElements.createButton(pane, 50, 200, 400, 20, "Начать новую партию");
        rePlay.setOnAction(actionEvent -> {
            pane.getChildren().clear();
            consumer.accept(statistics);
        });
    }


    /** Create button to go to the main activity */
    public static void createButtonToMainActivity(Pane pane) {
        Button buttonToMainActivity = CreateElements.createButton(pane, 50, 200, 400, 350, "В главное меню");
        buttonToMainActivity.setOnAction(actionEvent -> {
            pane.getChildren().clear();
            createMainActivity(pane);
        });
    }


    /**
     * Create button to go to statistics for hot seat. After clicking go to new activity
     *
     * @param go will be accepted after clicking button back
     */
    public static void createButtonGetStatisticsForOnePlayers(Pane pane, Statistics statistics, Consumer<Statistics> go) {
        Button getStatistics = CreateElements.createButton(pane, 50, 200, 200, 270, "Статистика");
        getStatistics.setOnAction(actionEvent -> {
            pane.getChildren().clear();
            CreateElements.createButtonToMainActivity(pane);
            Button buttonBack = CreateElements.createButton(pane, 50, 200, 400, 20, "Назад");
            buttonBack.setOnAction(actionEvent1 -> {
                pane.getChildren().clear();
                go.accept(statistics);
            });
            TextArea textArea = new TextArea();
            textArea.setLayoutY(0);
            textArea.setLayoutX(0);
            textArea.setPrefHeight(600);
            textArea.setPrefWidth(200);
            pane.getChildren().add(textArea);
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
    public static void createButtonGetStatisticsForTwoPlayers(Pane pane, Statistics statistics, Consumer<Statistics> go) {
        Button getStatistics = CreateElements.createButton(pane, 50, 200, 200, 240, "Статистика");
        getStatistics.setOnAction(actionEvent -> {
            pane.getChildren().clear();
            CreateElements.createButtonToMainActivity(pane);
            Button button = CreateElements.createButton(pane, 50, 200, 400, 20, "Назад");
            button.setOnAction(actionEvent1 -> {
                pane.getChildren().clear();
                go.accept(statistics);
            });
            TextArea textAreaForFirst = new TextArea();
            textAreaForFirst.setLayoutY(0);
            textAreaForFirst.setLayoutX(0);
            textAreaForFirst.setPrefHeight(600);
            textAreaForFirst.setPrefWidth(200);
            pane.getChildren().add(textAreaForFirst);
            textAreaForFirst.setText("Х \nколичество побед: " + statistics.getCountLose().toString()
                    + "\nколичество поражений: " + statistics.getCountWins().toString()
                    + "\nколичество ничьих: " + statistics.getCountDraw().toString());


            TextArea textAreaForSecond = new TextArea();
            textAreaForSecond.setLayoutY(0);
            textAreaForSecond.setLayoutX(200);
            textAreaForSecond.setPrefHeight(600);
            textAreaForSecond.setPrefWidth(200);
            pane.getChildren().add(textAreaForSecond);
            textAreaForSecond.setText("О \nколичество побед: " + statistics.getCountWins().toString()
                    + "\nколичество поражений: " + statistics.getCountLose().toString()
                    + "\nколичество ничьих: " + statistics.getCountDraw().toString());
        });
    }

    /** Print result on screen and update statistics */
    public static void setResult(Pane pane, Statistics statistics, String win) {
        pane.getChildren().clear();
        CreateElements.createButtonToMainActivity(pane);
        switch (win) {
            case "draw": {
                TextArea textArea = new TextArea();
                textArea.setLayoutY(150);
                textArea.setLayoutX(250);
                textArea.setPrefHeight(50);
                textArea.setPrefWidth(100);
                textArea.setText("draw");
                pane.getChildren().add(textArea);
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
                pane.getChildren().add(textArea);
                statistics.incLose();
                break;
            }
            default: {
                TextArea textArea = new TextArea();
                textArea.setLayoutY(150);
                textArea.setLayoutX(250);
                textArea.setPrefHeight(50);
                textArea.setPrefWidth(100);
                pane.getChildren().add(textArea);
                textArea.setText("O wins");
                statistics.incWins();
                break;
            }
        }

    }

}
