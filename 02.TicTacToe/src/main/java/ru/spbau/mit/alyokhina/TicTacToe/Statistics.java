package ru.spbau.mit.alyokhina.TicTacToe;

/** Class for collection information about games */
public class Statistics {
    private int countWins = 0;
    private int countLose = 0;
    private int countDraw = 0;

    public void incWins() {
        countWins++;
    }

    public void incLose() {
        countLose++;
    }
    public void incDraw() {
        countDraw++;
    }
    public Integer getCountWins() {
        return countWins;
    }

    public Integer getCountLose() {
        return countLose;
    }

    public Integer getCountDraw() {
        return countDraw;
    }
}
