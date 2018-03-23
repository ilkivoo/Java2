package ru.spbau.mit.alyokhina.TicTacToe;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Крестики - Нолики");
        Pane root = new Pane();
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        CreateElements.createMainActivity(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
