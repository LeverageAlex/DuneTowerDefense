package io.swapastack.dunetd;

public class Player {
    private int health, highscore, spice;
    private String name;

    public int getHealth() {
        return health;
    }

    public int getHighscore() {
        return highscore;
    }
    public void increaseHighscore(int increase) {
        highscore += increase;
    }

    public int getSpice() {
        return spice;
    }

    public void addSpice(int inc) {
        spice += inc;
    }
    public boolean isGameWon() {
        return false;
    }
    public void endGame() {

    }
}
