package ru.spbau.mit.alyokhina;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.spbau.mit.alyokhina.ui.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Крестики - Нолики");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        CreateNewElements.createMainActivity(gridPane);
        primaryStage.setScene(new Scene(gridPane, 600, 400));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}