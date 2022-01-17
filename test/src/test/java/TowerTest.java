import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.ConfigMgr;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.Enemys.BossUnit;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.Enemys.Infantry;
import io.swapastack.dunetd.GameScreen;
import io.swapastack.dunetd.Towers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TowerTest {
    static GameScreen screen;
        //ToDo Test SonicTower
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


    /**
     * Tests, if the authorisation-check before placement of tower works
     */
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


    @Test
    public void EnemyFireTest() {

        ArrayList<Enemy> arr = new ArrayList<>();
        arr.add(boss);
        Assertions.assertTrue(canon.fire(arr) != null);
        arr.remove(boss);

        arr.add(infantry);
        Assertions.assertFalse(canon.fire(arr) != null);
    }

}
