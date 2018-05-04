package ru.spbau.mit.alyokhina;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Game Pair. Find equals numbers on the board. Board size must be even.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Пары");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        final TextField textField = CreateElements.CreateTextField(gridPane);
        Button ok = CreateElements.createButton(gridPane, 50, 50, 4, 6, "OK");
        Game game = new Game(gridPane);
        ok.setOnAction(actionEvent -> {
            try {
                game.CreateBoard(textField.getText());
                game.startGame();
            } catch (Exception e) {
                Platform.exit();
            }


        });
        primaryStage.setScene(new Scene(gridPane, 600, 400));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
