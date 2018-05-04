package ru.spbau.mit.alyokhina;

import javafx.scene.layout.GridPane;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    @Test
    public void testCreateValueForTable() throws Exception {
        Game game = new Game( new GridPane());
        int[] value = game.CreateBoard("4");

        for (int i = 0; i < 8; i ++) {
            int count = 0;
            for (int j = 0; j < value.length; j++) {
                if (value[j] == i) {
                    count++;
                }
            }
            assertEquals(2, count);
        }

    }

    @Test
    public void testIsEmptyButtonsIfTrue() {
        Game game = new Game( new GridPane());
        String[] values = {"1", "2", "", ""};
        assertEquals(true, game.isEmptyButtons(values));
    }
    @Test
    public void testIsEmptyButtonsIfFalse() {
        Game game = new Game( new GridPane());
        String[] values = {"1", "2", "3", "4"};
        assertEquals(false, game.isEmptyButtons(values));
    }

}