package ru.spbau.mit.alyokhina.TicTacToe;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    @Test
    public void checkIfNoResult() {
        String board[] = new String[]{
                "", "", "",
                "", "", "",
                "", "", ""
        };

        assertEquals("", Game.check(board));
    }

    @Test
    public void checkIfXWinInDiagonal() {
        String board[] = new String[]{
                "X", "O", "O",
                "", "X", "",
                "", "", "X"
        };

        assertEquals("X", Game.check(board));
    }

    @Test
    public void checkIfXWinInVertical() {
        String board[] = new String[]{
                "X", "O", "",
                "X", "", "O",
                "X", "", ""
        };

        assertEquals("X", Game.check(board));
    }

    @Test
    public void checkIfXWinInHorizontal() {
        String board[] = new String[]{
                "", "O", "",
                "X", "X", "X",
                "", "O", ""
        };

        assertEquals("X", Game.check(board));
    }

    @Test
    public void checkIfOWinInDiagonal() {
        String board[] = new String[]{
                "O", "X", "X",
                "", "O", "",
                "", "", "O"
        };

        assertEquals("O", Game.check(board));
    }

    @Test
    public void checkIfOWinInVertical() {
        String board[] = new String[]{
                "O", "X", "",
                "O", "", "X",
                "O", "", ""
        };

        assertEquals("O", Game.check(board));
    }

    @Test
    public void checkIfOWinInHorizontal() {
        String board[] = new String[]{
                "", "X", "",
                "O", "O", "O",
                "", "X", ""
        };

        assertEquals("O", Game.check(board));
    }


    @Test
    public void checkIfDraw() {
        String board[] = new String[]{
                "X", "O", "X",
                "O", "X", "X",
                "O", "X", "O"
        };

        assertEquals("draw", Game.check(board));
    }

    @Test
    public void checkIfExistMove() {
        String board[] = new String[]{
                "X", "O", "",
                "O", "X", "X",
                "O", "X", "O"
        };

        assertEquals("", Game.check(board));
    }


}