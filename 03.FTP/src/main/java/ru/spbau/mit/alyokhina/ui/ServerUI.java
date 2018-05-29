package ru.spbau.mit.alyokhina.ui;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.spbau.mit.alyokhina.Server;

import java.io.IOException;

public class ServerUI {
    private Stage stage;

    public ServerUI(GridPane gridPane, Stage stage) {
        this.stage = stage;
        gridPane.getChildren().clear();
        NewElementsCreator.createLabel(gridPane, 50, 400, 0, 0, "Введите порт");
        TextField port = NewElementsCreator.createTextField(gridPane, 100, 200, 0, 2);
        NewElementsCreator.addActionToButton(
                NewElementsCreator.createButton(gridPane, 50, 100, 1, 3, "OK"),
                actionEvent -> {
                    createServer(gridPane, port.getText());
                }
        );
        NewElementsCreator.addActionToButton(
                NewElementsCreator.createButton(gridPane, 50, 100, 1, 0, "Назад"),
                actionEvent -> {
                    NewElementsCreator.createMainActivity(gridPane, stage);
                }
        );
    }

    private void result(GridPane gridPane) {
        gridPane.getChildren().clear();
        NewElementsCreator.createLabel(gridPane, 100, 200, 0, 0, "Сервер создан");
        NewElementsCreator.addActionToButton(
                NewElementsCreator.createButton(gridPane, 50, 200, 0, 1, "Назад"),
                actionEvent -> {
                    NewElementsCreator.createMainActivity(gridPane, stage);
                }
        );
    }


    private void createServer(GridPane gridPane, String namePort) {
        if (gridPane.getChildren().size() > 4) {
            for (int i = 4; i < gridPane.getChildren().size();i++) {
                gridPane.getChildren().remove(i);
            }
        }
        Integer port;
        try{
            port = Integer.parseInt(namePort);
        }
        catch (NumberFormatException e) {
            NewElementsCreator.createLabel(gridPane, 50, 400, 0, 3, "Неккоректный порт. \nПопробуйте еще раз", "#ff0000");
            return;
        }
        try {
            Server server = new Server(port);
            Thread thread = new Thread(server::start);
            thread.start();
            result(gridPane);
        }
        catch (IOException | IllegalArgumentException e) {
            NewElementsCreator.createLabel(gridPane, 50, 400, 0, 3, e.getMessage(), "#ff0000");
        }

    }
}
