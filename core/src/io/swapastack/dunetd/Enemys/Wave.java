package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.utils.Timer;
import io.swapastack.dunetd.GameScreen;
import io.swapastack.dunetd.Player;

import java.util.LinkedList;


public class Wave {
    private int enemyCounter, alive, killed, arrivedPortal;
    private LinkedList<Enemy> enemies;
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
        if(!enemies.isEmpty()) {
            Enemy enem;
            if((enem = enemies.get(0)) != null) {
                gameScreen.initEnemy(enem);
                alive++;
                enemies.remove(0);

            } else {
                enemies.remove(0);
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

    public void initEnemies(LinkedList<Enemy> enemys) {
        this.enemies = enemys;
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

    public void initEnemies(String wave) {
        for(int i = 0; i < wave.length(); i++) {
            switch(wave.charAt(i)) {
                case 1:
                    enemies.add(new Infantry());
                    break;
                case 2:
                    enemies.add(new HarvestMachine());
                    break;
                case 3:
                    enemies.add(new BossUnit());
                    break;
                default:
                    enemies.add(null);
            }
        }
    }
}
