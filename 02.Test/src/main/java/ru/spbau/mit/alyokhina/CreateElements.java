package ru.spbau.mit.alyokhina;


import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Create UI elements
 */
public class CreateElements {

    /**
     * Create TextField for write size of table
     *
     * @param gridPane for placing textField
     * @return textField which was created
     */
    public static TextField CreateTextField(GridPane gridPane) {
        TextField textField = new TextField();
        textField.setPrefHeight(200);
        textField.setPrefWidth(200);
        gridPane.add(textField, 0, 0);
        return textField;
    }

    /**
     * Create button
     *
     * @param gridPane for placing button
     * @param x        abscissa coordinate
     * @param y        ordinate coordinate
     * @param text     this text will be on the button
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
     * Create board for Pair. Every cell is button
     *
     * @param gridPane for placing buttons
     * @param n        size of board
     * @return array of buttons
     */
    public static Button[] createField(GridPane gridPane, Integer n) {
        Button[] buttons = new Button[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i * n + j] = createButton(gridPane, 60, 60, 210 + 60 * j, 110 + 60 * i, "");
            }
        }
        return buttons;
    }

    /**
     * Create Result Activity with Text "WIN"
     * @param gridPane for placing textField
     */
    public static void createResultActivity(GridPane gridPane) {
        gridPane.getChildren().clear();
        TextField textField = new TextField();
        textField.setPrefHeight(200);
        textField.setPrefWidth(200);
        gridPane.add(textField, 0, 0);
        textField.setText("You win =)");
        Button ok = CreateElements.createButton(gridPane, 100, 100, 2, 6, "Exit");
        ok.setOnAction(actionEvent -> {
            Platform.exit();
        });
    }

}
