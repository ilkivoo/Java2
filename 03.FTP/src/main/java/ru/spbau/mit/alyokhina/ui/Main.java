package ru.spbau.mit.alyokhina.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FTP_GUI");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(gridPane, 600, 400));
        primaryStage.show();
        NewElementsCreator.createMainActivity(gridPane, primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}