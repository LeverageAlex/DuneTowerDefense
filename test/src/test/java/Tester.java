import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.*;
import io.swapastack.dunetd.Enemys.BossUnit;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.Enemys.Infantry;
import io.swapastack.dunetd.Enemys.Wave;
import io.swapastack.dunetd.Towers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class Tester {
    static GameScreen screen;
    CanonTower canon = new CanonTower(null) {
        //This is needed to prevent tests from accessing LibGdx
        @Override
        public Vector3 getCoords() {
            return new Vector3(3.0f, 0, 3.0f);
        }

        @Override
        public boolean rotateTowardsVectorSmooth(Vector3 pointToRotate) {
            return true;
        }
    };

    BossUnit boss = new BossUnit() {
        @Override
        public Vector3 getCoords() {
            return new Vector3(2.8f, 2, 3.2f);
        }
    };

    Infantry infantry = new Infantry() {
        public Vector3 getCoords() {
            return new Vector3(1f, 2, 0f);
        }
    };


    @BeforeAll
    public static void configInit() {
        ConfigMgr.readCfg("C:\\Users\\Alex\\Documents\\_Studium\\Softwaregrundprojekt\\Einzelprojekt\\code\\alexander-fink\\core\\assets\\config.ini");
    }

    @Test
    public void HelloWorld()  {
     //   System.out.println("Hello World!");
    }

    @Test
    public void EnemyCollisionTest() {

        Enemy e = new BossUnit() {
            @Override
            public Vector3 getCoords() {
                return new Vector3(2.8f, 2, 3.2f);
            }
        };
        Assertions.assertTrue(e.collides(new Vector3(3, 2, 3), 1.f));
        Assertions.assertFalse(e.collides(new Vector3(2, 1, 1), 1.f));
    }

    @Test
    public void EnemyFireTest() {

        ArrayList<Enemy> arr = new ArrayList<>();
        arr.add(boss);
        Assertions.assertTrue(canon.fire(arr) != null);
        arr.remove(boss);

        arr.add(infantry);
        Assertions.assertFalse(canon.fire(arr) != null);
    }


    @Test
    public void playerDmgTest() {
        Player p  = new Player();
        Assertions.assertFalse(p.getHealth() <= 0);
        p.reduceHealth(300);
        Assertions.assertTrue(p.getHealth() <= 0);
    }

    @Test
    public void waveTest() {
        Wave w = new Wave(new GameScreen(new DuneTD()), new Player());
        Assertions.assertFalse(w.isStarted());
        Assertions.assertTrue(w.enemiesLeft() == 0);
        w.gotEaten();
        Assertions.assertFalse(w.enemiesLeft() == 0);
    }

    @Test
    public void EnemyHSPoint() {
        Assertions.assertEquals(boss.getHighscorePoints(), ConfigMgr.bossHSPoints);
        Assertions.assertEquals(infantry.getHighscorePoints(), ConfigMgr.infHSPoints);
    }

    @Test
    public void EnemyDmg() {
        Assertions.assertTrue(boss.isAlive());
        boss.gainDamage(500.f);
        Assertions.assertFalse(boss.isAlive());
    }

    @Test
    public void spiceTest() {
        Assertions.assertEquals(boss.getStoredSpice(), ConfigMgr.bossStoredSpice);
        Assertions.assertEquals(infantry.getStoredSpice(), ConfigMgr.infStoredSpice);
    }

    @Test
    public void knockerAvailabiltyTest() {
        Assertions.assertTrue(Knocker.isAvailable());
        Knocker.isAvailable = false;
        for (int i = 0; i < 50; i++) {
            Knocker.gambleAvailabilty();
        }
        Assertions.assertTrue(Knocker.isAvailable());
    }


    @Test
    public void GameStateEnumTest() {
        Assertions.assertEquals(GameStateEnum.gameState(GameStateEnum.WAVE), 1);
        Assertions.assertEquals(GameStateEnum.gameState(GameStateEnum.BUILDING), 0);
        Assertions.assertEquals(GameStateEnum.gameState(GameStateEnum.SELECTED), 3);
    }

    @Test
    public void towerEligibleToPlace() {
        //Init
        MapIterable[][] mapTowers = new MapIterable[5][5];
        for (int i = 0; i < mapTowers.length; i++) {
            for (int j = 0; j < mapTowers.length; j++) {
                mapTowers[i][j] = new IterableOverMap();
            }
        }
        Startportal x = new Startportal(0, 0, 0);
        Endportal y = new Endportal(4, 0, 4);
        mapTowers[0][0] = x;
        mapTowers[4][4] = y;
        screen = new GameScreen(new DuneTD()) {
            @Override
            public void dijkstraAddTile(int i, int[][] walkway){};

            @Override
            public void dijkstraRemoveTile() {};
        };
        screen.setMapTowers(mapTowers);
        screen.startPortal = x;
        screen.endPortal = y;


       Assertions.assertTrue(canon.isEligibleToPlace(mapTowers, screen, 3, 3));

       //Test if place fails on start & endportal
       Assertions.assertFalse(canon.isEligibleToPlace(mapTowers, screen, 4, 4));
       Assertions.assertFalse(canon.isEligibleToPlace(mapTowers, screen, 0, 0));

       //Place Tower on Field and then try to place another
       mapTowers[2][2] = new CanonTower(screen);
        Assertions.assertFalse(canon.isEligibleToPlace(mapTowers, screen, 2, 2));

    }

}
