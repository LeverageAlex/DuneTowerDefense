package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.utils.Timer;

public class Wave {
    private int enemyCounter, alive;
    private int[] enemys;
    Timer timer;
    private int delaySeconds = 5, intervalSeconds = 1;

    public Wave() {

    }
    //Activate waveSpawner
    public void startWave() {
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                continueWave();
            }
        }, delaySeconds, intervalSeconds);
    }

    public void continueWave() {
        System.out.println("Wave Timer triggered");
    }

    public int enemysLeft() {
        return -1;
    }

    public boolean waveKilled() {
        return false;
    }

    public void initEnemys(int[] enemys) {
        this.enemys = enemys;
    }
}
