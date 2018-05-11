package ru.spbau.mit.alyokhina.ui;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import ru.spbau.mit.alyokhina.client.Client;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;


/**
 * UI for Client
 */
public class ClientUI {
    /**
     * Constructor
     *
     * @param gridPane on which objects will be placed
     */
    public ClientUI(GridPane gridPane) {
        gridPane.getChildren().clear();
        CreateNewElements.createLabel(gridPane, 50, 300, 0, 0, "Введите порт");
        TextField textFieldForPort = CreateNewElements.createTextField(gridPane, 50, 300, 0, 1);
        CreateNewElements.createLabel(gridPane, 50, 300, 0, 2, "Введите хост");
        TextField textFieldForHost = CreateNewElements.createTextField(gridPane, 50, 300, 0, 3);
        CreateNewElements.createLabel(gridPane, 50, 300, 0, 4, "Введите путь");
        TextField textFieldForPath = CreateNewElements.createTextField(gridPane, 50, 300, 0, 5);

        CreateNewElements.addActionToButton(
                CreateNewElements.createButton(gridPane, 50, 100, 1, 6, "OK"),
                actionEvent -> startClient(gridPane, textFieldForPort.getText(), textFieldForHost.getText(), textFieldForPath.getText()));

        CreateNewElements.addActionToButton(
                CreateNewElements.createButton(gridPane, 50, 100, 1, 0, "Назад"),
                actionEvent -> CreateNewElements.createMainActivity(gridPane));
    }

    /**
     * Print files from the server on this path
     *
     * @param gridPane on which objects will be placed
     * @param client   to communicate with the server
     * @param path     on which we want to get the files
     */
    public void printFiles(GridPane gridPane, Client client, String path) {

        try {
            if (!client.isExists(path)) {
                CreateNewElements.createLabel(gridPane, 50, 100, 0, 6, "Такого пути не существует");
            } else {
                gridPane.getChildren().clear();
                CreateNewElements.createLabel(gridPane, 50, 100, 0, 0, path);
                List<Pair<String, Boolean>> files = client.list(path);
                HashSet<String> dir = new HashSet<>();
                for (Pair<String, Boolean> file : files) {
                    if (file.getValue()) {
                        dir.add(file.getKey());
                    }
                }
                ListView<String> listView = CreateNewElements.createListView(gridPane, files, dir);
                listView.setOnMouseClicked(event -> {
                    if (dir.contains(listView.getSelectionModel().getSelectedItem())) {
                        printFiles(gridPane, client, path + '/' + listView.getSelectionModel().getSelectedItem());
                    } else {
                        agreement(gridPane, client, path + '/' + listView.getSelectionModel().getSelectedItem());
                    }
                });

            }
        } catch (IOException e) {
            CreateNewElements.createLabel(gridPane, 50, 100, 0, 6, e.getMessage(), "#ff0000");
        }
        CreateNewElements.createBackButton(gridPane, 100, 100, 1, 0, "Назад", path, client, this);

        CreateNewElements.addActionToButton(
                CreateNewElements.createButton(gridPane, 100, 150, 2, 0, "В главное меню"),
                actionEvent -> CreateNewElements.createMainActivity(gridPane));
    }

    /**
     * Get an agreement to download a file
     *
     * @param gridPane on which objects will be placed
     * @param client   to communicate with the server
     * @param path     path of the file, that we want to download
     */
    private void agreement(GridPane gridPane, Client client, String path) {
        gridPane.getChildren().clear();
        CreateNewElements.createLabel(gridPane, 50, 200, 1, 0, "Вы уверены, что хотите скачать?");
        CreateNewElements.addActionToButton(
                CreateNewElements.createButton(gridPane, 50, 50, 0, 1, "Да"),
                actionEvent -> download(gridPane, client, path)
        );

        CreateNewElements.createBackButton(gridPane, 50, 50, 2, 1, "Нет", path, client, this);
    }

    /**
     * Download file from server
     *
     * @param gridPane on which objects will be placed
     * @param client   to communicate with the server
     * @param path     path of the file, that would be download
     */
    private void download(GridPane gridPane, Client client, String path) {
        gridPane.getChildren().clear();
        CreateNewElements.createLabel(gridPane, 50, 300, 0, 0, "Введите путь для сохранения");
        TextField textFieldForDowloadPath = CreateNewElements.createTextField(gridPane, 50, 300, 0, 1);
        CreateNewElements.createBackButton(gridPane, 50, 100, 1, 0, "Назад", path, client, this);
        CreateNewElements.addActionToButton(
                CreateNewElements.createButton(gridPane, 50, 100, 1, 2, "OK"),
                actionEvent -> {
                    try {
                        client.get(path, textFieldForDowloadPath.getText());
                        afterDownload(gridPane, client, path);
                    } catch (IOException e) {
                        if (gridPane.getChildren().size() > 4) {
                            for (int i = 4; i < gridPane.getChildren().size(); i++) {
                                gridPane.getChildren().remove(i);
                            }
                        }
                        CreateNewElements.createLabel(gridPane, 50, 300, 0, 2, e.getMessage(), "#ff0000");
                    }
                });


    }


    /**
     * Selecting the transition after downloading the file
     *
     * @param gridPane on which objects will be placed
     * @param client   to communicate with the server
     * @param path     of the file, that was downloaded
     */
    private void afterDownload(GridPane gridPane, Client client, String path) {
        gridPane.getChildren().clear();
        CreateNewElements.createBackButton(gridPane, 100, 200, 0, 0, "Продолжить", path, client, this);
        CreateNewElements.addActionToButton(
                CreateNewElements.createButton(gridPane, 100, 200, 0, 1, "В главное меню"),
                actionEvent -> CreateNewElements.createMainActivity(gridPane)
        );
        CreateNewElements.addActionToButton(
                CreateNewElements.createButton(gridPane, 100, 200, 0, 2, "Выход"),
                actionEvent -> Platform.exit()
        );

    }

    /**
     * Start client work
     *
     * @param gridPane on which objects will be placed
     * @param namePort for create client
     * @param host     gor create client
     * @param path     of the current directory
     */
    private void startClient(GridPane gridPane, String namePort, String host, String path) {
        if (gridPane.getChildren().size() > 8) {
            for (int i = 8; i < gridPane.getChildren().size(); i++) {
                gridPane.getChildren().remove(i);
            }
        }
        Integer port;
        try {
            port = Integer.parseInt(namePort);
        } catch (NumberFormatException e) {
            CreateNewElements.createLabel(gridPane, 50, 300, 0, 6, "Неккоректный порт. \nПопробуйте еще раз", "#ff0000");
            return;
        }
        try {
            Client client = new Client(host, port);
            printFiles(gridPane, client, path);
        } catch (IOException e) {
            CreateNewElements.createLabel(gridPane, 50, 300, 0, 6, e.getMessage(), "#ff0000");
        }
    }
}
