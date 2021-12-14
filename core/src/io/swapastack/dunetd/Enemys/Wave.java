package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.utils.Timer;
import io.swapastack.dunetd.GameScreen;
import io.swapastack.dunetd.Player;

import java.util.LinkedList;


public class Wave {
    private int enemyCounter, alive, killed, arrivedPortal;
    private LinkedList<Enemy> enemys;
    Timer timer;
    private float delaySeconds = 5.f, intervalSeconds = 1.5f;
    private GameScreen gameScreen;
    private Player player;
    private boolean started;

    public Wave(GameScreen gameScreen, Player player) {
        this.gameScreen = gameScreen;
        this.player = player;
        started = false;
        //this.enemyCounter = 6;
    }
    //Activate waveSpawner
    public void startWave() {
        started = true;
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
             //   System.out.println("No enemy spawned this tick!");
            }
        }

      //  System.out.println("Wave Timer triggered");
    }

    public int enemiesLeft() {
        return enemyCounter -killed - arrivedPortal;
    }

    public boolean waveKilled() {
        return enemiesLeft() == 0;
    }

    public void initEnemys(LinkedList<Enemy> enemys) {
        this.enemys = enemys;
        for (Enemy e: enemys) {
            if(e != null) {
                enemyCounter++;
            }
        }
    }

    public void enemyKilled(Enemy e) {
        player.increaseHighscore(e.getHighscorePoints());
        player.addSpice(e.getStoredSpice());
        alive--;
        killed++;
    }

    public void arrivedAtEndPortal(Enemy e) {
        player.reduceHealth(e.arrivedAtEndPortal());
        alive--;
        arrivedPortal++;
    }

    public boolean isStarted() {
        return started;
    }
}
