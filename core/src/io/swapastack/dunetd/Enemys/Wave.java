package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.utils.Timer;
import io.swapastack.dunetd.GameScreen;

import java.util.LinkedList;


public class Wave {
    private int enemyCounter, alive;
    private LinkedList<Enemy> enemys;
    Timer timer;
    private float delaySeconds = 5.f, intervalSeconds = 1.5f;
    private GameScreen gameScreen;

    public Wave(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
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
        if(!enemys.isEmpty()) {
            Enemy enem;
            if((enem = enemys.get(0)) != null) {
                gameScreen.initEnemy(enem);
                alive++;
                enemys.remove(0);

            } else {
                enemys.remove(0);
                System.out.println("No enemy spawned this tick!");
            }
        }

        System.out.println("Wave Timer triggered");
    }

    public int enemysLeft() {
        return -1;
    }

    public boolean waveKilled() {
        return false;
    }

    public void initEnemys(LinkedList<Enemy> enemys) {
        this.enemys = enemys;
    }

    public void enemyKilled() {
        alive--;
    }
}
