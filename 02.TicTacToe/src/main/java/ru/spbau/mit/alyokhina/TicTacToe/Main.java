package ru.spbau.mit.alyokhina.TicTacToe;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Крестики - Нолики");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        CreateElements.createMainActivity(gridPane);
        primaryStage.setScene(new Scene(gridPane, 600, 400));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
