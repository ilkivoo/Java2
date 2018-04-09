package ru.spbau.mit.alyokhina.TicTacToe;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameWithCompTest {
    @Test
    public void goodMoveEmptyBoard() {
        String board[] = new String[]{
                "", "", "",
                "", "", "",
                "", "", ""
        };

        assertEquals(-1, GameWithComp.goodMove(board, "X", "O"));
    }

    @Test
    public void goodMoveifCanBeMoveOnDiagonal() {
        String board[] = new String[]{
                "X", "", "",
                "", "", "",
                "O", "", "X"
        };

        assertEquals(4, GameWithComp.goodMove(board, "X", "O"));
    }

    @Test
    public void goodMoveIfCanBeMoveOnVertical() {
        String board[] = new String[]{
                "X", "X", "",
                "", "", "",
                "O", "", "O"
        };

        assertEquals(2, GameWithComp.goodMove(board, "X", "O"));
    }

    @Test
    public void goodMoveIfCanBeMoveOnHorizontal() {
        String board[] = new String[]{
                "X", "", "",
                "", "X", "X",
                "O", "", "O"
        };

        assertEquals(3, GameWithComp.goodMove(board, "X", "O"));
    }
}