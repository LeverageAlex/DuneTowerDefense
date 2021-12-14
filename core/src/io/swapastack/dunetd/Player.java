package io.swapastack.dunetd;

public class Player {
    private float health, highscore, spice;
    private String name;

    public float getHealth() {
        return health;
    }

    public void reduceHealth(float toReduce) {
        health -= toReduce;
    }

    public float getHighscore() {
        return highscore;
    }
    public void increaseHighscore(int increase) {
        highscore += increase;
    }

    public float getSpice() {
        return spice;
    }

    public void addSpice(float inc) {
        spice += inc;
    }
    public boolean isGameWon() {
        return false;
    }
    public void endGame() {

    }
}
