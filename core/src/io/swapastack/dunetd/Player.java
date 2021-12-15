package io.swapastack.dunetd;

public class Player {
    private int health, highscore, spice = 150;
    private String name;

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void reduceHealth(int toReduce) {
        health -= toReduce;
    }

    public int getHighscore() {
        return highscore;
    }
    public void increaseHighscore(int increase) {
        highscore += increase;
    }

    public float getSpice() {
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
