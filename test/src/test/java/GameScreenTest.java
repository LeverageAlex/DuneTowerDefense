import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.*;
import io.swapastack.dunetd.Enemys.BossUnit;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.Enemys.Infantry;
import io.swapastack.dunetd.Towers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GameScreenTest {
    static GameScreen screen;
/*    CanonTower canon = new CanonTower(null) {
        //This is needed to prevent tests from accessing LibGdx
        @Override
        public Vector3 getCoords() {
            return new Vector3(3.0f, 0, 3.0f);
        }

        @Override
        public boolean rotateTowardsVectorSmooth(Vector3 pointToRotate) {
            return true;
        }
    };*/

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
    public void playerDmgTest() {
        Player p  = new Player();
        Assertions.assertFalse(p.getHealth() <= 0);
        p.reduceHealth(300);
        Assertions.assertTrue(p.getHealth() <= 0);
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


    /**
     * Tests Dijkstra
     */
    @Test
    public void dijkstraTest() {
        MapIterable[][] mapTowers = new MapIterable[5][5];
        for (int i = 0; i < mapTowers.length; i++) {
            for (int j = 0; j < mapTowers.length; j++) {
                mapTowers[i][j] = new IterableOverMap();
            }
        }
        Startportal x = new Startportal(0, 0, 0);
        Endportal y = new Endportal(2, 0, 2);
        mapTowers[0][0] = x;
        mapTowers[2][2] = y;
        screen = new GameScreen(new DuneTD()) {
            @Override
            public void dijkstraAddTile(int i, int[][] walkway){};

            @Override
            public void dijkstraRemoveTile() {};
        };
        screen.setMapTowers(mapTowers);
        screen.startPortal = x;
        screen.endPortal = y;
        int[][] path = screen.pathFinder();

        Assertions.assertArrayEquals(new int[][] {{0,0},{1,0}, {2,0}, {2,1},{2,2}}, path);
        mapTowers[1][0] = new CanonTower(screen);
        path = screen.pathFinder();
        Assertions.assertArrayEquals(new int[][] {{0,0},{0,1}, {1,1}, {2,1},{2,2}}, path);

        //Blocking last availablePath
        mapTowers[0][1] = new CanonTower(screen);
        path = screen.pathFinder();
        Assertions.assertNull(path);
    }


    @Test
    public void inRangeOfFieldTest() {
        screen = new GameScreen(new DuneTD()) {
            @Override
            public void dijkstraAddTile(int i, int[][] walkway){};

            @Override
            public void dijkstraRemoveTile() {};
        };
        Assertions.assertTrue(screen.inRangeofField(new Vector3()));
        Assertions.assertFalse(screen.inRangeofField(new Vector3(ConfigMgr.rows, 0.f,ConfigMgr.cols)));
        Assertions.assertFalse(screen.inRangeofField(new Vector3(ConfigMgr.rows-1, 0.f,ConfigMgr.cols)));
        Assertions.assertFalse(screen.inRangeofField(new Vector3(ConfigMgr.rows, 0.f,ConfigMgr.cols-1)));
    }


}
