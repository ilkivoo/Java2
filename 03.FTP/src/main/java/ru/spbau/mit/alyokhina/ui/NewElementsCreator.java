package ru.spbau.mit.alyokhina.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.spbau.mit.alyokhina.Client;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;

public class NewElementsCreator {

    /**
     * Create button
     *
     * @param height height of the button
     * @param width  width of the button
     * @param x      abscissa coordinate
     * @param y      ordinate coordinate
     * @param text   this text will be on the button
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
     * Create label
     *
     * @param height height of the label
     * @param width  width of the label
     * @param x      abscissa coordinate
     * @param y      ordinate coordinate
     * @param text   this text will be on the label
     * @return new label
     */
    public static Label createLabel(GridPane gridPane, double height, double width, int x, int y, String text) {
        Label label = new Label();
        gridPane.add(label, x, y);
        label.setText(text);
        label.setPrefHeight(height);
        label.setPrefWidth(width);
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        return label;
    }

    /**
     * Create label
     *
     * @param height height of the label
     * @param width  width of the label
     * @param x      abscissa coordinate
     * @param y      ordinate coordinate
     * @param text   this text will be on the label
     * @param color color of the text
     */
    public static void createLabel(GridPane gridPane, double height, double width, int x, int y, String text, String color) {
        Label label = createLabel(gridPane, height, width, x, y, text);
        label.setTextFill(Color.web(color));
    }

    /**
     * Create TextField
     *
     * @param height height of the textField
     * @param width  width of the textField
     * @param x      abscissa coordinate
     * @param y      ordinate coordinate
     * @return new textField
     */
    public static TextField createTextField(GridPane gridPane, double height, double width, int x, int y) {
        TextField textField = new TextField();
        textField.setPrefHeight(height);
        textField.setPrefWidth(width);
        gridPane.add(textField, x, y);
        return textField;
    }


    /**
     * Add an action to click a button
     * @param button add action to this button
     * @param actionEvent this action will be after click
     */
    public static void addActionToButton(Button button, EventHandler<ActionEvent> actionEvent) {
        button.setOnAction(actionEvent);
    }


    /**
     *  Create ListView
     * @param gridPane on which objects will be placed
     * @param files list all files will be printed. First parameter is files name, second - true, if directory, else - false
     * @param dir set of name of files, which is directory
     * @return ListView
     */
    public static ListView<String> createListView(GridPane gridPane, List<Pair<String, Boolean>> files, HashSet<String> dir) {
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Pair<String, Boolean> file : files) {
            items.add(file.getKey());
        }
        listView.setItems(items);

        listView.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        if (dir.contains(name)) {
                            imageView.setImage(new Image(new File("src/main/resources/directory.png").toURI().toURL().toExternalForm()));
                        } else {
                            imageView.setImage(new Image(new File("src/main/resources/file.png").toURI().toURL().toExternalForm()));
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    setText(name);
                    imageView.setFitHeight(25);
                    imageView.setFitWidth(25);
                    setGraphic(imageView);
                }
            }
        });
        gridPane.add(listView, 0, 1);
        return listView;
    }


    /**
     * Create Main Activity. 3 buttons : create server, create client, exit
     * @param gridPane on which objects will be placed
     */
    public static void createMainActivity(GridPane gridPane, Stage stage) {
        gridPane.getChildren().clear();
        addActionToButton(
                createButton(gridPane, 50, 200, 0, 0, "Создать сервер"),
                actionEvent -> new ServerUI(gridPane, stage)
        );
        addActionToButton(
                createButton(gridPane, 50, 200, 0, 1, "Новый клиент"),
                actionEvent -> new ClientUI(gridPane, stage)
        );
        addActionToButton(
                createButton(gridPane, 50, 200, 0, 2, "Выход"),
                actionEvent -> Platform.exit()
        );
    }


    /**
     *  Create button for back
     * @param gridPane on which objects will be placed
     * @param height height of the button
     * @param width  width of the button
     * @param x      abscissa coordinate
     * @param y      ordinate coordinate
     * @param text   this text will be on the button
     * @param path   path for back from the current directory
     * @param client to communicate with the server
     * @param clientUI for print result
     */
    public static void createBackButton(GridPane gridPane, double height, double width, int x,
                                        int y, String text, String path, Client client, ClientUI clientUI, Stage stage) {

        addActionToButton(
                createButton(gridPane, height, width, x, y, text),
                actionEvent -> {
                    if (path.equals("/")) {
                        createMainActivity(gridPane, stage);
                    } else {
                        int end = 0;
                        for (int i = path.length() - 1; i >= 0; i--) {
                            if (path.charAt(i) == '/') {
                                end = i;
                                break;
                            }
                        }
                        if (end == 0) {
                            clientUI.printFiles(gridPane, client, "/");
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < end; i++) {
                                stringBuilder.append(path.charAt(i));
                            }
                            clientUI.printFiles(gridPane, client, stringBuilder.toString());
                        }
                    }
                });
    }
}
